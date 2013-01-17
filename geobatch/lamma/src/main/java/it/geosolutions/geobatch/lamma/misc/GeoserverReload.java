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
package it.geosolutions.geobatch.lamma.misc;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.management.BadAttributeValueExpException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public class GeoserverReload {
	protected final static Logger LOGGER = LoggerFactory.getLogger(GeoserverReload.class);
	
	final public static String PASS="pass";
	final public static String USER="user";
	final public static String URL="url";

	public static void reload(final File xml) throws BadAttributeValueExpException{
		XStream stream=new XStream();
		if (xml==null)
			throw new IllegalArgumentException("input file is null");
		
		FileInputStream input=null; 
		try {
			input=new FileInputStream(xml);
			Object o=stream.fromXML(input);
			if (o == null)
				throw new NullPointerException("Serialized object is null");
			if (o instanceof List<?>){
				// TODO better checks
				List<Map<String, String>> list=(List<Map<String, String>> )o;
				for (Map<String, String> map: list) {
					final String pass=map.get(PASS);
					final String user=map.get(USER);
					final String url=map.get(URL);
					
					final GeoServerRESTPublisher publisher=new GeoServerRESTPublisher(url, user, pass);
					if (!publisher.reload()){
						if (LOGGER.isInfoEnabled())
							LOGGER.info("Failed reload GS:"+url+" user "+user+" pass "+pass);
					} else {
						if (LOGGER.isInfoEnabled())
							LOGGER.info("Succesfully reloaded GS: "+url);
					}
				}
			}
			else
				throw new BadAttributeValueExpException("Bad file content:"+o.toString());
				
			
		}catch (IOException e){
			if (LOGGER.isErrorEnabled())
				LOGGER.error(e.getLocalizedMessage(),e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		
	}
	
	

}
