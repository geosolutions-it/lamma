package it.geosolutions.geobatch.lamma.geostore;

import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder;
import it.geosolutions.geobatch.flow.event.action.Action;
import it.geosolutions.geobatch.flow.event.action.ActionException;
import it.geosolutions.geobatch.imagemosaic.ImageMosaicOutput;
import it.geosolutions.geostore.core.model.Category;
import it.geosolutions.geostore.services.dto.ShortResource;
import it.geosolutions.geostore.services.dto.search.AndFilter;
import it.geosolutions.geostore.services.dto.search.BaseField;
import it.geosolutions.geostore.services.dto.search.CategoryFilter;
import it.geosolutions.geostore.services.dto.search.FieldFilter;
import it.geosolutions.geostore.services.dto.search.SearchFilter;
import it.geosolutions.geostore.services.dto.search.SearchOperator;
import it.geosolutions.geostore.services.rest.GeoStoreClient;
import it.geosolutions.geostore.services.rest.model.CategoryList;
import it.geosolutions.geostore.services.rest.model.RESTCategory;
import it.geosolutions.geostore.services.rest.model.RESTResource;
import it.geosolutions.geostore.services.rest.model.ShortResourceList;
import it.geosolutions.tools.freemarker.FreeMarkerUtils;
import it.geosolutions.tools.freemarker.filter.FreeMarkerFilter;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeoStoreUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(GeoStoreUtils.class);

    /**
     * using the passed FreeMarkerFilter and the root DataModel creates a
     * GeoStore Layer as JSONObject
     * 
     * @param layerFilter
     * @param root
     * @return
     * @throws Exception
     */
    public static JSONObject createGeoStoreLayer(FreeMarkerFilter layerFilter, Map root) throws Exception {

        String filteredLayer = FreeMarkerUtils.freeMarkerToString(root, layerFilter);

        return new JSONObject(filteredLayer);
    }

    /**
     * see {@link TemplateModelEvent} into FreeMarkerAction
     */
    public static final String EVENT_KEY = "event";

    public static Long appendNewResource(Logger logger, FreeMarkerFilter gstFilter, GeoStoreClient geostore,
                                         Collection<Map> mapList, RESTCategory gstCat, String resourceName)
        throws ActionException {

        String gstData = null;
        final RESTResource gstRes = new RESTResource();
        try {
            Map root = new HashMap();
            root.put(GeoStoreUtils.EVENT_KEY, mapList);
            gstData = FreeMarkerUtils.freeMarkerToString(root, gstFilter);

            if (logger != null && logger.isDebugEnabled())
                logger.debug(gstData);

            gstRes.setCategory(gstCat);
            gstRes.setName(resourceName);
            gstRes.setData(gstData);

            return geostore.insert(gstRes);

        } catch (Exception e) {
            throw new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
        }
    }

    /**
     * 
     * publish a new resource (removing existing one) using the same name for
     * catalog and resource
     * 
     * @param logger
     * @param gstFilter
     * @param geostore
     * @param mapList
     * @param resourceName
     * @throws ActionException
     */
    public static void publishOnGeoStore(Logger logger, FreeMarkerFilter gstFilter, GeoStoreClient geostore,
                                         Collection<Map> mapList, String resourceName) throws ActionException {
        publishOnGeoStore(logger, gstFilter, geostore, mapList, resourceName, resourceName);
    }

    /**
     * publish a new resource (removing existing one)
     * 
     * @param logger
     * @param gstFilter
     * @param geostore
     * @param mapList
     * @param categoryName
     * @param resourceName
     * @throws ActionException
     */
    public static void publishOnGeoStore(Logger logger, FreeMarkerFilter gstFilter, GeoStoreClient geostore,
                                         Collection<Map> mapList, String categoryName, String resourceName)
        throws ActionException {
        RESTCategory gstCat = searchCategoryByName(geostore, categoryName);
        Long catId = null;
        if (gstCat == null) {
            gstCat = new RESTCategory();
            gstCat.setName(categoryName);
            catId = geostore.insert(gstCat);
        }

        Long resId = searchResourceIdByName(logger, geostore, gstCat.getName(), resourceName);
        if (resId != null) {
            removeResource(logger, geostore, categoryName, resourceName);
        }
        appendNewResource(logger, gstFilter, geostore, mapList, gstCat, resourceName);
    }

    /**
     * publish or update a resource using the same name for catalog and resource
     * 
     * @param logger
     * @param gstFilter
     * @param layerFilter
     * @param geostore
     * @param mapList
     * @param resourceName
     * @throws ActionException
     */
    public static void publishAndUpdateOnGeoStore(Logger logger, FreeMarkerFilter gstFilter,
                                                  FreeMarkerFilter layerFilter, GeoStoreClient geostore,
                                                  Collection<Map> mapList, String resourceName)
        throws ActionException {

        publishAndUpdateOnGeoStore(logger, gstFilter, layerFilter, geostore, mapList, resourceName,
                                   resourceName);
    }

    /**
     * publish or update a resource into a catalog (works in append mode)
     * 
     * @param logger
     * @param gstFilter
     * @param layerFilter
     * @param geostore
     * @param mapList
     * @param categoryName
     * @param resourceName
     * @throws ActionException
     */
    public static void publishAndUpdateOnGeoStore(Logger logger, FreeMarkerFilter gstFilter,
                                                  FreeMarkerFilter layerFilter, GeoStoreClient geostore,
                                                  Collection<Map> mapList, String categoryName, String resourceName)
        throws ActionException {

        RESTCategory gstCat = searchCategoryByName(geostore, categoryName);
        Long catId = null;
        if (gstCat == null) {
            gstCat = new RESTCategory();
            gstCat.setName(categoryName);
            catId = geostore.insert(gstCat);
        }

        Long resId = searchResourceIdByName(logger, geostore, gstCat.getName(), resourceName);
        if (resId == null) {
            appendNewResource(logger, gstFilter, geostore, mapList, gstCat, resourceName);
        } else {
            updateResource(logger, layerFilter, geostore, mapList, resId);
        }
    }

    /**
     * return true if resource is successfully found and removed
     * 
     * @param logger
     * @param geostore
     * @param categoryName
     * @param resourceName
     * @return
     * @throws ActionException
     */
    public static boolean removeResource(Logger logger, GeoStoreClient geostore, String categoryName,
                                         String resourceName) throws ActionException {

        RESTCategory gstCat = searchCategoryByName(geostore, categoryName);
        Long catId = null;
        if (gstCat == null) {
            catId = geostore.insert(gstCat);
        }

        Long resId = searchResourceIdByName(logger, geostore, gstCat.getName(), resourceName);
        if (resId == null) {
            if (logger != null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Resource named: " + resourceName + " not found under category: "
                                + categoryName);
                }
            }
            return false;
        }

        try {
            geostore.deleteResource(resId);
        } catch (Exception e) {
            if (logger != null) {
                if (logger.isErrorEnabled()) {
                    logger.error("Problem removing resource : " + categoryName);
                }
            }
            throw new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
        }
        if (logger != null) {
            if (logger.isInfoEnabled()) {
                logger.info("Resource named: " + resourceName + " [ID: " + resId
                            + "] succesfully removed from category: " + categoryName);
            }
        }
        return true;
    }

    public static boolean removeCategory(Logger logger, GeoStoreClient geostore, String categoryName)
        throws ActionException {

        RESTCategory gstCat = searchCategoryByName(geostore, categoryName);
        if (gstCat != null) {
            try {
                geostore.deleteCategory(gstCat.getId());
            } catch (Exception e) {
                if (logger != null) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Problem removing category : " + categoryName);
                    }
                }
                throw new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
            }
        } else {
            if (logger != null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Category named: " + categoryName);
                }
            }
            return false;
        }

        if (logger != null) {
            if (logger.isInfoEnabled()) {
                logger.info("Category named: " + categoryName + " [ID: " + gstCat.getId()
                            + " ] successfully removed");
            }
        }
        return true;
    }

    /**
     * update an existing resource with new layers using data contained into
     * mapList
     * 
     * @param logger
     * @param layerFilter
     * @param geostore
     * @param mapList
     * @param gstResId
     * @throws ActionException
     */
    public static void updateResource(Logger logger, FreeMarkerFilter layerFilter, GeoStoreClient geostore,
                                      Collection<Map> mapList, Long gstResId) throws ActionException {
        String gstData = null;
        try {
            String json = geostore.getData(gstResId);
            JSONObject jsonObj = GSBlob.getBlob(json);
            JSONObject jsonMap = GSBlob.getMap(jsonObj);
            if (jsonMap != null) {
                final JSONArray updatedLayers = new JSONArray();
                final JSONArray layers = GSBlob.getLayers(jsonMap);
                if (layers != null) {

                    int size = layers.length();
                    for (int i = 0; i < size; i++) {
                        final JSONObject layer = layers.getJSONObject(i);
                        // workspace:layername
                        final String jsonLayerName = layer.getString("name");
                        if (jsonLayerName == null) {
                            // not recognized layer
                            if (logger != null) {
                                if (logger.isWarnEnabled()) {
                                    logger.warn("layer name not found: " + layer.toString());
                                }
                                if (logger.isInfoEnabled()) {
                                    logger.info("Appending to the layer list: " + layer.toString());
                                }
                            }
                            updatedLayers.put(layer);
                            continue;

                        }
                        final String layerName[] = jsonLayerName.split(":");
                        if (layerName.length != 2) {
                            // not recognized layer
                            if (logger != null) {
                                if (logger.isWarnEnabled()) {
                                    logger.warn("layer \'name\' not recognized as \'workspace:layername\' : "
                                                + jsonLayerName);
                                }
                                if (logger.isInfoEnabled()) {
                                    logger.info("Appending to the layer list the layer named: "
                                                + jsonLayerName);
                                }
                            }
                            updatedLayers.put(layer);
                            continue;
                        }

                        final String workspace = layerName[0];
                        final String name = layerName[1];
                        final Iterator<Map> mapListIt = mapList.iterator();
                        boolean updated = false;
                        while (mapListIt.hasNext()) {// ||!updated
                            final Map<String, Object> map = mapListIt.next();
                            final Object workspaceNameFromMosaic = map.get(ImageMosaicOutput.WORKSPACE);
                            // search for existing (to update) layers
                            if (workspaceNameFromMosaic != null && workspaceNameFromMosaic.equals(workspace)) {
                                final Object layerNameFromMosaic = map.get(ImageMosaicOutput.LAYERNAME);
                                if (layerNameFromMosaic != null && layerNameFromMosaic.equals(name)) {

                                    final JSONObject newLayer = GeoStoreUtils
                                        .createGeoStoreLayer(layerFilter, map);

                                    updatedLayers.put(newLayer);

                                    if (logger != null && logger.isInfoEnabled())
                                        logger.info("Updating layer: " + layer.toString());

                                    mapListIt.remove();
                                    updated = true;
                                    break;
                                }
                            }
                        }
                        if (!updated) {
                            // append not updated layer
                            updatedLayers.put(layer);
                        }

                    }
                    // adding remaining maps (new layers)
                    final Iterator<Map> mapListIt = mapList.iterator();
                    while (mapListIt.hasNext()) {

                        final Map<String, Object> map = mapListIt.next();
                        final JSONObject newLayer = GeoStoreUtils.createGeoStoreLayer(layerFilter, map);

                        updatedLayers.put(newLayer);

                        if (logger != null && logger.isInfoEnabled())
                            logger.info("Adding new layer: " + newLayer.toString());
                    }
                    GSBlob.putLayers(jsonMap, updatedLayers);
                } else {
                    throw new IllegalStateException("No layers found");
                }
            } else {
                throw new IllegalStateException("No map found");
            }

            if (logger != null && logger.isDebugEnabled())
                logger.debug(jsonObj.toString());

            geostore.setData(gstResId, jsonObj.toString());
        } catch (Exception e) {
            throw new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
        }
    }

    /**
     * returns the RESTCategory if it is found, null otherwise
     * 
     * @param geostore
     * @param resourceName
     * @return
     * @throws ActionException
     */
    public static RESTCategory searchCategoryByName(GeoStoreClient geostore, String resourceName)
        throws ActionException {

        CategoryList gstCatList = null;

        try {
            gstCatList = geostore.getCategories();
        } catch (Exception e) {
            throw new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
        }
        if (gstCatList != null && gstCatList.getList() != null && !gstCatList.getList().isEmpty()) {
            final Iterator<Category> itCat = gstCatList.getList().iterator();
            while (itCat.hasNext()) {
                Category cat = itCat.next();
                if (cat.getName().equals(resourceName)) {
                    RESTCategory restCat = new RESTCategory(cat.getId());
                    restCat.setName(cat.getName());
                    return restCat;
                }
            }
        }
        return null;
    }

    /**
     * Search a resource by name into the category named {@link #categoryName}
     * 
     * @param logger
     * @param geostore
     * @param categoryName
     * @param resourceName
     * @return
     * @throws ActionException
     */
    public static Long searchResourceIdByName(Logger logger, GeoStoreClient geostore, String categoryName,
                                              String resourceName) throws ActionException {

        final SearchFilter gstResFilter = new AndFilter(new FieldFilter(BaseField.NAME, resourceName,
                                                                        SearchOperator.EQUAL_TO),
                                                        new CategoryFilter(categoryName,
                                                                           SearchOperator.EQUAL_TO));
        ShortResourceList gstResList = null;
        try {
            gstResList = geostore.searchResources(gstResFilter);
        } catch (Exception e) {
            throw new ActionException(Action.class, e.getLocalizedMessage(), e.getCause());
        }

        if (gstResList != null && gstResList.getList() != null && !gstResList.getList().isEmpty()) {
            final Iterator<ShortResource> itRes = gstResList.getList().iterator();
            while (itRes.hasNext()) {
                ShortResource res = itRes.next();
                if (res.getName().equals(resourceName)) {
                    return res.getId();
                }
            }
        }
        if (logger != null && logger.isInfoEnabled()) {
            logger.info("Unable to locate the resource named: " + categoryName + ":" + resourceName);
        }
        return null;
    }

    /**
     * the default workspace
     */
    public static final String WORKSPACE = "WORKSPACE";

    /**
     * the key to get the GeoStore Url
     */
    public static final String GSTURL = "GSTURL";
    /**
     * the key to get the GeoStore user id
     */
    public static final String GSTUID = "GSTUID";
    /**
     * the key to get the GeoStore pass
     */
    public static final String GSTPWD = "GSTPWD";
    /**
     * GeoStore the key matching the path (relative to the configDir where the
     * GST metadata template is located
     */
    public static final String GST_METADATA_TEMPLATE = "GST_METADATA_TEMPLATE";
    /**
     * GeoStore the key matching the path (relative to the configDir where the
     * GST metadata layer template is located this template must define only a
     * layer
     */
    public static final String GST_LAYER_TEMPLATE = "GST_LAYER_TEMPLATE";

    /**
     * @param rootList list of map (datamodels)
     * @param argsMap
     * @return
     * @throws Exception
     */
    public static Map publishOnGeoStoreAction(final ProgressListenerForwarder listenerForwarder,
                                              final boolean failIgnore, final Collection<Map> rootList,
                                              final Map argsMap, final Map cfgProps, final File configDir)
        throws Exception {

        // set workspace
        String workspace = (String)cfgProps.get(WORKSPACE);
        if (workspace == null) {
            throw new ActionException(Action.class, "Unable to continue without a " + WORKSPACE
                                                    + " defined, please check your configuration");
        }

        // GEOSTORE
        // ////////////////////////////////////////////////////////////////

        final String gstTemplateName = (String)cfgProps.get(GST_METADATA_TEMPLATE);
        if (gstTemplateName == null)
            throw new IllegalArgumentException("The key " + GST_METADATA_TEMPLATE
                                               + " property is not set, please fix the configuration.");
        final FreeMarkerFilter gstFilter = new FreeMarkerFilter(new File(configDir, gstTemplateName));

        String gstUrl = (String)cfgProps.get(GSTURL);
        if (gstUrl == null) {
            LOGGER.warn("GeoStore URL is null. Forcing test configuration");
            gstUrl = "http://localhost:8383/geostore/rest/";
        }
        String gstUsr = (String)cfgProps.get(GSTUID);
        if (gstUsr == null) {
            LOGGER.warn("GeoStore user is null. Forcing test configuration");
            gstUsr = "admin";
        }
        String gstPwd = (String)cfgProps.get(GSTPWD);
        if (gstPwd == null) {
            LOGGER.warn("GeoStore password is null. Forcing test configuration");
            gstPwd = "admin";
        }
        // init geostore parameter connection
        final GeoStoreClient geostore = new GeoStoreClient();
        geostore.setGeostoreRestUrl(gstUrl);
        geostore.setUsername(gstUsr);
        geostore.setPassword(gstPwd);

        // GeoStore
        try {

            final String gstLayerTemplateName = (String)cfgProps.get(GST_LAYER_TEMPLATE);
            if (gstLayerTemplateName == null) {
                // use workspace as GeoStore Category and resource
                publishOnGeoStore(LOGGER, gstFilter, geostore, rootList, workspace);
            } else {
                final FreeMarkerFilter gstLayerFilter = new FreeMarkerFilter(new File(configDir,
                                                                                      gstLayerTemplateName));
                // use workspace as GeoStore Category and resource
                publishAndUpdateOnGeoStore(LOGGER, gstFilter, gstLayerFilter, geostore, rootList, workspace);
            }

        } catch (Exception e) {
            if (failIgnore) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(e.getLocalizedMessage(), e);
                }
            } else {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(e.getLocalizedMessage(), e);
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
