package org.example.company_p.controller;

import lombok.RequiredArgsConstructor;
import org.example.company_p.dto.SubTaskDto;
import org.example.company_p.dto.TaskDto;
import org.example.company_p.entity.SubTask;
import org.example.company_p.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<TaskDto> getTasks(@PathVariable Integer projectId) {
        return taskService.getTasksByProjectId(projectId);
    }

    @PostMapping("/project/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addTask(@PathVariable Integer projectId, @RequestBody TaskDto dto) {
        taskService.addTaskToProject(projectId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task added");
    }

    // --- Yeni Eklenen: Task Güncelleme
    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateTask(@PathVariable Integer taskId, @RequestBody TaskDto dto) {
        taskService.updateTask(taskId, dto);
        return ResponseEntity.ok("Task updated");
    }

    // --- Yeni Eklenen: Task Silme
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted");
    }

    // --- SubTask Endpointleri ---
    @GetMapping("/{taskId}/subtasks")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<SubTask> getSubTasks(@PathVariable Integer taskId) {
        return taskService.getSubTaskByTask(taskId);
    }

    @PostMapping("/{taskId}/subtasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addSubTask(@PathVariable Integer taskId, @RequestBody SubTaskDto dto) {
        taskService.addSubTaskToTask(taskId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Sub-task added");
    }

    @PutMapping("/subtasks/{subTaskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateSubTask(@PathVariable Integer subTaskId, @RequestBody SubTaskDto dto) {
        taskService.updateSubTask(subTaskId, dto);
        return ResponseEntity.ok("Sub-task updated");
    }

    @DeleteMapping("/subtasks/{subTaskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSubTask(@PathVariable Integer subTaskId) {
        taskService.deleteSubTask(subTaskId);
        return ResponseEntity.ok("Sub-task deleted");
    }
}
