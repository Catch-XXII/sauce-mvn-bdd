package com.sauce.hooks;
import com.sauce.context.TestContext;
import com.sauce.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.ByteArrayInputStream;

public class Hooks {

    private final TestContext ctx;
    private int stepCounter = 0;

    public Hooks(TestContext ctx) {
        this.ctx = ctx;
    }

    @Before
    public void setUp() {
        stepCounter = 0;

        TestContext.setScreenshotCallback(driver -> {
            stepCounter++;
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                String stepName = "Action " + stepCounter + " (highlighted)";
                Allure.addAttachment(stepName, "image/png",
                        new ByteArrayInputStream(screenshot), "png");
            } catch (Exception e) {
                System.out.println("Failed to capture screenshot: " + e.getMessage());
            }
        });

        // Use DriverManager to respect browser configuration from application.properties
        WebDriver driver = DriverManager.getDriver();
        ctx.setDriver(driver);
    }

    @After
    public void tearDown(Scenario scenario) {
        if (ctx.getDriver() != null) {
            if (scenario.isFailed()) {
                try {
                    byte[] screenshot = ((TakesScreenshot) ctx.getDriver()).getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment("Test Failed - Final Screenshot", "image/png",
                            new ByteArrayInputStream(screenshot), "png");
                } catch (Exception e) {
                    System.out.println("Failed to capture failure screenshot: " + e.getMessage());
                }
            }
            DriverManager.quitDriver();
        }
    }
}
