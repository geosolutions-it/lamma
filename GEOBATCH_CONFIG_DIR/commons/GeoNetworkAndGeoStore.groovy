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
import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder;
import it.geosolutions.geobatch.flow.event.action.Action;
import it.geosolutions.geobatch.flow.event.action.ActionException;
import it.geosolutions.geobatch.geotiff.retile.GeotiffRetiler;
import it.geosolutions.geobatch.imagemosaic.ImageMosaicCommand;
import it.geosolutions.geobatch.lamma.geonetwork.GeoNetworkUtils;
import it.geosolutions.geobatch.lamma.geostore.GeoStoreUtils;
import it.geosolutions.geobatch.lamma.models.ModelsUtils;
import it.geosolutions.geobatch.lamma.meteosat.RGBPythonUtils;
import it.geosolutions.geonetwork.GNClient;
import it.geosolutions.geostore.services.rest.GeoStoreClient;
import it.geosolutions.tools.compress.file.Extract;
import it.geosolutions.tools.freemarker.filter.FreeMarkerFilter;
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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


    /**
     * Script Main "execute" function
     **/
    public Map execute(Map argsMap) throws Exception {
  
	argsMap = ModelsUtils.GeoNetworkAndGeoStore(argsMap);
        
        return argsMap;
    }
