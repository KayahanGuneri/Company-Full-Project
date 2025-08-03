package org.example.company_p.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.company_p.dto.SubTaskDto;
import org.example.company_p.dto.TaskDto;
import org.example.company_p.entity.Employee;
import org.example.company_p.entity.SubTask;
import org.example.company_p.entity.Task;
import org.example.company_p.enums.TaskStatus;
import org.example.company_p.mapper.TaskMapper;
import org.example.company_p.repository.EmployeeRepository;
import org.example.company_p.repository.SubTaskRepository;
import org.example.company_p.repository.TaskRepository;
import org.example.company_p.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final TaskMapper taskMapper;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<TaskDto> getTasksByProjectId(Integer projectId) {
        List<Task> tasks = taskRepository.findTasksByProjectId(projectId);

        return tasks.stream().map(task -> {
            TaskDto dto = taskMapper.toDto(task);

            // Assigned Employee ad-soyad ekle
            if (task.getAssignedUserId() != null) {
                employeeRepository.findById(task.getAssignedUserId()).ifPresent(emp ->
                        dto.setAssignedUserFullName(emp.getName() + " " + emp.getSurname()));
            }

            // SubTask'ları ekle
            dto.setSubTasks(subTaskRepository.findSubTasksByTaskId(task.getId())
                    .stream()
                    .map(subTask -> {
                        SubTaskDto subDto = new SubTaskDto();
                        subDto.setId(subTask.getId());
                        subDto.setTitle(subTask.getTitle());
                        subDto.setAssignedUserId(subTask.getAssignedUserId());
                        subDto.setStatus(subTask.getStatus());

                        // Subtask Assigned Employee bilgisi
                        if (subTask.getAssignedUserId() != null) {
                            employeeRepository.findById(subTask.getAssignedUserId()).ifPresent(emp ->
                                    subDto.setAssignedUserFullName(emp.getName() + " " + emp.getSurname()));
                        }
                        return subDto;
                    }).toList());
            return dto;
        }).toList();
    }

    @Override
    public void addTaskToProject(Integer projectId, TaskDto taskDto) {
        // Çalışan ID kontrolü
        if (taskDto.getAssignedUserId() != null &&
                !employeeRepository.existsById(taskDto.getAssignedUserId())) {
            throw new EntityNotFoundException("Employee with ID " + taskDto.getAssignedUserId() + " not found");
        }

        Task task = new Task();
        task.setProjectId(projectId);
        task.setTitle(taskDto.getTitle());
        task.setAssignedUserId(taskDto.getAssignedUserId());
        task.setStatus(taskDto.getStatus() != null ? taskDto.getStatus() : TaskStatus.TODO);
        taskRepository.insertTask(task);
    }

    @Override
    public void updateTask(Integer taskId, TaskDto taskDto) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            throw new IllegalArgumentException("Task not found");
        }

        // Çalışan ID kontrolü
        if (taskDto.getAssignedUserId() != null &&
                !employeeRepository.existsById(taskDto.getAssignedUserId())) {
            throw new EntityNotFoundException("Employee with ID " + taskDto.getAssignedUserId() + " not found");
        }

        Task task = taskOpt.get();
        task.setTitle(taskDto.getTitle());
        task.setAssignedUserId(taskDto.getAssignedUserId());
        task.setStatus(taskDto.getStatus());
        taskRepository.updateTask(task);
    }

    @Override
    public void deleteTask(Integer taskId) {
        // Önce subtask'lar silinir
        List<SubTask> subTasks = subTaskRepository.findSubTasksByTaskId(taskId);
        subTasks.forEach(sub -> subTaskRepository.deleteSubTask(sub.getId()));

        taskRepository.deleteTask(taskId);
    }

    @Override
    public List<SubTask> getSubTaskByTask(Integer taskId) {
        return subTaskRepository.findSubTasksByTaskId(taskId);
    }

    @Override
    public void addSubTaskToTask(Integer taskId, SubTaskDto subTaskDto) {
        // Çalışan ID kontrolü
        if (subTaskDto.getAssignedUserId() != null &&
                !employeeRepository.existsById(subTaskDto.getAssignedUserId())) {
            throw new EntityNotFoundException("Employee with ID " + subTaskDto.getAssignedUserId() + " not found");
        }

        SubTask subTask = new SubTask();
        subTask.setTaskId(taskId);
        subTask.setTitle(subTaskDto.getTitle());
        subTask.setAssignedUserId(subTaskDto.getAssignedUserId());
        subTask.setStatus(subTaskDto.getStatus() != null ? subTaskDto.getStatus() : TaskStatus.TODO);
        subTaskRepository.insertSubTask(subTask);
    }

    @Override
    public void updateSubTask(Integer subTaskId, SubTaskDto dto) {
        Optional<SubTask> existingOpt = subTaskRepository.findById(subTaskId);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Subtask not found");
        }

        // Çalışan ID kontrolü
        if (dto.getAssignedUserId() != null &&
                !employeeRepository.existsById(dto.getAssignedUserId())) {
            throw new EntityNotFoundException("Employee with ID " + dto.getAssignedUserId() + " not found");
        }

        SubTask subTask = existingOpt.get();
        subTask.setTitle(dto.getTitle());
        subTask.setAssignedUserId(dto.getAssignedUserId());
        subTask.setStatus(dto.getStatus());
        subTaskRepository.updateSubtask(subTask);
    }

    @Override
    public void deleteSubTask(Integer subTaskId) {
        subTaskRepository.deleteSubTask(subTaskId);
    }
}
