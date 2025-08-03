package org.example.company_p.repository;

import lombok.RequiredArgsConstructor;
import org.example.company_p.entity.Task;
import org.example.company_p.mapper.TaskRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing tasks using JDBC.
 * No interface — direct implementation.
 *
 * @author Kayahan
 * @since 02.05.2025
 */
@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final JdbcTemplate jdbc;

    /**
     * Retrieves all tasks associated with a specific project ID.
     */
    public List<Task> findTasksByProjectId(Integer projectId) {
        String sql = "SELECT * FROM tasks WHERE project_id = ?";
        return jdbc.query(sql, new TaskRowMapper(), projectId);
    }

    /**
     * Inserts a new task into the database.
     */
    public int insertTask(Task task) {
        String sql = "INSERT INTO tasks (project_id, title, assigned_user_id, status) VALUES (?, ?, ?, ?)";
        return jdbc.update(sql,
                task.getProjectId(),
                task.getTitle(),
                task.getAssignedUserId(),
                task.getStatus().name()
        );
    }


    /**
     * Deletes a task by Id
     */
    public int deleteTask(Integer taskId){
        String sql = "DELETE FROM tasks WHERE id = ? ";
        return jdbc.update(sql, taskId);
    }


    // TaskRepository içine ekle
    public Optional<Task> findById(Integer taskId) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        List<Task> result = jdbc.query(sql, new TaskRowMapper(), taskId);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public int updateTask(Task task) {
        String sql = "UPDATE tasks SET title = ?, assigned_user_id = ?, status = ? WHERE id = ?";
        return jdbc.update(sql,
                task.getTitle(),
                task.getAssignedUserId(),
                task.getStatus().name(),
                task.getId()
        );
    }





}
