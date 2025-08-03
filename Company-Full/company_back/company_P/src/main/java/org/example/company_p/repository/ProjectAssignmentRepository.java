package org.example.company_p.repository;

import lombok.RequiredArgsConstructor;
import org.example.company_p.entity.ProjectAssignment;
import org.example.company_p.mapper.ProjectAssignmentRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectAssignmentRepository {

    private final JdbcTemplate jdbc;

    public int insert(ProjectAssignment assignment) {
        String sql = """
            INSERT INTO projects (project_id, employee_id, start_date, end_date)
            VALUES (?, ?, ?, ?)
        """;
        return jdbc.update(sql,
                assignment.getProjectId(),
                assignment.getEmployeeId(),
                assignment.getStartDate(),
                assignment.getEndDate());
    }

    public List<ProjectAssignment> findByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM projects WHERE employee_id = ?";
        return jdbc.query(sql, new Object[]{employeeId}, new ProjectAssignmentRowMapper());
    }

    public List<ProjectAssignment> findByProjectId(Long projectId) {
        String sql = "SELECT * FROM projects WHERE project_id = ?";
        return jdbc.query(sql, new Object[]{projectId}, new ProjectAssignmentRowMapper());
    }

    public Optional<ProjectAssignment> findByProjectIdAndEmployeeId(Long projectId, Long employeeId) {
        String sql = """
            SELECT * FROM projects
            WHERE project_id = ? AND employee_id = ?
        """;
        List<ProjectAssignment> results = jdbc.query(sql, new Object[]{projectId, employeeId}, new ProjectAssignmentRowMapper());
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public boolean hasOverlap(Long projectId, Long employeeId, LocalDate newStart, LocalDate newEnd) {
        String sql = """
            SELECT COUNT(*) FROM projects
            WHERE project_id = ?
              AND employee_id = ?
              AND (
                    (start_date <= ? AND (end_date IS NULL OR end_date >= ?))
                    OR
                    (? <= start_date AND (? IS NULL OR ? >= start_date))
              )
        """;
        Object[] params = {
                projectId,
                employeeId,
                newEnd,
                newStart,
                newStart,
                newEnd,
                newEnd
        };

        int[] types = {
                Types.BIGINT,
                Types.BIGINT,
                Types.DATE,
                Types.DATE,
                Types.DATE,
                Types.DATE,
                Types.DATE
        };

        Integer count = jdbc.queryForObject(sql, params, types, Integer.class);
        return count != null && count > 0;
    }

    // Eksik metot eklendi
    public int updateDates(ProjectAssignment assignment) {
        String sql = """
            UPDATE projects
            SET start_date = ?, end_date = ?
            WHERE project_id = ? AND employee_id = ?
        """;
        return jdbc.update(sql,
                assignment.getStartDate(),
                assignment.getEndDate(),
                assignment.getProjectId(),
                assignment.getEmployeeId());
    }


    public List<ProjectAssignment> findAssignmentsByEmployee(Long employeeId) {
        String sql = """
                SELECT project_id,employee_id,start_date,end_date
                FROM projects
                WHERE employee_id=?
                """;
        return jdbc.query(sql, new Object[]{employeeId},new ProjectAssignmentRowMapper());
    }
}
