package org.ats.dao;

import org.ats.entities.JobSkill;

import java.util.List;

public interface JobSkillDao {
    List<JobSkill> findByJobId(Long id);
}
