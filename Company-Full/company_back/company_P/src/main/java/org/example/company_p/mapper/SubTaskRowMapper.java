package org.example.company_p.mapper;

import org.example.company_p.entity.SubTask;
import org.example.company_p.enums.TaskStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Converts SQL result rows into SubTask entities.
 */
public class SubTaskRowMapper implements RowMapper<SubTask> {

    @Override
    public SubTask mapRow(ResultSet rs, int rowNum) throws SQLException {
        SubTask subTask = new SubTask();
        subTask.setId(rs.getInt("id"));
        subTask.setTaskId(rs.getInt("task_id"));
        subTask.setTitle(rs.getString("title"));
        subTask.setAssignedUserId(rs.getLong("assigned_user_id"));
        subTask.setStatus(TaskStatus.valueOf(rs.getString("status")));
        return subTask;
    }
}
