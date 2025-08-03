package org.example.company_p.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProjectAssignmentDto {


    private Long id;
    private Long projectId;
    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
}