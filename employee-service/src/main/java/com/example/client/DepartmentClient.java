
package com.example.client;

import com.example.dto.DepartmentResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DepartmentClient {

    private final RestClient restClient;

    public DepartmentClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

    public DepartmentResponseDto getDepartmentById(Long departmentId) {
        return restClient.get()
                .uri("/api/departments/{id}", departmentId)
                .retrieve()
                .body(DepartmentResponseDto.class);
    }
}
