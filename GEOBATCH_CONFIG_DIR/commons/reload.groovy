
import it.geosolutions.geobatch.action.scripting.ScriptingConfiguration;
import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder;
import it.geosolutions.geobatch.tools.file.Collector;
import it.geosolutions.geobatch.tools.file.Extract;

import java.io.File;

import java.io.IOException;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import it.geosolutions.geobatch.flow.event.IProgressListener
import it.geosolutions.geobatch.flow.event.ProgressListener
import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder

import it.geosolutions.filesystemmonitor.monitor.FileSystemEvent;
import it.geosolutions.filesystemmonitor.monitor.FileSystemEventType;
import it.geosolutions.geobatch.flow.event.action.ActionException;
import it.geosolutions.geobatch.flow.event.action.BaseAction;
import it.geosolutions.geobatch.tools.file.Collector;

import java.io.File;
import java.io.IOException;
import java.util.EventObject;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.FileImageInputStream;

import it.geosolutions.geobatch.imagemosaic.ImageMosaicCommand;
import it.geosolutions.geobatch.lamma.misc.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * Script Main "execute" function
 * @eventFilePath
 **/
public List execute(ScriptingConfiguration configuration, String eventFilePath, ProgressListenerForwarder listenerForwarder) throws Exception {
    // ////////////////////////////////////////////////////////////////////
    // Initializing input variables from Flow configuration
    // ////////////////////////////////////////////////////////////////////
    Map props = configuration.getProperties();

     final Logger LOGGER = LoggerFactory.getLogger("it.geosolutions.geobatch.action.scripting.ScriptingAction.class");

	    // regex to recognize the IMC name to merge
	    final String geoserverListFileName=props.get("geoserverList");

	    listenerForwarder.started();
	    final File xml=new File(geoserverListFileName);
	    try {
	      GeoserverReload.reload(xml);
	    } catch (Exception e) {
		listenerForwarder.failed(e);
		throw new ActionException(e);
	    }

	    // return
	    final Queue<String> ret=new LinkedList<String>();
	    
	    listenerForwarder.progressing(100,"completed");
	    listenerForwarder.completed();
	    return ret;
	}
