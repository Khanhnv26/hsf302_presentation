package org.ats.controller;

import lombok.RequiredArgsConstructor;
import org.ats.dto.JobCriteria;
import org.ats.dto.JobResponse;
import org.ats.services.JobService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/public/jobs")
@RequiredArgsConstructor
public class PublicJobController {
    private  final JobService jobService;

    @GetMapping
    public String findAll(@RequestParam(name = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "6") Integer pageSize,
                          @RequestParam(name = "keyword", required = false) String keyword,
                          Model model) {
        JobCriteria criteria = (keyword != null && !keyword.isBlank()) ? new JobCriteria(keyword) : null;
        Page<JobResponse> page = jobService.getJobsByCriteria(criteria, pageNumber, pageSize);
        model.addAttribute("jobs", page.getContent());
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("keyword", keyword);
        return "views/public/browse_job";
    }
}
