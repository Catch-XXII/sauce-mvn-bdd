package com.sauce.steps;

import com.sauce.api.ApiClient;
import com.sauce.api.models.Product;
import com.sauce.context.TestContext;
import com.sauce.pages.CartPage;
import com.sauce.pages.CheckoutPage;
import com.sauce.pages.LoginPage;
import com.sauce.pages.ProductsPage;
import io.cucumber.java.en.*;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;


public class EndToEndSteps {
    private static final Logger logger = LoggerFactory.getLogger(EndToEndSteps.class);
    private final TestContext context;

    public EndToEndSteps(TestContext ctx) {
        this.context = ctx;
    }

    @Given("I fetch product {int} from the backend API")
    public void fetchProductFromApi(Integer id) {
        logger.info("E2E Test: Fetching product with ID: {}", id);
        Allure.addAttachment("Product ID Requested", String.valueOf(id));

        ApiClient api = new ApiClient();
        Product product = api.getProductById(id);

        logger.info("E2E Test: Retrieved product - ID: {}, Title: {}, Price: ${}",
                product.id, product.title, product.price);

        Allure.addAttachment("Product ID Retrieved", String.valueOf(product.id));
        Allure.addAttachment("Product Title", product.title);
        Allure.addAttachment("Product Price", "$" + product.price);

        context.setApiProduct(product);
    }

    @Given("I open SauceDemo and login with {string} and {string}")
    public void userLogsIn(String username, String password) {
        LoginPage login = new LoginPage(context.getDriver()).open();
        login.login(username, password);
    }

    @Then("I should land on the products page")
    public void shouldLandOnProducts() {
        ProductsPage products = new ProductsPage(context.getDriver());
        Assert.assertTrue(products.isAt(), "User is not on Products page");
    }

    @When("I add the first product to the cart and open the cart")
    public void addFirstProductToCart() {
        ProductsPage products = new ProductsPage(context.getDriver());
        products.addFirstItemToCart();
        products.goToCart();
    }

    @Then("the cart product name and price should NOT match the API data")
    public void cartShouldNotMatchApi() {
        CartPage cart = new CartPage(context.getDriver());
        String uiName = cart.getCartItemName();
        double uiPrice = Double.parseDouble(cart.getCartItemPrice());

        Product api = context.getApiProduct();
        String apiName = api.title;
        double apiPrice = api.price;

        Assert.assertNotEquals(uiName, apiName,
                String.format("Product names are different as expected - UI: '%s', API: '%s'", uiName, apiName));
        Assert.assertNotEquals(uiPrice, apiPrice, 0.001,
                String.format("Product prices are different as expected - UI: $%.2f, API: $%.2f", uiPrice, apiPrice));
    }

    @When("I complete checkout with {string}, {string} and {string} info")
    public void completeCheckout(String firstname, String lastname, String zipcode){
        CartPage cart = new CartPage(context.getDriver());
        cart.checkout();
        CheckoutPage checkoutPage = new CheckoutPage(context.getDriver());
        checkoutPage.fillInfo(firstname, lastname, zipcode);
        checkoutPage.finish();
    }

    @Then("I should see the final confirmation message")
    public void shouldSeeConfirmationMessage() {
        CheckoutPage checkoutPage = new CheckoutPage(context.getDriver());
        Assert.assertEquals(checkoutPage.successText(), "Thank you for your order!", "Confirmation message mismatch");
    }
}