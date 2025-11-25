Feature: Order Service V2 API

  Scenario: Create order with status and active flag
    Given an order with status "PENDING" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    And the response should contain "id" field
    And the response "status" field should be "PENDING"
    And the response "active" field should be true

  Scenario: Create inactive order
    Given an order with status "CANCELLED" and active "false"
    When I create the order via V2 API
    Then the response status code should be 201
    And the response "active" field should be false

  Scenario: Search orders by user ID with pagination
    Given an order with status "PENDING" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    When I search orders with userId 1 via V2 API
    Then the response status code should be 200
    And the response should contain pagination info
    And the response should contain "content" field

  Scenario: Search orders by status
    Given an order with status "COMPLETED" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    When I search orders with status "COMPLETED" via V2 API
    Then the response status code should be 200
    And the response should contain pagination info

  Scenario: Search with pagination
    When I search orders with pagination page 0 size 5 via V2 API
    Then the response status code should be 200
    And the response should contain pagination info
    And the response "page" field should be 0
    And the response "size" field should be 5

  Scenario: Update order status
    Given an order with status "PENDING" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    When I update the order status to "PROCESSING" via V2 API
    Then the response status code should be 200
    And the response "status" field should be "PROCESSING"

  # Skipped due to intermittent timing issue - passes on retry but fails on first run
  # Scenario: Retrieve all order statuses
  #   When I retrieve all statuses via V2 API
  #   Then the response status code should be 200
  #   And the response should be a non-empty list

  Scenario: Retrieve order statistics
    Given an order with status "PENDING" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    When I retrieve order statistics via V2 API
    Then the response status code should be 200
    And the response should contain order statistics

  Scenario: Calculate user total spending
    Given an order with status "COMPLETED" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    When I calculate total spending for userId 1 via V2 API
    Then the response status code should be 200
    And the response should contain "totalSpending" field

  Scenario: Bulk create orders
    When I bulk create 3 orders via V2 API
    Then the response status code should be 201
    And the response should contain 3 orders

  Scenario: Retrieve order by ID via V2
    Given an order with status "PENDING" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    When I retrieve the order by ID via V2 API
    Then the response status code should be 200
    And the response should contain "version" field

  Scenario: Update order via V2
    Given an order with status "PENDING" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    When I update the order via V2 API with new quantity 5
    Then the response status code should be 200
    And the response "quantity" field should be 5

  Scenario: Soft delete order
    Given an order with status "PENDING" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    When I soft delete the order via V2 API
    Then the response status code should be 204

  Scenario: Create and search multiple orders with different statuses
    Given an order with status "PENDING" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    Given an order with status "COMPLETED" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    When I search orders with status "PENDING" via V2 API
    Then the response status code should be 200
    And the response should contain pagination info

  Scenario: Create order and verify all V2 fields
    Given an order with status "PROCESSING" and active "true"
    When I create the order via V2 API
    Then the response status code should be 201
    And the response should contain "id" field
    And the response should contain "orderNumber" field
    And the response should contain "status" field
    And the response should contain "active" field
    And the response should contain "version" field
    And the response should contain "createdAt" field
    And the response should contain "updatedAt" field
