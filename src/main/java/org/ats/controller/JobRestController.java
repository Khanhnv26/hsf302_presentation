package org.ats.controller;

import lombok.RequiredArgsConstructor;
import org.ats.dto.JobDto;
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
        JobRequest req = JobRequest.builder()
                .title(dto.getTitle().trim())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .minSalary(dto.getMinSalary())
                .maxSalary(dto.getMaxSalary())
                .deadline(dto.getDeadline())
                .departmentId(dto.getDepartmentId())
                .skillIds(dto.getSkillIds())
                .build();
        Job created = jobService.createJob(req);
        JobDto response = toJobDto(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    private JobDto toJobDto(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .deadline(job.getDeadline() != null
                        ? job.getDeadline().toLocalDate() : null)
                .status(job.getStatus())
                .departmentId(job.getDepartment() != null
                        ? job.getDepartment().getId() : null)
                .build();
    }
    // ============================================================
    // C — Edit + Delete
    // ============================================================

    // TODO C: load existing job qua id, gán field từ dto, gọi jobService.updateJob(id, JobRequest),
    // trả JobDto. Ném JobNotFoundException nếu không thấy.
    @PutMapping("/{id}")
    public JobDto update(@PathVariable Long id, @RequestBody JobDto dto) {
        JobRequest req = JobRequest.builder()
                .id(id)
                .title(dto.getTitle() != null ? dto.getTitle().trim() : null)
                .description(dto.getDescription())
                .location(dto.getLocation())
                .minSalary(dto.getMinSalary())
                .maxSalary(dto.getMaxSalary())
                .deadline(dto.getDeadline())
                .departmentId(dto.getDepartmentId())
                .skillIds(dto.getSkillIds())
                .build();

        Job updated = jobService.updateJob(id, req);
        return toJobDto(updated);
    }

    // TODO C: gọi jobService.delete(id), trả 204. Lỗi -> ApiExceptionHandling.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
