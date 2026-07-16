package org.ats.services;

import org.ats.dto.SkillResponse;

import java.util.List;

public interface SkillService {
    List<SkillResponse> findAll();
}
