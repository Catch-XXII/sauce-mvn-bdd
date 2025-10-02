package com.sauce.pages;

import com.sauce.context.TestContext;
import com.sauce.utils.SlowMotion;
import com.sauce.utils.UIEffects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends BasePage {

    private final By firstName  = By.id("first-name");
    private final By lastName   = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueBtn= By.id("continue");
    private final By finishBtn  = By.id("finish");
    private final By successMsg = By.cssSelector("[data-test='complete-header']");


    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void fillInfo(String first, String last, String zipCode) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zipCode);
        click(continueBtn);
    }

    public void finish() {
        click(finishBtn);
    }

    public String successText() {
        WebElement element = findElement(successMsg);
        UIEffects.highlight(driver, element);
        SlowMotion.intentionalWait();
        TestContext.takeScreenshotCallback(driver);
        return element.getText();
    }
}