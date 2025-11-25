package com.microservices.order.test.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.restassured.response.Response;

/**
 * Thread-safe singleton test context for storing test data across Cucumber steps.
 * Uses ThreadLocal to ensure thread safety for parallel test execution.
 */
@Component
public class TestContext {

    // ThreadLocal storage for each thread's test context
    private static final ThreadLocal<Map<String, Object>> CONTEXT = ThreadLocal.withInitial(ConcurrentHashMap::new);
    
    // REST Assured response storage
    private static final ThreadLocal<Response> RESPONSE = new ThreadLocal<>();

    /**
     * Store a value in the context
     */
    public void set(String key, Object value) {
        CONTEXT.get().put(key, value);
    }

    /**
     * Retrieve a value from the context
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) CONTEXT.get().get(key);
    }

    /**
     * Check if context contains a key
     */
    public boolean contains(String key) {
        return CONTEXT.get().containsKey(key);
    }

    /**
     * Remove a value from the context
     */
    public void remove(String key) {
        CONTEXT.get().remove(key);
    }

    /**
     * Store REST Assured response
     */
    public void setResponse(Response response) {
        RESPONSE.set(response);
    }

    /**
     * Retrieve REST Assured response
     */
    public Response getResponse() {
        return RESPONSE.get();
    }

    /**
     * Clear all context data for the current thread
     * MUST be called in @After hook to prevent memory leaks
     */
    public void reset() {
        CONTEXT.get().clear();
        RESPONSE.remove();
    }
}
