package com.microservices.order.test.steps;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import com.microservices.order.test.context.TestContext;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Step definitions for Order Service V1 API
 * Uses unique identifiers (timestamp + threadId + random) to prevent parallel test collisions
 */
public class OrderV1Steps {

    @Autowired
    private TestContext testContext;

    private static final String BASE_URL = "http://localhost:8083/api/v1/orders";
    private static final Random RANDOM = new Random();

    private String generateUniqueOrderNumber() {
        long timestamp = System.currentTimeMillis();
        long threadId = Thread.currentThread().getId();
        int random = RANDOM.nextInt(10000);
        return String.format("ORD-%d-%d-%d", timestamp, threadId, random);
    }

    @Given("an order with orderNumber {string}, userId {long}, productId {long}, quantity {int}, unitPrice {double}")
    public void anOrderWithDetails(String orderNumberPrefix, Long userId, Long productId, int quantity, double unitPrice) {
        String uniqueOrderNumber = orderNumberPrefix + "-" + generateUniqueOrderNumber();
        
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderNumber", uniqueOrderNumber);
        orderData.put("userId", userId);
        orderData.put("productId", productId);
        orderData.put("quantity", quantity);
        orderData.put("unitPrice", unitPrice);
        orderData.put("shippingAddress", "123 Test Street, Test City");
        orderData.put("notes", "Test order created at " + LocalDateTime.now());

        testContext.set("orderData", orderData);
        testContext.set("uniqueOrderNumber", uniqueOrderNumber);
    }

    @When("I create the order via V1 API")
    public void iCreateTheOrderViaV1API() {
        Map<String, Object> orderData = testContext.get("orderData");
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(orderData)
        .when()
            .post(BASE_URL)
        .then()
            .extract().response();

        testContext.setResponse(response);
        
        if (response.getStatusCode() == 201) {
            Long orderId = response.jsonPath().getLong("id");
            testContext.set("orderId", orderId);
        }
    }

    @When("I retrieve the order by ID via V1 API")
    public void iRetrieveTheOrderByIdViaV1API() {
        Long orderId = testContext.get("orderId");
        
        Response response = given()
        .when()
            .get(BASE_URL + "/" + orderId)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I retrieve the order by orderNumber via V1 API")
    public void iRetrieveTheOrderByOrderNumberViaV1API() {
        String orderNumber = testContext.get("uniqueOrderNumber");
        
        Response response = given()
        .when()
            .get(BASE_URL + "/number/" + orderNumber)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I update the order quantity to {int} and unitPrice to {double} via V1 API")
    public void iUpdateTheOrderQuantityAndUnitPriceViaV1API(int newQuantity, double newUnitPrice) {
        Long orderId = testContext.get("orderId");
        Map<String, Object> orderData = testContext.get("orderData");
        
        orderData.put("quantity", newQuantity);
        orderData.put("unitPrice", newUnitPrice);
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(orderData)
        .when()
            .put(BASE_URL + "/" + orderId)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I delete the order via V1 API")
    public void iDeleteTheOrderViaV1API() {
        Long orderId = testContext.get("orderId");
        
        Response response = given()
        .when()
            .delete(BASE_URL + "/" + orderId)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I retrieve all orders via V1 API")
    public void iRetrieveAllOrdersViaV1API() {
        Response response = given()
        .when()
            .get(BASE_URL)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I retrieve orders by userId {long} via V1 API")
    public void iRetrieveOrdersByUserIdViaV1API(Long userId) {
        Response response = given()
        .when()
            .get(BASE_URL + "/user/" + userId)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I retrieve orders by productId {long} via V1 API")
    public void iRetrieveOrdersByProductIdViaV1API(Long productId) {
        Response response = given()
        .when()
            .get(BASE_URL + "/product/" + productId)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I try to retrieve a non-existent order with ID {long} via V1 API")
    public void iTryToRetrieveANonExistentOrderViaV1API(Long nonExistentId) {
        Response response = given()
        .when()
            .get(BASE_URL + "/" + nonExistentId)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I try to create an order with invalid data via V1 API")
    public void iTryToCreateAnOrderWithInvalidDataViaV1API() {
        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("orderNumber", ""); // Invalid: empty
        invalidData.put("quantity", -1); // Invalid: negative
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(invalidData)
        .when()
            .post(BASE_URL)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }
}
