package org.example.company_p.dto;


import lombok.Data;
import java.time.LocalDate;
@Data

public class EmployeeBasicDto {

    private Long id;
    private String name;
    private String surname;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private String durationText;
}
