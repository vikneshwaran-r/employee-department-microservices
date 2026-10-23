
package com.example.service;

import com.example.dto.DepartmentRequestDto;
import com.example.entity.Department;
import com.example.repository.DepartmentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.example.exception.ResourceNotFoundException;
import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(
            DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(DepartmentRequestDto dto) {

        if (departmentRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException(
                    "Department already exists: " + dto.getName());
        }

        Department department = new Department();
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());

        return departmentRepository.save(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: " + id));
    }

    @Override
    public Department updateDepartment(
            Long id, DepartmentRequestDto dto) {

        Department department = getDepartmentById(id);

        boolean duplicate = departmentRepository.findAll().stream()
                .anyMatch(existing ->
                        !existing.getId().equals(id)
                        && existing.getName().equalsIgnoreCase(dto.getName()));

        if (duplicate) {
            throw new IllegalArgumentException(
                    "Department name already exists: " + dto.getName());
        }

        department.setName(dto.getName());
        department.setDescription(dto.getDescription());

        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        departmentRepository.delete(department);
    }
}
