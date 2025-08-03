package org.example.company_p.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.company_p.enums.TaskStatus;

import java.util.List;


@Data
public class TaskDto {

    private Integer id;
    private String title;
    private Long assignedUserId;

    private List<SubTaskDto> subTasks;
    private String assignedUserFullName;
    @Schema(description = "Görev durumu", allowableValues = {"TODO", "IN_PROGRESS", "DONE"})
    private TaskStatus status;

}
