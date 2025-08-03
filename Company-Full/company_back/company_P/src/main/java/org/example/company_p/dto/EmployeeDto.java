package org.example.company_p.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import lombok.*;

import java.util.List;


/**
 * EmployeeDto,istemci (frontend) ile sunucu(backend)  arasındaki veri aktarımını sağlayan bir DTO sınıfıdır
 * Veritabanı modeli yerine daha güvenli ve sade bir yapı sunar
 * API isteklerinde (POST/PUT) ve yanıtlarda (GET) kullanılır
 * Swagger entegresi için işe yarıyor
 */

//DTO:Data Transfer Object
@Getter
@Setter
@Schema(name = "Employee Data Transfer Object")
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EmployeeDto {                                    //bu alan zorunlu olduğunu belirtir
    @Schema(description = "Unique identifier of the employee",required = true)
    private Long id;


    @Schema(description = "Name of the employee",required = true)
    private String name;


    @Schema(description = "Surname of the employee",required = true)
    private String surname;

    @Schema(description = "Email of the employee",required = true)
    private String email;


    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    @Schema(description = "List of project experiences of employee")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ProjectExperienceDto> projects;



}