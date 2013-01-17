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
package it.geosolutions.geobatch.metocs.netcdf2geotiff.checker;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author Carlo Cancellieri - carlo.cancellieri@geo-solutions.it
 *
 */
public class NetcdfCheckerLoader implements ApplicationContextAware , InitializingBean{
    
    private static NetcdfCheckerLoader singleton;
    
    public NetcdfCheckerLoader(){
    }
    
    protected final static Logger LOGGER = LoggerFactory.getLogger(NetcdfCheckerLoader.class);
    
    private ApplicationContext applicationContext = null;
    
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
    
    public static NetcdfCheckerSPI getCheckerLoader(final String type){
        if (singleton== null) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("Underlying loader is null!");
            return null;
        }
        return singleton.getCheckerSPI(type);
    }
    
    private NetcdfCheckerSPI getCheckerSPI(final String type){
        if (applicationContext == null) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("Underlying applicationContext is null!");
            return null;
        }
        final Map<String,?> beans = applicationContext.getBeansOfType(NetcdfCheckerSPI.class);
        
        final Set<?> beanSet = beans.entrySet();
        
        NetcdfCheckerSPI spi=null;
        for (final Iterator<?> it = beanSet.iterator(); it.hasNext();) {
            final Map.Entry<String,?> entry = (Entry<String,?>) it.next();
            spi = (NetcdfCheckerSPI) entry.getValue();
            if (spi != null){
                if (spi.canRead(type)){
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("Creating an instance of: "+spi.getClass());
                    return spi;
                }
                
            }
        }

        if (LOGGER.isWarnEnabled())
            LOGGER.warn("Unable to find the needed SPI for type: "+type);
        
        return null;
    }

    public void afterPropertiesSet() throws Exception {
        if (applicationContext == null)
            throw new IllegalStateException("The provided applicationContext is null!");
        this.singleton=this;
    }

}
