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
import it.geosolutions.geobatch.lamma.geonetwork.GeoNetworkUtils;
import it.geosolutions.geobatch.lamma.geostore.GeoStoreUtils;
import it.geosolutions.geonetwork.GNClient;
import it.geosolutions.geoserver.rest.encoder.metadata.GSDimensionInfoEncoder.Presentation;
import it.geosolutions.geostore.services.rest.GeoStoreClient;
import it.geosolutions.tools.freemarker.filter.FreeMarkerFilter;

import java.io.File;
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
    
        final Map map = configuration.getProperties();
    
        final File tempDir = (File)argsMap.get(ScriptingAction.TEMPDIR_KEY);
        final File configDir = (File)argsMap.get(ScriptingAction.CONFIGDIR_KEY);
        final ProgressListenerForwarder listenerForwarder = (ProgressListenerForwarder)argsMap
            .get(ScriptingAction.LISTENER_KEY);
        final List<FileSystemEvent> events = (List)argsMap.get(ScriptingAction.EVENTS_KEY);
    
        List<Map> rootList=GeoNetworkUtils.publishOnGeoNetworkAction(listenerForwarder,failIgnore,tempDir,configDir,events,argsMap, map);
        
        argsMap=GeoStoreUtils.publishOnGeoStoreAction(listenerForwarder,failIgnore,rootList, argsMap,map,configDir);
        
        
        listenerForwarder.completed();
        return argsMap;
    }


    public static Map ImageMosaic(Map argsMap) throws Exception {
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
        final String PREFIX_LIST = "PREFIX_LIST";
        /**
         * The suffix to add to the prefix to obtain the MOSAIC_DIR KEY into the
         * map this is the directory to use as mosaic base dir (the directory
         * containing the mosaic)
         */
        final String MOSAIC_DIR = "_MOSAIC_DIR";
        /**
         * the style to apply into the mosaic
         */
        final String STYLE = "_STYLE";
        /**
         * the default workspace
         */
        final String WORKSPACE = "WORKSPACE";
        /**
         * the key to get the GeoServer Url
         */
        final String GSURL = "GSURL";
        /**
         * the key to get the GeoServer user id
         */
        final String GSUID = "GSUID";
        /**
         * the key to get the GeoServer pass
         */
        final String GSPWD = "GSPWD";
    
        final Logger logger = LoggerFactory
            .getLogger("it.geosolutions.geobatch.action.scripting.ScriptingAction.class");
    
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
    
        // used for geostore
        final Queue<EventObject> imcL = new LinkedList<EventObject>();
    
        // read prefix list
        final String prefixList = (String)map.get(PREFIX_LIST);
        if (prefixList == null)
            throw new IllegalArgumentException("The key " + PREFIX_LIST
                                               + " property is not set, please fix the configuration.");
        final String[] prefixes = prefixList.split(",");
        for (String prefix : prefixes) {
    
            final WildcardFileFilter filter = new WildcardFileFilter("*"+prefix+"*");
    
            // building IMC to add new granule updating mosaic
            final List<File> addFileList = new ArrayList<File>();
            for (FileSystemEvent ev : events) {
                final File inFile = ev.getSource();
                if (filter.accept(inFile)) {
                    addFileList.add(inFile);
                }
            }
    
            final String mosaicDirName = (String)map.get(prefix + MOSAIC_DIR);
            if (mosaicDirName == null) {
                if (failIgnore) {
                    logger.error("The key " + prefix + MOSAIC_DIR
                                 + " property is not set, please fix the configuration.");
                    continue;
                } else {
                    throw new IllegalArgumentException(
                                                       "The key "
                                                           + prefix
                                                           + MOSAIC_DIR
                                                           + " property is not set, please fix the configuration.");
                }
            }
            final File mosaicDir = new File(mosaicDirName);
    
            final ImageMosaicCommand imc = new ImageMosaicCommand(mosaicDir, addFileList, null);
    
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
    
            imc.setGeoserverPWD(gsPwd);
            imc.setGeoserverUID(gsUsr);
            imc.setGeoserverURL(gsUrl);
    
            // set style
            final String style = (String)map.get(prefix + STYLE);
            imc.setDefaultStyle(style != null ? style : "raster");
            imc.setDefaultNamespace(workspace != null ? workspace : "geosolutions");
    
            // TODO set specific settings for this mosaic (style, bb, etc...)
            Object obj=map.get(prefix+"_SRS");
            if (obj!=null){
                imc.setTimeRegex(obj.toString());
            } else {
                obj=map.get("SRS");
                if (obj!=null){
                    imc.setCrs(obj.toString());
                } else {
                    imc.setCrs("EPSG:4326");
                }
            }
            
            obj=map.get(prefix+"_TIME_REGEX");
            if (obj!=null){
                imc.setTimeDimEnabled("true");
                imc.setTimeRegex(obj.toString());
                imc.setTimePresentationMode(Presentation.LIST.toString());
            } else {
                obj=map.get("TIME_REGEX");
                if (obj!=null){
                    imc.setTimeDimEnabled("true");
                    imc.setTimeRegex(obj.toString());
                    imc.setTimePresentationMode(Presentation.LIST.toString());
                } else {
                    imc.setTimeDimEnabled("false");
                }
            }
            
            obj=map.get(prefix+"_ELEV_REGEX");
            if (obj!=null){
                imc.setElevDimEnabled("true");
                imc.setElevationRegex(obj.toString());
                imc.setElevationPresentationMode(Presentation.LIST.toString());
            } else {
                obj=map.get("ELEV_REGEX");
                if (obj!=null){
                    imc.setElevDimEnabled("true");
                    imc.setElevationRegex(obj.toString());
                    imc.setElevationPresentationMode(Presentation.LIST.toString());
                } else {
                    imc.setElevDimEnabled("false");
                }
            }
    
            obj=map.get(prefix+"_DATASTORE_PROPS");
            if (obj!=null){
                imc.setDatastorePropertiesPath(obj.toString());
            } else {
                obj=map.get("DATASTORE_PROPS");
                if (obj!=null){
                    imc.setDatastorePropertiesPath(obj.toString());
                } else {
                    imc.setDatastorePropertiesPath(null);
                }
            }
            
            imcL.add(new EventObject(imc));
    
        }
    
        final Queue<EventObject> queue = callImageMosaicAction(logger, tempDir, configDir, imcL);
        // if !success continue or break
        if (queue == null || queue.isEmpty()) {
            if (failIgnore) {
                logger.error("Unable to get output from the ImageMosaicAction " + workspace);
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
}
