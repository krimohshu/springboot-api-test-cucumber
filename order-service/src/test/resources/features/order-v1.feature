Feature: Order Service V1 API

  Scenario: Create a new order
    Given an order with orderNumber "TEST", userId 1, productId 1, quantity 2, unitPrice 99.99
    When I create the order via V1 API
    Then the response status code should be 201
    And the response should contain "id" field
    And the response should contain "orderNumber" field
    And the response "quantity" field should be 2

  Scenario: Retrieve order by ID
    Given an order with orderNumber "TEST", userId 1, productId 1, quantity 3, unitPrice 149.99
    When I create the order via V1 API
    And I retrieve the order by ID via V1 API
    Then the response status code should be 200
    And the response should contain "id" field
    And the response "quantity" field should be 3

  Scenario: Retrieve order by order number
    Given an order with orderNumber "SPECIFIC", userId 2, productId 2, quantity 1, unitPrice 79.99
    When I create the order via V1 API
    And I retrieve the order by orderNumber via V1 API
    Then the response status code should be 200
    And the response should contain "orderNumber" field
    And the response "userId" field should be 2

  Scenario: Update order
    Given an order with orderNumber "UPDATE", userId 1, productId 1, quantity 5, unitPrice 50.00
    When I create the order via V1 API
    And I update the order quantity to 10 and unitPrice to 45.00 via V1 API
    Then the response status code should be 200
    And the response "quantity" field should be 10
    And the response "unitPrice" field should be 45

  Scenario: Delete order
    Given an order with orderNumber "DELETE", userId 1, productId 1, quantity 1, unitPrice 25.00
    When I create the order via V1 API
    And I delete the order via V1 API
    Then the response status code should be 204

  Scenario: Retrieve all orders
    Given an order with orderNumber "ALL1", userId 1, productId 1, quantity 1, unitPrice 30.00
    When I create the order via V1 API
    And I retrieve all orders via V1 API
    Then the response status code should be 200
    And the response should be a non-empty list

  Scenario: Retrieve orders by user ID
    Given an order with orderNumber "USER", userId 3, productId 1, quantity 2, unitPrice 60.00
    When I create the order via V1 API
    And I retrieve orders by userId 3 via V1 API
    Then the response status code should be 200
    And the response should be a non-empty list

  Scenario: Retrieve orders by product ID
    Given an order with orderNumber "PRODUCT", userId 1, productId 5, quantity 3, unitPrice 40.00
    When I create the order via V1 API
    And I retrieve orders by productId 5 via V1 API
    Then the response status code should be 200
    And the response should be a non-empty list

  Scenario: Retrieve non-existent order returns 404
    When I try to retrieve a non-existent order with ID 999999 via V1 API
    Then the response status code should be 404
    And the response should contain error message

  Scenario: Create order with invalid data returns 400
    When I try to create an order with invalid data via V1 API
    Then the response status code should be 400

  Scenario: Create multiple orders for the same user
    Given an order with orderNumber "MULTI1", userId 4, productId 1, quantity 1, unitPrice 100.00
    When I create the order via V1 API
    Then the response status code should be 201
    Given an order with orderNumber "MULTI2", userId 4, productId 2, quantity 2, unitPrice 150.00
    When I create the order via V1 API
    Then the response status code should be 201
    When I retrieve orders by userId 4 via V1 API
    Then the response status code should be 200
    And the response should be a non-empty list
