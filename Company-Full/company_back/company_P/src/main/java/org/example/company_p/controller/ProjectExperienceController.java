package org.example.company_p.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.example.company_p.dto.EmployeeBasicDto;
import org.example.company_p.dto.ProjectAssignmentDto;
import org.example.company_p.dto.ProjectExperienceDto;
import org.example.company_p.service.ProjectAssignmentService;
import org.example.company_p.service.ProjectExperienceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//Bu sınıf tüm ProjectExperine API işlemlerini halleder
@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ProjectExperienceController {

    private final ProjectExperienceService service;
    private final ProjectExperienceService projectExperienceService;
    private final ProjectAssignmentService assignmentService;


    /**
     * It inserts project details.
     *
     * @author Kayahan Güneri
     * @since 09.04.2025 14:47
     */
    @PostMapping("/insertproject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectExperienceDto> insertProject(@RequestBody ProjectExperienceDto  dto){
       ProjectExperienceDto savedDto=service.insertProject(dto);
       return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }


    /**
     * It selects all employee records from "project_experiences" table.
     *
     * @author Kayahan Güneri
     * @since 09.04.2025 17:27
     */
    @GetMapping("/selectallprojectexp")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ProjectExperienceDto>> selectAllProjectExp(){
        return ResponseEntity.ok(service.selectAllProjectExp());
    }

    /**
     * It selects special employee records form "project_experiences" table
     *
     * @author Kayahan Güneri
     * @since 10.04.2025 14:47
     */

    @GetMapping("/projects/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ProjectExperienceDto>> getProjectsByEmployeeId(@PathVariable Long employeeId) {
        List<ProjectExperienceDto> result = service.selectProjectExp(employeeId);
        return ResponseEntity.ok(result);
    }

    /**
     * It selects special employee records form "project_experiences" table
     *
     * @author Kayahan Güneri
     * @since 10.04.2025 15:21
     */

    @PutMapping("/updateproject/{projectid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectExperienceDto> updateProject(@PathVariable Long projectid,@RequestBody ProjectExperienceDto dto){
        dto.setId(projectid);
        ProjectExperienceDto updated = service.updateProject(dto);
        return ResponseEntity.ok(updated);
    }


    /**
     * Returns all employees assigned to the given project ID.
     *
     * @author Kayahan Güneri
     * @since 16.04.2025 17:45
     */

    @GetMapping("/project/{projectId}/employees")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<EmployeeBasicDto>> getEmployeeByProject(@PathVariable Long projectId){
        List<EmployeeBasicDto> result=service.getEmployeeByProjectId(projectId);
        return ResponseEntity.ok(result);
    }

    /**
     * Partially updates a project experience with the given field
     *
     * @author Kayahan Güneri
     * @since 18.04.2025 13:32
     */
    @PatchMapping("api/experiences/oneupdate/{projectid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> patchProject(
            @PathVariable Long projectid,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Update Example",
                                    value = "{\n" +
                                            "  \"startDate\": \"2025-01-01\",\n" +
                                            "  \"endDate\": \"2026-01-01\",\n" +
                                            "  \"projectName\": \"New Project Title\"\n" +
                                            "}"
                            )
                    )
            )
            @RequestBody Map<String,Object> updates)
    {
        projectExperienceService.patchUpdate(projectid,updates);
        return ResponseEntity.ok("Project updated successfully");
    }


    /**
     * Updates the start and end dates of a project assignment.
     *
     * @author Kayahan Güneri
     * @since 21.04.2025 16:08
     */

    @PutMapping("/assignments/update-dates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateAssignmentDates(@RequestBody ProjectAssignmentDto dto){
        assignmentService.updateAssignmentDates(dto);
        return ResponseEntity.ok("Assignment updated successfully");

    }

    /**
     * Retrieves detailed project information by project ID.
     *
     * @author Kayahan Güneri
     * @since 02.05.2025 13:36
     */
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ProjectExperienceDto> getProjectDetail(@PathVariable Long id){
        Optional<ProjectExperienceDto> dto = projectExperienceService.getProjectById(id);
        return dto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Put employee to project
     */
    @PostMapping("/assignment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectAssignmentDto> assignEmployee(@RequestBody ProjectAssignmentDto dto){
        ProjectAssignmentDto savedDto = assignmentService.assignEmployeeToProject(dto);
        return new ResponseEntity<>(savedDto,HttpStatus.CREATED);
    }

    @GetMapping("/assignments/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ProjectAssignmentDto>> getAssignmentsByEmployee(@PathVariable Long employeeId) {
        List<ProjectAssignmentDto> list = assignmentService.getAssignmentsByEmployeeId(employeeId);
        return ResponseEntity.ok(list);
    }

}