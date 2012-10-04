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
package it.geosolutions.geobatch.lamma.misc;

import it.geosolutions.geobatch.imagemosaic.ImageMosaicCommand;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import it.geosolutions.tools.io.file.Collector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Carlo Cancellieri - carlo.cancellieri@geo-solutions.it
 * 
 */
public class MeteosatUtilsTest {

    Map<String, String> map = new HashMap<String, String>();
    File configDir;
    File tempDir;

    String gsurl;
    String gsUsr;
    String gsPwd;
    
    @Before
    public void setup() {

        final String airmassPrefix = "AIRMASS";
        // global
        map.put(MeteosatUtils.PREFIX_LIST, airmassPrefix+",FAKE");
        map.put(MeteosatUtils.ENVIRONMENT_FILE, "env2.properties");
        
        // airmass
        map.put(airmassPrefix + MeteosatUtils.FILTER, "*_WV_062_05*,*_WV_073_06*,*_IR_097_08*,*_IR_108_09*");
        map.put(airmassPrefix + MeteosatUtils.CALC_FUNCTION, "airmass");
        map.put(airmassPrefix + MeteosatUtils.SCRIPT, "python/createRGB_2.py");
        map.put(airmassPrefix + MeteosatUtils.MOSAIC_DIR, "src/test/resources/"+airmassPrefix);

        
        configDir = new File("src/test/resources/");
        tempDir = new File("/home/carlo/work/data/RGB_LaMMA_20110411/Airmass_2");
        

        gsurl = "http://localhost:9090/geoserver/";
        gsUsr = "admin";
        gsPwd = "geoserver";
        
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
     * @throws IOException
     */
    @Test
    public void airMass() throws IOException {

        final String envFileName = map.get(MeteosatUtils.ENVIRONMENT_FILE);
        if (envFileName == null)
            throw new IllegalArgumentException("The key " + MeteosatUtils.ENVIRONMENT_FILE
                                               + " property is not set, please fix the configuration.");

        final String prefixList = map.get(MeteosatUtils.PREFIX_LIST);
        if (prefixList == null)
            throw new IllegalArgumentException("The key " + MeteosatUtils.PREFIX_LIST
                                               + " property is not set, please fix the configuration.");

        final Logger logger=LoggerFactory.getLogger(this.getClass());
        
        String[] prefixes = prefixList.split(",");
        for (String prefix : prefixes) {
            if (logger.isDebugEnabled()){
                logger.debug("Doing stuff for prefix: "+prefix);
            }
            
            final String mosaicDirName = map.get(prefix + MeteosatUtils.MOSAIC_DIR);
            if (mosaicDirName == null)
                throw new IllegalArgumentException("The key " + prefix + MeteosatUtils.MOSAIC_DIR
                                                   + " property is not set, please fix the configuration.");
            final File mosaicDir=new File(mosaicDirName);
            if (!mosaicDir.exists()){
                throw new IllegalArgumentException("The target mosaic directory: "+mosaicDir+" does not exists.");
            }
            
            final File outFile = new File(tempDir, prefix + ".tif"); // TODO append parsed date

            final String[] args = buildArgs(configDir, tempDir, outFile, prefix, map);

            final File props = new File(configDir, envFileName);

            boolean ret = MeteosatUtils.run(logger, props, null, args);
            Assert.assertTrue(ret);

            if (!ret) {
                Assert.fail();
            }

            // building IMC to add new granule updating mosaic
            ImageMosaicCommand imc = new ImageMosaicCommand();
            List<File> addFileList = new ArrayList<File>();
            addFileList.add(outFile);
            imc.setAddFiles(addFileList);
            imc.setBaseDir(mosaicDir);

            // TODO set specific settings for this mosaic (style, bb, etc...)
            
            // TODO call ImageMosaicAction
            
            // check existence for the same layer in another workspace ... 
            final String layerName=mosaicDir.getName();
            final GeoServerRESTReader reader = new GeoServerRESTReader(gsurl, gsUsr, gsPwd);
            final RESTLayer layer = reader.getLayer(layerName);
            if (layer == null) {
                // publish a new
                GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(gsurl, gsUsr, gsPwd);
                //TODO
                String workspace = "";
                String storename = layerName; // TODO check
                GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
                // TODO set specific settings for this mosaic (style, bb, etc...)
                // ...
                GSLayerEncoder layerEncoder = new GSLayerEncoder();
                // TODO set specific settings for this mosaic (style, bb, etc...)
                // ...
                if (!publisher.publishDBLayer(workspace, storename, fte, layerEncoder)){
                    Assert.fail();
                }
            }
            
            // publish on GeoNetwork


        }




    }

    /**
     * builds args[] array to be used with the RGB python script:
     * /script.py calc.function() OutFile.tif Channel_0.tif ...
     * 
     * @param configDir
     * @param tempDir
     * @param outFile
     * @param prefix
     * @param map
     * @return
     */
    public static String[] buildArgs(File configDir, File tempDir, File outFile, String prefix,
                                     Map<String, String> map) {

        String wildcardsString = map.get(prefix + MeteosatUtils.FILTER);
        if (wildcardsString == null)
            throw new IllegalArgumentException("The key " + prefix + MeteosatUtils.FILTER
                                               + " property is not set, please fix the configuration.");

        final String[] wildcards = wildcardsString.split(",");
        final int size=wildcards.length;
        final File[] channels = new File[size];
        for (int i=0; i<size; i++) {
            final Collector coll = new Collector(new WildcardFileFilter(wildcards[i]));
            final List<File> filteredFiles=coll.collect(tempDir);
            if (filteredFiles.size()==1){
                channels[i]=filteredFiles.get(0);
            } else {
                throw new IllegalArgumentException("Please provide a better filter or check the file channel folder. Filtered file list size is !=0. " 
                                                    +filteredFiles.toString());
            }
        }

        final String calcFunction = map.get(prefix + MeteosatUtils.CALC_FUNCTION);
        if (calcFunction == null)
            throw new IllegalArgumentException("The key " + prefix + MeteosatUtils.CALC_FUNCTION
                                               + " property is not set, please fix the configuration.");

        final String scriptFileName = map.get(prefix + MeteosatUtils.SCRIPT);
        if (calcFunction == null)
            throw new IllegalArgumentException("The key " + prefix + MeteosatUtils.SCRIPT
                                               + " property is not set, please fix the configuration.");

        final File script = new File(configDir, scriptFileName);

        // python ./createRGB_2.py calc.function() OutFile.tif Channel_0.tif ...
        // Channel_n.tif
        final String[] args = new String[channels.length + 3];
        int i = 0;
        args[i] = script.getPath();
        args[++i] = calcFunction;
        args[++i] = outFile.getPath();
        for (File file : channels) {
            args[++i] = file.getPath();
        }
        return args;
    }

}
