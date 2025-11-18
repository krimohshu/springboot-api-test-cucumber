# Spring Boot API Testing Framework

A comprehensive REST API testing framework built with **Spring Boot**, **REST Assured**, and **Cucumber BDD** featuring thread-safe parallel test execution.

## 🚀 Features

- **Spring Boot 3.2.0** REST API with complete CRUD operations
- **In-Memory H2 Database** for fast, isolated testing
- **REST Assured 5.3.2** for API testing
- **Cucumber BDD** with 15 comprehensive test scenarios
- **Thread-Safe Parallel Execution** (dynamic strategy)
- **JPA/Hibernate** for data persistence
- **Bean Validation** for request validation
- **Global Exception Handling** with proper error responses
- **Lombok** for reduced boilerplate code

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## 🏗️ Project Structure

```
springboot-api-testing/
├── src/
│   ├── main/
│   │   ├── java/com/api/springboot/
│   │   │   ├── Application.java           # Spring Boot main class
│   │   │   ├── config/
│   │   │   │   └── ApplicationConfig.java # Application configuration
│   │   │   ├── controller/
│   │   │   │   └── ApiObjectController.java # REST endpoints
│   │   │   ├── dto/
│   │   │   │   ├── ApiObjectRequest.java   # Request DTO
│   │   │   │   └── ApiObjectResponse.java  # Response DTO
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── model/
│   │   │   │   └── ApiObject.java         # JPA Entity
│   │   │   ├── repository/
│   │   │   │   └── ApiObjectRepository.java # JPA Repository
│   │   │   └── service/
│   │   │       └── ApiObjectService.java   # Business logic
│   │   └── resources/
│   │       └── application.properties      # App configuration
│   └── test/
│       ├── java/com/api/springboot/test/
│       │   ├── config/
│       │   │   └── CucumberSpringConfiguration.java
│       │   ├── context/
│       │   │   └── TestContext.java        # Test state management
│       │   ├── hooks/
│       │   │   └── TestHooks.java          # Before/After hooks
│       │   ├── runner/
│       │   │   └── TestRunner.java         # JUnit Platform Suite
│       │   └── steps/
│       │       ├── GivenSteps.java         # Setup steps
│       │       ├── WhenSteps.java          # Action steps
│       │       └── ThenSteps.java          # Assertion steps
│       └── resources/
│           ├── application-test.properties
│           ├── junit-platform.properties   # Parallel execution config
│           └── features/
│               └── api-object-management.feature
└── pom.xml
```

## 🔧 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/objects` | Get all objects |
| GET | `/api/objects/{id}` | Get object by ID |
| POST | `/api/objects` | Create new object |
| PUT | `/api/objects/{id}` | Update object |
| DELETE | `/api/objects/{id}` | Delete object |
| GET | `/api/objects/search?name={name}` | Search objects by name |

## 📦 Request/Response Examples

### Create Object
```bash
curl -X POST http://localhost:8080/api/objects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15",
    "data": {
      "color": "blue",
      "price": 999.99,
      "storage": "256GB"
    }
  }'
```

Response:
```json
{
  "id": 1,
  "name": "iPhone 15",
  "data": {
    "color": "blue",
    "price": 999.99,
    "storage": "256GB"
  },
  "createdAt": "2025-11-18T12:00:00",
  "updatedAt": "2025-11-18T12:00:00"
}
```

## 🧪 Running Tests

### Run all tests (with parallel execution)
```bash
mvn clean test
```

### Run Spring Boot application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access H2 Console (for debugging)
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (leave empty)
```

## 🔄 Parallel Execution

Tests are configured to run in parallel using Cucumber's dynamic strategy:

**Configuration** (`junit-platform.properties`):
```properties
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.config.strategy=dynamic
cucumber.execution.parallel.config.dynamic.factor=1.0
```

**Performance:**
- Sequential: ~8-10 seconds
- Parallel (4 threads): ~3-4 seconds
- **~60% performance improvement**

## 🧪 Test Scenarios

The framework includes 15 comprehensive BDD scenarios:

1. ✅ Create a new object
2. ✅ Get all objects
3. ✅ Get object by ID
4. ✅ Update an object
5. ✅ Delete an object
6. ✅ Get non-existent object (404)
7. ✅ Create object with invalid data (validation)
8. ✅ Search objects by name
9. ✅ Verify data persistence
10. ✅ Concurrent object creation
11. ✅ Update non-existent object
12. ✅ Delete non-existent object
13. ✅ Verify timestamps (createdAt, updatedAt)
14. ✅ Create multiple objects and verify count
15. ✅ Verify object data types preservation

## 🛡️ Thread-Safety

The framework is designed with thread-safety in mind:

- **REST Assured `path()` method**: Uses native Java JSON parsing (thread-safe)
- **Spring Boot Test Context**: Proper isolation per test
- **TestContext Component**: Thread-safe data storage using ConcurrentHashMap
- **Database Isolation**: Each test uses clean H2 in-memory database

## 🏗️ Architecture Highlights

### Layered Architecture
```
Controller → Service → Repository → Database
     ↓          ↓          ↓
    DTO    Business    Entity
           Logic
```

### Key Design Patterns
- **Repository Pattern**: JPA repositories for data access
- **DTO Pattern**: Separate request/response objects
- **Service Layer**: Business logic separation
- **Exception Handling**: Centralized with @RestControllerAdvice
- **Dependency Injection**: Constructor injection with Lombok @RequiredArgsConstructor

## 📊 Test Reports

After running tests, view the Cucumber report:
```bash
open target/cucumber-reports/cucumber.html
```

## 🔍 Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| Spring Boot | 3.2.0 | Web framework |
| Java | 17 | Programming language |
| REST Assured | 5.3.2 | API testing |
| Cucumber | 7.14.0 | BDD framework |
| JUnit 5 | 5.10.0 | Test runner |
| H2 Database | 2.2.224 | In-memory database |
| Hibernate | 6.3.1 | ORM framework |
| Lombok | 1.18.30 | Code generation |
| AssertJ | 3.24.2 | Fluent assertions |

## 🚀 Quick Start

1. **Clone the repository**
```bash
git clone <repository-url>
cd springboot-api-testing
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run tests**
```bash
mvn test
```

4. **Run the application**
```bash
mvn spring-boot:run
```

## 📝 Writing New Tests

1. Add scenario to `api-object-management.feature`
2. Implement steps in appropriate step definition file
3. Use `TestContext` for sharing state between steps
4. Run tests: `mvn test`

Example:
```gherkin
Scenario: Custom test scenario
  Given the API is running
  When I perform some action
  Then I should see expected result
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is open source and available under the MIT License.

## 👤 Author

**krish**
- GitHub: [@krimohshu](https://github.com/krimohshu)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- REST Assured community for the powerful testing library
- Cucumber team for BDD support
