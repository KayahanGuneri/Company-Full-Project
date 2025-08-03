package org.example.company_p.security.auth;

import lombok.RequiredArgsConstructor;
import org.example.company_p.security.user.Role;
import org.example.company_p.security.user.User;
import org.example.company_p.security.user.UserRepository;
import org.example.company_p.security.user.UserStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Tüm metodlara admin kontrolü uygula
public class AdminUserController {

    private final UserRepository userRepository;

    @PatchMapping("/{id}/promote")
    public ResponseEntity<String> promoteToAdmin(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return ResponseEntity.ok("User promoted to ADMIN.");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeUserStatus(@PathVariable Long id, @RequestParam UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        userRepository.save(user);
        return ResponseEntity.ok("User status changed to " + status);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
