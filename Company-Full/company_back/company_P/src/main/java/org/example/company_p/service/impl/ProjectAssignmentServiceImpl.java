package org.example.company_p.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.company_p.dto.ProjectAssignmentDto;
import org.example.company_p.entity.ProjectAssignment;
import org.example.company_p.mapper.ProjectAssignmentMapper;
import org.example.company_p.repository.EmployeeRepository;
import org.example.company_p.repository.ProjectAssignmentRepository;
import org.example.company_p.service.ProjectAssignmentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectAssignmentServiceImpl implements ProjectAssignmentService {

    private final ProjectAssignmentRepository assignmentRepository;
    private final EmployeeRepository employeeRepository;

    @Qualifier("projectAssignmentMapperImpl")
    private final ProjectAssignmentMapper assignmentMapper;

    @Override
    public ProjectAssignmentDto assignEmployeeToProject(ProjectAssignmentDto dto) {
        // Tarih çakışması kontrolü
        if (assignmentRepository.hasOverlap(
                dto.getProjectId(),
                dto.getEmployeeId(),
                dto.getStartDate(),
                dto.getEndDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This employee is already assigned during the selected dates."
            );
        }

        ProjectAssignment assignment = assignmentMapper.toEntity(dto);
        int result = assignmentRepository.insert(assignment);
        if (result == 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Employee could not be assigned. Check if project or employee exists or if already assigned."
            );
        }


        return dto;
    }

    @Override
    public List<ProjectAssignmentDto> getAssignmentsByEmployeeId(Long employeeId) {
        return assignmentRepository.findByEmployeeId(employeeId)
                .stream()
                .map(assignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectAssignmentDto> getActiveAssignments(Long employeeId) {
        List<ProjectAssignment> list = assignmentRepository.findByEmployeeId(employeeId);
        return list.stream()
                .filter(a -> a.getEndDate() == null || a.getEndDate().isAfter(LocalDate.now()))
                .map(assignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectAssignmentDto> getPastAssignments(Long employeeId) {
        List<ProjectAssignment> list = assignmentRepository.findByEmployeeId(employeeId);
        return list.stream()
                .filter(a -> a.getEndDate() != null && a.getEndDate().isBefore(LocalDate.now()))
                .map(assignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateAssignmentDates(ProjectAssignmentDto dto) {
        if (dto.getProjectId() == null || dto.getEmployeeId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid project or employee ID.");
        }

        if (!employeeRepository.findByIdAndIsDeletedFalse(dto.getEmployeeId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee does not exist. ID: " + dto.getEmployeeId());
        }

        if (assignmentRepository.hasOverlap(
                dto.getProjectId(),
                dto.getEmployeeId(),
                dto.getStartDate(),
                dto.getEndDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This employee is already assigned during the selected dates."
            );
        }

        ProjectAssignment assignment = assignmentRepository
                .findByProjectIdAndEmployeeId(dto.getProjectId(), dto.getEmployeeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found for update."));

        assignment.setStartDate(dto.getStartDate());
        assignment.setEndDate(dto.getEndDate());

        int updated = assignmentRepository.updateDates(assignment);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update assignment dates.");
        }
    }
}
