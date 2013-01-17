/*
 *  Copyright (C) 2007 - 2013 GeoSolutions S.A.S.
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

import com.thoughtworks.xstream.XStream;
import it.geosolutions.filesystemmonitor.monitor.FileSystemEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class ImageMosaicUtils {

    protected final static Logger LOGGER = LoggerFactory.getLogger(MergeImageUtils.class);

    /**
     * Load the metadata map as saved by the ImageMosaic Action
     * 
     * @throws IOException
     */
    public static Map loadMetadata(File mdataMapFile)
            throws IOException  {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            XStream xstream = new XStream();
            fis = new FileInputStream(mdataMapFile);
            bis = new BufferedInputStream(fis);
            return (Map) xstream.fromXML(bis);
        } catch (Exception e) {
            throw new IOException("Error loading metadata file " + mdataMapFile + ": " + e.getLocalizedMessage(), e);
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(fis);
        }
    }

    /**
     * Load the metadata maps as saved by the ImageMosaic Action
     *
     * @throws IOException
     */
    public static Map<File, Map> loadMetadataMap(final List<FileSystemEvent> events) throws IOException {

        final Map<File, Map> ret = new HashMap<File, Map>();

        for (FileSystemEvent event : events) {

            File imageMosaicOutput = event.getSource();

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Loading ImageMosaic metadata from " + imageMosaicOutput);
            }

            Map metadataMap = loadMetadata(imageMosaicOutput);
            ret.put(imageMosaicOutput, metadataMap);
        }

        return ret;
    }

}
