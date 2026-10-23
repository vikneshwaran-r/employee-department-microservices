
package com.example.service;

import com.example.client.DepartmentClient;
import com.example.dto.DepartmentResponseDto;
import com.example.dto.EmployeeRequestDto;
import com.example.dto.EmployeeResponseDto;
import com.example.entity.Employee;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentClient departmentClient;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            DepartmentClient departmentClient) {
        this.employeeRepository = employeeRepository;
        this.departmentClient = departmentClient;
    }

    @Override
    public Employee createEmployee(EmployeeRequestDto dto) {

        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Employee email already exists: " + dto.getEmail()
            );
        }

        // Verify that the department exists
        departmentClient.getDepartmentById(dto.getDepartmentId());

        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary().doubleValue());
        employee.setDepartmentId(dto.getDepartmentId());

        return employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with ID: " + id
                        )
                );
    }

    // Get employee details along with department details
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeWithDepartment(Long id) {

        Employee employee = getEmployeeById(id);

        DepartmentResponseDto department =
                departmentClient.getDepartmentById(
                        employee.getDepartmentId()
                );

        EmployeeResponseDto response = new EmployeeResponseDto();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setEmail(employee.getEmail());
        response.setSalary(employee.getSalary());
        response.setDepartmentId(employee.getDepartmentId());
        response.setDepartment(department);

        return response;
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeRequestDto dto) {

        Employee employee = getEmployeeById(id);

        employeeRepository.findAll().stream()
                .filter(existing ->
                        existing.getEmail().equals(dto.getEmail()))
                .filter(existing ->
                        !existing.getId().equals(id))
                .findFirst()
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Email already used by another employee"
                    );
                });

        // Verify that the department exists
        departmentClient.getDepartmentById(dto.getDepartmentId());

        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary().doubleValue());
        employee.setDepartmentId(dto.getDepartmentId());

        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }
}
