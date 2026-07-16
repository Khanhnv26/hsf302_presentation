package org.ats.controller;

import lombok.RequiredArgsConstructor;
import org.ats.dto.JobRequest;
import org.ats.services.JobService;
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

    // TODO B: validate dto (title != blank, deadline != null), map sang JobRequest,
    // gọi jobService.createJob, trả 201 + JobRequest. Lỗi -> ApiExceptionHandling.
    @PostMapping
    public ResponseEntity<JobRequest> create(@RequestBody JobRequest dto) {
        return null;
    }

    // ============================================================
    // C — Edit + Delete
    // ============================================================

    // TODO C: load existing job qua id, gán field từ dto, gọi jobService.updateJob(id, JobRequest),
    // trả JobRequest. Ném JobNotFoundException nếu không thấy.
    @PutMapping("/{id}")
    public JobRequest update(@PathVariable Long id, @RequestBody JobRequest dto) {
        return null;
    }

    // TODO C: gọi jobService.delete(id), trả 204. Lỗi -> ApiExceptionHandling.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return null;
    }
}
