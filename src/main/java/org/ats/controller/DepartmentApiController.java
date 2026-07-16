package org.ats.controller;

import lombok.RequiredArgsConstructor;
import org.ats.dto.DepartmentDto;
import org.ats.services.DepartmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentApiController {
    private final DepartmentService departmentService;

    // TODO A: departmentService.findAll() -> map sang List<DepartmentDto> (id + departmentName)
    @GetMapping
    public List<DepartmentDto> findAll() {
        return null;
    }
}
