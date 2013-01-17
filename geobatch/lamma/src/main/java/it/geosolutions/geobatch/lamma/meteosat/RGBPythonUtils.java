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
package it.geosolutions.geobatch.lamma.meteosat;

import it.geosolutions.tools.ant.Task;
import it.geosolutions.tools.io.file.Collector;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.types.Environment.Variable;
import org.slf4j.Logger;

public class RGBPythonUtils {

    public static File[] getChannels(File channelsDir, String prefix, Map<String, String> map) throws IllegalArgumentException {
        String wildcardsString = map.get(prefix + RGBPythonUtils.FILTER);
        if (wildcardsString == null)
            throw new IllegalArgumentException("The key " + prefix + RGBPythonUtils.FILTER
                                               + " property is not set, please fix the configuration.");
    
        final String[] wildcards = wildcardsString.split(",");
        final int size = wildcards.length;
        final File[] channels = new File[size];
        for (int i = 0; i < size; i++) {
            final Collector coll = new Collector(new WildcardFileFilter(wildcards[i]));
            final List<File> filteredFiles = coll.collect(channelsDir);
            if (filteredFiles.size() == 1) {
                channels[i] = filteredFiles.get(0);
            } else {
                throw new IllegalArgumentException(
                                                   "Please provide a better filter or check the file channel folder. Filtered file list size is !=0. "
                                                       + filteredFiles.toString());
            }
        }
        return channels;
    }

    /**
     * builds args[] array to be used with the RGB python script:<br>
     * ./script.py calc.function() OutFile.tif Channel_0.tif ...<br>
     * 
     * @param configDir
     * @param channelsDir
     * @param outFile
     * @param prefix
     * @param map
     * @return
     */
    public static String[] buildArgs(File configDir, File[] channels, File outFile, String prefix,
                                     Map<String, String> map) {
    
        final String calcFunction = map.get(prefix + RGBPythonUtils.CALC_FUNCTION);
        if (calcFunction == null)
            throw new IllegalArgumentException("The key " + prefix + RGBPythonUtils.CALC_FUNCTION
                                               + " property is not set, please fix the configuration.");
    
        final String scriptFileName = map.get(prefix + RGBPythonUtils.SCRIPT);
        if (scriptFileName == null)
            throw new IllegalArgumentException("The key " + prefix + RGBPythonUtils.SCRIPT
                                               + " property is not set, please fix the configuration.");
    
        final File script = new File(configDir, scriptFileName);
    
        // python ./createRGB_2.py calc.function() OutFile.tif Channel_0.tif ...
        // Channel_n.tif
        final String[] args = new String[channels.length + 3];
        int i = 0;
        args[i] = script.getPath();
        args[++i] = calcFunction;
        args[++i] = outFile.getPath();
        for (File file : channels) {
            args[++i] = file.getPath();
        }
        return args;
    }

    /**
     * used to call:<br>
     * python script outFile inFiles[0] ... inFiles[N]
     * 
     * @param logger can be null
     * @param script
     * @param tempDir from where the process should be called
     * @param outFile
     * @param inFiles
     * @return true or false
     * @throws IOException
     */
    public static boolean run(Logger logger, File env, File tempDir, String... args) throws IOException {
    
        Variable[] vars = Task.loadVars(env);
    
        // python ./createRGB_2.py calc.function() OutFile.tif Channel_0.tif ...
        // Channel_n.tif
        ExecTask task = Task.buildTask("python", tempDir, args, vars);
        task.setFailIfExecutionFails(true);
        task.setFailonerror(true);
        // task.setResolveExecutable(true);
        // Project p=Task.buildSimpleProject("TASK", task);
        try {
            // p.executeTarget("TASK");
            task.execute();
        } catch (BuildException e) {
            if (logger != null) {
                if (logger.isDebugEnabled()) {
                    logger.error(e.getLocalizedMessage(), e);
                } else if (logger.isErrorEnabled()) {
                    logger.error(e.getMessage());
                }
            }
            return false;
        }
        return true;
    }

    /**
     * The suffix to add to the prefix to obtain the filter KEY into the map
     * this filter will be used to collect (in the filter order) the files to
     * pass to the script
     */
    public final static String FILTER = "_FILTER";
    /**
     * The suffix to add to the prefix to obtain the CALC function KEY into the
     * map this is the function called into the calc.py file for this prefix
     * variable
     */
    public final static String CALC_FUNCTION = "_CALC";
    /**
     * The suffix to add to the prefix to obtain the script KEY into the map
     * this is the script called for this prefix variable
     */
    public final static String SCRIPT = "_SCRIPT";

}
