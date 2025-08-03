package org.example.company_p.security.jwt;


/**
 * Bu sınıf JWT token oluşturma,çözme (decode) ve doğrulama işlemlerini yapar
 */

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {
    // Gizli anahtar (secret key), token'ı imzalamak ve çözmek için kullanılır
    private static final String SECRET_KEY = "jwtgibibirkelimeolmayanama64byteolacakgizlianahtarkelimesi!!!";

    //Token'ın geçerlilik süresi
    private static final long EXPIRATION_TIME = 86400000;

    /**
     * Kullanıcıya ait bir JWT token oluşturulur
     */

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Yetkileri ROLE_ önekiyle ekleyelim
        claims.put("authorities", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Token'dan kullanıcı email bilgisini alır
     */

    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    /**
     * Token geçerli mi?HEm doğru kullanıcıya ait mi hem de süresi dolmamış mı
     */

    public boolean isTokenValid(String token,UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername())&& !istokenExpired(token);

    }

    /**
     * Token süresi halen daha geçerli mi
     */
    private boolean istokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Token'daki tüm bilgileri alır
     */
    //token çözümlenir sub,iat,exp
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Token'ı çözmek için gizli anahtar
                .build()  //Parser yapılandırmasını tamamlar
                .parseClaimsJws(token) //Verilen token'ı ayrıştırır (parse eder)
                .getBody(); //İçeriği (payload) al
    }

    /**
     * String türündeki SECRET_KEY'den Key obejesi üret
     */
    private Key getSignInKey() {

        // String olarak tanımlanana SECRET_KEY'i, JWT token'ı imzalamak için kullanılabilecek bir Key nesnesine dönüştürür
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    }

}