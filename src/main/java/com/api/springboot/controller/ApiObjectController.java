package com.api.springboot.controller;

import com.api.springboot.dto.ApiObjectRequest;
import com.api.springboot.dto.ApiObjectResponse;
import com.api.springboot.service.ApiObjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objects")
@RequiredArgsConstructor
public class ApiObjectController {
    
    private final ApiObjectService service;
    
    @GetMapping
    public ResponseEntity<List<ApiObjectResponse>> getAllObjects() {
        return ResponseEntity.ok(service.getAllObjects());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiObjectResponse> getObjectById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getObjectById(id));
    }
    
    @PostMapping
    public ResponseEntity<ApiObjectResponse> createObject(@Valid @RequestBody ApiObjectRequest request) {
        ApiObjectResponse created = service.createObject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiObjectResponse> updateObject(
            @PathVariable Long id,
            @Valid @RequestBody ApiObjectRequest request) {
        return ResponseEntity.ok(service.updateObject(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObject(@PathVariable Long id) {
        service.deleteObject(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ApiObjectResponse>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }
}
