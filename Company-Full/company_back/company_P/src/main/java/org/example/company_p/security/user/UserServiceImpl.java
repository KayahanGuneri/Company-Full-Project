package org.example.company_p.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Bu metot login işleminde çağrılır.
     * Email ile kullanıcı veritabanında aranır.
     * Kullanıcı pasif ise girişe izin verilmez.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (user.getStatus() == UserStatus.PASSIVE) {
            throw new UsernameNotFoundException("Hesabınız pasif durumda. Lütfen yöneticiniz ile iletişime geçin.");
        }

        return user;
    }
}
