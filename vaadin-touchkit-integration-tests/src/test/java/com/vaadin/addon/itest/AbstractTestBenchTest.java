package com.vaadin.addon.itest;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;

public class AbstractTestBenchTest extends TestBenchTestCase {

    @Rule
    public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(
            this, true);

    protected static final String BASEURL = "http://localhost:5678/";
    private static final File REF_IMAGE_ROOT = new File(
            "src/test/resources/screenshots/reference");

    public AbstractTestBenchTest() {
        super();
    }

    @Before
    public void setUp() {
        Parameters.setScreenshotErrorDirectory("target/testbench/errors/");
        Parameters.setScreenshotComparisonTolerance(0.01);
    }

    @After
    public void tearDown() {

    }

    protected void startBrowser() {
        setDriver(TestBench.createDriver(new ChromeDriver()));
        // dimension includes browser chrome
        getDriver().manage().window().setSize(new Dimension(450, 750));
    }

    public File getReferenceImage(String name) {
        return new File(REF_IMAGE_ROOT, name);
    }

}
