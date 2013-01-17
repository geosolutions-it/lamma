package it.geosolutions.geobatch.lamma.models;

import it.geosolutions.filesystemmonitor.monitor.FileSystemEvent;
import it.geosolutions.geobatch.action.scripting.ScriptingAction;
import it.geosolutions.geobatch.action.scripting.ScriptingConfiguration;
import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder;
import it.geosolutions.geobatch.flow.event.action.Action;
import it.geosolutions.geobatch.flow.event.action.ActionException;
import it.geosolutions.geobatch.imagemosaic.ImageMosaicOutput;
import it.geosolutions.geobatch.lamma.geonetwork.GeoNetworkUtils;
import it.geosolutions.geobatch.lamma.geostore.GeoStoreUtils;
import it.geosolutions.geobatch.lamma.misc.ImageMosaicUtils;
import it.geosolutions.geostore.services.rest.GeoStoreClient;
import it.geosolutions.tools.freemarker.filter.FreeMarkerFilter;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelsUtils {
    
    public static String resolveResourceName(Collection<Map> rootList){
        String storeName=(String)rootList.iterator().next().get(ImageMosaicOutput.STORENAME);
        return resolveResourceName(storeName);
    }
    
    public static String resolveResourceName(String storeName){
        return storeName.substring(storeName.lastIndexOf("_")+1);
    }

    /**
     * Script Main "execute" function
     **/
    public static Map GeoNetworkAndGeoStore(Map argsMap) throws Exception {

        final ScriptingConfiguration configuration = (ScriptingConfiguration)argsMap
            .get(ScriptingAction.CONFIG_KEY);
    
        boolean failIgnore = true;
        // TODO
        if (!configuration.isFailIgnored()) {
            failIgnore = false;
        }
    
        final Map cfgProperties = configuration.getProperties();
    
        final File tempDir = (File)argsMap.get(ScriptingAction.TEMPDIR_KEY);
        final File configDir = (File)argsMap.get(ScriptingAction.CONFIGDIR_KEY);
        final ProgressListenerForwarder listenerForwarder = (ProgressListenerForwarder)argsMap
            .get(ScriptingAction.LISTENER_KEY);
        final List<FileSystemEvent> events = (List)argsMap.get(ScriptingAction.EVENTS_KEY);

        listenerForwarder.setTask("Loading metadata map");
        listenerForwarder.progressing();

        Map<File, Map> rootList = ImageMosaicUtils.loadMetadataMap(events);

        listenerForwarder.setTask("Publishing to GeoNetwork");
        listenerForwarder.progressing();

        GeoNetworkUtils.publishOnGeoNetworkAction(listenerForwarder,failIgnore,tempDir,configDir, rootList, argsMap, cfgProperties);

        listenerForwarder.setTask("Publishing to GeoStore");
        listenerForwarder.progressing();

        argsMap = ModelsUtils.publishOnGeoStoreAction(listenerForwarder,failIgnore, rootList.values(), argsMap, cfgProperties, configDir);

        listenerForwarder.setTask("Publishing complete");
        listenerForwarder.progressing();
        listenerForwarder.completed();
        
        listenerForwarder.completed();
        return argsMap;
    }

    /**
     * @param rootList list of map (datamodels)
     * @param argsMap
     * @return
     * @throws Exception
     */
    /**
     * @param rootList list of map (datamodels)
     * @param argsMap
     * @return
     * @throws Exception
     */
    public static Map publishOnGeoStoreAction(final ProgressListenerForwarder listenerForwarder , final boolean failIgnore, final Collection<Map> rootList , final Map argsMap, final Map cfgProps, final File configDir) throws Exception {
    
        // set workspace
        String workspace = (String)cfgProps.get(GeoStoreUtils.WORKSPACE);
        if (workspace == null) {
            throw new ActionException(Action.class, "Unable to continue without a " + GeoStoreUtils.WORKSPACE
                                                    + " defined, please check your configuration");
        }
    
        final Logger logger = LoggerFactory
            .getLogger(GeoStoreUtils.class);
    
        // GEOSTORE
        // ////////////////////////////////////////////////////////////////
    
        final String gstTemplateName = (String)cfgProps.get(GeoStoreUtils.GST_METADATA_TEMPLATE);
        if (gstTemplateName == null)
            throw new IllegalArgumentException("The key " + GeoStoreUtils.GST_METADATA_TEMPLATE
                                               + " property is not set, please fix the configuration.");
        final FreeMarkerFilter gstFilter = new FreeMarkerFilter(new File(configDir, gstTemplateName));
    
        
    
        String gstUrl = (String)cfgProps.get(GeoStoreUtils.GSTURL);
        if (gstUrl == null) {
            gstUrl = "http://localhost:8383/geostore/rest/";
        }
        String gstUsr = (String)cfgProps.get(GeoStoreUtils.GSTUID);
        if (gstUsr == null) {
            gstUsr = "admin";
        }
        String gstPwd = (String)cfgProps.get(GeoStoreUtils.GSTPWD);
        if (gstPwd == null) {
            gstPwd = "admin";
        }
        // init geostore parameter connection
        final GeoStoreClient geostore = new GeoStoreClient();
        geostore.setGeostoreRestUrl(gstUrl);
        geostore.setUsername(gstUsr);
        geostore.setPassword(gstPwd);
    
        // GeoStore
        try {
            final String gstLayerTemplateName = (String)cfgProps.get(GeoStoreUtils.GST_LAYER_TEMPLATE);
            if (gstLayerTemplateName == null) {
                // use workspace as GeoStore Category and resource
                GeoStoreUtils.publishOnGeoStore(logger, gstFilter, geostore, rootList, workspace,resolveResourceName(rootList));
            } else {
                final FreeMarkerFilter gstLayerFilter = new FreeMarkerFilter(new File(configDir,
                                                                                      gstLayerTemplateName));
                // use workspace as GeoStore Category and resource
                GeoStoreUtils.publishAndUpdateOnGeoStore(logger, gstFilter, gstLayerFilter, geostore, rootList, workspace,resolveResourceName(rootList));
            }
        } catch (Exception e) {
            if (failIgnore) {
                if (logger.isErrorEnabled()) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            } else {
                if (logger.isErrorEnabled()) {
                    logger.error(e.getLocalizedMessage(), e);
                }
                ActionException ae = new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
                listenerForwarder.failed(ae);
                throw ae;
            }
        }
    
        listenerForwarder.completed();
        return argsMap;
    }
}
