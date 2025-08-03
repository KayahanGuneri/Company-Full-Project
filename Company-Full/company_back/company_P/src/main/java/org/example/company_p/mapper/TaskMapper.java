package org.example.company_p.mapper;

import org.example.company_p.dto.TaskDto;
import org.example.company_p.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDto toDto(Task task);
    Task toEntity(TaskDto dto);
}
