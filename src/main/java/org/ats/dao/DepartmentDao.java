package org.ats.dao;

import org.ats.entities.Department;

import java.util.List;

public interface DepartmentDao {
    List<Department> findAll();

    Department createDepartment(Department dept);

    Department findById(Long id);

    boolean isExisted(String name);

}
