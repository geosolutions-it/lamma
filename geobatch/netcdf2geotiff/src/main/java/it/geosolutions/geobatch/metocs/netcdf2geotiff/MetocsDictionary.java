/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  http://geobatch.codehaus.org/
 *  Copyright (C) 2007-2008-2009 GeoSolutions S.A.S.
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
package it.geosolutions.geobatch.metocs.netcdf2geotiff;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Carlo Cancellieri - carlo.cancellieri@geo-solutions.it
 *
 * @param <DICTIONARY_KEY> Type of the dictionary key into a dictionary (
 * @param <SECTION> Type of the section key (the type of the section instance extending Map<?,String>)
 */
public abstract class MetocsDictionary <DICTIONARY_KEY, SECTION extends Map<?, String>> {
	private final Logger LOGGER;
	
    private final Map<DICTIONARY_KEY, SECTION > dictionary;

    @SuppressWarnings("unused")
	private MetocsDictionary() {
        super();
        this.dictionary = null;
        LOGGER=null;
    }

    public MetocsDictionary(Map<DICTIONARY_KEY, SECTION > dictionary) {
        super();
        this.dictionary = dictionary;
        LOGGER=LoggerFactory.getLogger(MetocsDictionary.class);
    }

    /**
     * returns a Section of the dictionary
     * @param key
     * @return
     */
    public SECTION getVal(final String key) {
        return dictionary.get(key);
    }

    protected Map<DICTIONARY_KEY, SECTION> getDictionary() {
        return dictionary;
    }
    
    /**
     * Search into the dictionary the key passed in 'key' parameter first trying to search into the
     * section passed into the 'section' parameter then, if not found, trying to search at the ROOT
     * section.
     * 
     * @note can return null.
     * @note avoid call this method using ROOT_KEY as section, for that use
     *       getValueFromRootDictionary
     * @param section
     * @param key
     * @return
     */
    public String getValueFromDictionary(final String section, final String key) {
        // search into the dictionary at variable section
        final SECTION varDictionary = getVal(section);
        String name=null;
        if (varDictionary != null) {
            name= varDictionary.get(key);
        }
        if (name==null){
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("Unable to find into the dictionary a section with key value: \'"
                        + section + "\'. Trying to search for the key: \'" + key
                        + "\' at ROOT level...");
            // search into the dictionary ROOT section
            name = getValueFromRootDictionary(key);
        }
        return name;
    }
    
    /**
     * Method to implement to implement a dictionary
     * @param key
     * @return
     */
    public abstract String getValueFromRootDictionary(final String key);

}
