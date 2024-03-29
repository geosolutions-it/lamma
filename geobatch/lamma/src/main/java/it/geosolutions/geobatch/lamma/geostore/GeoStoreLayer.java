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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public interface GeoStoreLayer<T>{
    public JSONObject toJSONObject() throws JSONException;
    public void readGSLayer(T type) throws Exception;
    /**
     * T matches this object (by id or name or some else).
     * equals may return false if this object need some update.
     * @param type
     * @return
     */
    public boolean matches(T type) throws Exception;
    
    public boolean equals(GeoStoreLayer<T> obj) throws Exception;
}
