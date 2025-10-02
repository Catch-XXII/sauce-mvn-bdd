@e2e @suite:SauceDemoTestSuite
Feature: End-to-End Tests
  SauceDemo end-to-end with API→UI validation

  Scenario Outline: Buy first product and verify API vs UI data mismatch
    Given I fetch product 1 from the backend API
    And I open SauceDemo and login with "<username>" and "<password>"
    Then I should land on the products page
    When I add the first product to the cart and open the cart
    Then the cart product name and price should NOT match the API data
    When I complete checkout with "<firstname>", "<lastname>" and "<zipcode>" info
    Then I should see the final confirmation message


    Examples:
      |username     |   password   | firstname | lastname | zipcode |
      |standard_user| secret_sauce |    John   |   Doe    |  34000  |