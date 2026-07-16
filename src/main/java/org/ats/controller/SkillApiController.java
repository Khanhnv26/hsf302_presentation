package org.ats.controller;

import lombok.RequiredArgsConstructor;
import org.ats.dto.SkillResponse;
import org.ats.services.SkillService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillApiController {
    private final SkillService skillService;

    @GetMapping
    public List<SkillResponse> findAll() {
        return skillService.findAll();
    }
}
