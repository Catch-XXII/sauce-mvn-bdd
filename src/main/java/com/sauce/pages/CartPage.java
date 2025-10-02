package com.sauce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {


    private final By cartItemName  = By.cssSelector("[data-test='inventory-item-name']");
    private final By cartItemPrice = By.cssSelector("[data-test='inventory-item-price']");
    private final By checkoutBtn   = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getCartItemName() {
        return findElement(cartItemName).getText();
    }

    public String getCartItemPrice() {
        return findElement(cartItemPrice).getText().replace("$", "");
    }

    public void checkout() {
        click(checkoutBtn);
    }
}