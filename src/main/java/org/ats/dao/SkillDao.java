package org.ats.dao;


import org.ats.entities.Skill;

import java.util.List;

public interface SkillDao {
    Skill createSkill(Skill skill);

    void delete(Long id);

    Skill updateSkill(Skill skill);

    List<Skill> findByName(String keyword);

    List<Skill> findAll();

}
