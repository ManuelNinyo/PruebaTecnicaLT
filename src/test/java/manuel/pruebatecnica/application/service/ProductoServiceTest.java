package manuel.pruebatecnica.application.service;

import manuel.pruebatecnica.domain.model.Producto;
import manuel.pruebatecnica.infrastructure.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto testProducto;
    private Producto updatedProducto;

    @BeforeEach
    void setUp() {
        testProducto = createTestProducto();
        updatedProducto = createUpdatedProducto();
    }

    @Test
    void findAll_ShouldReturnAllProductos() {
        List<Producto> expectedProductos = Arrays.asList(testProducto);
        when(productoRepository.findAll()).thenReturn(expectedProductos);

        List<Producto> result = productoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProducto.getCodigo(), result.get(0).getCodigo());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnProducto_WhenExists() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(testProducto));

        Optional<Producto> result = productoService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(testProducto.getCodigo(), result.get().getCodigo());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Producto> result = productoService.findById(1L);

        assertFalse(result.isPresent());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void findByEmpresaNit_ShouldReturnProductos() {
        List<Producto> expectedProductos = Arrays.asList(testProducto);
        when(productoRepository.findByEmpresaNit("123456789")).thenReturn(expectedProductos);

        List<Producto> result = productoService.findByEmpresaNit("123456789");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProducto.getCodigo(), result.get(0).getCodigo());
        verify(productoRepository, times(1)).findByEmpresaNit("123456789");
    }

    @Test
    void save_ShouldSaveProducto_WhenCodigoDoesNotExist() {
        when(productoRepository.existsByCodigo(testProducto.getCodigo())).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(testProducto);

        Producto result = productoService.save(testProducto);

        assertNotNull(result);
        assertEquals(testProducto.getCodigo(), result.getCodigo());
        verify(productoRepository, times(1)).existsByCodigo(testProducto.getCodigo());
        verify(productoRepository, times(1)).save(testProducto);
    }

    @Test
    void save_ShouldThrowException_WhenCodigoAlreadyExists() {
        when(productoRepository.existsByCodigo(testProducto.getCodigo())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.save(testProducto);
        });

        assertEquals("Ya existe un producto con ese código", exception.getMessage());
        verify(productoRepository, times(1)).existsByCodigo(testProducto.getCodigo());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void update_ShouldUpdateProducto_WhenExistsAndCodigoIsSame() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(testProducto));
        when(productoRepository.existsByCodigo(updatedProducto.getCodigo())).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(updatedProducto);

        Producto result = productoService.update(1L, updatedProducto);

        assertNotNull(result);
        assertEquals(updatedProducto.getCodigo(), result.getCodigo());
        assertEquals(updatedProducto.getNombre(), result.getNombre());
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(testProducto);
    }

    @Test
    void update_ShouldUpdateProducto_WhenExistsAndCodigoIsDifferentAndDoesNotExist() {
        testProducto.setCodigo("OLD001");
        updatedProducto.setCodigo("NEW001");
        
        when(productoRepository.findById(1L)).thenReturn(Optional.of(testProducto));
        when(productoRepository.existsByCodigo("NEW001")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(updatedProducto);

        Producto result = productoService.update(1L, updatedProducto);

        assertNotNull(result);
        assertEquals(updatedProducto.getCodigo(), result.getCodigo());
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).existsByCodigo("NEW001");
        verify(productoRepository, times(1)).save(testProducto);
    }

    @Test
    void update_ShouldThrowException_WhenProductoNotFound() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.update(1L, updatedProducto);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void update_ShouldThrowException_WhenCodigoAlreadyExistsAndIsDifferent() {
        testProducto.setCodigo("OLD001");
        updatedProducto.setCodigo("EXIST001");
        
        when(productoRepository.findById(1L)).thenReturn(Optional.of(testProducto));
        when(productoRepository.existsByCodigo("EXIST001")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.update(1L, updatedProducto);
        });

        assertEquals("Ya existe un producto con ese código", exception.getMessage());
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).existsByCodigo("EXIST001");
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void deleteById_ShouldDeleteProducto_WhenExists() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(1L);

        productoService.deleteById(1L);

        verify(productoRepository, times(1)).existsById(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowException_WhenProductoNotExists() {
        when(productoRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.deleteById(1L);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productoRepository, times(1)).existsById(1L);
        verify(productoRepository, never()).deleteById(anyLong());
    }

    private Producto createTestProducto() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setCodigo("PROD001");
        producto.setNombre("Test Product");
        producto.setPrecio(BigDecimal.valueOf(100.0));
        producto.setMoneda("USD");
        producto.setCaracteristicas("Test characteristics");
        return producto;
    }

    private Producto createUpdatedProducto() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setCodigo("PROD002");
        producto.setNombre("Updated Product");
        producto.setPrecio(BigDecimal.valueOf(200.0));
        producto.setMoneda("EUR");
        producto.setCaracteristicas("Updated characteristics");
        return producto;
    }
}
