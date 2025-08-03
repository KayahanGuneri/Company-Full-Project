package org.example.company_p.entity;

import lombok.*;
import jakarta.persistence.*;



/**
 * Veritabanındaki tabloların sütünları ifade etmek için kullanılır
 */

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    //Burası yerine GUID de kullanılabilir alternatif olarak
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id veritabanı tarafından otomatik artırılır (auto increment)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false, unique = true)
    private String email;

    private boolean isDeleted; //Soft delete için


}