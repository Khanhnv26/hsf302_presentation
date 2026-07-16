package org.ats.services;

import org.ats.dto.JobCriteria;
import org.ats.dto.JobRequest;
import org.ats.dto.JobResponse;
import org.ats.entities.Job;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {
    Job createJob(JobRequest jobRequest);

    Page<Job> search(String title, String location, Integer pageIndex, Integer pageSize);

    List<Job> getAll(String keyword);

    List<JobRequest> getAllAsDto(String keyword);

    void delete(Long id);

    JobRequest getJobById(Long id);
    Job getJobByTitle(String title);

    Page<JobResponse> getJobsByCriteria(JobCriteria criteria, Integer pageNumber, Integer pageSize);
    JobRequest toDto(Job job);
    Job updateJob(Long id, JobRequest jobRequest);
}
