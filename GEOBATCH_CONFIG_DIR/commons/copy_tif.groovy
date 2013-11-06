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
import it.geosolutions.geobatch.lamma.meteosat.MeteosatUtils;
import it.geosolutions.geobatch.lamma.meteosat.RGBPythonUtils;
import it.geosolutions.geobatch.lamma.geonetwork.GeoNetworkUtils;
import it.geosolutions.geobatch.lamma.geostore.GeoStoreUtils;

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

    final String ENVIRONMENT_FILE = "ENV_FILE";
    final String SCRIPT_FILE = "SCRIPT";
    final String MODELS_FILE = "MODELS";
    
    final ScriptingConfiguration configuration = (ScriptingConfiguration)argsMap
        .get(ScriptingAction.CONFIG_KEY);

    boolean failIgnore = true;
    // TODO
    if (!configuration.isFailIgnored()) {
        failIgnore = false;
    }

        final Map map = configuration.getProperties();
        final ProgressListenerForwarder listenerForwarder = (ProgressListenerForwarder)argsMap
            .get(ScriptingAction.LISTENER_KEY);        

        final Logger logger = LoggerFactory
            .getLogger("it.geosolutions.geobatch.action.scripting.ScriptingAction.class");

    final File configDir = (File)argsMap.get(ScriptingAction.CONFIGDIR_KEY);
    final String envFileName = (String)map.get(ENVIRONMENT_FILE);
    final String script = (String)map.get(SCRIPT_FILE);
    final String models = (String)map.get(MODELS_FILE);
    final String[] args = [script, models];

    final File props = new File(configDir, envFileName);
    boolean ret = RGBPythonUtils.run(logger, props, null, args);

        //argsMap.put(ScriptingAction.EVENTS_KEY, queue);

        //argsMap = MeteosatUtils.ImageMosaic(argsMap);
	
	argsMap.put(ScriptingAction.EVENTS_KEY, argsMap.get(ScriptingAction.RETURN_KEY));

        //argsMap = MeteosatUtils.GeoNetworkAndGeoStore(argsMap);

        listenerForwarder.completed();
        return argsMap;

}