package org.ats.dto;

import jakarta.validation.constraints.*;
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

    @NotBlank(message = "title must not be blank")
    private String title;

    private String description;

    @NotBlank(message = "location must not be blank")
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

    private Long departmentId;

    private List<Long> skillIds;

    private LocalDate publishedAt;
}
