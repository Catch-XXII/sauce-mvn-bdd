package com.sauce.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class UiEffects {
    private UiEffects() {}
    public static void highlight(WebDriver driver, WebElement el) {
        try {
            // Always highlight elements for screenshot visibility
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].style.outline='2px dashed red';", el);
            // Small wait to ensure CSS is applied before screenshot is taken
            Thread.sleep(50);
        } catch (Exception ignored) {}
    }
}