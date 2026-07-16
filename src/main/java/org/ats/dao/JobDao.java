package org.ats.dao;

import org.ats.entities.Job;

import java.util.List;

public interface JobDao {
    Job createJob(Job job);

    List<Job> findByTitle(String title);

    List<Job> findAll(String keyword);

    List<Job> findAll();
    Job updateJob(Job job);

    Job findById(Long id);
    void delete(Long id);
}
