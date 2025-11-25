package com.microservices.order.test.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;

import com.microservices.order.test.context.TestContext;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;

/**
 * Common step definitions shared across all test scenarios
 */
public class CommonSteps {

    @Autowired
    private TestContext testContext;

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        assertEquals(expectedStatusCode, response.getStatusCode(), 
            "Status code mismatch");
    }

    @Then("the response should contain error message")
    public void theResponseShouldContainErrorMessage() {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        String message = response.jsonPath().getString("message");
        assertNotNull(message, "Error message should not be null");
        assertFalse(message.isEmpty(), "Error message should not be empty");
    }

    @Then("the response should contain {string} field")
    public void theResponseShouldContainField(String fieldName) {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        Object field = response.jsonPath().get(fieldName);
        assertNotNull(field, "Field '" + fieldName + "' should not be null");
    }

    @Then("the response {string} field should be {string}")
    public void theResponseFieldShouldBe(String fieldName, String expectedValue) {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        String actualValue = response.jsonPath().getString(fieldName);
        assertEquals(expectedValue, actualValue, 
            "Field '" + fieldName + "' value mismatch");
    }

    @Then("the response {string} field should be {int}")
    public void theResponseFieldShouldBeInt(String fieldName, Integer expectedValue) {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        Object actualValue = response.jsonPath().get(fieldName);
        
        if (actualValue instanceof Number) {
            assertEquals(expectedValue.intValue(), ((Number) actualValue).intValue(), 
                "Field '" + fieldName + "' value mismatch");
        } else {
            assertEquals(expectedValue, actualValue, 
                "Field '" + fieldName + "' value mismatch");
        }
    }

    @Then("the response {string} field should be true")
    public void theResponseFieldShouldBeTrue(String fieldName) {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        Boolean actualValue = response.jsonPath().getBoolean(fieldName);
        assertTrue(actualValue, "Field '" + fieldName + "' should be true");
    }

    @Then("the response {string} field should be false")
    public void theResponseFieldShouldBeFalse(String fieldName) {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        Boolean actualValue = response.jsonPath().getBoolean(fieldName);
        assertFalse(actualValue, "Field '" + fieldName + "' should be false");
    }

    @Then("the response should be a non-empty list")
    public void theResponseShouldBeANonEmptyList() {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        int size = response.jsonPath().getList("$").size();
        assertTrue(size > 0, "Response list should not be empty");
    }
}
