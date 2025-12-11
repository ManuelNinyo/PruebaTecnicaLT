package manuel.pruebatecnica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply CORS to specific paths
                .allowedOrigins("http://localhost:3000", "https://manuelninyo.github.io/PruebaTecnicaLT/") // Allow requests from these origins
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true) // Allow sending cookies and authentication headers
                .maxAge(3600); // Cache preflight requests for 1 hour
    }
}