package org.example.company_p.entity;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.company_p.enums.TaskStatus;


@Data
public class SubTask {
    private Integer id;
    private Integer taskId;
    private String title;
    private Long assignedUserId;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
