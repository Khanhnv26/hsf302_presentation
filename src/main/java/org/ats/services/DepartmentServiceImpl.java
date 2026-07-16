package org.ats.services;

import lombok.RequiredArgsConstructor;
import org.ats.dao.DepartmentDao;
import org.ats.dto.DepartmentDto;
import org.ats.entities.Department;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("departmentService") // IoC
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDao departmentDao;
    
//    public void setDepartmentDao(DepartmentDao departmentDao) {
//        this.departmentDao = departmentDao;
//    }

    @Override
    public List<Department> findAll() {
        return departmentDao.findAll();
    }

    @Override
    public Department createDepartment(DepartmentDto dept) {
        // Check existing (by name) , null?, empty ""

        if ((dept.getDepartmentName() == null) || (dept.getDepartmentName().isEmpty())) {
            throw new RuntimeException("Department name cannot be empty");
        }

        // Check existing (by name)
        if (departmentDao.isExisted(dept.getDepartmentName())) {
            throw new RuntimeException("Department with name " + dept.getDepartmentName() + " already is existing");
        }

        Department department = new Department();
        department.setDepartmentName(dept.getDepartmentName());
        department.setDescription(dept.getDescription());
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        department.setIsDeleted(false);
        department.setCreatedBy("Quynh");

        // Call DAO
        return departmentDao.createDepartment(department);
    }

    @Override
    public Department findById(Long id) {
        return null;
    }
}
