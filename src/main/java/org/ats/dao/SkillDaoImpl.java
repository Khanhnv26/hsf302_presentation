package org.ats.dao;

import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.ats.entities.Skill;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SkillDaoImpl implements SkillDao {
    private final SessionFactory sessionFactory;

    @Override
    public Skill createSkill(Skill skill) {
        Session session = sessionFactory.openSession();

        session.persist(skill);

        return skill;

    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Skill skill = session.get(Skill.class, id);

        session.remove(skill);
    }

    @Override
    public Skill updateSkill(Skill skill) {
        Session session = sessionFactory.openSession();
        session.merge(skill);
        return skill;

    }

    @Override
    public List<Skill> findByName(String keyword) {
        Session session = sessionFactory.openSession();
        TypedQuery<Skill> query = session.createNamedQuery("findByName", Skill.class);
        query.setParameter("keyword", "%" + keyword + "%");

        return query.getResultList();
    }

    @Override
    public List<Skill> findAll() {
        return sessionFactory.openSession().createQuery("FROM Skill", Skill.class).list();
    }
}
