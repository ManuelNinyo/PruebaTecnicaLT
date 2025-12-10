package manuel.pruebatecnica.application.service;

import manuel.pruebatecnica.domain.model.Empresa;
import manuel.pruebatecnica.infrastructure.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private EmpresaService empresaService;

    private Empresa testEmpresa;
    private Empresa updatedEmpresa;

    @BeforeEach
    void setUp() {
        testEmpresa = createTestEmpresa();
        updatedEmpresa = createUpdatedEmpresa();
    }

    @Test
    void findAll_ShouldReturnAllEmpresas() {
        List<Empresa> expectedEmpresas = Arrays.asList(testEmpresa);
        when(empresaRepository.findAll()).thenReturn(expectedEmpresas);

        List<Empresa> result = empresaService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEmpresa.getNit(), result.get(0).getNit());
        verify(empresaRepository, times(1)).findAll();
    }

    @Test
    void findByNit_ShouldReturnEmpresa_WhenExists() {
        when(empresaRepository.findById("123456789")).thenReturn(Optional.of(testEmpresa));

        Optional<Empresa> result = empresaService.findByNit("123456789");

        assertTrue(result.isPresent());
        assertEquals(testEmpresa.getNit(), result.get().getNit());
        verify(empresaRepository, times(1)).findById("123456789");
    }

    @Test
    void findByNit_ShouldReturnEmpty_WhenNotExists() {
        when(empresaRepository.findById("123456789")).thenReturn(Optional.empty());

        Optional<Empresa> result = empresaService.findByNit("123456789");

        assertFalse(result.isPresent());
        verify(empresaRepository, times(1)).findById("123456789");
    }

    @Test
    void save_ShouldSaveEmpresa_WhenNombreDoesNotExist() {
        when(empresaRepository.existsByNombre(testEmpresa.getNombre())).thenReturn(false);
        when(empresaRepository.save(any(Empresa.class))).thenReturn(testEmpresa);

        Empresa result = empresaService.save(testEmpresa);

        assertNotNull(result);
        assertEquals(testEmpresa.getNit(), result.getNit());
        assertEquals(testEmpresa.getNombre(), result.getNombre());
        verify(empresaRepository, times(1)).existsByNombre(testEmpresa.getNombre());
        verify(empresaRepository, times(1)).save(testEmpresa);
    }

    @Test
    void save_ShouldThrowException_WhenNombreAlreadyExists() {
        when(empresaRepository.existsByNombre(testEmpresa.getNombre())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaService.save(testEmpresa);
        });

        assertEquals("Ya existe una empresa con ese nombre", exception.getMessage());
        verify(empresaRepository, times(1)).existsByNombre(testEmpresa.getNombre());
        verify(empresaRepository, never()).save(any(Empresa.class));
    }

    @Test
    void update_ShouldUpdateEmpresa_WhenExistsAndNombreIsSame() {
        when(empresaRepository.findById("123456789")).thenReturn(Optional.of(testEmpresa));
        when(empresaRepository.existsByNombre(updatedEmpresa.getNombre())).thenReturn(false);
        when(empresaRepository.save(any(Empresa.class))).thenReturn(updatedEmpresa);

        Empresa result = empresaService.update("123456789", updatedEmpresa);

        assertNotNull(result);
        assertEquals(updatedEmpresa.getNit(), result.getNit());
        assertEquals(updatedEmpresa.getNombre(), result.getNombre());
        verify(empresaRepository, times(1)).findById("123456789");
        verify(empresaRepository, times(1)).save(testEmpresa);
    }

    @Test
    void update_ShouldUpdateEmpresa_WhenExistsAndNombreIsDifferentAndDoesNotExist() {
        testEmpresa.setNombre("Old Company");
        updatedEmpresa.setNombre("New Company");
        
        when(empresaRepository.findById("123456789")).thenReturn(Optional.of(testEmpresa));
        when(empresaRepository.existsByNombre("New Company")).thenReturn(false);
        when(empresaRepository.save(any(Empresa.class))).thenReturn(updatedEmpresa);

        Empresa result = empresaService.update("123456789", updatedEmpresa);

        assertNotNull(result);
        assertEquals(updatedEmpresa.getNombre(), result.getNombre());
        verify(empresaRepository, times(1)).findById("123456789");
        verify(empresaRepository, times(1)).existsByNombre("New Company");
        verify(empresaRepository, times(1)).save(testEmpresa);
    }

    @Test
    void update_ShouldThrowException_WhenEmpresaNotFound() {
        when(empresaRepository.findById("123456789")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaService.update("123456789", updatedEmpresa);
        });

        assertEquals("Empresa no encontrada", exception.getMessage());
        verify(empresaRepository, times(1)).findById("123456789");
        verify(empresaRepository, never()).save(any(Empresa.class));
    }

    @Test
    void update_ShouldThrowException_WhenNombreAlreadyExistsAndIsDifferent() {
        testEmpresa.setNombre("Old Company");
        updatedEmpresa.setNombre("Existing Company");
        
        when(empresaRepository.findById("123456789")).thenReturn(Optional.of(testEmpresa));
        when(empresaRepository.existsByNombre("Existing Company")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaService.update("123456789", updatedEmpresa);
        });

        assertEquals("Ya existe una empresa con ese nombre", exception.getMessage());
        verify(empresaRepository, times(1)).findById("123456789");
        verify(empresaRepository, times(1)).existsByNombre("Existing Company");
        verify(empresaRepository, never()).save(any(Empresa.class));
    }

    @Test
    void deleteByNit_ShouldDeleteEmpresa_WhenExists() {
        when(empresaRepository.existsById("123456789")).thenReturn(true);
        doNothing().when(empresaRepository).deleteById("123456789");

        empresaService.deleteByNit("123456789");

        verify(empresaRepository, times(1)).existsById("123456789");
        verify(empresaRepository, times(1)).deleteById("123456789");
    }

    @Test
    void deleteByNit_ShouldThrowException_WhenEmpresaNotExists() {
        when(empresaRepository.existsById("123456789")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaService.deleteByNit("123456789");
        });

        assertEquals("Empresa no encontrada", exception.getMessage());
        verify(empresaRepository, times(1)).existsById("123456789");
        verify(empresaRepository, never()).deleteById(anyString());
    }

    private Empresa createTestEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setNit("123456789");
        empresa.setNombre("Test Company");
        empresa.setDireccion("Test Address");
        empresa.setTelefono("123456789");
        return empresa;
    }

    private Empresa createUpdatedEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setNit("123456789");
        empresa.setNombre("Updated Company");
        empresa.setDireccion("Updated Address");
        empresa.setTelefono("987654321");
        return empresa;
    }
}
