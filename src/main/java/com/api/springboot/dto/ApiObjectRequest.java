package com.api.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiObjectRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    private Object data;
}
