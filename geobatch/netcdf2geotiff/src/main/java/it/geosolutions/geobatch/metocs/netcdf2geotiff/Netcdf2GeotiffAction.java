/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  http://code.google.com/p/geobatch/
 *  Copyright (C) 2007-2008-2009 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.geobatch.metocs.netcdf2geotiff;

import it.geosolutions.filesystemmonitor.monitor.FileSystemEvent;
import it.geosolutions.geobatch.actions.tools.adapter.EventAdapter;
import it.geosolutions.geobatch.flow.event.action.ActionException;
import it.geosolutions.geobatch.flow.event.action.BaseAction;
import it.geosolutions.geobatch.metocs.netcdf2geotiff.checker.NetcdfChecker;
import it.geosolutions.geobatch.metocs.netcdf2geotiff.checker.NetcdfCheckerLoader;
import it.geosolutions.geobatch.metocs.netcdf2geotiff.checker.NetcdfCheckerSPI;
import it.geosolutions.geobatch.metocs.utils.io.METOCSActionsIOUtils;
import it.geosolutions.geobatch.metocs.utils.io.Utilities;
import it.geosolutions.tools.commons.file.Path;

import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.io.FilenameUtils;
import org.geotools.geometry.GeneralEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ucar.ma2.Array;
import ucar.ma2.Range;
import ucar.ma2.Section;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.units.Converter;

/**
 * 
 * Public class to split NetCDF_CF Geodetic to GeoTIFFs and consequently send them to GeoServer
 * along with their basic metadata.
 * 
 * For the NetCDF_CF Geodetic file we assume that it contains georectified geodetic grids and
 * therefore has a maximum set of dimensions as follows:
 * 
 * lat { lat:long_name = "Latitude" lat:units = "degrees_north" }
 * 
 * lon { lon:long_name = "Longitude" lon:units = "degrees_east" }
 * 
 * time { time:long_name = "time" time:units = "seconds since 1980-1-1 0:0:0" }
 * 
 * depth { depth:long_name = "depth"; depth:units = "m"; depth:positive = "down"; }
 * 
 * height { height:long_name = "height"; height:units = "m"; height:positive = "up"; }
 * 
 */
public class Netcdf2GeotiffAction extends BaseAction<EventObject> implements
        EventAdapter<NetcdfEvent> {

    /**
     * GeoTIFF Writer Default Params
     */

    private static final int DEFAULT_TILE_SIZE = 256;

    private static final double DEFAULT_COMPRESSION_RATIO = 0.75;

    private static final String DEFAULT_COMPRESSION_TYPE = "LZW";

    protected final static Logger LOGGER = LoggerFactory.getLogger(Netcdf2GeotiffAction.class);

    private final Netcdf2GeotiffConfiguration configuration;

    /**
     * Static DateFormat Converter
     */
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmssSSS'Z'");

    protected Netcdf2GeotiffAction(Netcdf2GeotiffConfiguration configuration) throws IOException {
//        super(configuration.getId(), configuration.getName(), configuration.getDescription());
        super(configuration);
        this.configuration = configuration;
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    /**
     * This method define the mapping between input and output EventObject instance
     * 
     * @param ieo
     *            is the object to transform
     * @return the EventObject adapted
     */
    public NetcdfEvent adapter(EventObject ieo) throws ActionException {
        NetcdfEvent eo = null;
        if (ieo != null)
            try {
                /**
                 * Map the FileSystemEvent to a NetCDFDataset event object
                 */
                if (ieo instanceof FileSystemEvent) {

                    NetcdfFile ncFileIn = null;
                    File inputFile = null;

                    FileSystemEvent fs_event = (FileSystemEvent) ieo;

                    inputFile = new File(fs_event.getSource().getAbsolutePath());

                    /**
                     * Here we assume that each FileSystemEvent file represent a valid NetcdfFile.
                     * This is done (without checks) since the specific class implementation name
                     * define the file type should be passed. Be careful when build flux
                     */
                    // TODO we should check if this file is a netcdf file!

                    ncFileIn = NetcdfFile.open(inputFile.getAbsolutePath());
                    NetcdfDataset d = new NetcdfDataset(ncFileIn);// TODO: add performBackup arg
                    eo = new NetcdfEvent(d);
                }
                /**
                 * if it is a NetcdfEvent we only have to return a NetcdfEvent input instance
                 */
                else if (ieo instanceof NetcdfEvent) {
                    return (NetcdfEvent) ieo;
                } else {
                    throw new ActionException(this, "Passed event is not a FileSystemEvent instance");
                }
            } catch (IOException ioe) {
                throw new ActionException(this, ioe.getLocalizedMessage(),ioe.getCause());
            }
        return eo;
    }

    /**
     * EXECUTE METHOD
     */
    public Queue<EventObject> execute(Queue<EventObject> events) throws ActionException {

        // checks
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting checks...");

        listenerForwarder.started();
        listenerForwarder.setTask("Starting checks");
        /*
         * get the output data dir
         */
        File outputBaseDir = null;
        
        if (configuration.getLayerParentDirectory() != null) {
        	// is absolute path?
        	File layerParDir = new File(configuration.getLayerParentDirectory());
        	if (layerParDir.isAbsolute()){
        		 if (layerParDir.exists() && layerParDir.isDirectory() && layerParDir.canWrite()) {
 	                outputBaseDir = layerParDir;
 	            } else if (layerParDir.mkdirs()) {
 	                outputBaseDir = layerParDir;
 	            }
        	}
        	else {
	        	layerParDir = new File(getTempDir(),configuration.getLayerParentDirectory());
	            if (layerParDir.exists() && layerParDir.isDirectory() && layerParDir.canWrite()) {
	                outputBaseDir = layerParDir;
	            } else if (layerParDir.mkdirs()) {
	                outputBaseDir = layerParDir;
	            }
        	}
            if (outputBaseDir != null && LOGGER.isInfoEnabled())
                LOGGER.info("Output directory: \'"
                            + outputBaseDir.getAbsolutePath()+"\'");
        }

        // if layerDir is not a valid dir let's use the working dir
        if (outputBaseDir == null) {
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("Unable to get a writeable output directory: "
                        + "going to use the working dir...");
            outputBaseDir = getTempDir();
            if (outputBaseDir != null && outputBaseDir.exists() && outputBaseDir.isDirectory() && outputBaseDir.canWrite()) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Netcdf2GeotiffAction.execute(): Output directory \'"
                            + outputBaseDir.getAbsolutePath()+"\'");
            } else {
                final ActionException ae=new ActionException(this,"Unable to get a writeable layerDir");
                listenerForwarder.failed(ae);
    			if (LOGGER.isErrorEnabled()) {
    				LOGGER.error(ae.getLocalizedMessage());
    			}
    			throw ae;
            }
        }

        listenerForwarder.setTask("Starting extracting variables");
        
        // the output
        final Queue<EventObject> ret=new LinkedList<EventObject>();
        
        final int eventSize=events.size();
        // looking for file
        while (events.size() > 0) {
            NetcdfFile ncFileIn = null;
            try {
                // ----------------------------------------------------------------------------------
            	final EventObject event=events.remove();
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Starting processing event: "+event.toString());
                // adapt the input event
                final NetcdfEvent netcdfEvent = adapter(event);
    
                ncFileIn = netcdfEvent.getSource();
    
                final String inputFileBaseName;
                if (ncFileIn != null)
                    inputFileBaseName = FilenameUtils.getBaseName(ncFileIn.getLocation()).replaceAll("_", "");
                else {
                    final String message = "Unable to locate event file sources for event: "+netcdfEvent.getPath();
                    if (LOGGER.isWarnEnabled())
                        LOGGER.warn(message);
                    if (configuration.isFailIgnored()){
                    	continue;
                    } else {
                    	final ActionException e=new ActionException(this, message);
                    	listenerForwarder.failed(e);
                    	throw e;
                    }
                }
                final File layerOutputBaseDir = new File(outputBaseDir,new Date().getTime() + "_"+ inputFileBaseName);
    
                
                // ----------------------------------------------------------------------------------
                
//    System.out.println("DEEP:"+ncFileIn.getIosp().getDetailInfo());
//    System.out.println("TypeID:"+ncFileIn.getIosp().toString());
    
                // ----------------------------------------------------------------------------------
    			// SPI LOADING
                final NetcdfChecker<EventObject> checker;
                NetcdfCheckerSPI spi=NetcdfCheckerLoader.getCheckerLoader(ncFileIn.getIosp().getClass().toString());//getFileTypeId()
                if (spi != null){
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("Netcdf2GeotiffAction.execute(): Creating an instance of: "+spi.getClass());
                    final File dictionaryFile=Path.findLocation(configuration.getMetocDictionaryPath(),getConfigDir());
                    checker=spi.getChecker(ncFileIn, dictionaryFile);
                    if (checker==null){
                        final String message="Netcdf2GeotiffAction.execute(): Failed creating an instance of: "+spi.getClass();
                        if (LOGGER.isWarnEnabled())
                            LOGGER.warn(message);
                        if (configuration.isFailIgnored()){
                        	continue;
                        } else {
                        	final ActionException e=new ActionException(this, message);
                        	listenerForwarder.failed(e);
                        	throw e;
                        }
                    }
                }
                else {
                    final String message="Netcdf2GeotiffAction.execute(): Unable to get spi for...";
                    if (LOGGER.isWarnEnabled())
                        LOGGER.warn(message);
                    if (configuration.isFailIgnored()){
                    	continue;
                    } else {
                    	final ActionException e=new ActionException(this, message);
                    	listenerForwarder.failed(e);
                    	throw e;
                    }
                }
                
    			// SPI LOADING                
                // ----------------------------------------------------------------------------------
                // PROGRESS
                
                final List<Variable> foundVariables=ncFileIn.getVariables();
                final Set<String> readVariables;
                
                float progress = 0;
                // foundVariables.size>0
                final float step ;
                
                if (configuration.getVariables()!=null){
                    readVariables=new TreeSet<String>(configuration.getVariables());
                    if (LOGGER.isTraceEnabled()){
                        LOGGER.trace("Selected variables: "+readVariables.toString());
                    }
                    step=100/(readVariables.size()*eventSize); // TODO check progress for multiple input files
                }
                else {
                    readVariables=Collections.emptySet();
                    step=100/(foundVariables.size()*eventSize); // TODO check progress for multiple input files
                }

                // PROGRESS
                // ----------------------------------------------------------------------------------
                
//                final String runTime = checker.getRunTime();
//                final String tau = checker.getTAU();
                
                for (Variable var : foundVariables) {
                    if (var == null) {
                    	if (LOGGER.isWarnEnabled())
                            LOGGER.warn("Skipping NULL variable!!!");
                        continue;
                    }
                    
                        if (readVariables.size()>0){
                            if (!readVariables.contains(var.getName())){
                                if (LOGGER.isTraceEnabled())
                                    LOGGER.trace("Skipping variable named: \'"+var.getName()+"\'");
                                continue;
                            }
                        }
                        
                        final String task = "Extracting variable named \'"
                                + var.getFullName() + "\' with dimensions: " + var.getDimensionsString();
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info(task);
                        listenerForwarder.setProgress((++progress)*step);
                        listenerForwarder.setTask(task);
                        
                        // INITIALIZE checker variables
                        if (checker.initVar(var)!=true){
                        	if (LOGGER.isErrorEnabled())
                                LOGGER.error("Failed to initialize cache for this variable.");
                        	continue;
                        }
                        
                        /*
                         * VARIABLE (DIRECTORY) NAMING CONVENTION
                         * build the output layer directory using the getVarName implementation for
                         */
                        final File layerOutputVarDir = new File(layerOutputBaseDir,checker.getVarName(var));
    
                        if (!layerOutputVarDir.exists()) {
                            if (!layerOutputVarDir.mkdirs()){
                                if (LOGGER.isWarnEnabled())
                                    LOGGER.warn("Unable to build build the outptut dir: \'"
                                            + layerOutputVarDir.getAbsolutePath() + "\'");
                                continue;
                            }
                        }

                        // building Envelope
                        final GeneralEnvelope envelope = checker.getEnvelope();
					    if (envelope==null)
					    	continue; // TODO LOG!
    
    
    ////
    // defining the SampleModel data type
    // //
    final SampleModel outSampleModel = it.geosolutions.geobatch.metocs.utils.io.Utilities
            .getSampleModel(var.getDataType(), checker.getLonSize(), checker.getLatSize(), 1);

//    /*
//     * Creating a new ImageMosaicCommand to add a layer using this geotiff
//     * (variable)
//     */
//    final List<File> addList = new ArrayList<File>();
//    final ImageMosaicCommand cmd = new ImageMosaicCommand(layerOutputVarDir, addList, null);

    final WritableRaster userRaster = Raster.createWritableRaster(outSampleModel, null);
    
    final int[] shape = var.getShape();
    final Section section = new Section(shape);
    final int rank=section.getRank();
    //TODO
    final Section section2d=new Section();
    if (rank==4){
    	// z,t,y,x
    	section2d.appendRange();
    	section2d.appendRange();
    	section2d.appendRange(shape[shape.length-2]);
        section2d.appendRange(shape[shape.length-1]);
    }
    else if (rank>=3){
    	// t,y,x
    	section2d.appendRange();
    	section2d.appendRange(shape[shape.length-2]);
        section2d.appendRange(shape[shape.length-1]);
    }
    else if (rank>1){
    	// y,x
    	section2d.appendRange(shape[shape.length-2]);
        section2d.appendRange(shape[shape.length-1]);
    }
    else
    	continue;//TODO log

    final Number fillValue = checker.getFillValue();
    
    if (LOGGER.isInfoEnabled())
		LOGGER.info("Missing value is \'"+fillValue.toString()+"\'");
    
	final Converter converter=checker.getConverter();
	
	if (LOGGER.isInfoEnabled())
		LOGGER.info("Loading converter for this variable is \'"+converter+"\'");
	
    
                            for (int z = 0; z < checker.getZetaSize(); z++) {
                            	
                                if (rank==4){
                                    section2d.setRange(1, new Range(z, z));
                                }
                                
                                for (int t = 0; t < checker.getTimeSize(); t++) {

                                    if (rank>=3)
                                        section2d.setRange(0, new Range(t, t));
                                    
                                	final Array originalVarArray= var.read(section2d); // TODO MOVE INTO THE FOR USING section2d)
                                	
                                	METOCSActionsIOUtils.<Integer>gWrite2DData(userRaster, var, originalVarArray, converter, fillValue,configuration.isFlipY());
                                	
//                                    METOCSActionsIOUtils.write2DData(userRaster, var, originalVarArray,
//                                            false, false, section2d.getShape(),
////                                            (hasLocalZLevel ? new int[] { t, z, nLat,
////                                                    nLon } : new int[] { t, nLat, nLon }),
//                                            configuration.isFlipY());

                                    // ////
                                    // producing the Coverage here...
                                    // ////
                                    final String coverageName=checker.buildName(var, t, z);
                                    
                                    if (LOGGER.isDebugEnabled())
                                        LOGGER.debug("Writing GeoTiff named: \'"+coverageName+"\'");
                                    
                                    // Storing variables Variables as GeoTIFFs
                                    final File gtiffFile = Utilities.storeCoverageAsGeoTIFF(layerOutputVarDir,
                                            coverageName, checker.getVarName(var), userRaster, Double.NaN, //Double.parseDouble(checker.getFillValue(var).toString()),
                                            envelope, DEFAULT_COMPRESSION_TYPE,
                                            DEFAULT_COMPRESSION_RATIO, DEFAULT_TILE_SIZE);
    
                                    checker.addOutput(gtiffFile);
                                } // FOR
                            }     // FOR
    
                            //set ouptut
                            final EventObject ev=checker.writeOutput(layerOutputVarDir,var);
                            if (ev!=null)
                            	ret.add(ev);
                            
                            checker.getOutList().clear();
                            
                } // for vars
            } catch (Throwable t) {
                if (LOGGER.isErrorEnabled())
                    LOGGER.error(t.getLocalizedMessage(), t); 
                listenerForwarder.failed(t);
    			if (LOGGER.isErrorEnabled()) {
    				LOGGER.error(t.getLocalizedMessage(), t);
    			}
    			final ActionException ae=new ActionException(this, t.getLocalizedMessage());
    			throw ae;
            } finally {
                try {
                    if (ncFileIn != null) {
                        ncFileIn.close();
                    }
                } catch (IOException e) {
                    if (LOGGER.isErrorEnabled())
                        LOGGER.error(e.getLocalizedMessage(), e);
                }
            }
        } // while events
        return ret;
    }

    
    
}