package org.example.company_p.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

/**
 * Project experience data transfer object.
 *
 * @author Kayahan Güneri
 * @since 09.04.2025 14:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectExperienceDto {

    private String employeeName;
    private String employeeSurname;
    private Long id;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long employeeId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate employeeStartDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate employeeEndDate;
    private boolean active;
    private String description;



    // Additional fields.
    private String durationText;

}