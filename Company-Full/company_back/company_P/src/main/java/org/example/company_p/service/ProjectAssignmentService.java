package org.example.company_p.service;

import org.example.company_p.dto.ProjectAssignmentDto;
import java.util.List;

public interface ProjectAssignmentService {
    ProjectAssignmentDto assignEmployeeToProject(ProjectAssignmentDto dto);
    List<ProjectAssignmentDto> getAssignmentsByEmployeeId(Long employeeId);
    List<ProjectAssignmentDto> getActiveAssignments(Long employeeId);
    List<ProjectAssignmentDto> getPastAssignments(Long employeeId);
    void updateAssignmentDates(ProjectAssignmentDto dto);

}
