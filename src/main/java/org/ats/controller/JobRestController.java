package org.ats.controller;

import lombok.RequiredArgsConstructor;
import org.ats.dto.JobRequest;
import org.ats.entities.Job;
import org.ats.services.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobRestController {
    private final JobService jobService;



    @GetMapping
    public List<JobRequest> list(@RequestParam(required = false) String keyword) {
        return jobService.getAllAsDto(keyword);
    }


    @GetMapping("/{id}")
    public JobRequest getById(@PathVariable Long id) {

        return jobService.getJobById(id);
    }

    // ============================================================
    // B — Create
    // ============================================================

    @PostMapping
    public ResponseEntity<JobRequest> create(@RequestBody JobRequest dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new RuntimeException("title must not be blank");
        }
        if (dto.getDeadline() == null) {
            throw new RuntimeException("deadline must not be null");
        }
        if (dto.getMinSalary() == null || dto.getMaxSalary() == null
                || dto.getMinSalary() > dto.getMaxSalary()) {
            throw new RuntimeException("salary range invalid");
        }
        Job created = jobService.createJob(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.toDto(created));
    }

    // ============================================================
    // C — Edit + Delete
    // ============================================================

    @PutMapping("/{id}")
    public JobRequest update(@PathVariable Long id, @RequestBody JobRequest dto) {
        dto.setId(id);
        Job updated = jobService.updateJob(id, dto);
        return jobService.toDto(updated);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
