/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  http://code.google.com/p/geobatch/
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
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
package it.geosolutions.geobatch.lamma.meteosat;

import it.geosolutions.filesystemmonitor.monitor.FileSystemEvent;
import it.geosolutions.geobatch.action.scripting.ScriptingAction;
import it.geosolutions.geobatch.action.scripting.ScriptingConfiguration;
import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder;
import it.geosolutions.geobatch.flow.event.action.Action;
import it.geosolutions.geobatch.flow.event.action.ActionException;
import it.geosolutions.geobatch.imagemosaic.ImageMosaicAction;
import it.geosolutions.geobatch.imagemosaic.ImageMosaicCommand;
import it.geosolutions.geobatch.imagemosaic.ImageMosaicConfiguration;
import it.geosolutions.geobatch.imagemosaic.granuleutils.GranuleRemover;
import it.geosolutions.geobatch.lamma.geonetwork.GeoNetworkUtils;
import it.geosolutions.geobatch.lamma.geostore.GeoStoreUtils;
import it.geosolutions.geoserver.rest.encoder.metadata.GSDimensionInfoEncoder.Presentation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.media.jai.JAI;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.geotools.utils.imageoverviews.OverviewsEmbedder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Carlo Cancellieri - carlo.cancellieri@geo-solutions.it
 * 
 */
public class MeteosatUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(MeteosatUtils.class);


    public static Queue<EventObject> callImageMosaicAction(Logger logger, File tempDir, File configDir, ImageMosaicCommand imc){

        // call ImageMosaicAction
        Queue<EventObject> queue=new LinkedList<EventObject>();
        queue.add(new EventObject(imc));
        
        return callImageMosaicAction(logger, tempDir, configDir, queue);
        
    }
    
    public static Queue<EventObject> callImageMosaicAction(Logger logger, File tempDir, File configDir, Queue<EventObject> queue){
        final ImageMosaicConfiguration imConfig=new ImageMosaicConfiguration("ImageMosaicConfiguration", "Configuration", "Configuration");

        final ImageMosaicAction imAction=new ImageMosaicAction(imConfig);
        imAction.setTempDir(tempDir);
        imAction.setConfigDir(configDir);
        // TODO listeners...
        Queue<EventObject> ret=null;
        try {
            ret=imAction.execute(queue);
        } catch (ActionException ae){
            if (logger!=null){
                if (logger.isDebugEnabled()){
                    logger.debug(ae.getLocalizedMessage(),ae);
                } else {
                    logger.error(ae.getLocalizedMessage());
                }
            }
        }
        return ret;
    }


    public static String getTime(String name) {
        String time = name.split("_", 3)[1];
        return time.substring(0, 8) + "T" + time.substring(8) + "00000Z";
    }

    /**
     * run addo on the input file with options specified by the map
     * @param file
     * @param map
     */
    public static void addo(File file, Map map) {
         final OverviewsEmbedder oe = new OverviewsEmbedder();
         
         oe.setSourcePath(file.getPath());
         
         Object opt=map.get("DOWNSAMPLE_STEP");
         Integer downsampleStep=null;
         if (opt!=null){
             downsampleStep=Integer.parseInt(opt.toString());
         } else {
             downsampleStep=2;
         }
         oe.setDownsampleStep(downsampleStep);
         
         Integer numSteps=null;
         opt=map.get("NUM_STEPS");
         if (opt!=null){
             numSteps=Integer.parseInt(opt.toString());
         } else {
             numSteps=8;
         }
         oe.setNumSteps(numSteps);
         
         // SG: this way we are sure we use the standard tile cache
         oe.setTileCache(JAI.getDefaultInstance().getTileCache());
        
         String scaleAlgorithm=null;
         opt=map.get("SCALE_ALGORITHM");
         if (opt!=null){
             scaleAlgorithm=opt.toString();
         } else {
             scaleAlgorithm="average";
         }
         oe.setScaleAlgorithm(scaleAlgorithm);
         
         Integer tileH=null;
         opt=map.get("TILE_H");
         if (opt!=null){
             tileH=Integer.parseInt(opt.toString());
         } else {
             tileH=256;
         }
         oe.setTileHeight(tileH);
         
         Integer tileW=null;
         opt=map.get("TILE_W");
         if (opt!=null){
             tileW=Integer.parseInt(opt.toString());
         } else {
             tileW=256;
         }
         oe.setTileWidth(tileW);
         
         oe.run();
    }

    /**
     * Script Main "execute" function
     **/
    public static Map GeoNetworkAndGeoStore(Map argsMap) throws Exception {

        final ScriptingConfiguration configuration = (ScriptingConfiguration)argsMap
            .get(ScriptingAction.CONFIG_KEY);
    
        boolean failIgnore = configuration.isFailIgnored();
    
        final Map cfgProperties = configuration.getProperties();
    
        final File tempDir = (File)argsMap.get(ScriptingAction.TEMPDIR_KEY);
        final File configDir = (File)argsMap.get(ScriptingAction.CONFIGDIR_KEY);
        final ProgressListenerForwarder listenerForwarder = (ProgressListenerForwarder)argsMap
            .get(ScriptingAction.LISTENER_KEY);
        final List<FileSystemEvent> events = (List)argsMap.get(ScriptingAction.EVENTS_KEY);
    
        List<Map> rootList=GeoNetworkUtils.publishOnGeoNetworkAction(listenerForwarder,failIgnore,tempDir,configDir,events,argsMap, cfgProperties);
        
        argsMap=GeoStoreUtils.publishOnGeoStoreAction(listenerForwarder,failIgnore,rootList, argsMap, cfgProperties, configDir);
        
        
        listenerForwarder.completed();
        return argsMap;
    }


    /**
     * this is the list (a comma separated string) of prefixes present into
     * the map. Each prefix represents a different mosaic for the Meteosat
     * flow. Its settings should be set into the map as:<br>
     * {prefix}{_setting}<br>
     * where setting is one of the following:
     * <ul>
     * <li>_FILTER</li>
     * <li>_SCRIPT</li>
     * <li>_CALC</li>
     * <li>_MOSAIC_DIR</li>
     * <li>_STYLE</li>
     * </ul>
     */
    private static final String PREFIX_LIST = "PREFIX_LIST";

    private static final String DELETE_GRANULES_DELTA_DAYS = "DELETE_GRANULES_DELTA_DAYS";
    private static final String DELETE_GRANULES_FROM_DISK = "DELETE_GRANULES_FROM_DISK";

    private static final String PERFORM_GEOSERVER_RESET = "PERFORM_GEOSERVER_RESET";
    /**
     * The suffix to add to the prefix to obtain the MOSAIC_DIR KEY into the
     * map this is the directory to use as mosaic base dir (the directory
     * containing the mosaic)
     */
    private static final String MOSAIC_DIR = "_MOSAIC_DIR";
    /**
     * the style to apply into the mosaic
     */
    private static final String STYLE = "STYLE";
    /**
     * the default workspace
     */
    private static final String WORKSPACE = "WORKSPACE";
    /**
     * the key to get the GeoServer Url
     */
    private static final String GSURL = "GSURL";
    /**
     * the key to get the GeoServer user id
     */
    private static final String GSUID = "GSUID";
    /**
     * the key to get the GeoServer pass
     */
    private static final String GSPWD = "GSPWD";

    public static Map ImageMosaic(Map argsMap) throws Exception {
        
        final ScriptingConfiguration configuration = (ScriptingConfiguration)argsMap
            .get(ScriptingAction.CONFIG_KEY);
    
        boolean failIgnore = true;
        if (!configuration.isFailIgnored()) {
            failIgnore = false;
        }
    
        final Map map = configuration.getProperties();
    
        final File tempDir = (File)argsMap.get(ScriptingAction.TEMPDIR_KEY);
        final File configDir = (File)argsMap.get(ScriptingAction.CONFIGDIR_KEY);

        final ProgressListenerForwarder listenerForwarder = (ProgressListenerForwarder)argsMap
            .get(ScriptingAction.LISTENER_KEY);
        final List<FileSystemEvent> events = (List<FileSystemEvent>)argsMap.get(ScriptingAction.EVENTS_KEY);
    
        if (events.size() < 1) {
            ActionException ae = new ActionException(Action.class,
                                                     "No args encountered. We need a list of files");
            listenerForwarder.failed(ae);
        }
    
        // set workspace
        String workspace = (String)map.get(WORKSPACE);
        if (workspace == null) {
            throw new ActionException(Action.class, "Unable to continue without a " + WORKSPACE
                                                    + " defined, please check your configuration");
        }

        //=== GS stuff
        String gsUrl = (String)map.get(GSURL);
        // config.put(GSURL, gsUrl);
        if (gsUrl == null) {
            gsUrl="http://localhost:8080/geoserver";
        }

        String gsUsr = (String)map.get(GSUID);
        if (gsUsr == null) {
            gsUsr="admin";
        }

        String gsPwd = (String)map.get(GSPWD);
        if (gsPwd == null) {
            gsPwd="admin";
        }

        //===
        // used for geostore
        final Queue<EventObject> imcQueue = new LinkedList<EventObject>();

        //===
        String performGeoserverResetParam = (String)map.get(PERFORM_GEOSERVER_RESET);
        boolean performGeoserverReset = "true".equalsIgnoreCase(performGeoserverResetParam);

        //== DeleteGranules stuff
        String deleteGranulesDeltaDaysParam = (String)map.get(DELETE_GRANULES_DELTA_DAYS);
        Integer deleteGranulesDeltaDays = null;
        if(deleteGranulesDeltaDaysParam != null) {
            try {
                deleteGranulesDeltaDays = Integer.valueOf(deleteGranulesDeltaDaysParam);
            } catch (NumberFormatException e) {
                LOGGER.error("Can't parse " + DELETE_GRANULES_DELTA_DAYS + " = " + deleteGranulesDeltaDaysParam);
            }
        }

        String deleteGranulesFromDiskParam = (String)map.get(DELETE_GRANULES_FROM_DISK);
        boolean deleteGranulesFromDisk = "true".equalsIgnoreCase(deleteGranulesFromDiskParam);
        //===

        // read prefix list
        final String prefixList = (String)map.get(PREFIX_LIST);
        if (prefixList == null)
            throw new IllegalArgumentException("The key " + PREFIX_LIST
                                               + " property is not set, please fix the configuration.");
        
        for (String prefix : prefixList.split(",")) {

            final ImageMosaicCommand imc = new ImageMosaicCommand();
            imc.setGeoserverPWD(gsPwd);
            imc.setGeoserverUID(gsUsr);
            imc.setGeoserverURL(gsUrl);
            imc.setDefaultNamespace(workspace != null ? workspace : "geosolutions");
            imc.setFinalReset(performGeoserverReset);

            if ( ! fillIMC(imc, prefix, events, map, failIgnore) ) {
                continue;
            }
            imcQueue.add(new EventObject(imc));

            if(deleteGranulesDeltaDays != null) {
                try {
                    GranuleRemover remover = new GranuleRemover();
                    remover.setDaysAgo(deleteGranulesDeltaDays);
                    //remover.setTypeName();
                    remover.enrich(imc);
                    imc.setDeleteGranules(deleteGranulesFromDisk);
                } catch (IOException ex) {
                    LOGGER.error("Could not remove older granules: " + ex.getMessage(), ex);
                } catch (IllegalStateException ex) {
                    LOGGER.error("Error setting up the GranuleRemover: " + ex.getMessage() + " -- Please check your configuration");
                }
            }
        }
    
        final Queue<EventObject> queue = callImageMosaicAction(LOGGER, tempDir, configDir, imcQueue);
        // if !success continue or break
        if (queue == null || queue.isEmpty()) {
            if (failIgnore) {
                LOGGER.error("Unable to get output from the ImageMosaicAction " + workspace);
            } else {
                throw new ActionException(Action.class, "Unable to get output from the ImageMosaicAction "
                                                        + workspace);
            }
        }
    
        argsMap.put(ScriptingAction.RETURN_KEY, queue);
    
        listenerForwarder.completed();
        return argsMap;
    }

    public static File getOutFile(String prefix, File inFile){
        String name=FilenameUtils.getBaseName(inFile.getName());
        
        // TIME_REGEX -> [0-9]{12}(\?!.\*[0-9]{12}.\*)
        
        if (prefix.equalsIgnoreCase("CAPPI")){
          // CAPPI_ELEV_REGEX ->  [0-9]{4}(\?!.\*[0-9]{4}.\*)</string>
          
          // f.e.: inFile.getName() -> CAPPI_Z.3000_201109220215.tif
          // return CAPPI_3000_20110922T021500000Z.tif
          final String splittedName[]=name.split("_");
          final String elev=splittedName[1].substring(2, 6);
          final String time=splittedName[2].substring(0, 8) + "T" + splittedName[2].substring(8) + "00000Z";
          return new File(inFile.getParent(),"CAPPI_"+elev+"_"+time+".tif");
          
        } else if (prefix.equalsIgnoreCase("SRI")) {
            
            // f.e.: inFile.getName() -> SRI_201109220215.tif
            // return SRI_20110922T021500000Z.tif
            final String splittedName[]=name.split("_");
            final String time=splittedName[1].substring(0, 8) + "T" + splittedName[1].substring(8) + "00000Z";
            return new File(inFile.getParent(),"SRI_"+time+".tif");
          
        } else if (prefix.equalsIgnoreCase("SRT")) {

            // f.e.: inFile.getName() -> SRT.1_201109220215.tif
            // return SRT.1_20110922T021500000Z.tif
            final String splittedName[]=name.split("_");
            final String time=splittedName[1].substring(0, 8) + "T" + splittedName[1].substring(8) + "00000Z";
            return new File(inFile.getParent(),"SRT_"+time+".tif");
          
        } else if (prefix.equalsIgnoreCase("VMI")) {
          
            // f.e.: inFile.getName() -> VMI_201109220215.tif
            // return VMI_20110922T021500000Z.tif
            final String splittedName[]=name.split("_");
            final String time=splittedName[1].substring(0, 8) + "T" + splittedName[1].substring(8) + "00000Z";
            return new File(inFile.getParent(),"VMI_"+time+".tif");

        }
        return null;
    }

    protected static boolean fillIMC(final ImageMosaicCommand imc, String prefix, final List<FileSystemEvent> events, final Map map, boolean failIgnore) throws IllegalArgumentException {

        //=== MOSAIC DIR
        final String mosaicDirName = (String)map.get(prefix + MOSAIC_DIR);
        if (mosaicDirName == null) {
            final String msg = "The key " + prefix + MOSAIC_DIR
                             + " property is not set, please fix the configuration.";
            if (failIgnore) {
                LOGGER.error(msg);
                return false;
            } else {
                throw new IllegalArgumentException(msg);
            }
        }
        final File mosaicDir = new File(mosaicDirName);
        imc.setBaseDir(mosaicDir);

        //== building new granule list
        final WildcardFileFilter filter = new WildcardFileFilter("*"+prefix+"*");

        final List<File> addFileList = new ArrayList<File>();
        for (FileSystemEvent ev : events) {
            final File inFile = ev.getSource();
            if (filter.accept(inFile)) {
                addFileList.add(inFile);
            }
        }
        imc.setAddFiles(addFileList);


        // TODO set specific settings for this mosaic (style, bb, etc...)
        imc.setDefaultStyle(getValueFromMap(map, prefix, STYLE, "raster"));
        imc.setCrs(getValueFromMap(map, prefix, "SRS", "EPSG:4326"));
        imc.setDatastorePropertiesPath(getValueFromMap(map, prefix, "DATASTORE_PROPS", null));

        String timeregex = getValueFromMap(map, prefix, "TIME_REGEX", null);
        if(timeregex != null) {
            imc.setTimeDimEnabled("true");
            imc.setTimeRegex(timeregex);
            imc.setTimePresentationMode(getValueFromMap(map, prefix, "TIME_PRESENTATION", Presentation.LIST.name()));
        } else {
            imc.setTimeDimEnabled("false");
        }

        String elevregex = getValueFromMap(map, prefix, "ELEV_REGEX", null);
        if(elevregex != null) {
            imc.setElevDimEnabled("true");
            imc.setElevationRegex(elevregex);
            imc.setElevationPresentationMode(getValueFromMap(map, prefix, "ELEV_PRESENTATION", Presentation.LIST.name()));
        } else {
            imc.setElevDimEnabled("false");
        }

        return true;
    }

    /**
     * Search a possibly customized key.
     *
     * At first, a customized key "PREFIX_KEY" is searched.
     * If not found, a general "KEY" is searched.
     *
     * @return
     */
    private static String getValueFromMap(Map map, String prefix, String key, String defValue) {
        // First search for a specific value for the key in that given prefix
        Object obj=map.get(prefix+"_"+key);
        if (obj!=null){
            return obj.toString();
        } else { // else search for the generic value
            obj=map.get(key);
            if (obj!=null){
                return obj.toString();
            } else {
                return defValue;
            }
        }
    }

}
