package manuel.pruebatecnica.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import manuel.pruebatecnica.application.service.AuthService;
import manuel.pruebatecnica.domain.model.Usuario;
import manuel.pruebatecnica.infrastructure.dto.AuthRequest;
import manuel.pruebatecnica.infrastructure.dto.UsuarioDTO;
import manuel.pruebatecnica.infrastructure.mapper.EntityMapper;
import manuel.pruebatecnica.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import manuel.pruebatecnica.config.TestSecurityConfig;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private EntityMapper entityMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario testUsuario;
    private AuthRequest authRequest;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        testUsuario = createTestUsuario();
        authRequest = createAuthRequest();
        usuarioDTO = createUsuarioDTO();
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() throws Exception {
        when(authService.authenticate(authRequest.getEmail(), authRequest.getPassword())).thenReturn(testUsuario);
        when(jwtTokenUtil.generateToken(testUsuario.getEmail(), testUsuario.getRol().name())).thenReturn("jwt.token.here");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token").value("jwt.token.here"))
                .andExpect(jsonPath("$.data.type").value("Bearer"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"))
                .andExpect(jsonPath("$.data.email").value(testUsuario.getEmail()));
    }

    @Test
    void login_ShouldReturnBadRequest_WhenCredentialsAreInvalid() throws Exception {
        when(authService.authenticate(authRequest.getEmail(), authRequest.getPassword()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid credentials: Invalid credentials"));
    }

    @Test
    void register_ShouldReturnUsuarioDTO_WhenRegistrationIsSuccessful() throws Exception {
        Usuario registeredUsuario = createTestUsuario();
        UsuarioDTO registeredDTO = createUsuarioDTO();

        when(authService.register(anyString(), anyString(), any(Usuario.Role.class))).thenReturn(registeredUsuario);
        when(entityMapper.toUsuarioDTO(registeredUsuario)).thenReturn(registeredDTO);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.email").value(registeredDTO.getEmail()))
                .andExpect(jsonPath("$.data.role").value(registeredDTO.getRole()));
    }

    @Test
    void register_ShouldUseExternalRole_WhenRoleIsNull() throws Exception {
        UsuarioDTO usuarioDTORoleNull = createUsuarioDTO();
        usuarioDTORoleNull.setRole(null);

        Usuario registeredUsuario = createTestUsuario();
        registeredUsuario.setRol(Usuario.Role.EXTERNO);
        UsuarioDTO registeredDTO = createUsuarioDTO();
        registeredDTO.setRole("EXTERNO");

        when(authService.register(anyString(), anyString(), eq(Usuario.Role.EXTERNO))).thenReturn(registeredUsuario);
        when(entityMapper.toUsuarioDTO(registeredUsuario)).thenReturn(registeredDTO);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTORoleNull)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.role").value("EXTERNO"));
    }

    @Test
    void register_ShouldReturnBadRequest_WhenRegistrationFails() throws Exception {
        when(authService.register(anyString(), anyString(), any(Usuario.Role.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Registration failed: Email already exists"));
    }

    private Usuario createTestUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@example.com");
        usuario.setPassword("encodedPassword");
        usuario.setRol(Usuario.Role.ADMIN);
        return usuario;
    }

    private AuthRequest createAuthRequest() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        return request;
    }

    private UsuarioDTO createUsuarioDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setRole("ADMIN");
        return dto;
    }
}
