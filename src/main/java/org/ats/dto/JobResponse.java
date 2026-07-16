package org.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private OffsetDateTime deadline;
    private String jobType;

}
