package org.ats.services;

import lombok.RequiredArgsConstructor;
import org.ats.dao.SkillDao;
import org.ats.dto.SkillResponse;
import org.ats.entities.Skill;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillDao skillDao;

    @Override
    public List<SkillResponse> findAll() {
        List<Skill> skills = skillDao.findAll();


        return toDto(skills);
    }

    private List<SkillResponse> toDto(List<Skill> skills) {
        return skills.stream().map((skill) -> {
            SkillResponse skillResponse = SkillResponse.builder()
                    .id(skill.getId())
                    .skillName(skill.getSkillName()).build();
            return skillResponse;
        }).collect(Collectors.toList());
    }

}
