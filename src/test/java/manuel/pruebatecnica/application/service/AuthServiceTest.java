package manuel.pruebatecnica.application.service;

import manuel.pruebatecnica.domain.model.Usuario;
import manuel.pruebatecnica.infrastructure.repository.UsuarioRepository;
import manuel.pruebatecnica.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthService authService;

    private Usuario testUsuario;
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final String ENCODED_PASSWORD = "encodedPassword123";
    private final String JWT_TOKEN = "jwt.token.here";

    @BeforeEach
    void setUp() {
        testUsuario = createTestUsuario();
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUsuario));
        when(passwordEncoder.matches(TEST_PASSWORD, testUsuario.getPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(TEST_EMAIL, "ADMIN")).thenReturn(JWT_TOKEN);

        String result = authService.login(TEST_EMAIL, TEST_PASSWORD);

        assertEquals(JWT_TOKEN, result);
        verify(usuarioRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).matches(TEST_PASSWORD, testUsuario.getPassword());
        verify(jwtTokenUtil, times(1)).generateToken(TEST_EMAIL, "ADMIN");
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(TEST_EMAIL, TEST_PASSWORD);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsIncorrect() {
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUsuario));
        when(passwordEncoder.matches(TEST_PASSWORD, testUsuario.getPassword())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(TEST_EMAIL, TEST_PASSWORD);
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).matches(TEST_PASSWORD, testUsuario.getPassword());
        verify(jwtTokenUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    void authenticate_ShouldReturnUser_WhenCredentialsAreValid() {
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUsuario));
        when(passwordEncoder.matches(TEST_PASSWORD, testUsuario.getPassword())).thenReturn(true);

        Usuario result = authService.authenticate(TEST_EMAIL, TEST_PASSWORD);

        assertNotNull(result);
        assertEquals(testUsuario.getEmail(), result.getEmail());
        assertEquals(testUsuario.getRol(), result.getRol());
        verify(usuarioRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).matches(TEST_PASSWORD, testUsuario.getPassword());
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticate(TEST_EMAIL, TEST_PASSWORD);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void authenticate_ShouldThrowException_WhenPasswordIsIncorrect() {
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUsuario));
        when(passwordEncoder.matches(TEST_PASSWORD, testUsuario.getPassword())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticate(TEST_EMAIL, TEST_PASSWORD);
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).matches(TEST_PASSWORD, testUsuario.getPassword());
    }

    @Test
    void register_ShouldCreateUser_WhenEmailIsNotRegistered() {
        when(usuarioRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(testUsuario);

        Usuario result = authService.register(TEST_EMAIL, TEST_PASSWORD, Usuario.Role.valueOf("ADMIN"));

        assertNotNull(result);
        assertEquals(TEST_EMAIL, result.getEmail());

        verify(usuarioRepository, times(1)).existsByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).encode(TEST_PASSWORD);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailIsAlreadyRegistered() {
        when(usuarioRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(TEST_EMAIL, TEST_PASSWORD, Usuario.Role.ADMIN);
        });

        assertEquals("El email ya está registrado", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(TEST_EMAIL);
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void register_ShouldCreateAdminUser_WhenRoleIsAdmin() {
        when(usuarioRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(testUsuario);

        Usuario result = authService.register(TEST_EMAIL, TEST_PASSWORD, Usuario.Role.ADMIN);

        assertNotNull(result);
        assertEquals(TEST_EMAIL, result.getEmail());
        assertEquals(Usuario.Role.ADMIN, result.getRol());
        verify(usuarioRepository, times(1)).existsByEmail(TEST_EMAIL);
        verify(passwordEncoder, times(1)).encode(TEST_PASSWORD);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    private Usuario createTestUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(TEST_EMAIL);
        usuario.setPassword(ENCODED_PASSWORD);
        usuario.setRol(Usuario.Role.ADMIN);
        return usuario;
    }
}
