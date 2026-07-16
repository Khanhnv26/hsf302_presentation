package org.ats.repositories;


import org.ats.dto.JobResponse;
import org.ats.entities.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByTitleContainingAndLocationEquals(String keyword, String location,Pageable page);

    @Override
    @EntityGraph(attributePaths = {"department", "skills", "skills.skill"})
    List<Job> findAll();

    @EntityGraph(attributePaths = {"department", "skills", "skills.skill"})
    List<Job> findByTitleContainingOrDescriptionContaining(String title, String des);
    List<Job> findByStatus(String status);

    Optional<Job> findByTitle(String title);

    @Query("""
            SELECT new org.ats.dto.JobResponse(j.id, j.title, j.description, j.location, j.minSalary, j.maxSalary, j.deadline, j.jobType) FROM Job j WHERE j.status = :status
        """)
    Page<JobResponse> findAllByStatus(@Param("status") String status, Pageable pageable);

    @Query("""
            SELECT new org.ats.dto.JobResponse(j.id, j.title, j.description, j.location, j.minSalary, j.maxSalary, j.deadline, j.jobType)
            FROM Job j
            WHERE j.status = :status
              AND (LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """)
    Page<JobResponse> findAllByStatusAndKeyword(@Param("status") String status,
                                                @Param("keyword") String keyword,
                                                Pageable pageable);

}
