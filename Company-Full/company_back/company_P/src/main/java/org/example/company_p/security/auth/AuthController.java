package org.example.company_p.security.auth;

import lombok.RequiredArgsConstructor;
import org.example.company_p.security.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Bu sınıf,kullanıcıdan gelen istekleri karşılar (login & register)
 * AuthService sınıfını kullanarak işlem yapar
 */

@RestController //Bu sınıf bir REST controller'dır
@RequestMapping ("/api/auth") //URL'mim naşı /api/auth olacak
@RequiredArgsConstructor //final değişkenler için construcotr otomatik tanımlanır

public class AuthController {


    //İş mantığını yöneten servis sınıfı
    private final AuthService authService;
    private final UserRepository userRepository;

    /**
     * Kullanıcı kayıt olur
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
    }

    /**
     * Kullanıcı giriş yapar
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        AuthenticationResponse response = authService.login(request);
        return ResponseEntity.ok(response); // Bu token'ı döner
    }


}

