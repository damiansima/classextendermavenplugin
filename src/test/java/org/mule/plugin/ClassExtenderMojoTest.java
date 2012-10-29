/**
 * Mule Class Extender Maven Plugin
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test validate that the execute method change the classes in the defined
 * directory to extend the provided class.
 * 
 * @author damiansima
 * 
 */
public class ClassExtenderMojoTest {
    private static final String BASE_DIR = System.getProperty("user.dir") + "/src/test/resources/";

    private static final String BASE_TEST_FILE_A = BASE_DIR + "/A.java";
    private static final String TEST_FILE_A = BASE_DIR + "/org/mule/plugin/A.java";

    private static final String BASE_TEST_FILE_B = BASE_DIR + "/B.java";
    private static final String TEST_FILE_B = BASE_DIR + "/org/mule/plugin/B.java";
    
    private static final String CLASS_TO_EXTEND = "FakeClass";

    @Before
    public void setUp() throws IOException {
        FileUtils.copyFile(new File(BASE_TEST_FILE_A), new File(TEST_FILE_A));
        FileUtils.copyFile(new File(BASE_TEST_FILE_B), new File(TEST_FILE_B));
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.copyFile(new File(BASE_TEST_FILE_A), new File(TEST_FILE_A));
        FileUtils.copyFile(new File(BASE_TEST_FILE_B), new File(TEST_FILE_B));
    }

    @Test
    public void executeTest() throws MojoExecutionException, MojoFailureException, FileNotFoundException {
        List<String> packages = new ArrayList<String>();
        packages.add("org.mule.plugin");

        AbstractMojo mojo = new ClassExtenderMojo(BASE_DIR, packages, CLASS_TO_EXTEND);

        mojo.execute();

        Pattern pattern = Pattern.compile("public class .+ extends .+ \\{");

        Scanner testFile = new Scanner(new File(TEST_FILE_A));
        while (testFile.hasNextLine()) {
            String testLine = testFile.nextLine();

            Matcher matcher = pattern.matcher(testLine);
            if (matcher.matches()) {
                Assert.assertTrue("The class should extend the provided class.", testLine.contains(CLASS_TO_EXTEND));
            }
        }
        
        Scanner testFileB = new Scanner(new File(TEST_FILE_B));
        while (testFileB.hasNextLine()) {
            String testLine = testFileB.nextLine();

            Matcher matcher = pattern.matcher(testLine);
            if (matcher.matches()) {
                Assert.assertTrue("The class should extend the provided class.", testLine.contains("A"));
            }
        }
    }
}
