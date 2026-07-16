package org.ats.dao;

import lombok.RequiredArgsConstructor;
import org.ats.entities.JobSkill;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JobSkillDaoImpl implements  JobSkillDao {
    private final SessionFactory sessionFactory;


    @Override
    public List<JobSkill> findByJobId(Long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM JobSkill js WHERE js.job.id = :id", JobSkill.class)
                .setParameter("id", id).list();
    }
}
