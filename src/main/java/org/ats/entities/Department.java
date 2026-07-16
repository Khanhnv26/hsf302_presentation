package org.ats.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(indexes = {@Index(name = "UNIX_NAME", columnList = "department_name")},
        name = "departments")
@NoArgsConstructor@AllArgsConstructor
@Setter@Getter
@ToString(exclude = {"jobs"})
@Builder
public class Department extends  BaseEntity{

    @Id
    @Column(name = "dept_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_name", unique = true, nullable = false, columnDefinition = "VARCHAR(255)")
    private String departmentName;

    private String description;

    @OneToMany(mappedBy = "department", fetch =  FetchType.LAZY, cascade =   CascadeType.ALL)
    private Set<Job> jobs;
}
