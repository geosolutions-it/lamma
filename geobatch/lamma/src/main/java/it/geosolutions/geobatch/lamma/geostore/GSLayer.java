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
package it.geosolutions.geobatch.lamma.geostore;

import it.geosolutions.geobatch.imagemosaic.ImageMosaicOutput;
import it.geosolutions.geobatch.lamma.meteosat.GeoNetworkUtils;

import java.util.Map;
import java.util.TreeSet;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;

/**
 * TODO use decorato pattern instead of the following
 * 
 * @author Carlo Cancellieri - carlo.cancellieri@geo-solutions.it
 *
 */
public class GSLayer extends org.codehaus.jettison.json.JSONObject implements GeoStoreLayer<Map<String,Object>>{
    private String name;
    private String workspace;
    private String elevation;
    private String time;
    private String uuid;
    private String group;
    
    public GSLayer(Map<String,Object> imageMosaicOuptut) throws Exception {
        readGSLayer(imageMosaicOuptut);
    }
    
    @Override
    public JSONObject toJSONObject() throws JSONException{
        JSONObject newLayer=new JSONObject();
        
        newLayer.put("name",getWorkspace()+":"+getName());
        
        newLayer.put("title", getWorkspace()+" "+getName());
        
        newLayer.put("group", getGroup());
        
        final String elevObj=getElevation();
        if (elevObj!=null){
            newLayer.put("elevation",elevObj);
        }
        
        final String timeObj=getTime();
        if (timeObj!=null){
            newLayer.put("time",timeObj);
        }
        
        final Object uuidObj=getUuid();
        if (uuidObj!=null){
            newLayer.put("uuid",uuidObj);
        }
        return newLayer;
    }
    
    /**
     * {<br>
     * "format":"image/png",<br>
     * "group":"lamma_2011-09-23T07:15:00",<br>
     * "name":"lamma:AIRMASS",<br>
     * "opacity":0.7,<br>
     * "selected":false,<br>
     * "source":"lamma",<br>
     * "styles":[],<br>
     * "title":"lamma AIRMASS",<br>
     * "transparent":true,<br>
     * "uuid":"4d34fd47-965e-41e1-8851-aa56cf68b9df",<br>
     * "visibility":false,<br>
     * "ratio":1,<br>
     * "srs":"EPSG:900913",<br>
     * "transitionEffect":"resize",<br>
     * "time":"2011-09-23T07:15:00",<br>
     * "elevation":"2011-09-23T07:15:00"<br>
     * }<br>
     */
    @Override
    public void readGSLayer(Map<String,Object> imageMosaicOuptut) throws Exception {
        // workspace:layername
        final Object workspaceObj=imageMosaicOuptut.get(ImageMosaicOutput.WORKSPACE);
        if (workspaceObj!=null){
            this.workspace=(String)workspaceObj;
        }
        
        final Object nameObj=imageMosaicOuptut.get(ImageMosaicOutput.LAYERNAME);
        if (nameObj!=null){
            this.name =(String)nameObj;
        } 
        
        final Object elevObj=imageMosaicOuptut.get(AbstractGridCoverage2DReader.ELEVATION_DOMAIN);
        if (elevObj!=null){
            final TreeSet<String> elev=(TreeSet)elevObj;
            this.elevation=elev.first();
        }
        
        final Object timeObj=imageMosaicOuptut.get(AbstractGridCoverage2DReader.TIME_DOMAIN);
        String firstTime="";
        if (timeObj!=null){
            final TreeSet<String> time=(TreeSet)timeObj;
            firstTime=time.first();
            this.time=firstTime;
        }
        
        final Object uuidObj=imageMosaicOuptut.get(GeoNetworkUtils.GN_UUID);
        if (uuidObj!=null){
            String uuid=(String)uuidObj;
            this.uuid=uuid;
        }
        
        this.group=firstTime.isEmpty()?(String)this.workspace:(String)this.name+"_"+firstTime;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getElevation() {
        return elevation;
    }

    @Override
    public boolean matches(Map<String,Object> imageMosaicOutput) throws Exception {
        GSLayer layer=new GSLayer(imageMosaicOutput);
        return this.matches(layer);
    }
    
    public boolean matches(GSLayer layer){
        final String workspaceName = layer.getWorkspace();
        if (workspaceName != null && workspaceName.equals(workspace)) {
            final Object layerName = layer.getWorkspace();
            if (layerName != null && layerName.equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(GeoStoreLayer<Map<String,Object>> layerB){
        if (this.equals(layerB)){
            return true;
        }
        return false;
    }
    
    
}
