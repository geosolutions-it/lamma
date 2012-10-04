package it.geosolutions.geobatch.lamma.geostore;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GSBlob {

    public static JSONArray getLayers(String blob) throws JSONException{
        if (blob==null || blob.isEmpty()){
            throw new IllegalArgumentException("Unable to get layers using a null blob string");
        }
        JSONObject jsonObj = GSBlob.getBlob(blob);
        JSONObject jsonMap = GSBlob.getMap(jsonObj);
        if (jsonMap != null) {
            return GSBlob.getLayers(jsonMap);
        }
        return null;
    }
    
    public static JSONArray getLayers(JSONObject jsonMap) throws JSONException{
        if (jsonMap==null){
            throw new IllegalArgumentException("Unable to get layers using a null jsonMap object");
        }
        return jsonMap.getJSONArray("layers");
    }
    
    public static void putLayers(JSONObject jsonMap, JSONArray layers) throws JSONException{
        if (jsonMap==null){
            throw new IllegalArgumentException("Unable to get layers using a null jsonMap object");
        }
        jsonMap.put("layers", layers);
    }
    
    public static JSONObject getMap(JSONObject blob) throws JSONException, IllegalArgumentException {
        if (blob==null){
            throw new IllegalArgumentException("Unable to getMap using a null blob object");
        }
        final JSONObject jsonMap = blob.getJSONObject("map");
        if (jsonMap != null) {
            return jsonMap;
        }
        return null;
    }
    
    public static JSONObject getBlob(String blob) throws JSONException{
        return new JSONObject(blob);
    }
    
}
