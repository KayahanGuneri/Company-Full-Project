package org.example.company_p.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

/**
 * Entity for "project_experiences" table.
 *
 * @author Kayahan Güneri
 * @since 09.04.2025 15:07
 */
@Entity
@Table(name = "project_experiences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //otomatik olarak artan ıd
    private Long id;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "description")
    private String description;


    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_surname")
    private String employeeSurname;

    private Long employeeId;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
}