package manuel.pruebatecnica.config;

import manuel.pruebatecnica.security.JwtTokenUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    @Primary
    public JwtTokenUtil testJwtTokenUtil() {
        return new JwtTokenUtil() {
            @Override
            public String generateToken(String email, String role) {
                return "test.jwt.token";
            }

            @Override
            public String getEmailFromToken(String token) {
                return "test@example.com";
            }

            @Override
            public String getRoleFromToken(String token) {
                return "ADMIN";
            }

            @Override
            public boolean validateToken(String token) {
                return true;
            }
        };
    }
}
