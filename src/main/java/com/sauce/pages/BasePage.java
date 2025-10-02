package com.sauce.pages;

import com.sauce.context.TestContext;
import com.sauce.utils.SlowMotion;
import com.sauce.utils.UiEffects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait  = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        UiEffects.highlight(driver, element);
        SlowMotion.intentionalWait();
        TestContext.takeScreenshotCallback(driver);
        element.click();
    }

    protected void type(By locator, String text) {
        WebElement element = findElement(locator);
        UiEffects.highlight(driver, element);
        SlowMotion.intentionalWait();
        TestContext.takeScreenshotCallback(driver);
        element.clear();
        element.sendKeys(text);
        SlowMotion.intentionalWait();
    }
}