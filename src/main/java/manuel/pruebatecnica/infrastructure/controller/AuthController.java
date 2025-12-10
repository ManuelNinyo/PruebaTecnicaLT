package manuel.pruebatecnica.infrastructure.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import manuel.pruebatecnica.application.service.AuthService;
import manuel.pruebatecnica.domain.model.Usuario;
import manuel.pruebatecnica.infrastructure.dto.*;
import manuel.pruebatecnica.infrastructure.mapper.EntityMapper;
import manuel.pruebatecnica.security.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for authentication")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;
    private final EntityMapper entityMapper;

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Usuario usuario = authService.authenticate(authRequest.getEmail(), authRequest.getPassword());
            
            String token = jwtTokenUtil.generateToken(usuario.getEmail(), usuario.getRol().name());
            
            AuthResponse response = new AuthResponse(
                token, 
                "Bearer", 
                86400000L, 
                usuario.getRol().name(), 
                usuario.getEmail()
            );
            
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid credentials: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Register a new user")
    public ResponseEntity<ApiResponse<UsuarioDTO>> register(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario.Role role = usuarioDTO.getRole() != null ? 
                Usuario.Role.valueOf(usuarioDTO.getRole().toUpperCase()) : 
                Usuario.Role.EXTERNO;
            
            Usuario usuario = authService.register(usuarioDTO.getEmail(), "defaultPassword", role);
            
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", entityMapper.toUsuarioDTO(usuario)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }
}
