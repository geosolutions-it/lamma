import java.io.File;
import java.io.IOException;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import it.geosolutions.geobatch.action.scripting.*;
import it.geosolutions.geobatch.imagemosaic.ImageMosaicCommand;

import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder;
import it.geosolutions.geobatch.flow.event.IProgressListener
import it.geosolutions.geobatch.flow.event.ProgressListener
import it.geosolutions.geobatch.flow.event.ProgressListenerForwarder
import it.geosolutions.geobatch.flow.event.action.ActionException;
import it.geosolutions.geobatch.flow.event.action.BaseAction;
import it.geosolutions.filesystemmonitor.monitor.FileSystemEvent;
import it.geosolutions.filesystemmonitor.monitor.FileSystemEventType;

import it.geosolutions.tools.commons.time.TimeParser;
import it.geosolutions.tools.commons.file.Path;
import it.geosolutions.tools.io.file.Collector;
import it.geosolutions.tools.io.file.writer.*;
import it.geosolutions.tools.compress.file.Extract;

import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.FileImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.geosolutions.geobatch.lamma.misc.*;

import com.thoughtworks.xstream.XStream;

// gt time parsing
import java.text.SimpleDateFormat;
import java.util.Date;

// FreeMarker
import it.geosolutions.geobatch.actions.freemarker.*;


import it.geosolutions.geoserver.rest.*;

// GEONETWORK
import it.geosolutions.geobatch.actions.geonetwork.configuration.GeonetworkDeleteConfiguration;
import it.geosolutions.geobatch.actions.geonetwork.op.GNDelete;
import it.geosolutions.geonetwork.GNClient;
import it.geosolutions.geonetwork.exception.GNException;
import it.geosolutions.geonetwork.exception.GNLibException;
import it.geosolutions.geonetwork.exception.GNServerException;
import it.geosolutions.geonetwork.util.GNInsertConfiguration;
import it.geosolutions.geonetwork.util.GNSearchRequest;
import it.geosolutions.geonetwork.util.GNSearchResponse;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

// GS Manager
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTCoverageStore;

//^^^manual^^^
import it.geosolutions.geobatch.lamma.meteosat.RGBPythonUtils;
import it.geosolutions.geobatch.lamma.models.ModelsUtils;
import it.geosolutions.geobatch.lamma.geostore.GeoStoreUtils;
import it.geosolutions.geostore.services.rest.GeoStoreClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

    /**
     * Script Main "execute" function
     * @eventFileName
     **/
    public Map execute(Map argsMap) throws Exception {
      // ////////////////////////////////////////////////////////////////////
      // Initializing input variables from Flow configuration
      // ////////////////////////////////////////////////////////////////////
	final String ENVIRONMENT_FILE = "ENV_FILE";
    
	final ScriptingConfiguration configuration=argsMap.get(ScriptingAction.CONFIG_KEY);
	final File tempDir=argsMap.get(ScriptingAction.TEMPDIR_KEY);
    
    final Map map = configuration.getProperties();
    
	final File configDir=argsMap.get(ScriptingAction.CONFIGDIR_KEY);
	final List events=argsMap.get(ScriptingAction.EVENTS_KEY);
	final FileSystemEvent event=events.poll();
        final File eventFile=event.getSource();
        final String eventFileName=eventFile.getName();
	final ProgressListenerForwarder listenerForwarder=argsMap.get(ScriptingAction.LISTENER_KEY);

	final Logger LOGGER = LoggerFactory.getLogger("it.geosolutions.geobatch.action.scripting.ScriptingAction.class");

// dataDir
// get the directory to scan
	final File dataDir=new File(DataDir);
	String dataDirName=dataDir.getPath();

	if (!dataDir.exists() || !dataDir.canWrite()){
	    final String message="::LaMMa:: problem the output data dir "+dataDirName+" do not exists or is not writeable";
	    final Exception e=new Exception(message);
	    listenerForwarder.failed(e);
	    throw e;
	}

	// The timestamp to use as starting point:
        final String timestamp=FilenameUtils.getBaseName(eventFileName);

	// parse as date
	final TimeParser parser=new TimeParser();
	final List dates=parser.parse(timestamp);
	//final Date actualDate=dates.get(0);
	final Date actualDate=new Date(Long.parseLong(timestamp));


	
	if (LOGGER.isInfoEnabled()) {
	    LOGGER.info("::Removing : actual date: "+actualDate.toString());
	}


	String gstUrl = GSTURL;
	if (gstUrl == null) {
	    gstUrl = "http://localhost:8383/geostore/rest/";
	}
	String gstUsr = GSTUID;
	if (gstUsr == null) {
	    gstUsr = "admin";
	}
	String gstPwd = GSTPWD;
	if (gstPwd == null) {
	    gstPwd = "admin";
	}
	// init geostore parameter connection
	final GeoStoreClient geostore = new GeoStoreClient();
	geostore.setGeostoreRestUrl(gstUrl);
	geostore.setUsername(gstUsr);
	geostore.setPassword(gstPwd);
	
        try {
            /* ----------------------------------------------------------------- */

		// get days ago to keep
//		final String daysAgo = argsMap.get("DaysAgo");
		final Calendar cal=getPastDate(actualDate.getTime(),Integer.parseInt(DaysAgo));
		if (cal==null){
		    final String message="::LaMMa:: deletion treshold not valid:"+DaysAgo;
 		    listenerForwarder.progressing(100,"skipped");
                    LOGGER.info(message);

			// standard return block
  		        final List results = new ArrayList();
		        results.add(eventFileName);
		        Map retMap=new HashMap();
		        retMap.put(ScriptingAction.RETURN_KEY, results);
		        return retMap;

//		    final Exception e=new Exception(message);
//		    listenerForwarder.failed(e);
//		    throw e;
		}

//////////////////////////////////////GeoNetwork////////////////////////////////////////////////////
//		final String gnUrl  = argsMap.get("GeoNetworkURL");  //"http://localhost:8080/geonetwork"
//		final String gnUser = argsMap.get("GeoNetworkUser"); //"admin"
//		final String gnPass   = argsMap.get("GeoNetworkPass");   //"admin"
//		final boolean ret=gnDelete(gnUrl,gnUser,gnPass,cal.getTime());
		final boolean ret=gnDelete(GeoNetworkURL,GeoNetworkUser,GeoNetworkPass,cal.getTime());
		
		if (!ret){
		      final String message="::LaMMa:: problem GeoNetworkDelete: "+ret;
		      if (LOGGER.isErrorEnabled())
			LOGGER.error(message);
//		      final Exception e=new Exception(message);
//		      listenerForwarder.failed(e);
//		      throw e;
		}
//////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////GeoServer////////////////////////////////////////////////////////
//	final String RESTURL  = argsMap.get("GeoServerURL");  //"http://localhost:8080/geoserver"
//	final String RESTUSER = argsMap.get("GeoServerUser"); //"admin"
//	final String RESTPW   = argsMap.get("GeoServerPass");   //"geoserver"
//	      final GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);
	      final GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(GeoServerURL, GeoServerUser, GeoServerPass);

	      
	      if (publisher==null){
		  final String message="::LaMMa:: problem could not open a reader connection to the GeoServer:\nURL:"+GeoServerURL+"\nUser:"+GeoServerUser+"\nPASS:"+GeoServerPass;
		  if (LOGGER.isErrorEnabled())
			  LOGGER.error(message);
		  final Exception e=new Exception(message);
		  listenerForwarder.failed(e);
		  throw e;
	      }

		final GeoServerRESTReader reader = new GeoServerRESTReader(GeoServerURL, GeoServerUser, GeoServerPass);
		if (reader==null){

                  final String message="::LaMMa:: problem could not open a reader connection to the GeoServer:\nURL:"+GeoServerURL+"\nUser:"+GeoServerUser+"\nPASS:"+GeoServerPass;
                  if (LOGGER.isErrorEnabled())
                          LOGGER.error(message);
                  final Exception e=new Exception(message);
                  listenerForwarder.failed(e);
                  throw e;
              	}
//////////////////////////////////////REMOVE STORES ///////////////////////////////////////////////////////

        final List<File> removeList=new ArrayList<File>();
        for (File workspaceDir:dataDir.listFiles()){
        	removeList.addAll(collectOlder(cal.getTime(),workspaceDir));
        	removeList.remove(workspaceDir);
        }
		// remove dataDir directory from the collection
		removeList.remove(dataDir);
		
// DEBUG
	if (LOGGER.isDebugEnabled()) {
	    for (String t : removeList){
	      LOGGER.debug("::Start remove for : Collected:"+t);
	    }
	}

// END DEBUG
	

		if (removeList.size()==0){
			listenerForwarder.progressing(100,"completed");
			listenerForwarder.completed();
			// ////
			// forwarding event to the next action
			// results
			final List results = new ArrayList();
			results.add(eventFileName);
			Map retMap=new HashMap();
			retMap.put(ScriptingAction.RETURN_KEY, results);
			return retMap;
		}
		
	      // These tests will destroy data, so let's make sure we do want to run them
		final List<File> secureRemoveFileList=new ArrayList();
		for (File remove : removeList){
			final File[] stores=remove.listFiles((FileFilter)FileFilterUtils.directoryFileFilter());
			// traking GeoStore resource removal
			// each resource contains a set of layers (stores in GeoServer)
			boolean firstTryRemoveResource=true;
			final boolean removedResource=false;
			for (File store : stores){
				final String wsName = store.getPath().split("/")[4].toUpperCase();
if (LOGGER.isInfoEnabled())
    LOGGER.info("Extracted workspace (from path:"+store+") is "+wsName);
			
				
				
				final String storeName = store.getName();
                
                //REMOVE DIRECTOY DATASTORE FROM 172.16.1.137 (GEONETWORKS LINKS TO TIFF FILES)
                final String envFileName = (String)map.get(ENVIRONMENT_FILE);
                final File props = new File(configDir, envFileName);
                final String[] args = ["/opt/geobatch/conf/remove/../commons/rimuovi_tif.py", wsName, storeName];
                boolean rem_tif = RGBPythonUtils.run(null, props, null, args);
                
                

	    // DEBUG
	    
				if (LOGGER.isDebugEnabled()) {
				    LOGGER.debug("::Removing : STORE NAME: "+storeName);
				}
				
				// Check if store exists...
				RESTCoverageStore coverageStore=reader.getCoverageStore(wsName, storeName);
				if (coverageStore==null){
					// store does not exists remove collected old file
					secureRemoveFileList.add(store);
					continue;
				}
				// Check if layer exists...
//				final RESTLayer layer= reader.getLayer(storeName);
//				if (layer == null){
//					// layer do not exists remove collected old file
//					secureRemoveFileList.add(store);
//					continue;
//				}

				String resourceName=null;
				if (firstTryRemoveResource){
/////////////////////////////////// REMOVE RESOURCE /////////////////////////////////
				    resourceName=ModelsUtils.resolveResourceName(storeName);
					LOGGER.info("Going to remove :"+resourceName+ " from GeoStore");
				    removedResource = GeoStoreUtils.removeResource(LOGGER, geostore, wsName, resourceName);
				}
				// check geostore removal output status
				if (!removedResource){
				    if (LOGGER.isErrorEnabled()){
						LOGGER.error("Unable to remove resource from geostore: (category: "+wsName+") resource:"+resourceName);
					}
					// skip geoserver and file system removal ops
					continue;
					
				} else {
				    firstTryRemoveResource=false;
				    final boolean removedCoverage = publisher.unpublishCoverage(wsName, storeName, storeName);
				    if (!removedCoverage){
						final String message="::LaMMa:: problem could not remove the coverage named "+storeName+" in the workspace named "+wsName;
						if (LOGGER.isErrorEnabled())
							LOGGER.error(message);
				    }
				    final boolean removed = publisher.removeCoverageStore(wsName, storeName);
				    if (!removed){
						final String message="::LaMMa:: problem could not remove the store named "+storeName+" in the workspace named "+wsName;
						if (LOGGER.isErrorEnabled())
						  LOGGER.error(message);
				    }
				    if (removed){
						secureRemoveFileList.add(store);
				    }
				}
			}
		}
		if (secureRemoveFileList.size()==0){
		    final String message="::LaMMa:: problem could not remove any selected store";
		    if (LOGGER.isErrorEnabled())
		      LOGGER.error(message);
		    final Exception e=new Exception(message);
		    listenerForwarder.failed(e);
		    throw e;
		}
//////////////////////////////////////RESET //////////////////////////////////////////////////////////////
		publisher.reset();
//////////////////////////////////////REMOVING FILE///////////////////////////////////////////////////////
		for (File remove : secureRemoveFileList){
		    if (LOGGER.isDebugEnabled())
			  LOGGER.debug("::Removing : removing file from filesystem: "+remove.getAbsolutePath());
		    // REMOVE FILES
		    FileUtils.deleteQuietly(remove);
		}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		listenerForwarder.progressing(100,"completed");
		listenerForwarder.completed();

        } catch (Exception e) {
	    if (LOGGER.isErrorEnabled())
		  LOGGER.error(e.getLocalizedMessage(),e);
            listenerForwarder.failed(e);
//do not rethrow exceptions to permit next actions (reload or other remove)
            //throw e;
        }
        // ////
        // forwarding event to the next action
	// results
	final List results = new ArrayList();
	results.add(eventFileName);
	Map retMap=new HashMap();
        retMap.put(ScriptingAction.RETURN_KEY, results);
        return retMap;
    }
    

    public Calendar getPastDate(final long time,final int daysAgo){
	final Logger LOGGER = LoggerFactory.getLogger("it.geosolutions");
	if (daysAgo<0){
    		return null;
    	}
    	Calendar cal=Calendar.getInstance();
    	cal.setTimeInMillis(time);
    	int days=cal.get(Calendar.DAY_OF_YEAR);
    	if (days>=daysAgo)
    		cal.set(Calendar.DAY_OF_YEAR, days-daysAgo);
    	else {
    		// TODO use  getActualMaximum for days
    		cal.set(Calendar.DAY_OF_YEAR, (354+(days-daysAgo)));
    		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)-1);
    	}
	if (LOGGER.isDebugEnabled()) {
//println("calendar date: "+cal.getTime().toString());	    
	    LOGGER.debug("::Removing : remove before date: "+cal.getTime().toString());
	}

	return cal;
    }

    public List<File> collectOlder(final Date time,final File root){
    	
//FileFilterUtils.andFileFilter(FileFilterUtils.directoryFileFilter(),FileFilterUtils.ageFileFilter(time, false))
    	final Collector coll=new Collector(FileFilterUtils.andFileFilter(FileFilterUtils.directoryFileFilter(),FileFilterUtils.ageFileFilter(time, true)),1);
    	return coll.collect(root);
    }

    public boolean gnDelete(final String url, final String user, final String pass, final Date instant) throws IOException{
    	final File requestFile;
	final Logger LOGGER = LoggerFactory.getLogger("it.geosolutions");
    	try{
		final SimpleDateFormat iso801= new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		final String time=iso801.format(instant);
	    	// create a request file
	        final Element request = new Element("request");
		request.addContent(new Element("dateTo").setText(time));
		request.addContent(new Element("themekey").setText("\"gfs\" or \"arw\""));
		request.addContent(new Element("similarity").setText("1"));

	        requestFile = File.createTempFile("gb_delete_request", ".xml");
	        FileUtils.forceDeleteOnExit(requestFile);
	        final XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat());
	        FileUtils.writeStringToFile(requestFile, outputter.outputString(request));
    	}catch (IOException e){
	      if (LOGGER.isErrorEnabled())
		      LOGGER.error("::Removing : Could not write the request to the filesystem", e);
	      return false;
    	}

        final GNClient client = new GNClient(url);
        if( ! client.login(user, pass)){
	    if (LOGGER.isErrorEnabled())
		    LOGGER.error("::Removing : Could not login into GeoNetwork");
            return false;
        }
        
        // run the operation now
        final GeonetworkDeleteConfiguration cfg = new GeonetworkDeleteConfiguration("LaMMa", "LaMMa", "LaMMa");
        cfg.setGeonetworkServiceURL(url);
        cfg.setLoginUsername(user);
        cfg.setLoginPassword(pass);
        
        final GNDelete gnd = new GNDelete(cfg);
        boolean ret = gnd.run(client, requestFile);
        if(ret){
	    if (LOGGER.isInfoEnabled())
		LOGGER.info("::Removing : Clear procedure success");
        }
        else {
	    if (LOGGER.isWarnEnabled())
		LOGGER.warn("::Removing : Problems in clear procedure");
        }
	return ret;
    }

