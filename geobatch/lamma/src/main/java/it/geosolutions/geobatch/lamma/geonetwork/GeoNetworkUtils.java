/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  http://code.google.com/p/geobatch/
 *  Copyright (C) 2007-2013 GeoSolutions S.A.S.
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

import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder;
import it.geosolutions.geobatch.flow.event.action.Action;
import it.geosolutions.geobatch.flow.event.action.ActionException;
import it.geosolutions.geobatch.imagemosaic.ImageMosaicOutput;
import it.geosolutions.geonetwork.GNClient;
import it.geosolutions.geonetwork.exception.GNException;
import it.geosolutions.geonetwork.exception.GNLibException;
import it.geosolutions.geonetwork.exception.GNServerException;
import it.geosolutions.geonetwork.util.GNInsertConfiguration;
import it.geosolutions.tools.freemarker.filter.FreeMarkerFilter;
import it.geosolutions.tools.freemarker.FreeMarkerUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.geosolutions.geonetwork.op.GNMetadataGetInfo.MetadataInfo;
import it.geosolutions.geonetwork.util.GNPrivConfiguration;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

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

    public final static String GNPRIV = "GNPRIV";

    /**
     * GeoNetwork the key matching the path (relative to the configDir where the GN metadata template is located
     */
    public final static String GN_METADATA_TEMPLATE = "GN_METADATA_TEMPLATE";

    /**
     * Script Main "execute" function
     *
     * May enrich the metadata maps with the UUID of the geonetwork metadata
     */
    public static void publishOnGeoNetworkAction(final ProgressListenerForwarder listenerForwarder,
            boolean failIgnore, final File tempDir,
            final File configDir,
            final Map<File, Map> rootList, Map argsMap,
            final Map mapFromConfig) throws Exception {

        final Logger logger = LoggerFactory.getLogger(GeoNetworkUtils.class);

        if( ! checkGNParams(mapFromConfig) ) {
            if( logger.isWarnEnabled()) {
                logger.warn("GeoNetwork configuration is incomplete, GN will not be used");
            }
            return;       
        }

        GNClient geonetworkClient = createClientAndLogin(mapFromConfig);
        if (geonetworkClient == null) {
            throw new ActionException(Action.class, "Unable to connect to GeoNetwork");
        }
        
        final String gnTemplateName = (String) mapFromConfig.get(GN_METADATA_TEMPLATE);
        if (gnTemplateName == null) {
            throw new IllegalArgumentException("The key " + GN_METADATA_TEMPLATE
                    + " property is not set, please fix the configuration.");
        }

        final FreeMarkerFilter gnFilter = new FreeMarkerFilter(new File(configDir, gnTemplateName));

        GNInsertConfiguration insertConfiguration = createGNInsertConfiguration(mapFromConfig);
        GNPrivConfiguration   privConfiguration   = createGNPrivConfiguration(mapFromConfig);

        for (Map.Entry<File, Map> fileMetadata : rootList.entrySet()) {
            File file = fileMetadata.getKey();
            Map metadataMap = fileMetadata.getValue();

            if (logger.isDebugEnabled()) {
                logger.debug("Running GeoNetwork processing on " + file);
            }

            listenerForwarder.setTask("GN: publishing " + metadataMap.get(ImageMosaicOutput.LAYERNAME));
            listenerForwarder.progressing();
        
            try {
                String gnUuid = GeoNetworkUtils.publishOnGeoNetwork(metadataMap, tempDir, gnFilter,
                        geonetworkClient, insertConfiguration, privConfiguration,
                        failIgnore, logger);

                if(gnUuid != null) {
                    metadataMap.put(GeoNetworkUtils.GN_UUID, gnUuid);
                }

            } catch (ActionException e) {
                if (failIgnore) {
                    logger.error(e.getLocalizedMessage(), e);
                    continue;
                } else {
                    listenerForwarder.failed(e);
                    throw e;
                }
            }
        }
    }


    /**
     * @return the UUID of the metadata in GeoNetwork
     */
    public static String publishOnGeoNetwork(Map root, File tempDir, FreeMarkerFilter gnFilter,
            GNClient geonetwork,
            GNInsertConfiguration insertConfiguration, GNPrivConfiguration privConfiguration,
            boolean failIgnore, Logger logger)
                throws ActionException, IllegalArgumentException, IOException {

        // use Freemarker to produce metadata for geoNetwork
        final File gnMetadataFile;

        String resourceName;
        try {
            resourceName = (String) root.get(ImageMosaicOutput.LAYERNAME);
            gnMetadataFile = File.createTempFile(resourceName, ".xml", tempDir);
            FreeMarkerUtils.freeMarker(root, gnFilter, gnMetadataFile);
        } catch (Exception e) {
            throw new ActionException(Action.class, e.getLocalizedMessage(), e);
        }

        try {
            geoNetworkUpdate(geonetwork, gnMetadataFile, resourceName, insertConfiguration, privConfiguration);
            return resourceName;
//            root.put(GeoNetworkUtils.GN_UUID, resourceName);
        } catch (Exception e) {
            if (failIgnore) {
                if (logger != null && logger.isErrorEnabled()) {
                    logger.error(e.getLocalizedMessage(), e);
                }
                return null;
                // continue;
                // try publishing on GeoStore at least
            } else {
                throw new ActionException(Action.class, e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * update/insert into geonetwork<br>
     *
     * @throws ActionException
     */
    public static void geoNetworkUpdate(GNClient client, File gnMetadataFile, String metadataUuid,
            GNInsertConfiguration insertConfiguration, GNPrivConfiguration privConfiguration)
            throws ActionException {

        final Logger logger = LoggerFactory.getLogger(GeoNetworkUtils.class);

        try {            
            MetadataInfo info = getByUuid(client, metadataUuid);

            if (info == null) {
                logger.info("Inserting metadata with uuid " + metadataUuid);
                long id = client.insertMetadata(insertConfiguration, gnMetadataFile);
                if(privConfiguration != null) {
                    logger.info("Setting privileges to metadata with uuid " + metadataUuid);
                    client.setPrivileges(id, privConfiguration);
                }
            } else {
                logger.info("Updating metadata with uuid " + metadataUuid);
                client.updateMetadata(info.getId(), info.getVersion(), gnMetadataFile);
            }

        } catch (GNException e) {
            throw new ActionException(Action.class, e.getLocalizedMessage(), e);
        }
    }

    public static GNClient createClientAndLogin(final Map map) {
        String gnUrl = (String) map.get(GNURL);
        if (gnUrl == null) {
            gnUrl = "http://localhost:8282/geonetwork";
        }
        String gnUsr = (String) map.get(GNUID);
        if (gnUsr == null) {
            gnUsr = "admin";
        }
        String gnPwd = (String) map.get(GNPWD);
        if (gnPwd == null) {
            gnPwd = "admin";
        }

        GNClient client = new GNClient(gnUrl);
        if (!client.login(gnUsr, gnPwd)) {
            return null;
        } else {
            return client;
        }
    }

    public static boolean checkGNParams(final Map map) {
        if (StringUtils.isBlank((String) map.get(GNURL))) {
            return false;
        }
        if (StringUtils.isBlank((String) map.get(GNUID))) {
            return false;
        }
        if (StringUtils.isBlank((String) map.get(GNPWD))) {
            return false;
        }
        return true;
    }


    public static MetadataInfo getByUuid(GNClient client, String uuid) throws GNLibException, GNServerException {

        try {
            return client.getInfo(uuid, true);
        } catch (GNServerException ex) {
            // usually it's a metadata not found, but no way to check for it (e.g. no 404 returned)
            return null;
        }
    }

    protected static GNInsertConfiguration createGNInsertConfiguration(Map mapFromConfig) {

        final GNInsertConfiguration insertRequest = new GNInsertConfiguration();

        String gnCat = (String) mapFromConfig.get(GNCAT);
        if (gnCat == null) {
            gnCat = "datasets";
        }
        insertRequest.setCategory(gnCat);

        String gnGroup = (String) mapFromConfig.get(GNGRP);
        if (gnGroup == null) {
            gnGroup = "1";
        }
        insertRequest.setGroup(gnGroup); // group 1 is usually "all"

        String gnStyleSheet = (String) mapFromConfig.get(GNSTYLE);
        if (gnStyleSheet == null) {
            gnStyleSheet = "_none_";
        }
        insertRequest.setStyleSheet(gnStyleSheet);

        insertRequest.setValidate(Boolean.FALSE);

        return insertRequest;
    }

    protected static GNPrivConfiguration createGNPrivConfiguration(Map mapFromConfig) {

        String gnPriv = (String) mapFromConfig.get(GNPRIV);
        if (gnPriv == null) {
            return null;
        }

        final Logger logger = LoggerFactory.getLogger(GeoNetworkUtils.class);
        GNPrivConfiguration pcfg = new GNPrivConfiguration();

        for (StringTokenizer stringTokenizer = new StringTokenizer(gnPriv, ", "); stringTokenizer.hasMoreTokens();) {
            String token = stringTokenizer.nextToken().trim();
            if( ! token.contains("_")) {
                logger.warn("Privileges error: bad token '"+token+"'");
                return null;
            }
            String group = token.substring(0,token.indexOf("_"));
            String privs = token.substring(token.indexOf("_")+1);

            try{
                pcfg.addPrivileges(Integer.valueOf(group), privs);
            } catch(Exception ex) {
                logger.warn("Privileges error: bad privileges: " + ex.getMessage());
                return null;
            }
        }

        return pcfg;
    }
    
}
