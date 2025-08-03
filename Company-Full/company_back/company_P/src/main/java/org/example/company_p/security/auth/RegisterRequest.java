package org.example.company_p.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Kayıt işlemlerinde kullanıcıdan gelen verileri taşıyan sınıf
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String tc;
    private LocalDateTime birthday;
    private String gender;
    private String university;
    private String department;
    private Integer gradutaionYear;
    private String city;
    private LocalDateTime jobStartDate;
    private String title;
    private List<String> skills;
}
