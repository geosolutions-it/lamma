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
package it.geosolutions.geobatch.lamma.meteosat.test;

import it.geosolutions.filesystemmonitor.monitor.FileSystemEvent;
import it.geosolutions.filesystemmonitor.monitor.FileSystemEventType;
import it.geosolutions.geobatch.action.scripting.ScriptingAction;
import it.geosolutions.geobatch.action.scripting.ScriptingConfiguration;
import it.geosolutions.geobatch.configuration.event.listener.ProgressListenerConfiguration;
import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder;
import it.geosolutions.geobatch.flow.event.action.Action;
import it.geosolutions.geobatch.flow.event.action.ActionException;
import it.geosolutions.geobatch.geotiff.retile.GeotiffRetiler;
import it.geosolutions.geobatch.lamma.meteosat.MeteosatUtils;
import it.geosolutions.geobatch.lamma.meteosat.RGBPythonUtils;
import it.geosolutions.tools.compress.file.Extract;
import it.geosolutions.tools.io.file.Collector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Carlo Cancellieri - carlo.cancellieri@geo-solutions.it
 * 
 */
public class MeteosatUtilsTest {

    /**
     * this is the list (a comma separated string) of prefixes present into the
     * map. Each prefix represents a different mosaic for the Meteosat flow. Its
     * settings should be set into the map as:<br>
     * {prefix}{_setting}<br>
     * where setting is one of the following:
     * <ul>
     * <li>_FILTER</li>
     * <li>_SCRIPT</li>
     * <li>_CALC</li>
     * <li>_MOSAIC_DIR</li>
     * <li>_STYLE</li>
     * <li>_WORKSPACE</li>
     * </ul>
     */
    public final static String PREFIX_LIST = "PREFIX_LIST";
    /**
     * The suffix to add to the prefix to obtain the MOSAIC_DIR KEY into the map
     * this is the directory to use as mosaic base dir (the directory containing
     * the mosaic)
     */
    public final static String MOSAIC_DIR = "_MOSAIC_DIR";
    /**
     * the style to apply into the mosaic
     */
    public final static String STYLE = "_STYLE";
    /**
     * the default workspace
     */
    public final static String DEF_WORKSPACE = "WORKSPACE";
    /**
     * the key to get the relative path (to the configDir) to load environment
     * to call python
     */
    public final static String ENVIRONMENT_FILE = "ENV_FILE";
    /**
     * the key to get the GeoServer Url
     */
    public final static String GSURL = "GSURL";
    /**
     * the key to get the GeoServer user id
     */
    public final static String GSUID = "GSUID";
    /**
     * the key to get the GeoServer pass
     */
    public final static String GSPWD = "GSPWD";

    /**
     * the key to get the GeoStore Url
     */
    public final static String GSTURL = "GSTURL";
    /**
     * the key to get the GeoStore user id
     */
    public final static String GSTUID = "GSTUID";
    /**
     * the key to get the GeoStore pass
     */
    public final static String GSTPWD = "GSTPWD";
    /**
     * GeoStore the key matching the path (relative to the configDir where the
     * GST metadata template is located
     */
    public final static String GST_METADATA_TEMPLATE = "GST_METADATA_TEMPLATE";

    /**
     * the key to get the GeoServer Url
     */
    public final static String GNURL = "GNURL";
    /**
     * the key to get the GeoServer user id
     */
    public final static String GNUID = "GNUID";
    /**
     * the key to get the GeoServer pass
     */
    public final static String GNPWD = "GNPWD";
    /**
     * GeoNetwork category (dataset)
     */
    public final static String GNCAT = "GNCAT";
    /**
     * GeoNetwork group (1)
     */
    public final static String GNGRP = "GNGRP";
    /**
     * GeoNetwork styleSheet (_none_)
     */
    public final static String GNSTYLE = "GNSTYLE";
    /**
     * GeoNetwork the key matching the path (relative to the configDir where the
     * GN metadata template is located
     */
    public final static String GN_METADATA_TEMPLATE = "GN_METADATA_TEMPLATE";
    public final static String GST_LAYER_TEMPLATE = "GST_LAYER_TEMPLATE";

    Map<String, Object> argsMap = new HashMap<String, Object>();

    // File configDir;
    // File tempDir;

    @Before
    public void setup() throws IOException {

        ScriptingConfiguration config = new ScriptingConfiguration("id", "", "");
        List listeners = new ArrayList<ProgressListenerConfiguration>();
        listeners.add(new ProgressListenerConfiguration("", "", ""));
        config.setListenerConfigurations(listeners);

        Map<String, Object> properties = new HashMap<String, Object>();

        config.setProperties(properties);

        argsMap.put(ScriptingAction.CONFIG_KEY, config);
        // ----------------------- -------------------------

        argsMap.put(ScriptingAction.CONFIGDIR_KEY, new File("src/test/resources/"));
        argsMap.put(ScriptingAction.TEMPDIR_KEY, new File("src/test/resources/"));
        // ----------------------- -------------------------
        File zipped = new File("src/test/resources/channels_20110411.tar.bz2");
        // File zipped = new
        // File("src/test/resources/MSG2_201202221700.tar.gz");

        FileUtils.copyFile(new File("/home/carlo/work/data/RGB_LaMMA_20110411/channels.tar.gz"), zipped);
        // FileUtils.copyFile(new
        // File("/home/carlo/work/data/RGB_LaMMA_20110411/MSG2_201202221700.tar.gz"),zipped);

        Queue<FileSystemEvent> queue = new LinkedList<FileSystemEvent>();
        queue.add(new FileSystemEvent(zipped, FileSystemEventType.FILE_ADDED));
        argsMap.put(ScriptingAction.EVENTS_KEY, queue);

        properties.put(DEF_WORKSPACE, "lamma");

        final String airmassPrefix = "AIRMASS";
        final String natColours = "NatColours";
        final String dust = "Dust";
        // global
        properties.put(PREFIX_LIST, airmassPrefix + "," + dust + "," + natColours + ",Channel_1,FAKE");
        properties.put(ENVIRONMENT_FILE, "env.properties");

        // airmass
        properties.put(airmassPrefix + RGBPythonUtils.FILTER,
                       "*_WV_062_05*,*_WV_073_06*,*_IR_097_08*,*_IR_108_09*");
        properties.put(airmassPrefix + RGBPythonUtils.CALC_FUNCTION, "airmass");
        properties.put(airmassPrefix + RGBPythonUtils.SCRIPT, "python/createRGB_2.py");
        properties.put(airmassPrefix + MOSAIC_DIR, "src/test/resources/" + airmassPrefix);

        // NatColours
        properties.put(natColours + RGBPythonUtils.FILTER, "*_VIS006_01*,*_VIS008_02*,*_IR_016_03*");
        properties.put(natColours + RGBPythonUtils.CALC_FUNCTION, "NatColours");
        properties.put(natColours + RGBPythonUtils.SCRIPT, "python/createRGB_2.py");
        properties.put(natColours + MOSAIC_DIR, "src/test/resources/" + natColours);

        // // Hrv_Fog
        // final String Hrv_Fog="Hrv_Fog";
        // map.put(airmassPrefix + FILTER,
        // "*_HRV_12*,*_HRV_12*,*_IR_016_03*");
        // map.put(Hrv_Fog + CALC_FUNCTION, "Hrv_Fog");
        // map.put(Hrv_Fog + SCRIPT, "python/createRGB_2.py");
        // map.put(Hrv_Fog + MOSAIC_DIR, "src/test/resources/"
        // + Hrv_Fog);
        // map.put(Hrv_Fog + WORKSPACE, "lamma");

        // final String dust="Dust";
        properties.put(dust + RGBPythonUtils.FILTER, "*_IR_087_07*,*_IR_108_09*,*_IR_120_10*");
        properties.put(dust + RGBPythonUtils.CALC_FUNCTION, "Dust");
        properties.put(dust + RGBPythonUtils.SCRIPT, "python/createRGB_2.py");
        properties.put(dust + MOSAIC_DIR, "src/test/resources/" + dust);

        // Channel_1
        String prefix = "Channel_1";
        properties.put(prefix + RGBPythonUtils.FILTER, "*_WV_062_05*");
        // map.put(prefix+ CALC_FUNCTION, "airmass");
        // map.put(prefix+ SCRIPT, "python/createRGB_2.py");
        properties.put(prefix + MOSAIC_DIR, "src/test/resources/" + prefix);

        // configDir = new File("src/test/resources/");
        // tempDir = new
        // File("/home/carlo/work/data/RGB_LaMMA_20110411/Airmass_2");
        //
        // MSG2_201109230915_eurafr_IR_087_07.tif
        properties.put(GSPWD, "geoserver");
        properties.put(GSUID, "admin");
        properties.put(GSURL, "http://localhost:8181/geoserver/");

        // geonetwork
        properties.put(GNPWD, "admin");
        properties.put(GNUID, "admin");
        properties.put(GNURL, "http://localhost:8282/geonetwork/");
        properties.put(GN_METADATA_TEMPLATE, "gn_template.xml");

        // geostore
        properties.put(GSTPWD, "admin");
        properties.put(GSTUID, "admin");
        properties.put(GSTURL, "http://localhost:8383/geostore/rest/");
        properties.put(GST_METADATA_TEMPLATE, "commons/gst_msg_template.js");
        properties.put(GST_LAYER_TEMPLATE, "commons/gst_msg_layer_template.js");

    }

    /**
     * used to call:<br>
     * python script outFile inFiles[0] ... inFiles[N]
     * 
     * @param logger can be null
     * @param script
     * @param tempDir from where the process should be called
     * @param outFile
     * @param inFiles
     * @return true or false
     * @throws Exception
     */
    @Test
    @Ignore
    public void airMass() throws Exception {
        execute(argsMap);
    }

    /**
     * Script Main "execute" function
     **/
    public Map execute(Map argsMap) throws Exception {

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
         * the key to get the relative path (to the configDir) to load
         * environment to call python
         */
        final String ENVIRONMENT_FILE = "ENV_FILE";
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

        final ScriptingConfiguration configuration = (ScriptingConfiguration)argsMap
            .get(ScriptingAction.CONFIG_KEY);

        boolean failIgnore = true;
        // TODO
        if (!configuration.isFailIgnored()) {
            failIgnore = false;
        }

        final Map map = configuration.getProperties();

        final File tempDir = (File)argsMap.get(ScriptingAction.TEMPDIR_KEY);
        final File configDir = (File)argsMap.get(ScriptingAction.CONFIGDIR_KEY);
        final ProgressListenerForwarder listenerForwarder = (ProgressListenerForwarder)argsMap
            .get(ScriptingAction.LISTENER_KEY);
        final List events = (List)argsMap.get(ScriptingAction.EVENTS_KEY);
        if (events.size() != 1) {
            ActionException ae = new ActionException(Action.class,
                                                     "More than 1 argument encountered. We only need a zip file");
            listenerForwarder.failed(ae);
        }

        // set workspace
        String workspace = (String)map.get(WORKSPACE);
        if (workspace == null) {
            throw new ActionException(Action.class, "Unable to continue without a " + WORKSPACE
                                                    + " defined, please check your configuration");
        }

        FileSystemEvent ev = (FileSystemEvent)events.remove(0);
        // extract input
        File channelsDir = Extract.extract(ev.getSource(), tempDir, true);

        final Logger logger = LoggerFactory
            .getLogger("it.geosolutions.geobatch.action.scripting.ScriptingAction.class");

        // read env
        final String envFileName = (String)map.get(ENVIRONMENT_FILE);
        if (envFileName == null)
            throw new IllegalArgumentException("The key " + ENVIRONMENT_FILE
                                               + " property is not set, please fix the configuration.");

        // used for geostore
        final Queue<EventObject> queue = new LinkedList<EventObject>();

        // read prefix list
        final String prefixList = (String)map.get(PREFIX_LIST);
        if (prefixList == null)
            throw new IllegalArgumentException("The key " + PREFIX_LIST
                                               + " property is not set, please fix the configuration.");
        final String[] prefixes = prefixList.split(",");
        for (String prefix : prefixes) {

            if (logger.isDebugEnabled()) {
                logger.debug("Doing stuff for prefix: " + prefix);
            }

            // TODO NOTE here tempDir is the directory where to find channels
            // NOT the action getTempDir()
            // ------------------------------------------------
            final Collector c = new Collector(new WildcardFileFilter("MSG*_*.tif"));
            final File[] fileList = c.collect(channelsDir).toArray(new File[] {});// groovy->
                                                                                  // .toArray([new
                                                                                  // File("")]);
            if (fileList.length == 0) {
                final IllegalArgumentException iae = new IllegalArgumentException(
                                                                                  "The passed folder "
                                                                                      + channelsDir
                                                                                      + " does not contain channel files in the correct form.");
                listenerForwarder.failed(iae);
                throw iae;
            }
            final String modelName = fileList[0].getName();
            final String time = MeteosatUtils.getTime(modelName);
            final File outFile = new File(channelsDir, prefix + "_" + time + ".tif");
            // GeoStore
            final File[] channels;
            try {
                // use workspace as GeoStore Category and resource
                channels = RGBPythonUtils.getChannels(channelsDir, prefix, map);
            } catch (Exception e) {
                if (failIgnore) {
                    if (logger != null && logger.isErrorEnabled()) {
                        logger.error(e.getLocalizedMessage(), e);
                    }
                    continue;
                } else {
                    throw new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
                }
            }

            if (channels.length > 1) { // call python channels to RGB process
                final String[] args = RGBPythonUtils.buildArgs(configDir, channels, outFile, prefix, map);
                final File props = new File(configDir, envFileName);
                boolean ret = RGBPythonUtils.run(logger, props, null, args);

                // if !success continue or break
                if (!ret) {
                    final String message = "Unable to get output from the python script process for prefix: "
                                           + prefix;
                    if (failIgnore) {
                        logger.warn(message);
                        continue;
                    } else {
                        // listenerForwarder.failed();
                        logger.error(message);
                        break;
                    }
                }
            } else { // simply rename the tiff to publish it
                // File inFile, File tiledTiffFile, double compressionRatio,
                // String
                // compressionType, int tileW, int tileH, boolean forceBigTiff)
                // throws IOException {
                GeotiffRetiler.reTile(channels[0], outFile, 0.7, "LZW", 256, 256, false);

                // add-overview
                MeteosatUtils.addo(outFile, map);
            }

            queue.add(new FileSystemEvent(outFile, FileSystemEventType.FILE_ADDED));
        }

        // if !success continue or break
        if (queue == null || queue.isEmpty()) {
            if (failIgnore) {
                logger.error("Unable to get output ");
            } else {
                throw new ActionException(Action.class, "Unable to get output");
            }
        }

        argsMap.put(ScriptingAction.EVENTS_KEY, queue);

        argsMap = MeteosatUtils.ImageMosaic(argsMap);

        argsMap = MeteosatUtils.GeoNetworkAndGeoStore(argsMap);

        listenerForwarder.completed();
        return argsMap;
    }
}
