package org.example.company_p.dto;

import lombok.Data;
import org.example.company_p.enums.TaskStatus;

@Data
public class SubTaskDto {
    private Integer id;
    private String title;
    private Long assignedUserId;
    private String assignedUserFullName;
    private TaskStatus status;
}
