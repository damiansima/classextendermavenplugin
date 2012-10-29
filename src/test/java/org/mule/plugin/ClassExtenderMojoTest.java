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

    private static final String TEST_FILE = BASE_DIR + "/org/mule/plugin/A.java";
    private static final String BASE_TES_FILE = BASE_DIR + "/A.java";
    private static final String CLASS_TO_EXTEND = "FakeClass";

    @Before
    public void setUp() throws IOException {
        FileUtils.copyFile(new File(BASE_TES_FILE), new File(TEST_FILE));
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.copyFile(new File(BASE_TES_FILE), new File(TEST_FILE));
    }

    @Test
    public void executeTest() throws MojoExecutionException, MojoFailureException, FileNotFoundException {
        List<String> packages = new ArrayList<String>();
        packages.add("org.mule.plugin");

        AbstractMojo mojo = new ClassExtenderMojo(BASE_DIR, packages, CLASS_TO_EXTEND);

        mojo.execute();

        Pattern pattern = Pattern.compile("public class .+ extends .+ \\{");

        Scanner testFile = new Scanner(new File(TEST_FILE));
        while (testFile.hasNextLine()) {
            String testLine = testFile.nextLine();

            Matcher matcher = pattern.matcher(testLine);
            if (matcher.matches()) {
                Assert.assertTrue("The class should extend the provided class.", testLine.contains(CLASS_TO_EXTEND));
            }
        }
    }
}
