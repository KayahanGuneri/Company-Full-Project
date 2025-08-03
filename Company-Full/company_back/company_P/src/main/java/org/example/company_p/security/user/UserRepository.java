package org.example.company_p.security.user;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Kullanıcını e-mail bilgisine göre bulunmasını sağlar
 * Giriş işlemlerinde bu method kullanılır
 */

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}