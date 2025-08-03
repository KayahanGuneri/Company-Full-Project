package org.example.company_p.security.auth;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

/**
 * Giriş işlemi başarılı olduğunu kullanıcıya dönecek olan JWT token yapısı
 */
public class AuthenticationResponse {
    private String token;
    private String role;
}