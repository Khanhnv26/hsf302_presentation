package org.ats.services;

import lombok.RequiredArgsConstructor;
import org.ats.dao.JobSkillDao;
import org.ats.dto.JobCriteria;
import org.ats.dto.JobRequest;
import org.ats.dto.JobResponse;
import org.ats.entities.*;
import org.ats.exceptions.JobNotFoundException;
import org.ats.repositories.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final JobSkillDao jobSkillDao;

    @Override
    public Job createJob(JobRequest jobRequest) {
        // Validate
        return jobRepository.save(toEntity(jobRequest));
    }

    @Override
    public Page<Job> search(String title, String location, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        return jobRepository.findByTitleContainingAndLocationEquals(title, location, pageable);
    }

    @Override
    public List<Job> getAll(String keyword) {
        if (keyword == null) {
            return jobRepository.findAll();
        }

        return jobRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword);
    }

    @Override
    public List<JobRequest> getAllAsDto(String keyword) {
        List<Job> jobs = jobRepository.findAll();
        Stream<Job> stream = jobs.stream();
        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.toLowerCase();
            stream = stream.filter(j ->
                (j.getTitle() != null && j.getTitle().toLowerCase().contains(kw)) ||
                (j.getDescription() != null && j.getDescription().toLowerCase().contains(kw)));
        }
        return stream.map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job not found"));

        jobRepository.delete(job);
    }

    @Override
    public JobRequest getJobById(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job not found"));

        return toDto(job);
    }

    @Override
    public Job getJobByTitle(String title) {
        return jobRepository.findByTitle(title).orElse(null);
    }

    @Override
    public Page<JobResponse> getJobsByCriteria(JobCriteria criteria, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if (criteria == null || criteria.getKeyword() == null || criteria.getKeyword().isBlank()) {
            return jobRepository.findAllByStatus(JobStatus.PUBLISH.toString(), pageable);
        }
        return jobRepository.findAllByStatusAndKeyword(JobStatus.PUBLISH.toString(), criteria.getKeyword().trim(), pageable);
    }



    @Override
    public Job updateJob(Long id, JobRequest jobRequest) {
        // TODO C: load existing job by id, gán field từ jobRequest, save & return.
        // Ném JobNotFoundException nếu id không tồn tại.
        return null;
    }



    @Override
    public JobRequest toDto(Job job) {
        JobRequest dto  = JobRequest.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .jobType(job.getJobType())
                .status(job.getStatus())
                .build();

        if (job.getDeadline() != null) {
            dto.setDeadline(job.getDeadline().toLocalDate());
        }

        if (job.getPublishedAt() != null) {
            dto.setPublishedAt(job.getPublishedAt().toLocalDate());
        }

        if (job.getDepartment() != null) {
            dto.setDepartmentId(job.getDepartment().getId());
        }

        List<JobSkill> jobSkills = new ArrayList<>(job.getSkills());
        dto.setSkillIds(jobSkills.stream()
                .map(js -> js.getSkill().getId())
                .collect(Collectors.toList()));

        return dto;
    }

    private Job toEntity(JobRequest jobRequest) {
        Set<JobSkill> jobSkills = new java.util.HashSet<>();
        if (jobRequest.getSkillIds() != null) {
            jobSkills = jobRequest.getSkillIds().stream().map(skillId -> {
                JobSkill jobSkill = new JobSkill();
                jobSkill.setSkill(Skill.builder().id(skillId).build());
                return jobSkill;
            }).collect(Collectors.toSet());
        }

        Job job = Job.builder()
                .id(jobRequest.getId())
                .title(jobRequest.getTitle())
                .deadline(jobRequest.getDeadline() != null
                        ? OffsetDateTime.of(jobRequest.getDeadline(), LocalTime.now(), ZoneOffset.ofHours(7))
                        : null)
                .description(jobRequest.getDescription())
                .location(jobRequest.getLocation())
                .maxSalary(jobRequest.getMaxSalary())
                .minSalary(jobRequest.getMinSalary())
                .jobType(jobRequest.getJobType())
                .status(JobStatus.DRAFT.toString())
                .build();

        if (jobRequest.getDepartmentId() != null) {
            job.setDepartment(Department.builder().id(jobRequest.getDepartmentId()).build());
        }

        for (JobSkill jobSkill : jobSkills) {
            jobSkill.setJob(job);
        }

        job.setSkills(jobSkills);

        return job;
    }
}
