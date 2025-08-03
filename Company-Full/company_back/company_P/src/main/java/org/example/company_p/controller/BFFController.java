package org.example.company_p.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.company_p.dto.EmployeeDto;
import org.example.company_p.dto.ProjectExperienceDto;
import org.example.company_p.service.EmployeeService;
import org.example.company_p.service.ProjectExperienceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/bff")
@RequiredArgsConstructor

public class BFFController {

    private final EmployeeService employeeService;
    private final ProjectExperienceService projectExperienceService;


    /**
     * Returns employee details along with their project history
     *
     * @author Kayahan Güneri
     * @since 18.04.2025  16:13
     */
    @Operation(summary = "Get employee with project experiences", description = "Returns employee basic info + list of project experiences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/employee-dashboard/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeDashboard(@PathVariable Long id) {
        Optional<EmployeeDto> employeeOpt = employeeService.getEmployeeWithProjects(id);
        return employeeOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }







}