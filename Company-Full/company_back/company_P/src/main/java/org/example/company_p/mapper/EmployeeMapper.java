package org.example.company_p.mapper;

import org.example.company_p.dto.EmployeeDto;
import org.example.company_p.entity.Employee;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    //İki tabloda da aynı sütünlar olduğu için mapping kullanılmadı(source ve target kullanılmadı)

    //Employee entity-EmployeeDto dönüşümü
    EmployeeDto employeeToEmployeeDto(Employee employee);

    //EmployeeDto-Employee entity dönüşümü
    Employee employeeDtoToEmployee(EmployeeDto employeeDto);


}