package org.ats.controller;

import lombok.RequiredArgsConstructor;
import org.ats.dto.DepartmentDto;
import org.ats.services.DepartmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentApiController {
    private final DepartmentService departmentService;


    @GetMapping
    public List<DepartmentDto> findAll() {
        return departmentService.findAll().stream().
                map(dept -> {
                    DepartmentDto dto = new DepartmentDto();
                    dto.setId(dept.getId());
                    dto.setDepartmentName(dept.getDepartmentName());
                    return dto;
                }).collect(Collectors.toList());
    }
}
