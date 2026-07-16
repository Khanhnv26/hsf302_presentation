package org.ats.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "skills")
@NamedQueries({@NamedQuery(name = "findByName",
        query = "SELECT s FROM Skill s WHERE s.skillName LIKE :keyword")
        , @NamedQuery(name = "findAll", query = "SELECT s FROM Skill s")
        , @NamedQuery(name = "findById", query = "SELECT s FROM Skill s WHERE s.id = :id")
})
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_name", nullable = false, columnDefinition = "VARCHAR(150)")
    private String skillName;

    @OneToMany(mappedBy = "skill")
    Set<JobSkill> jobSkills;

}
