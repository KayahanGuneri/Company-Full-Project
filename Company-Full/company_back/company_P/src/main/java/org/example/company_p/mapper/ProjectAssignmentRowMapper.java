package org.example.company_p.mapper;

import org.example.company_p.entity.ProjectAssignment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ProjectAssignmentRowMapper implements RowMapper<ProjectAssignment>{
    @Override
    public ProjectAssignment mapRow(ResultSet rs,int rowNum) throws SQLException {
        ProjectAssignment a=new ProjectAssignment();

        if(rs.getLong("id")!=0L)
            a.setId(rs.getLong("id"));

        if (rs.getInt("project_id")!=0L)
            a.setProjectId(rs.getLong("project_id"));

        if(rs.getLong("employee_id")!=0L)
            a.setEmployeeId(rs.getLong("employee_id"));

        if(rs.getDate("start_date")!=null)
            a.setStartDate(rs.getDate("start_date").toLocalDate());

        if (rs.getDate("end_date")!=null)
            a.setEndDate(rs.getDate("end_date").toLocalDate());

        return a;
    }

}