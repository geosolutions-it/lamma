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

import it.geosolutions.geobatch.registry.AliasRegistrar;
import it.geosolutions.geobatch.registry.AliasRegistry;

/**
 * 
 * @author Carlo Cancellieri - carlo.cancellieri@geo-solutions.it
 *
 */
public class Netcdf2GeotiffAliasRegistrar extends AliasRegistrar {

    public Netcdf2GeotiffAliasRegistrar(AliasRegistry registry) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info(getClass().getSimpleName() + ": registering alias.");
        registry.putAlias("Netcdf2GeotiffConfiguration", Netcdf2GeotiffConfiguration.class);
    }
    
}