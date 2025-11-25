# 🚀 Quick Start Guide - Automation Suite

## TL;DR - Get Started in 30 Seconds

```bash
# 1. Make scripts executable (one time only)
chmod +x *.sh

# 2. Run the master automation script
./automate.sh
```

That's it! The interactive menu will guide you through everything.

---

## Common Tasks

### Task 1: Run Full Test Suite

```bash
# Option A: Using master script (recommended)
./automate.sh
# Select: 9) Full Automation

# Option B: Manual steps
./start-all-services.sh    # Starts all services
./run-tests.sh              # Runs all tests
./view-latest-results.sh    # Shows results
./stop-all-services.sh      # Cleanup
```

### Task 2: Quick Smoke Test

```bash
./start-all-services.sh
./run-tests.sh --tags "@smoke"
./stop-all-services.sh
```

### Task 3: Test Specific Service

```bash
./start-all-services.sh
./run-tests.sh --service order-service
./stop-all-services.sh
```

### Task 4: Development Mode

```bash
# Start services (keep them running)
./start-all-services.sh

# Make code changes...

# Run tests repeatedly
./run-tests.sh --service user-service
./run-tests.sh --service user-service --tags "@v2"

# Stop when done
./stop-all-services.sh
```

---

## Script Reference

| Script | Purpose | Usage |
|--------|---------|-------|
| `automate.sh` | Interactive menu for all operations | `./automate.sh` |
| `start-all-services.sh` | Start all 3 microservices | `./start-all-services.sh` |
| `stop-all-services.sh` | Stop all services & free ports | `./stop-all-services.sh` |
| `run-tests.sh` | Run tests with options | `./run-tests.sh [options]` |
| `view-latest-results.sh` | View test results | `./view-latest-results.sh` |

---

## Test Tag Examples

```bash
# V1 APIs only
./run-tests.sh --tags "@v1"

# V2 APIs only
./run-tests.sh --tags "@v2"

# Smoke tests only
./run-tests.sh --tags "@smoke"

# Exclude skipped tests
./run-tests.sh --tags "not @Skip"

# Order Service V2 APIs
./run-tests.sh --service order-service --tags "@v2"
```

---

## Service Ports

- **User Service**: http://localhost:8082
- **Product Service**: http://localhost:8081  
- **Order Service**: http://localhost:8083

Health Checks:
- http://localhost:8082/actuator/health
- http://localhost:8081/actuator/health
- http://localhost:8083/actuator/health

---

## Logs & Reports

**Service Logs:**
```bash
tail -f logs/user-service.log
tail -f logs/product-service.log
tail -f logs/order-service.log
```

**Test Reports:**
```bash
open test-reports/latest/index.html
```

---

## Troubleshooting One-Liners

```bash
# Check service status
lsof -i :8082 && lsof -i :8081 && lsof -i :8083

# Kill all services manually
lsof -ti :8082 | xargs kill -9
lsof -ti :8081 | xargs kill -9
lsof -ti :8083 | xargs kill -9

# Clean everything
./stop-all-services.sh && rm -rf logs/ pids/ test-reports/

# Rebuild a service
cd user-service && mvn clean package -DskipTests
```

---

## Need Help?

See detailed documentation: [AUTOMATION_README.md](AUTOMATION_README.md)

---

**Happy Testing! 🎉**
