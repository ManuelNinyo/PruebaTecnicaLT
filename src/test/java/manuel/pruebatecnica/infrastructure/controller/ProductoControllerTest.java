package manuel.pruebatecnica.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import manuel.pruebatecnica.application.service.ProductoService;
import manuel.pruebatecnica.domain.model.Producto;
import manuel.pruebatecnica.domain.model.Empresa;
import manuel.pruebatecnica.infrastructure.dto.ProductoDTO;
import manuel.pruebatecnica.infrastructure.mapper.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import manuel.pruebatecnica.config.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductoController.class)
@Import(TestSecurityConfig.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private EntityMapper entityMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto testProducto;
    private ProductoDTO testProductoDTO;

    @BeforeEach
    void setUp() {
        testProducto = createTestProducto();
        testProductoDTO = createTestProductoDTO();
    }

    @Test
    void findAll_ShouldReturnAllProductos() throws Exception {
        List<Producto> productos = Arrays.asList(testProducto);
        List<ProductoDTO> productoDTOs = Arrays.asList(testProductoDTO);

        when(productoService.findAll()).thenReturn(productos);
        when(entityMapper.toProductoDTOList(productos)).thenReturn(productoDTOs);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].codigo").value(testProductoDTO.getCodigo()));
    }

    @Test
    void findById_ShouldReturnProducto_WhenExists() throws Exception {
        when(productoService.findById(1L)).thenReturn(Optional.of(testProducto));
        when(entityMapper.toProductoDTO(testProducto)).thenReturn(testProductoDTO);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.codigo").value(testProductoDTO.getCodigo()));
    }

    @Test
    void findById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(productoService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByEmpresaNit_ShouldReturnProductos() throws Exception {
        List<Producto> productos = Arrays.asList(testProducto);
        List<ProductoDTO> productoDTOs = Arrays.asList(testProductoDTO);

        when(productoService.findByEmpresaNit("123456789")).thenReturn(productos);
        when(entityMapper.toProductoDTOList(productos)).thenReturn(productoDTOs);

        mockMvc.perform(get("/api/productos/empresa/123456789"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].codigo").value(testProductoDTO.getCodigo()));
    }

    @Test
    void create_ShouldReturnCreatedProducto() throws Exception {
        Producto createdProducto = createTestProducto();
        createdProducto.setId(1L);

        when(entityMapper.toProducto(any(ProductoDTO.class))).thenReturn(testProducto);
        when(productoService.save(any(Producto.class))).thenReturn(createdProducto);
        when(entityMapper.toProductoDTO(createdProducto)).thenReturn(testProductoDTO);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product created successfully"))
                .andExpect(jsonPath("$.data.codigo").value(testProductoDTO.getCodigo()));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        when(entityMapper.toProducto(any(ProductoDTO.class))).thenReturn(testProducto);
        when(productoService.save(any(Producto.class))).thenThrow(new RuntimeException("Product code already exists"));

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Failed to create product: Product code already exists"));
    }

    @Test
    void update_ShouldReturnUpdatedProducto() throws Exception {
        Producto updatedProducto = createTestProducto();
        updatedProducto.setNombre("Updated Product");

        ProductoDTO updatedDTO = createTestProductoDTO();
        updatedDTO.setNombre("Updated Product");

        when(entityMapper.toProducto(any(ProductoDTO.class))).thenReturn(testProducto);
        when(productoService.update(anyLong(), any(Producto.class))).thenReturn(updatedProducto);
        when(entityMapper.toProductoDTO(updatedProducto)).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product updated successfully"))
                .andExpect(jsonPath("$.data.nombre").value("Updated Product"));
    }

    @Test
    void update_ShouldReturnNotFound_WhenRuntimeExceptionOccurs() throws Exception {
        when(entityMapper.toProducto(any(ProductoDTO.class))).thenReturn(testProducto);
        when(productoService.update(anyLong(), any(Producto.class))).thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductoDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ShouldReturnSuccess_WhenProductoExists() throws Exception {
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    void delete_ShouldReturnNotFound_WhenProductoNotExists() throws Exception {
        doThrow(new RuntimeException("Producto no encontrado")).when(productoService).deleteById(999L);
        
        mockMvc.perform(delete("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    private Producto createTestProducto() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setCodigo("PROD001");
        producto.setNombre("Test Product");
        producto.setPrecio(BigDecimal.valueOf(100.0));
        producto.setMoneda("USD");
        producto.setCaracteristicas("Test characteristics");
        
        Empresa empresa = new Empresa();
        empresa.setNit("123456789");
        producto.setEmpresa(empresa);
        
        return producto;
    }

    private ProductoDTO createTestProductoDTO() {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(1L);
        dto.setCodigo("PROD001");
        dto.setNombre("Test Product");
        dto.setPrecio(BigDecimal.valueOf(100.0));
        dto.setMoneda("USD");
        dto.setCaracteristicas("Test characteristics");
        dto.setEmpresaNit("123456789");
        return dto;
    }
}
