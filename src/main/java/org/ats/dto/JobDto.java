package org.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private LocalDate deadline;
    private String jobType;
    private String status;
    private Long departmentId;
    private List<Long> skillIds;
    private LocalDate publishedAt;
}
