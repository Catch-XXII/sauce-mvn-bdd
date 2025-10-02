package com.sauce.context;

import com.sauce.api.models.Product;
import org.openqa.selenium.WebDriver;

public class TestContext {
    private WebDriver driver;
    private Product apiProduct;
    private static ScreenshotCallback screenshotCallback;

    public interface ScreenshotCallback {
        void takeScreenshot(WebDriver driver);
    }
    public WebDriver getDriver() { return driver; }
    public void setDriver(WebDriver driver) { this.driver = driver; }
    public Product getApiProduct() { return apiProduct; }
    public void setApiProduct(Product apiProduct) { this.apiProduct = apiProduct; }

    public static void setScreenshotCallback(ScreenshotCallback callback) {
        screenshotCallback = callback;
    }

    public static void takeScreenshotCallback(WebDriver driver) {
        if (screenshotCallback != null) {
            screenshotCallback.takeScreenshot(driver);
        }
    }
}

