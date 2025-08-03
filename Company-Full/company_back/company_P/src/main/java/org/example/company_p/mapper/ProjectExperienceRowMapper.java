package org.example.company_p.mapper;

import org.example.company_p.dto.ProjectExperienceDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectExperienceRowMapper implements RowMapper<ProjectExperienceDto> {

    @Override
    public ProjectExperienceDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProjectExperienceDto dto = new ProjectExperienceDto();

        dto.setId(rs.getLong("experience_id"));
        dto.setProjectName(rs.getString("project_name"));
        dto.setDescription(rs.getString("description"));
        dto.setEmployeeName(rs.getString("employee_name"));
        dto.setEmployeeSurname(rs.getString("employee_surname"));

        // Employee ID React tarafına da gitsin
        dto.setEmployeeId(rs.getLong("employee_id"));

        // Projenin kendi tarihleri
        if (rs.getDate("experience_start_date") != null) {
            dto.setStartDate(rs.getDate("experience_start_date").toLocalDate());
        }
        if (rs.getDate("experience_end_date") != null) {
            dto.setEndDate(rs.getDate("experience_end_date").toLocalDate());
        }

        // Çalışanın projedeki tarihleri
        if (rs.getDate("project_start_date") != null) {
            dto.setEmployeeStartDate(rs.getDate("project_start_date").toLocalDate());
        }
        if (rs.getDate("project_end_date") != null) {
            dto.setEmployeeEndDate(rs.getDate("project_end_date").toLocalDate());
        }

        return dto;
    }
}
