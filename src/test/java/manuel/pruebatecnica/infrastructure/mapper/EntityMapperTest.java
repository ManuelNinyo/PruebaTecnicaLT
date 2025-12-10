package manuel.pruebatecnica.infrastructure.mapper;

import manuel.pruebatecnica.domain.model.*;
import manuel.pruebatecnica.infrastructure.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EntityMapperTest {

    private EntityMapper entityMapper;

    @BeforeEach
    void setUp() {
        entityMapper = new EntityMapper();
    }

    @Test
    void toEmpresaDTO_ShouldMapEmpresaToDTO() {
        Empresa empresa = createTestEmpresa();
        EmpresaDTO result = entityMapper.toEmpresaDTO(empresa);

        assertNotNull(result);
        assertEquals(empresa.getNit(), result.getNit());
        assertEquals(empresa.getNombre(), result.getNombre());
        assertEquals(empresa.getDireccion(), result.getDireccion());
        assertEquals(empresa.getTelefono(), result.getTelefono());
        assertNotNull(result.getProductos());
        assertEquals(1, result.getProductos().size());
    }

    @Test
    void toEmpresaDTO_ShouldReturnNull_WhenEmpresaIsNull() {
        EmpresaDTO result = entityMapper.toEmpresaDTO(null);
        assertNull(result);
    }

    @Test
    void toEmpresa_ShouldMapDTOToEmpresa() {
        EmpresaDTO dto = createTestEmpresaDTO();
        Empresa result = entityMapper.toEmpresa(dto);

        assertNotNull(result);
        assertEquals(dto.getNit(), result.getNit());
        assertEquals(dto.getNombre(), result.getNombre());
        assertEquals(dto.getDireccion(), result.getDireccion());
        assertEquals(dto.getTelefono(), result.getTelefono());
    }

    @Test
    void toEmpresa_ShouldReturnNull_WhenDTOIsNull() {
        Empresa result = entityMapper.toEmpresa(null);
        assertNull(result);
    }

    @Test
    void toEmpresaDTOList_ShouldMapList() {
        List<Empresa> empresas = Arrays.asList(createTestEmpresa());
        List<EmpresaDTO> result = entityMapper.toEmpresaDTOList(empresas);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(empresas.get(0).getNit(), result.get(0).getNit());
    }

    @Test
    void toProductoDTO_ShouldMapProductoToDTO() {
        Producto producto = createTestProducto();
        ProductoDTO result = entityMapper.toProductoDTO(producto);

        assertNotNull(result);
        assertEquals(producto.getId(), result.getId());
        assertEquals(producto.getCodigo(), result.getCodigo());
        assertEquals(producto.getNombre(), result.getNombre());
        assertEquals(producto.getPrecio(), result.getPrecio());
        assertEquals(producto.getMoneda(), result.getMoneda());
        assertEquals(producto.getCaracteristicas(), result.getCaracteristicas());
        assertEquals(producto.getEmpresa().getNit(), result.getEmpresaNit());
        assertEquals(producto.getCategorias().get(0).getId(), result.getCategoriaId());
    }

    @Test
    void toProductoDTO_ShouldReturnNull_WhenProductoIsNull() {
        ProductoDTO result = entityMapper.toProductoDTO(null);
        assertNull(result);
    }

    @Test
    void toProducto_ShouldMapDTOToProducto() {
        ProductoDTO dto = createTestProductoDTO();
        Producto result = entityMapper.toProducto(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getCodigo(), result.getCodigo());
        assertEquals(dto.getNombre(), result.getNombre());
        assertEquals(dto.getPrecio(), result.getPrecio());
        assertEquals(dto.getMoneda(), result.getMoneda());
        assertEquals(dto.getCaracteristicas(), result.getCaracteristicas());
        assertNotNull(result.getEmpresa());
        assertEquals(dto.getEmpresaNit(), result.getEmpresa().getNit());
    }

    @Test
    void toProducto_ShouldNotSetId_WhenIdIsNull() {
        ProductoDTO dto = createTestProductoDTO();
        dto.setId(null);
        Producto result = entityMapper.toProducto(dto);

        assertNull(result.getId());
    }

    @Test
    void toProducto_ShouldReturnNull_WhenDTOIsNull() {
        Producto result = entityMapper.toProducto(null);
        assertNull(result);
    }

    @Test
    void toProductoDTOList_ShouldMapList() {
        List<Producto> productos = Arrays.asList(createTestProducto());
        List<ProductoDTO> result = entityMapper.toProductoDTOList(productos);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productos.get(0).getCodigo(), result.get(0).getCodigo());
    }

    @Test
    void toCategoriaDTO_ShouldMapCategoriaToDTO() {
        Categoria categoria = createTestCategoria();
        CategoriaDTO result = entityMapper.toCategoriaDTO(categoria);

        assertNotNull(result);
        assertEquals(categoria.getId(), result.getId());
        assertEquals(categoria.getNombre(), result.getNombre());
        assertEquals(categoria.getDescripcion(), result.getDescripcion());
    }

    @Test
    void toCategoriaDTO_ShouldReturnNull_WhenCategoriaIsNull() {
        CategoriaDTO result = entityMapper.toCategoriaDTO(null);
        assertNull(result);
    }

    @Test
    void toCategoria_ShouldMapDTOToCategoria() {
        CategoriaDTO dto = createTestCategoriaDTO();
        Categoria result = entityMapper.toCategoria(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getNombre(), result.getNombre());
        assertEquals(dto.getDescripcion(), result.getDescripcion());
    }

    @Test
    void toCategoria_ShouldNotSetId_WhenIdIsNull() {
        CategoriaDTO dto = createTestCategoriaDTO();
        dto.setId(null);
        Categoria result = entityMapper.toCategoria(dto);

        assertNull(result.getId());
    }

    @Test
    void toCategoria_ShouldReturnNull_WhenDTOIsNull() {
        Categoria result = entityMapper.toCategoria(null);
        assertNull(result);
    }

    @Test
    void toCategoriaDTOList_ShouldMapList() {
        List<Categoria> categorias = Arrays.asList(createTestCategoria());
        List<CategoriaDTO> result = entityMapper.toCategoriaDTOList(categorias);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categorias.get(0).getNombre(), result.get(0).getNombre());
    }

    @Test
    void toClienteDTO_ShouldMapClienteToDTO() {
        Cliente cliente = createTestCliente();
        ClienteDTO result = entityMapper.toClienteDTO(cliente);

        assertNotNull(result);
        assertEquals(cliente.getId(), result.getId());
        assertEquals(cliente.getNombre(), result.getNombre());
        assertEquals(cliente.getEmail(), result.getEmail());
        assertEquals(cliente.getTelefono(), result.getTelefono());
        assertEquals(cliente.getDireccion(), result.getDireccion());
        assertNull(result.getFechaRegistro());
    }

    @Test
    void toClienteDTO_ShouldReturnNull_WhenClienteIsNull() {
        ClienteDTO result = entityMapper.toClienteDTO(null);
        assertNull(result);
    }

    @Test
    void toCliente_ShouldMapDTOToCliente() {
        ClienteDTO dto = createTestClienteDTO();
        Cliente result = entityMapper.toCliente(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getNombre(), result.getNombre());
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getTelefono(), result.getTelefono());
        assertEquals(dto.getDireccion(), result.getDireccion());
    }

    @Test
    void toCliente_ShouldNotSetId_WhenIdIsNull() {
        ClienteDTO dto = createTestClienteDTO();
        dto.setId(null);
        Cliente result = entityMapper.toCliente(dto);

        assertNull(result.getId());
    }

    @Test
    void toCliente_ShouldReturnNull_WhenDTOIsNull() {
        Cliente result = entityMapper.toCliente(null);
        assertNull(result);
    }

    @Test
    void toClienteDTOList_ShouldMapList() {
        List<Cliente> clientes = Arrays.asList(createTestCliente());
        List<ClienteDTO> result = entityMapper.toClienteDTOList(clientes);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clientes.get(0).getNombre(), result.get(0).getNombre());
    }

    @Test
    void toUsuarioDTO_ShouldMapUsuarioToDTO() {
        Usuario usuario = createTestUsuario();
        UsuarioDTO result = entityMapper.toUsuarioDTO(usuario);

        assertNotNull(result);
        assertEquals(usuario.getId(), result.getId());
        assertEquals(usuario.getEmail(), result.getUsername());
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(usuario.getRol().name(), result.getRole());

    }

    @Test
    void toUsuarioDTO_ShouldReturnNull_WhenUsuarioIsNull() {
        UsuarioDTO result = entityMapper.toUsuarioDTO(null);
        assertNull(result);
    }

    @Test
    void toUsuario_ShouldMapDTOToUsuario() {
        UsuarioDTO dto = createTestUsuarioDTO();
        Usuario result = entityMapper.toUsuario(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getRole().toUpperCase(), result.getRol().name());
    }

    @Test
    void toUsuario_ShouldNotSetId_WhenIdIsNull() {
        UsuarioDTO dto = createTestUsuarioDTO();
        dto.setId(null);
        Usuario result = entityMapper.toUsuario(dto);

        assertNull(result.getId());
    }

    @Test
    void toUsuario_ShouldReturnNull_WhenDTOIsNull() {
        Usuario result = entityMapper.toUsuario(null);
        assertNull(result);
    }

    @Test
    void toUsuarioDTOList_ShouldMapList() {
        List<Usuario> usuarios = Arrays.asList(createTestUsuario());
        List<UsuarioDTO> result = entityMapper.toUsuarioDTOList(usuarios);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(usuarios.get(0).getEmail(), result.get(0).getEmail());
    }

    private Empresa createTestEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setNit("123456789");
        empresa.setNombre("Test Empresa");
        empresa.setDireccion("Test Address");
        empresa.setTelefono("123456789");
        empresa.setProductos(Arrays.asList(createTestProducto()));
        return empresa;
    }

    private EmpresaDTO createTestEmpresaDTO() {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setNit("123456789");
        dto.setNombre("Test Empresa");
        dto.setDireccion("Test Address");
        dto.setTelefono("123456789");
        return dto;
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
        
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        producto.setCategorias(Arrays.asList(categoria));
        
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
        dto.setCategoriaId(1L);
        return dto;
    }

    private Categoria createTestCategoria() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Test Category");
        categoria.setDescripcion("Test Description");
        return categoria;
    }

    private CategoriaDTO createTestCategoriaDTO() {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(1L);
        dto.setNombre("Test Category");
        dto.setDescripcion("Test Description");
        return dto;
    }

    private Cliente createTestCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Test Client");
        cliente.setEmail("client@test.com");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Client Address");
        return cliente;
    }

    private ClienteDTO createTestClienteDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(1L);
        dto.setNombre("Test Client");
        dto.setEmail("client@test.com");
        dto.setTelefono("123456789");
        dto.setDireccion("Client Address");
        return dto;
    }

    private Usuario createTestUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("user@test.com");
        usuario.setRol(Usuario.Role.ADMIN);
        return usuario;
    }

    private UsuarioDTO createTestUsuarioDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setEmail("user@test.com");
        dto.setRole("ADMIN");
        return dto;
    }
}
