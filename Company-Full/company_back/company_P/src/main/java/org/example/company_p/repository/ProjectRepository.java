package org.example.company_p.repository;


import org.example.company_p.dto.ProjectExperienceDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbc;

    public ProjectRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    /**
     * Inster a new project if it doesn't already exist
     *
     * @author Kayahan Güneri
     * @since 150.04.2025 11:38
     */

    public Integer insertIfNotExist(String name, LocalDate start, LocalDate end, String description)
    {
        String checkSql = "SELECT id FROM project_experiences WHERE LOWER(project_name) = LOWER(?)";

        List<Integer> existingIds = jdbc.query(checkSql, (rs, rowNum) -> rs.getInt("id"), name);

        // Eğer proje varsa tekrar oluşturma
        if (!existingIds.isEmpty()) {
            return existingIds.get(0);
        }

        // Yoksa ekle ve ID'sini döndür
        String insertSql = "INSERT INTO project_experiences (project_name, start_date, end_date, description) VALUES (?, ?, ?, ?) RETURNING id";
        List<Integer> insertedIds = jdbc.query(insertSql, (rs, rowNum) -> rs.getInt("id"), name, start, end, description);


        if (insertedIds.isEmpty()) {
            throw new RuntimeException("Failed to add project: " + name);
        }

        return insertedIds.get(0);
    }






}