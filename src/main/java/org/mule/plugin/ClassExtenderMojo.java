/**
 * Mule CMIS Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * The class will iterate go to the base directory defined. Ones there it will
 * iterate through the listed packages. Each class found there will be change to
 * exted the defined class.
 * 
 * @author damiansima
 * @goal extend
 * 
 */
public class ClassExtenderMojo extends AbstractMojo {

    private static final String BASE_DIR = System.getProperty("user.dir") + "/target/generated-soruces/";
    private static final String FILE_EXT_MARK = ".ext";

    private static final String CLASS_DEFINITION_LINE_PATTERN = "public class .+ \\{";
    /**
     * Define the base directory location for the classes to be extended
     * 
     * @parameter expression={extend.baseDir}
     */
    private String baseDir;

    /**
     * The List of package we should process.
     * 
     * @parameter expression={extend.packagesToExtend}
     * @required
     */
    private List<String> packagesToExtend;

    /**
     * The binary name of the class we should extend of.
     * 
     * @parameter expression={extend.classToExtend}
     * @required
     */
    private String classToExtend;

    public ClassExtenderMojo() {
        super();
    }

    public ClassExtenderMojo(String baseDir, List<String> packagesToExtend, String classToExtend) {
        super();
        this.baseDir = baseDir;
        this.packagesToExtend = packagesToExtend;
        this.classToExtend = classToExtend;
    }

    /**
     * Iterate the list of packages. For each class in them make it extends the
     * defined class.
     * 
     */
    public void execute() throws MojoExecutionException {

        getLog().info("Iterating through the package List.");
        for (String packageName : packagesToExtend) {

            // Create dir path from the package name
            String dirName = this.generateBaseDirectory() + packageName.replaceAll("\\.", "/") + "/";
            File packageDir = new File(dirName);

            getLog().info("Iterating through the classes in package: " + packageName);
            File[] classes = packageDir.listFiles();
            for (File classFile : classes) {
                if (!classFile.getName().contains(FILE_EXT_MARK)) {
                    this.extendClass(classFile);
                }
            }

        }
    }

    /**
     * It generates the base directory path. If none was provided it will
     * default to in {@link BASE_DIR}
     * 
     * It also handles the final forward slash issue. It ensures that the base
     * directory path has a final forward slash.
     * 
     * @return the path of the base directory to search for class to extend.
     */
    private String generateBaseDirectory() {
        StringBuilder actualBaseDir = new StringBuilder();

        if (this.baseDir == null || this.baseDir.trim().equals("")) {
            return BASE_DIR;
        }

        actualBaseDir.append(this.baseDir);
        if (!baseDir.endsWith("/")) {
            actualBaseDir.append("/");
        }

        return actualBaseDir.toString();
    }

    /**
     * It open the provided file and looks for a string patter that match the
     * class definition line.
     * 
     * @param classFile
     *            the file to be extended.
     */
    private void extendClass(File classFile) {
        Pattern pattern = Pattern.compile(CLASS_DEFINITION_LINE_PATTERN);

        // Define output file to be written
        File outFile = new File(classFile.getAbsolutePath() + FILE_EXT_MARK);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(outFile));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(classFile));

            String line;
            while ((line = br.readLine()) != null) {

                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    // process the line
                    String[] parts = line.split("\\{");
                    line = parts[0] + " extends " + this.classToExtend + " {";
                }

                // save the line to a new file
                bw.write(line);

                // add a new line
                bw.newLine();
            }

            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // rename out file to match the original file
        outFile.renameTo(classFile);
    }
}
