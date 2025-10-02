package com.sauce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage extends BasePage {

    private final By title          = By.cssSelector(".title");
    private final By addToCart = By.id("add-to-cart-sauce-labs-backpack");
    private final By cartButton = By.id("shopping_cart_container");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAt() {
        return findElement(title).getText().equalsIgnoreCase("Products");
    }

    public void addFirstItemToCart() {
        click(addToCart);
    }

    public void goToCart() {
        click(cartButton);
    }
}