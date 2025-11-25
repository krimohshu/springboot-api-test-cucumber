package com.microservices.order.test.hooks;

import org.springframework.beans.factory.annotation.Autowired;
import com.microservices.order.test.context.TestContext;

import io.cucumber.java.After;
import io.cucumber.java.Before;

/**
 * Cucumber hooks for setup and teardown
 */
public class TestHooks {

    @Autowired
    private TestContext testContext;

    @Before
    public void setUp() {
        // Setup before each scenario
        // TestContext is already thread-safe with ThreadLocal
    }

    @After
    public void tearDown() {
        // CRITICAL: Reset TestContext to prevent memory leaks in parallel execution
        testContext.reset();
    }
}
