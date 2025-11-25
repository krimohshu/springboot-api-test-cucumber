package com.microservices.order.test.steps;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import com.microservices.order.test.context.TestContext;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Step definitions for Order Service V2 API
 * Uses unique identifiers (timestamp + threadId + random) to prevent parallel test collisions
 */
public class OrderV2Steps {

    @Autowired
    private TestContext testContext;

    private static final String BASE_URL = "http://localhost:8083/api/v2/orders";
    private static final Random RANDOM = new Random();

    private String generateUniqueOrderNumber() {
        long timestamp = System.currentTimeMillis();
        long threadId = Thread.currentThread().getId();
        int random = RANDOM.nextInt(10000);
        return String.format("ORD-%d-%d-%d", timestamp, threadId, random);
    }

    @Given("an order with status {string} and active {string}")
    public void anOrderWithStatusAndActive(String status, String activeStr) {
        String uniqueOrderNumber = generateUniqueOrderNumber();
        boolean active = Boolean.parseBoolean(activeStr);
        
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderNumber", uniqueOrderNumber);
        orderData.put("userId", 1L);
        orderData.put("productId", 1L);
        orderData.put("quantity", 2);
        orderData.put("unitPrice", 99.99);
        orderData.put("status", status);
        orderData.put("active", active);
        orderData.put("shippingAddress", "123 Test Street, Test City");
        orderData.put("notes", "V2 test order created at " + LocalDateTime.now());

        testContext.set("orderData", orderData);
        testContext.set("uniqueOrderNumber", uniqueOrderNumber);
    }

    @When("I create the order via V2 API")
    public void iCreateTheOrderViaV2API() {
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

    @When("I search orders with userId {long} via V2 API")
    public void iSearchOrdersWithUserIdViaV2API(Long userId) {
        Map<String, Object> searchRequest = new HashMap<>();
        searchRequest.put("userId", userId);
        searchRequest.put("page", 0);
        searchRequest.put("size", 10);
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(searchRequest)
        .when()
            .post(BASE_URL + "/search")
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I search orders with status {string} via V2 API")
    public void iSearchOrdersWithStatusViaV2API(String status) {
        Map<String, Object> searchRequest = new HashMap<>();
        searchRequest.put("status", status);
        searchRequest.put("page", 0);
        searchRequest.put("size", 10);
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(searchRequest)
        .when()
            .post(BASE_URL + "/search")
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I search orders with pagination page {int} size {int} via V2 API")
    public void iSearchOrdersWithPaginationViaV2API(int page, int size) {
        Map<String, Object> searchRequest = new HashMap<>();
        searchRequest.put("page", page);
        searchRequest.put("size", size);
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(searchRequest)
        .when()
            .post(BASE_URL + "/search")
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I update the order status to {string} via V2 API")
    public void iUpdateTheOrderStatusViaV2API(String newStatus) {
        Long orderId = testContext.get("orderId");
        
        Response response = given()
            .queryParam("status", newStatus)
        .when()
            .put(BASE_URL + "/" + orderId + "/status")
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I retrieve all statuses via V2 API")
    public void iRetrieveAllStatusesViaV2API() {
        Response response = given()
        .when()
            .get(BASE_URL + "/statuses")
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I retrieve order statistics via V2 API")
    public void iRetrieveOrderStatisticsViaV2API() {
        Response response = given()
        .when()
            .get(BASE_URL + "/stats")
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I calculate total spending for userId {long} via V2 API")
    public void iCalculateTotalSpendingForUserIdViaV2API(Long userId) {
        Response response = given()
        .when()
            .get(BASE_URL + "/user/" + userId + "/total-spending")
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I bulk create {int} orders via V2 API")
    public void iBulkCreateOrdersViaV2API(int count) {
        List<Map<String, Object>> orders = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            String uniqueOrderNumber = generateUniqueOrderNumber();
            
            Map<String, Object> order = new HashMap<>();
            order.put("orderNumber", uniqueOrderNumber);
            order.put("userId", 1L);
            order.put("productId", 1L);
            order.put("quantity", i + 1);
            order.put("unitPrice", 50.0 + i);
            order.put("status", "PENDING");
            order.put("active", true);
            order.put("shippingAddress", "123 Bulk Street " + i);
            order.put("notes", "Bulk order " + i);
            
            orders.add(order);
        }
        
        Map<String, Object> bulkRequest = new HashMap<>();
        bulkRequest.put("orders", orders);
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(bulkRequest)
        .when()
            .post(BASE_URL + "/bulk")
        .then()
            .extract().response();

        testContext.setResponse(response);
        
        if (response.getStatusCode() == 201) {
            testContext.set("bulkCreatedCount", count);
        }
    }

    @When("I retrieve the order by ID via V2 API")
    public void iRetrieveTheOrderByIdViaV2API() {
        Long orderId = testContext.get("orderId");
        
        Response response = given()
        .when()
            .get(BASE_URL + "/" + orderId)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I update the order via V2 API with new quantity {int}")
    public void iUpdateTheOrderViaV2APIWithNewQuantity(int newQuantity) {
        Long orderId = testContext.get("orderId");
        Map<String, Object> orderData = testContext.get("orderData");
        
        orderData.put("quantity", newQuantity);
        
        Response response = given()
            .contentType(ContentType.JSON)
            .body(orderData)
        .when()
            .put(BASE_URL + "/" + orderId)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @When("I soft delete the order via V2 API")
    public void iSoftDeleteTheOrderViaV2API() {
        Long orderId = testContext.get("orderId");
        
        Response response = given()
        .when()
            .delete(BASE_URL + "/" + orderId)
        .then()
            .extract().response();

        testContext.setResponse(response);
    }

    @Then("the response should contain pagination info")
    public void theResponseShouldContainPaginationInfo() {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        
        assertNotNull(response.jsonPath().get("page"), "Page should not be null");
        assertNotNull(response.jsonPath().get("size"), "Size should not be null");
        assertNotNull(response.jsonPath().get("totalElements"), "Total elements should not be null");
        assertNotNull(response.jsonPath().get("totalPages"), "Total pages should not be null");
    }

    @Then("the response should contain {int} orders")
    public void theResponseShouldContainOrders(int expectedCount) {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        
        int actualCount = response.jsonPath().getList("$").size();
        assertEquals(expectedCount, actualCount, "Order count mismatch");
    }

    @Then("the response should contain order statistics")
    public void theResponseShouldContainOrderStatistics() {
        Response response = testContext.getResponse();
        assertNotNull(response, "Response should not be null");
        
        assertNotNull(response.jsonPath().get("totalOrders"), "Total orders should not be null");
        assertNotNull(response.jsonPath().get("activeOrders"), "Active orders should not be null");
        assertNotNull(response.jsonPath().get("ordersByStatus"), "Orders by status should not be null");
    }
}
