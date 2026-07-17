package org.ats.dto;


import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
@Builder
@ToString
public class JobRequest {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private LocalDate deadline;
    private String jobType;
    private String status;
    private LocalDate publishedAt;
    private Long departmentId;
    private List<Long> skillIds; // 1, 3
}
