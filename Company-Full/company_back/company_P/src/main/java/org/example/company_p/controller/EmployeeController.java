package org.example.company_p.controller;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;

import org.example.company_p.dto.EmployeeBasicDto;
import org.example.company_p.dto.EmployeeDto;


import org.example.company_p.entity.Employee;
import org.example.company_p.repository.EmployeeRepository;
import org.example.company_p.service.EmployeeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController // Bu sınıfın bir REST controller olduğunu Spring'e bildirir
@RequestMapping("/api/employees") // Bu sınıfın tüm endpoint'leri bu path ile başlar

public class EmployeeController {
    //EmployeeService,iş mantığını yöneten servis katmanıdır
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    //EmployeeService bağımlılığını bu constructor ile enjekte eder
    //Böylece controller içinde employeeService kullanılabilir
    public EmployeeController(EmployeeService employeeService, EmployeeRepository employeeRepository) {

        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }


    //Yeni bir çalışan kaydetmen için kullanılır
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<EmployeeDto> saveEmployee(
            @Valid @RequestBody EmployeeDto employeeDto) { // JSON verisini alır ve geçerli olup olmadığını kontrol eder
        EmployeeDto saved = employeeService.saveEmployee(employeeDto); // Kaydeder
        return new ResponseEntity<>(saved, HttpStatus.CREATED); // 201 Created döner

    }
    //Tüm çalışanları listelemek için kullanılır
    @CrossOrigin
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK); //Başarılı olma durumunda 200 OK durumunu döndürür ve listeler,boşsa "[]" döndürür
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        Optional<EmployeeDto> employeeOpt = employeeService.getEmployeeWithoutProjects(id);
        return employeeOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Çalışanı güncellemek için HTTP PUT kullanılır
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable Long id,  // URL'deki ID değeri
            @Valid @RequestBody EmployeeDto employeeDto) { // Gövdedeki veriyi alır ve doğrular

        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto); // Günceller
        return ResponseEntity.ok(updatedEmployee); // 200 OK döner
    }

    //Çalışanı silmek (soft delete) için kullanılır
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployeeBId(@PathVariable(name = "id") Long employeeId) {
        employeeService.deleteEmployee(employeeId);  //Servis katmanı silmeyi gerçekleştirir
        return new ResponseEntity<>(HttpStatus.ACCEPTED);  //202 accepted döner
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<String> patchEmployee(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"name\": \"Kayahan\",\n" +
                                            "  \"surname\": \"Guneri\",\n" +
                                            "  \"email\": \"kayahan.guneri@example.com\"\n" +
                                            "}"
                            )
                    )
            )
            @RequestBody Map<String,Object> updates
            ){
        employeeService.patchUpdate(id,updates);
        return ResponseEntity.ok("Employee successfully patched");
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}/projects")
    public ResponseEntity<EmployeeDto> getEmployeeWithProjects(@PathVariable Long id){
        return employeeService.getEmployeeWithProjects(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/active")
    public List<EmployeeDto> getActiveEmployees(){
        return employeeService.getActiveEmployee();
    }





}