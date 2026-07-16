package org.ats.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SkillResponse {
    private Long id;
    private String skillName;

}
