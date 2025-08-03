package org.example.company_p.repository;

import lombok.RequiredArgsConstructor;
import org.example.company_p.entity.SubTask;
import org.example.company_p.mapper.SubTaskRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubTaskRepository {

    private final JdbcTemplate jdbc;

    public List<SubTask> findSubTasksByTaskId(Integer taskId) {
        String sql = "SELECT * FROM sub_tasks WHERE task_id = ?";
        return jdbc.query(sql, new SubTaskRowMapper(), taskId);
    }

    public Optional<SubTask> findById(Integer subTaskId) {
        String sql = "SELECT * FROM sub_tasks WHERE id = ?";
        List<SubTask> result = jdbc.query(sql, new SubTaskRowMapper(), subTaskId);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public int insertSubTask(SubTask subTask) {
        String sql = "INSERT INTO sub_tasks (task_id, title, assigned_user_id, status) VALUES (?, ?, ?, ?)";
        return jdbc.update(sql,
                subTask.getTaskId(),
                subTask.getTitle(),
                subTask.getAssignedUserId(),
                subTask.getStatus().name()
        );
    }

    // FIXED SQL
    public int updateSubtask(SubTask subTask) {
        String sql = "UPDATE sub_tasks SET title = ?, assigned_user_id = ?, status = ? WHERE id = ?";
        return jdbc.update(sql,
                subTask.getTitle(),
                subTask.getAssignedUserId(),
                subTask.getStatus().name(),
                subTask.getId()
        );
    }

    public int deleteSubTask(Integer subTaskId) {
        String sql = "DELETE FROM sub_tasks WHERE id = ?";
        return jdbc.update(sql, subTaskId);
    }
}
