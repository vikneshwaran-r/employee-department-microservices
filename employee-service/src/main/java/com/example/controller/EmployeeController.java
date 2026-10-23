
package com.example.controller;

import com.example.dto.EmployeeRequestDto;
import com.example.dto.EmployeeResponseDto;
import com.example.entity.Employee;
import com.example.service.EmployeeService;
import com.example.service.EmployeeServiceImpl;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeServiceImpl employeeServiceImpl;

    public EmployeeController(
            EmployeeService employeeService,
            EmployeeServiceImpl employeeServiceImpl) {
        this.employeeService = employeeService;
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody EmployeeRequestDto dto) {

        Employee employee = employeeService.createEmployee(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employee);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(
                employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id));
    }

    // Get employee with department details
    @GetMapping("/{id}/details")
    public ResponseEntity<EmployeeResponseDto> getEmployeeWithDepartment(
            @PathVariable Long id) {

//    	GET http://localhost:8081/api/employees/1/details
    	
        return ResponseEntity.ok(
                employeeServiceImpl.getEmployeeWithDepartment(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto dto) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}
