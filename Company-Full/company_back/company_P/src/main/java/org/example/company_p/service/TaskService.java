package org.example.company_p.service;

import org.example.company_p.dto.SubTaskDto;
import org.example.company_p.dto.TaskDto;
import org.example.company_p.entity.SubTask;

import java.util.List;

public interface TaskService {
    List<TaskDto> getTasksByProjectId(Integer projectId);

    void addTaskToProject(Integer projectId, TaskDto taskDto);

    void addSubTaskToTask(Integer taskId, SubTaskDto subTaskDto);

    void updateSubTask(Integer subTaskId, SubTaskDto dto);

    void deleteSubTask(Integer subTaskId);

    List<SubTask> getSubTaskByTask(Integer taskId);

    // --- Task Update ---
    void updateTask(Integer taskId, TaskDto taskDto);

    void deleteTask(Integer taskId); // Task + subtasks silme
}
