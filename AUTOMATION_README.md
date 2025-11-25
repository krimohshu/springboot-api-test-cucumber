# 🚀 Microservices Test Automation Suite

Complete automation solution for managing Spring Boot microservices lifecycle: starting, testing, reporting, and teardown.

## 📋 Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Available Scripts](#available-scripts)
- [Usage Examples](#usage-examples)
- [Test Tag Filtering](#test-tag-filtering)
- [Directory Structure](#directory-structure)
- [Troubleshooting](#troubleshooting)

## 🎯 Overview

This automation suite provides comprehensive tooling for:

- ✅ **Service Management**: Start/Stop all microservices with health checks
- ✅ **Automated Testing**: Run tests with tag filtering and parallel execution
- ✅ **Test Reporting**: Generate and view HTML test reports
- ✅ **Port Management**: Automatic port conflict detection and cleanup
- ✅ **Interactive Menu**: User-friendly CLI interface for all operations

### Services Included

| Service | Port | Database | API Versions |
|---------|------|----------|--------------|
| User Service | 8082 | H2 (userdb) | V1, V2 |
| Product Service | 8081 | H2 (productdb) | V1, V2 |
| Order Service | 8083 | H2 (orderdb) | V1, V2 |

## 🔧 Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.8+ installed and in PATH
- **Bash**: macOS/Linux shell (tested on macOS)
- **Ports**: 8081, 8082, 8083 must be available
- **Tools**: `lsof`, `curl` (usually pre-installed)

## 🚀 Quick Start

### 1. Make Scripts Executable

```bash
chmod +x *.sh
```

### 2. Run Master Automation Script

```bash
./automate.sh
```

This opens an interactive menu with all options.

### 3. Or Use Individual Scripts

```bash
# Start all services
./start-all-services.sh

# Run all tests
./run-tests.sh

# View latest results
./view-latest-results.sh

# Stop all services
./stop-all-services.sh
```

## 📜 Available Scripts

### 1. `automate.sh` - Master Automation Script

Interactive menu-driven interface for all operations.

```bash
./automate.sh
```

**Features:**
- Start/Stop/Restart all services
- Run tests with various options
- View test results
- Check service status
- Full automation pipeline (start → test → report → stop)

### 2. `start-all-services.sh` - Start Services

Starts all three microservices with health checks.

```bash
./start-all-services.sh
```

**What it does:**
- Builds each service (Maven package)
- Starts services in background (nohup)
- Waits for health check confirmation
- Saves PIDs for cleanup
- Creates logs in `logs/` directory

**Output:**
- Service logs: `logs/<service-name>.log`
- Build logs: `logs/<service-name>-build.log`
- PID files: `pids/<service-name>.pid`

### 3. `run-tests.sh` - Test Runner

Runs tests with advanced filtering and reporting.

```bash
# Run all tests
./run-tests.sh

# Run tests with tag filtering
./run-tests.sh --tags "@smoke"
./run-tests.sh --tags "not @Skip"

# Run tests for specific service
./run-tests.sh --service user-service

# Run with code coverage
./run-tests.sh --coverage

# Combined options
./run-tests.sh --service order-service --tags "@v2" --coverage
```

**Options:**

| Option | Description | Example |
|--------|-------------|---------|
| `-s, --service <name>` | Run tests for specific service | `--service user-service` |
| `-t, --tags <tags>` | Cucumber tag filter | `--tags "@smoke"` |
| `-c, --coverage` | Generate coverage report | `--coverage` |
| `-f, --fail-fast` | Stop on first failure | `--fail-fast` |
| `-h, --help` | Show help message | `--help` |

### 4. `view-latest-results.sh` - Results Viewer

View latest test execution results.

```bash
./view-latest-results.sh
```

**Features:**
- Summary of all test results
- Pass/Fail statistics per service
- Interactive menu to view logs
- Opens HTML report in browser

### 5. `stop-all-services.sh` - Service Teardown

Stops all services and releases ports.

```bash
./stop-all-services.sh
```

**What it does:**
- Kills services by PID (graceful)
- Force kills if needed
- Releases ports 8081, 8082, 8083
- Cleans up PID files
- Verifies ports are free

## 💡 Usage Examples

### Example 1: Quick Test Run

```bash
# Start services, run tests, stop services
./start-all-services.sh
./run-tests.sh
./stop-all-services.sh
```

### Example 2: Development Workflow

```bash
# Start services
./start-all-services.sh

# Run smoke tests only
./run-tests.sh --tags "@smoke"

# Make code changes...

# Re-run specific service tests
./run-tests.sh --service order-service

# Keep services running for manual testing
# Stop when done
./stop-all-services.sh
```

### Example 3: CI/CD Pipeline

```bash
# Full automation in one go
./automate.sh

# Then select option 9: Full Automation
# Or use scripts directly:
./start-all-services.sh && \
./run-tests.sh --fail-fast && \
./stop-all-services.sh
```

### Example 4: V1 vs V2 API Testing

```bash
# Test only V1 APIs
./run-tests.sh --tags "@v1"

# Test only V2 APIs
./run-tests.sh --tags "@v2"

# Test V2 excluding experimental features
./run-tests.sh --tags "@v2 and not @experimental"
```

## 🏷️ Test Tag Filtering

### Available Tags

| Tag | Description | Example |
|-----|-------------|---------|
| `@v1` | V1 API tests | `--tags "@v1"` |
| `@v2` | V2 API tests | `--tags "@v2"` |
| `@smoke` | Smoke tests | `--tags "@smoke"` |
| `@regression` | Regression tests | `--tags "@regression"` |
| `@Skip` | Tests to skip | `--tags "not @Skip"` |

### Tag Combinations

```bash
# AND operator
./run-tests.sh --tags "@v2 and @smoke"

# OR operator
./run-tests.sh --tags "@v1 or @v2"

# NOT operator
./run-tests.sh --tags "not @Skip"

# Complex combinations
./run-tests.sh --tags "@v2 and (@smoke or @regression) and not @Skip"
```

## 📁 Directory Structure

```
.
├── automate.sh                    # Master automation script
├── start-all-services.sh          # Start services script
├── stop-all-services.sh           # Stop services script
├── run-tests.sh                   # Test runner script
├── view-latest-results.sh         # Results viewer script
├── AUTOMATION_README.md           # This file
│
├── user-service/                  # User Service source
├── product-service/               # Product Service source
├── order-service/                 # Order Service source
│
├── logs/                          # Service runtime logs
│   ├── user-service.log
│   ├── product-service.log
│   ├── order-service.log
│   ├── user-service-build.log
│   ├── product-service-build.log
│   └── order-service-build.log
│
├── pids/                          # Process ID files
│   ├── user-service.pid
│   ├── product-service.pid
│   └── order-service.pid
│
└── test-reports/                  # Test execution reports
    ├── latest/                    # Latest test run
    │   ├── index.html            # Combined report
    │   ├── user-service/
    │   ├── product-service/
    │   └── order-service/
    └── <timestamp>/              # Historical runs
```

## 🔍 Troubleshooting

### Problem: Port Already in Use

```bash
# Check what's using the port
lsof -i :8082

# Kill specific process
kill -9 <PID>

# Or use the stop script
./stop-all-services.sh
```

### Problem: Service Won't Start

```bash
# Check build logs
cat logs/user-service-build.log

# Check runtime logs
cat logs/user-service.log

# Try rebuilding manually
cd user-service
mvn clean package
```

### Problem: Tests Fail to Connect

```bash
# Verify services are running
lsof -i :8081
lsof -i :8082
lsof -i :8083

# Check health endpoints
curl http://localhost:8082/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8083/actuator/health

# Restart services
./stop-all-services.sh
./start-all-services.sh
```

### Problem: Permission Denied

```bash
# Make scripts executable
chmod +x *.sh

# Or for all scripts at once
find . -name "*.sh" -exec chmod +x {} \;
```

### Problem: Maven Not Found

```bash
# Check Maven installation
mvn -version

# Install Maven (macOS)
brew install maven

# Set MAVEN_HOME (add to ~/.zshrc or ~/.bash_profile)
export MAVEN_HOME=/usr/local/opt/maven
export PATH=$PATH:$MAVEN_HOME/bin
```

## 📊 Test Reports

### HTML Reports

After running tests, reports are generated at:

```
test-reports/latest/index.html
```

Open in browser:

```bash
open test-reports/latest/index.html
```

Or use the viewer:

```bash
./view-latest-results.sh
```

### Report Contents

- **Combined Dashboard**: Overview of all services
- **Per-Service Reports**: Detailed Cucumber reports
- **Test Logs**: Full test execution logs
- **Pass/Fail Statistics**: Test counts and percentages

## 🎯 Best Practices

1. **Always stop services after testing**
   ```bash
   ./stop-all-services.sh
   ```

2. **Use tag filtering for faster feedback**
   ```bash
   ./run-tests.sh --tags "@smoke"
   ```

3. **Check service status before testing**
   ```bash
   # In automate.sh menu, option 8
   ./automate.sh
   ```

4. **Keep logs for debugging**
   - Logs are in `logs/` directory
   - Not deleted by stop script

5. **Use full automation for CI/CD**
   ```bash
   ./automate.sh
   # Select option 9
   ```

## 🔗 Related Documentation

- [User Service README](user-service/README.md)
- [Product Service README](product-service/README.md)
- [Order Service README](order-service/README.md)

## 📝 License

This automation suite is part of the Spring Boot Microservices Testing project.

## 🤝 Contributing

Improvements welcome! Key areas:

- Windows compatibility (WSL/PowerShell)
- Docker integration
- CI/CD pipeline templates
- Additional test tags

---

**Made with ❤️ for Spring Boot Microservices Testing**
