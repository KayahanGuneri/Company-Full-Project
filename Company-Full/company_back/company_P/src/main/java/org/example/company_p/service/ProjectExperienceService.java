package org.example.company_p.service;

import org.example.company_p.dto.EmployeeBasicDto;
import org.example.company_p.dto.ProjectExperienceDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Contract for project experience services.
 *
 * @author Kayahan Güneri
 * @since 09.04.2025 14:50
 */
public interface ProjectExperienceService {

    /**
     * Inserts a new project experience and assignment.
     */
    ProjectExperienceDto insertProject(ProjectExperienceDto dto);

    /**
     * Returns project experiences for a given employee.
     */
    List<ProjectExperienceDto> selectProjectExp(Long employeeId);

    /**
     * Returns all project experiences.
     */
    List<ProjectExperienceDto> selectAllProjectExp();

    /**
     * Updates an existing project.
     */
    ProjectExperienceDto updateProject(ProjectExperienceDto dto);

    /**
     * Returns employees assigned to a project with work period and active status.
     */
    List<EmployeeBasicDto> getEmployeeByProjectId(Long projectId);

    /**
     * Partially updates a project.
     */
    void patchUpdate(Long projectId, Map<String, Object> update);

    /**
     * Retrieves detailed project information by ID.
     */
    Optional<ProjectExperienceDto> getProjectById(Long id);
}
