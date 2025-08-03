package org.example.company_p.repository;

import org.example.company_p.dto.ProjectAssignmentDto;
import org.example.company_p.dto.ProjectExperienceDto;
import org.example.company_p.entity.ProjectAssignment;
import org.example.company_p.mapper.ProjectAssignmentRowMapper;
import org.example.company_p.mapper.ProjectExperienceRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProjectExperienceRepository {

    private final JdbcTemplate jdbc;

    public ProjectExperienceRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Tüm proje deneyimlerini döner.
     */
    public List<ProjectExperienceDto> selectAllProjectExp() {
        String sql = """
            SELECT 
                pe.id AS experience_id,
                pe.project_name,
                pe.description,
                pe.employee_name,
                pe.employee_surname,
                e.id AS employee_id,
                pe.start_date AS experience_start_date,
                pe.end_date AS experience_end_date,
                p.start_date AS project_start_date,
                p.end_date AS project_end_date
            FROM project_experiences pe
            JOIN projects p ON p.project_id = pe.id
            JOIN employees e ON e.id = p.employee_id
        """;
        return jdbc.query(sql, new ProjectExperienceRowMapper());
    }

    /**
     * Belirli bir çalışanın proje deneyimlerini döner.
     */
    public List<ProjectExperienceDto> selectProjectExp(Long employeeId) {
        String sql = """
            SELECT 
                pe.id AS experience_id,
                pe.project_name,
                pe.description,
                pe.employee_name,
                pe.employee_surname,
                e.id AS employee_id,
                pe.start_date AS experience_start_date,
                pe.end_date AS experience_end_date,
                p.start_date AS project_start_date,
                p.end_date AS project_end_date
            FROM project_experiences pe
            JOIN projects p ON pe.id = p.project_id
            JOIN employees e ON e.id = p.employee_id
            WHERE p.employee_id = ?
        """;
        return jdbc.query(sql, new Object[]{employeeId}, new ProjectExperienceRowMapper());
    }

    /**
     * Belirli bir proje ve çalışana ait proje deneyimini döner.
     */
    public List<ProjectExperienceDto> findByProjectIdAndEmployeeId(Integer projectId, Long employeeId) {
        String sql = """
            SELECT 
                pe.id AS experience_id,
                pe.project_name,
                pe.description,
                pe.employee_name,
                pe.employee_surname,
                e.id AS employee_id,
                pe.start_date AS experience_start_date,
                pe.end_date AS experience_end_date,
                p.start_date AS project_start_date,
                p.end_date AS project_end_date
            FROM project_experiences pe
            JOIN projects p ON p.project_id = pe.id
            JOIN employees e ON e.id = p.employee_id
            WHERE pe.id = ? AND p.employee_id = ?
        """;
        return jdbc.query(sql, new Object[]{projectId, employeeId}, new ProjectExperienceRowMapper());
    }

    /**
     * Proje güncelleme işlemi.
     */
    public int updateProject(ProjectExperienceDto p) {
        String sql = "UPDATE project_experiences SET project_name = ?, start_date = ?, end_date = ?, description = ? WHERE id = ?";
        return jdbc.update(sql, p.getProjectName(), p.getStartDate(), p.getEndDate(), p.getDescription(), p.getId());
    }

    /**
     * ID’ye göre tek bir proje kaydını döner.
     */
    public Optional<ProjectExperienceDto> findById(Long id) {
        String sql = """
            SELECT 
                pe.id AS experience_id,
                pe.project_name,
                pe.description,
                pe.employee_name,
                pe.employee_surname,
                e.id AS employee_id,
                pe.start_date AS experience_start_date,
                pe.end_date AS experience_end_date,
                p.start_date AS project_start_date,
                p.end_date AS project_end_date
            FROM project_experiences pe
            JOIN projects p ON p.project_id = pe.id
            JOIN employees e ON e.id = p.employee_id
            WHERE pe.id = ?
        """;
        List<ProjectExperienceDto> results = jdbc.query(sql, new Object[]{id}, new ProjectExperienceRowMapper());
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }



}
