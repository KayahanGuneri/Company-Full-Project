package org.example.company_p.security.auth;

import lombok.RequiredArgsConstructor;
import org.example.company_p.security.jwt.JwtService;
import org.example.company_p.security.user.Role;
import org.example.company_p.security.user.User;
import org.example.company_p.security.user.UserRepository;
import org.example.company_p.security.user.UserStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * AuthService:
 * - Kullanıcıyı REGISTER eder (kayıt olur)
 * - Kullanıcıyı LOGIN eder (şifreyi doğrular, token üretir)
 */
@Service
@RequiredArgsConstructor // Lombok: final alanlar için constructor oluşturur
public class AuthService {

    private final UserRepository userRepository;         // Veritabanı işlemleri için
    private final PasswordEncoder passwordEncoder;       // Şifreleri güvenli biçimde saklamak için
    private final JwtService jwtService;                 // Token üretmek için
    private final AuthenticationManager authenticationManager; // Login sırasında kimlik doğrulamak için

    /**
     * Kullanıcı kayıt olur.
     * Bu metod sadece kullanıcıyı veritabanına ekler, token üretmez!
     */
    public void register(RegisterRequest request) {
        // E-mail veritabanında kayıtlı mı diye kontrol
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("This email already exists");
        }

        // Yeni bir kullanıcı nesnesi oluştur
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Şifreyi encode et
                .role(Role.USER) // Varsayılan olarak USER rolü veriyoruz
                .name(request.getName())
                .surname(request.getSurname())
                .tc(request.getTc())
                .birthday(request.getBirthday())
                .gender(request.getGender())
                .university(request.getUniversity())
                .department(request.getDepartment())
                .gradutaionYear(request.getGradutaionYear())
                .city(request.getCity())
                .jobStartDate(request.getJobStartDate())
                .title(request.getTitle())
                .skills(request.getSkills())
                .status(UserStatus.ACTIVE)
                .activatedAt(LocalDateTime.now())
                .build();

        // Kullanıcıyı veritabanına kaydet
        userRepository.save(user);
    }



    /**
     * Kullanıcı giriş yapar (login)
     * Bilgiler doğruysa JWT token üretir ve geri döner
     */
    public AuthenticationResponse login(LoginRequest request) {
        // Email + şifre doğru mu diye kontrol eder (Spring Security)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Doğrulanan kullanıcıyı veritabanından getir
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        // Token üret
        String jwtToken = jwtService.generateToken(user);

        // Token'ı response objesi ile geri döndür
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole().name())
                .build();
    }
}
