package org.example.company_p.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.company_p.dto.EmployeeBasicDto;
import org.example.company_p.dto.ProjectAssignmentDto;
import org.example.company_p.dto.ProjectExperienceDto;
import org.example.company_p.entity.Employee;
import org.example.company_p.entity.ProjectAssignment;
import org.example.company_p.mapper.ProjectAssignmentMapper;
import org.example.company_p.mapper.ProjectExperienceMapper;
import org.example.company_p.repository.EmployeeRepository;
import org.example.company_p.repository.ProjectAssignmentRepository;
import org.example.company_p.repository.ProjectExperienceRepository;
import org.example.company_p.repository.ProjectRepository;
import org.example.company_p.service.ProjectExperienceService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectExperienceServiceImpl implements ProjectExperienceService {

    @Qualifier("projectAssignmentMapperImpl")
    private final ProjectAssignmentMapper assignmentMapper;
    private final ProjectExperienceRepository repository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectAssignmentRepository assignmentRepository;

    @Override
    @Transactional
    public ProjectExperienceDto insertProject(ProjectExperienceDto dto) {
        Long employeeId = dto.getEmployeeId();



        if (!employeeRepository.findByIdAndIsDeletedFalse(employeeId).isPresent()) {
            throw new IllegalArgumentException("Employee does not exist.");
        }

        String normalizedName = dto.getProjectName().trim().toLowerCase();

        Integer projectId = projectRepository.insertIfNotExist(
                normalizedName,
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getDescription()
        );

        if (assignmentRepository.hasOverlap(
                Long.valueOf(projectId),
                employeeId,
                dto.getStartDate(),
                dto.getEndDate())) {
            throw new RuntimeException("Employee already has an overlapping assignment in this project.");
        }

        ProjectAssignment assignment = ProjectAssignment.builder()
                .projectId(Long.valueOf(projectId))
                .employeeId(employeeId)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        int inserted = assignmentRepository.insert(assignment);
        if (inserted == 0) {
            throw new RuntimeException("Failed to insert project assignment.");
        }

        List<ProjectExperienceDto> matches = repository.findByProjectIdAndEmployeeId(projectId, employeeId);
        ProjectExperienceDto saved = matches.get(matches.size() - 1);
        saved.setDescription(dto.getDescription());

        return saved;
    }

    @Override
    public List<ProjectExperienceDto> selectProjectExp(Long employeeId) {
        List<ProjectExperienceDto> list = repository.selectProjectExp(employeeId);

        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        return list; // Mapper’a gerek yok çünkü zaten DTO
    }

    @Override
    public List<ProjectExperienceDto> selectAllProjectExp() {
        List<ProjectExperienceDto> all = repository.selectAllProjectExp();

        if (all == null || all.isEmpty()) {
            return Collections.emptyList();
        }

        return all; // Mapper’a gerek yok çünkü zaten DTO
    }

    @Override
    public ProjectExperienceDto updateProject(ProjectExperienceDto dto) {
        Optional<ProjectExperienceDto> existingOpt = repository.findById(dto.getId());
        ProjectExperienceDto existing = existingOpt.orElseThrow(
                () -> new IllegalArgumentException("Project does not exist. " + dto.getId())
        );

        existing.setProjectName(dto.getProjectName());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setDescription(dto.getDescription());

        repository.updateProject(existing);

        return repository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Failed to fetch updated project."));
    }

    @Override
    public List<EmployeeBasicDto> getEmployeeByProjectId(Long projectId) {
        List<ProjectAssignment> assignments = assignmentRepository.findByProjectId(projectId);

        return assignments.stream().map(assignment -> {
            Employee employee = employeeRepository.findById(assignment.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee does not exist."));
            EmployeeBasicDto dto = new EmployeeBasicDto();
            dto.setId(employee.getId());
            dto.setName(employee.getName());
            dto.setSurname(employee.getSurname());
            dto.setStartDate(assignment.getStartDate());
            dto.setEndDate(assignment.getEndDate());
            dto.setActive(assignment.getEndDate() == null || assignment.getEndDate().isAfter(LocalDate.now()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void patchUpdate(Long projectId, Map<String, Object> update) {
        ProjectExperienceDto project = repository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project does not exist. " + projectId));

        if (update.containsKey("projectName")) {
            project.setProjectName(update.get("projectName").toString());
        }
        if (update.containsKey("startDate")) {
            project.setStartDate(LocalDate.parse((String) update.get("startDate")));
        }
        if (update.containsKey("endDate")) {
            project.setEndDate(LocalDate.parse((String) update.get("endDate")));
        }
        if (update.containsKey("description")) {
            project.setDescription(update.get("description").toString());
        }

        repository.updateProject(project);
    }

    @Override
    public Optional<ProjectExperienceDto> getProjectById(Long id) {
        // mapper::toDto kaldırıldı çünkü repository zaten DTO döndürüyor
        return repository.findById(id);
    }

    @Transactional
    public void updateAssignmentDates(ProjectAssignmentDto dto) {
        if (dto.getProjectId() == null || dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("Invalid project or employee ID.");
        }

        if (!employeeRepository.findByIdAndIsDeletedFalse(dto.getEmployeeId()).isPresent()) {
            throw new IllegalArgumentException("Employee does not exist. ID: " + dto.getEmployeeId());
        }
        if (assignmentRepository.hasOverlap(
                dto.getProjectId(),
                dto.getEmployeeId(),
                dto.getStartDate(),
                dto.getEndDate())) {
            throw new RuntimeException("Employee already has an overlapping assignment for this project.");
        }

        ProjectAssignment assignment = assignmentRepository
                .findByProjectIdAndEmployeeId(dto.getProjectId(), dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Assignment not found for update."));

        assignment.setStartDate(dto.getStartDate());
        assignment.setEndDate(dto.getEndDate());

        int updated = assignmentRepository.updateDates(assignment);
        if (updated == 0) {
            throw new RuntimeException("Failed to update assignment dates.");
        }
    }

    public List<ProjectAssignmentDto> getAssignmentsForOverlapCheck(Long employeeId) {
        List<ProjectAssignment> list = assignmentRepository.findAssignmentsByEmployee(employeeId);
        return list.stream()
                .map(assignmentMapper::toDto)
                .toList();
    }

}
