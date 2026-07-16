package org.ats.repositories;


import org.ats.dto.JobResponse;
import org.ats.entities.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByTitleContainingAndLocationEquals(String keyword, String location,Pageable page);

    List<Job> findByTitleContainingOrDescriptionContaining(String title, String des);
    List<Job> findByStatus(String status);

    @Query("""
            SELECT new org.ats.dto.JobResponse(j.id, j.title, j.description, j.location, j.minSalary, j.maxSalary, j.deadline, j.jobType) FROM Job j WHERE j.status = :status
        """)
    Page<JobResponse> findAllByStatus(@Param("status") String status, Pageable pageable);

}
