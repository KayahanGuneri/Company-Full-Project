package org.example.company_p.mapper;

import org.example.company_p.entity.Task;
import org.example.company_p.enums.TaskStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Converts SQL result rows into Task entities.
 */
public class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setProjectId(rs.getInt("project_id"));
        task.setTitle(rs.getString("title"));
        task.setAssignedUserId(rs.getLong("assigned_user_id"));
        task.setStatus(TaskStatus.valueOf(rs.getString("status")));;
        return task;
    }
}
