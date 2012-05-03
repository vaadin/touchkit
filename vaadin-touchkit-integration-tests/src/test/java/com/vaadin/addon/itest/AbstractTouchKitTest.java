package com.vaadin.addon.itest;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.commands.TestBenchCommands;

public class AbstractTouchKitTest {

    protected static final String BASEURL = "http://localhost:5678/";
    private static final File REF_IMAGE_ROOT = new File(
            "src/test/resources/screenshots/reference");
    protected WebDriver driver;
    protected TestBenchCommands testBench;

    public AbstractTouchKitTest() {
        super();
    }

    @Before
    public void setUp() {
        Parameters.setScreenshotErrorDirectory("target/testbench/errors/");
        Parameters.setScreenshotComparisonTolerance(0.01);
        Parameters.setCaptureScreenshotOnFailure(true);
        System.setProperty("webdriver.chrome.driver",
                "/usr/local/bin/chromedriver");
    }

    @After
    public void tearDown() {

    }

    protected void startBrowser() {
        driver = TestBench.createDriver(new ChromeDriver());
        // dimension includes browser chrome
        driver.manage().window().setSize(new Dimension(450, 750));
        testBench = (TestBenchCommands) driver;
    }

    public File getReferenceImage(String name) {
        return new File(REF_IMAGE_ROOT, name);
    }

}