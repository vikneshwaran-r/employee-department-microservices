
package com.example.service;

import com.example.dto.DepartmentRequestDto;
import com.example.entity.Department;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(DepartmentRequestDto dto);

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    Department updateDepartment(Long id, DepartmentRequestDto dto);

    void deleteDepartment(Long id);
}
