package org.example.company_p.security.auth;


/**
 * Giriş (login) işleminde kullanıcıdan gelen verileri taşıyan sınıftır
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}