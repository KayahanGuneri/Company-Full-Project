package org.example.company_p.mapper;


import org.example.company_p.dto.ProjectAssignmentDto;
import org.example.company_p.entity.ProjectAssignment;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")


public interface ProjectAssignmentMapper {
    ProjectAssignment toEntity(ProjectAssignmentDto dto);
    ProjectAssignmentDto toDto(ProjectAssignment entity);
}