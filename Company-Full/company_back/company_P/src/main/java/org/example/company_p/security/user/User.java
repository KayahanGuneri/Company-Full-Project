package org.example.company_p.security.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


/**
 * User:Sistemde giriş yapana kullanıcıyı temsil eder
 * Spring Security ile çalışabilmesi için UserDetails arayüzünü implement eder
 */

@Entity
@Table(name = "users")  //Veritabanında "users" adında bir tabloya karşılık gelir
@Data //Lombok:getter/setter,toString,equals/hashCode otomatik oluşturur
@Builder //Lombok:nesne oluştururken .builder() şeklinde çağırabilmemizi sağlar
@NoArgsConstructor //Lombok:boş construcot(User user=new User();)
@AllArgsConstructor //Lombok:tüm alanları içeren construcotr

public class User implements UserDetails {  //Spring Securty'nin tandığı kullanıcı tipi
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Otomatik artan ID
    private Long id;

    private String name;
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 11,unique = true)
    private String tc;

    private LocalDateTime birthday;
    private String gender;
    private String university;
    private String department;
    private Integer gradutaionYear;
    private String city;
    private LocalDateTime jobStartDate;
    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> skills;

    @Enumerated(EnumType.STRING) //Enum verisi veritabanına string olarak yazılır(USER,ADMIN)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    private LocalDateTime activatedAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name());
    }

    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
