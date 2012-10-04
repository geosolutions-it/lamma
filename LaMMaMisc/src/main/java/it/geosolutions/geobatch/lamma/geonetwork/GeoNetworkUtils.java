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
package it.geosolutions.geobatch.lamma.geonetwork;

import it.geosolutions.filesystemmonitor.monitor.FileSystemEvent;
import it.geosolutions.geobatch.action.scripting.ScriptingAction;
import it.geosolutions.geobatch.action.scripting.ScriptingConfiguration;
import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder;
import it.geosolutions.geobatch.flow.event.action.Action;
import it.geosolutions.geobatch.flow.event.action.ActionException;
import it.geosolutions.geobatch.imagemosaic.ImageMosaicOutput;
import it.geosolutions.geonetwork.GNClient;
import it.geosolutions.geonetwork.exception.GNException;
import it.geosolutions.geonetwork.exception.GNLibException;
import it.geosolutions.geonetwork.exception.GNServerException;
import it.geosolutions.geonetwork.util.GNInsertConfiguration;
import it.geosolutions.geonetwork.util.GNSearchRequest;
import it.geosolutions.geonetwork.util.GNSearchResponse;
import it.geosolutions.geonetwork.util.GNSearchResponse.GNMetadata;
import it.geosolutions.tools.freemarker.filter.FreeMarkerFilter;
import it.geosolutions.tools.freemarker.FreeMarkerUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public class GeoNetworkUtils {

    /**
     * key used to enrich the root model map to add the GeoNetwork UUID
     */
    public final static String GN_UUID = "GN_UUID";

    /**
     * the default workspace
     */
    public final static String WORKSPACE = "WORKSPACE";

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

    /**
     * Script Main "execute" function
     **/
    public static List<Map> publishOnGeoNetworkAction(final ProgressListenerForwarder listenerForwarder,
                                                      boolean failIgnore, final File tempDir,
                                                      final File configDir,
                                                      final List<FileSystemEvent> events, Map argsMap,
                                                      final Map map) throws Exception {


        // set workspace
        String workspace = (String)map.get(WORKSPACE);
        if (workspace == null) {
            throw new ActionException(Action.class, "Unable to continue without a " + WORKSPACE
                                                    + " defined, please check your configuration");
        }

        final Logger logger = LoggerFactory.getLogger(GeoNetworkUtils.class);

        // used for geostore
        final List<Map> rootList = new ArrayList<Map>();

        for (FileSystemEvent event : events) {

            File imageMosaicOutput = event.getSource();

            if (logger.isDebugEnabled()) {
                logger.debug("Doing stuff for: " + imageMosaicOutput);
            }

            // GEONETWORK
            // ////////////////////////////////////////////////////////////////

            final String gnTemplateName = (String)map.get(GN_METADATA_TEMPLATE);
            if (gnTemplateName == null)
                throw new IllegalArgumentException("The key " + GN_METADATA_TEMPLATE
                                                   + " property is not set, please fix the configuration.");

            final FreeMarkerFilter gnFilter = new FreeMarkerFilter(new File(configDir, gnTemplateName));

            String gnUrl = (String)map.get(GNURL);
            if (gnUrl == null) {
                gnUrl = "http://localhost:8282/geonetwork";
            }
            String gnUsr = (String)map.get(GNUID);
            if (gnUsr == null) {
                gnUsr = "admin";
            }
            String gnPwd = (String)map.get(GNPWD);
            if (gnPwd == null) {
                gnPwd = "admin";
            }
            final GNClient geonetwork = GeoNetworkUtils.createClientAndLogin(gnUrl, gnUsr, gnPwd);

            String gnCat = (String)map.get(GNCAT);
            String gnGroup = (String)map.get(GNGRP);
            String gnStyleSheet = (String)map.get(GNSTYLE);

            try {
                Map root = GeoNetworkUtils.publishOnGeoNetwork(tempDir, imageMosaicOutput, gnFilter,
                                                               geonetwork, gnCat, gnGroup, gnStyleSheet,
                                                               failIgnore, logger);
                rootList.add(root);
            } catch (ActionException e) {
                if (failIgnore) {
                    logger.error(e.getLocalizedMessage(), e);
                    continue;
                } else {
                    listenerForwarder.failed(e);
                    throw e;
                }
            }

        } // for each file into the queue

        listenerForwarder.completed();
        return rootList;
    }

    private final static XStream xstream = new XStream();

    public static Map publishOnGeoNetwork(File tempDir, File mosaicOutput, FreeMarkerFilter gnFilter,
                                          GNClient geonetwork, String gnCat, String gnGroup,
                                          String gnStyleSheet, boolean failIgnore, Logger logger)
        throws ActionException, IllegalArgumentException, IOException {

        // use Freemarker to produce metadata for geoNetwork
        final File gnMetadataFile;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        Map root = null;
        String resourceName;
        try {

            fis = new FileInputStream(mosaicOutput);
            bis = new BufferedInputStream(fis);
            root = (Map)xstream.fromXML(bis);
            resourceName = (String)root.get(ImageMosaicOutput.LAYERNAME);
            gnMetadataFile = File.createTempFile(resourceName, ".xml", tempDir);
            FreeMarkerUtils.freeMarker(root, gnFilter, gnMetadataFile);
        } catch (Exception e) {
            throw new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(fis);
        }

        // GeoNetwork
        try {
            geoNetworkUpdate(geonetwork, root, gnMetadataFile, resourceName, gnCat, gnGroup, gnStyleSheet);
        } catch (Exception e) {
            if (failIgnore) {
                if (logger != null && logger.isErrorEnabled()) {
                    logger.error(e.getLocalizedMessage(), e);
                }
                // continue;
                // try publishing on GeoStore at least
            } else {
                throw new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
            }
        }

        return root;
    }

    /**
     * update/insert into geonetwork<br>
     * 
     * also enrich the root with GN_UUID
     * 
     * @param client
     * @param root
     * @param gnMetadataFile
     * @param metadataTitle
     * @throws ActionException
     */
    public static void geoNetworkUpdate(GNClient client, Map root, File gnMetadataFile, String metadataTitle,
                                        String gnCat, String gnGroup, String gnStyleSheet)
        throws ActionException {

        if (client == null) {
            throw new ActionException(Action.class, "Unable to connect to geonetwork");
        }
        try {
            // search METADATA by TITLE and get ID
            final GNSearchResponse searchResponse = searchMetadata(client, metadataTitle);
            // if TITLE is not found -> INSERT
            if (searchResponse.getCount() == 0) {
                // create metadata using
                final GNInsertConfiguration insertRequest = new GNInsertConfiguration();
                // add metadata to GeoNetwork
                if (gnCat == null) {
                    gnCat = "datasets";
                }
                insertRequest.setCategory(gnCat);

                if (gnGroup == null) {
                    gnGroup = "1";
                }
                insertRequest.setGroup(gnGroup); // group 1 is usually "all"

                if (gnStyleSheet == null) {
                    gnStyleSheet = "_none_";
                }
                insertRequest.setStyleSheet(gnStyleSheet);
                insertRequest.setValidate(Boolean.FALSE);

                Long id = client.insertMetadata(insertRequest, gnMetadataFile);

                // search the added metadata to get the UUID
                GNSearchResponse addedMetadataSearchResponse = searchMetadata(client, metadataTitle);
                if (addedMetadataSearchResponse.getCount() != 0) {
                    GNMetadata metadata = addedMetadataSearchResponse.getMetadata(0);
                    String uuid = metadata.getUUID();
                    ((Map)root).put(GeoNetworkUtils.GN_UUID, uuid);
                }

            } else {
                // else -> UPDATE
                GNMetadata metadata = searchResponse.getMetadata(0);
                Long id = metadata.getId();
                String uuid = metadata.getUUID();

                ((Map)root).put(GeoNetworkUtils.GN_UUID, uuid);

                client.updateMetadata(id, gnMetadataFile);

                // TODO CHECK IS the UUID CHANGED?
            }
        } catch (GNException e) {
            throw new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
        }
    }

    public static GNClient createClientAndLogin(String gnURL, String gnUSR, String gnPWD) {
        GNClient client = new GNClient(gnURL);
        if (!client.login(gnUSR, gnPWD)) {
            return null;
        } else {
            return client;
        }

    }

    public static GNSearchResponse searchMetadataByTitle(GNClient client, String title)
        throws GNLibException, GNServerException {
        GNSearchRequest searchRequest = new GNSearchRequest();
        searchRequest.addParam(GNSearchRequest.Param.title, title);
        return client.search(searchRequest);
    }

    /**
     * TODO by ID
     * 
     * @param client
     * @param title
     * @return
     * @throws GNLibException
     * @throws GNServerException
     */
    public static GNSearchResponse searchMetadata(GNClient client, String title) throws GNLibException,
        GNServerException {
        GNSearchRequest searchRequest = new GNSearchRequest();
        searchRequest.addParam(GNSearchRequest.Param.title, title);
        return client.search(searchRequest);
    }
}
