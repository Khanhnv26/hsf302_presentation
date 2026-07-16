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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void delete(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));

        jobRepository.delete(job);
    }

    @Override
    public JobRequest getJobById(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job not found"));

        return toDto(job);
    }

    @Override
    public Job getJobByTitle(String title) {
        return null;
    }

    @Override
    public Page<JobResponse> getJobsByCriteria(JobCriteria criteria, Integer pageNumber, Integer pageSize) {
        if (criteria == null) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<JobResponse> page = jobRepository.findAllByStatus(JobStatus.PUBLISH.toString(), pageable);
            return page;
        }
        return new PageImpl(null);
    }

    private JobRequest toDto(Job job) {
        JobRequest jobRequest = JobRequest.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .build();

        if (job.getDeadline() != null) {
            jobRequest.setDeadline(job.getDeadline().toLocalDate());
        }

        if (job.getDepartment() != null) {
            jobRequest.setDepartmentId(job.getDepartment().getId());
        }

        List<JobSkill> jobSkills = jobSkillDao.findByJobId(job.getId());

        List<Long> jobSkillIds = jobSkills.stream().map((jobSkill -> {
            return jobSkill.getSkill().getId();
        })).collect(Collectors.toList());

        jobRequest.setSkillIds(jobSkillIds);

        return jobRequest;
    }

    private Job toEntity(JobRequest jobRequest) {
        Set<JobSkill> jobSkills = jobRequest.getSkillIds().stream().map((skillId) -> {
            JobSkill jobSkill = new JobSkill();
            jobSkill.setSkill(Skill.builder().id(skillId).build()); // 1, 3

            return jobSkill;
        }).collect(Collectors.toSet());


        Job job = Job.builder()
                .id(jobRequest.getId())
                .title(jobRequest.getTitle())
                .deadline(OffsetDateTime.of(jobRequest.getDeadline(), LocalTime.now(), ZoneOffset.ofHours(7)))
                .description(jobRequest.getDescription())
                .location(jobRequest.getLocation())
                .maxSalary(jobRequest.getMaxSalary())
                .minSalary(jobRequest.getMinSalary())
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
