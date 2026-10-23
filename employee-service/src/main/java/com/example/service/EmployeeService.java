
package com.example.service;

import com.example.dto.EmployeeRequestDto;
import com.example.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(EmployeeRequestDto dto);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);

    Employee updateEmployee(Long id, EmployeeRequestDto dto);

    void deleteEmployee(Long id);
}
