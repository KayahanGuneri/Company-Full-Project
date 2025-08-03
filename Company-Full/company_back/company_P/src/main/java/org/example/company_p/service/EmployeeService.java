package org.example.company_p.service;
import org.example.company_p.dto.EmployeeDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Employee ile ilgili iş mantığını tanımlayan servis arayüzü.
 */

public interface EmployeeService {
    List<EmployeeDto> getAllEmployees();
    Optional<EmployeeDto> getEmployeeById(Long id);
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDTO);
    void deleteEmployee(Long id);
    void patchUpdate(Long id, Map<String,Object> update);
    Optional<EmployeeDto> getEmployeeWithProjects(Long id);
    Optional<EmployeeDto> getEmployeeWithoutProjects(Long id);


    List<EmployeeDto> getActiveEmployee();
}