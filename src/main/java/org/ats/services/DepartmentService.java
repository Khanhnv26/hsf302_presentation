package org.ats.services;

import org.ats.dto.DepartmentDto;
import org.ats.entities.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> findAll();

    Department createDepartment(DepartmentDto dept);

    Department findById(Long id);
}
