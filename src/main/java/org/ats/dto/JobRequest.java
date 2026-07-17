package org.ats.dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
@Builder
@ToString
public class JobRequest {
    private Long id;

    @NotBlank(message = "title must not be blank")
    private String title;

    private String description;
    private String location;

    @NotNull(message = "minSalary must not be null")
    @PositiveOrZero(message = "minSalary must be >= 0")
    private Double minSalary;

    @NotNull(message = "maxSalary must not be null")
    @PositiveOrZero(message = "maxSalary must be >= 0")
    private Double maxSalary;

    @NotNull(message = "deadline must not be null")
    private LocalDate deadline;

    private String jobType;
    private String status;
    private LocalDate publishedAt;
    private Long departmentId;
    private List<Long> skillIds;
}
