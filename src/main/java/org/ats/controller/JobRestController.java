package org.ats.controller;

import lombok.RequiredArgsConstructor;
import org.ats.dto.JobDto;
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

    // ============================================================
    // A — List + Read
    // ============================================================

    // TODO A: gọi jobService.getAll(keyword) -> map sang List<JobDto> (dùng toDto)
    @GetMapping
    public List<JobDto> list(@RequestParam(required = false) String keyword) {
        return null;
    }

    // TODO A: lấy JobRequest qua jobService.getJobById(id) rồi map sang JobDto,
    // hoặc thêm service mới trả JobDto trực tiếp. Ném JobNotFoundException nếu không thấy.
    @GetMapping("/{id}")
    public JobDto getById(@PathVariable Long id) {
        return null;
    }

    // ============================================================
    // B — Create
    // ============================================================

    // TODO B: validate dto (title != blank, deadline != null), map sang JobRequest,
    // gọi jobService.createJob, trả 201 + JobDto. Lỗi -> ApiExceptionHandling.
    @PostMapping
    public ResponseEntity<JobDto> create(@RequestBody JobDto dto) {
        return null;
    }

    // ============================================================
    // C — Edit + Delete
    // ============================================================

    // TODO C: load existing job qua id, gán field từ dto, gọi jobService.updateJob(id, JobRequest),
    // trả JobDto. Ném JobNotFoundException nếu không thấy.
    @PutMapping("/{id}")
    public JobDto update(@PathVariable Long id, @RequestBody JobDto dto) {
        return null;
    }

    // TODO C: gọi jobService.delete(id), trả 204. Lỗi -> ApiExceptionHandling.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return null;
    }
}
