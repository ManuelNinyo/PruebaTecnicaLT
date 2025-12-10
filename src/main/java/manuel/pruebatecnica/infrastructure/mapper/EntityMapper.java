package manuel.pruebatecnica.infrastructure.mapper;

import manuel.pruebatecnica.domain.model.*;
import manuel.pruebatecnica.infrastructure.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    // Empresa mappings
    public EmpresaDTO toEmpresaDTO(Empresa empresa) {
        if (empresa == null) return null;
        
        EmpresaDTO dto = new EmpresaDTO();
        dto.setNit(empresa.getNit());
        dto.setNombre(empresa.getNombre());
        dto.setDireccion(empresa.getDireccion());
        dto.setTelefono(empresa.getTelefono());
        
        if (empresa.getProductos() != null) {
            dto.setProductos(empresa.getProductos().stream()
                    .map(this::toProductoDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    public Empresa toEmpresa(EmpresaDTO dto) {
        if (dto == null) return null;
        
        Empresa empresa = new Empresa();
        empresa.setNit(dto.getNit());
        empresa.setNombre(dto.getNombre());
        empresa.setDireccion(dto.getDireccion());
        empresa.setTelefono(dto.getTelefono());
        
        return empresa;
    }
    
    public List<EmpresaDTO> toEmpresaDTOList(List<Empresa> empresas) {
        return empresas.stream().map(this::toEmpresaDTO).collect(Collectors.toList());
    }

    // Producto mappings
    public ProductoDTO toProductoDTO(Producto producto) {
        if (producto == null) return null;
        
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setCodigo(producto.getCodigo());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setMoneda(producto.getMoneda());
        dto.setCaracteristicas(producto.getCaracteristicas());
        
        if (producto.getEmpresa() != null) {
            dto.setEmpresaNit(producto.getEmpresa().getNit());
        }
        
        if (producto.getCategorias() != null && !producto.getCategorias().isEmpty()) {
            dto.setCategoriaId(producto.getCategorias().get(0).getId());
        }
        
        return dto;
    }
    
    public Producto toProducto(ProductoDTO dto) {
        if (dto == null) return null;
        
        Producto producto = new Producto();
        // Only set ID if it's not null (for updates, not for creates)
        if (dto.getId() != null && dto.getId() > 0) {
            producto.setId(dto.getId());
        }
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setMoneda(dto.getMoneda());
        producto.setCaracteristicas(dto.getCaracteristicas());
        
        // Set Empresa relationship if empresaNit is provided
        if (dto.getEmpresaNit() != null && !dto.getEmpresaNit().trim().isEmpty()) {
            Empresa empresa = new Empresa();
            empresa.setNit(dto.getEmpresaNit());
            producto.setEmpresa(empresa);
        }
        
        return producto;
    }
    
    public List<ProductoDTO> toProductoDTOList(List<Producto> productos) {
        return productos.stream().map(this::toProductoDTO).collect(Collectors.toList());
    }

    // Categoria mappings
    public CategoriaDTO toCategoriaDTO(Categoria categoria) {
        if (categoria == null) return null;
        
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        
        return dto;
    }
    
    public Categoria toCategoria(CategoriaDTO dto) {
        if (dto == null) return null;
        
        Categoria categoria = new Categoria();
        // Only set ID if it's not null (for updates, not for creates)
        if (dto.getId() != null && dto.getId() > 0) {
            categoria.setId(dto.getId());
        }
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        
        return categoria;
    }
    
    public List<CategoriaDTO> toCategoriaDTOList(List<Categoria> categorias) {
        return categorias.stream().map(this::toCategoriaDTO).collect(Collectors.toList());
    }

    // Cliente mappings
    public ClienteDTO toClienteDTO(Cliente cliente) {
        if (cliente == null) return null;
        
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());
        dto.setFechaRegistro(null); // Cliente entity doesn't have fechaRegistro field
        
        return dto;
    }
    
    public Cliente toCliente(ClienteDTO dto) {
        if (dto == null) return null;
        
        Cliente cliente = new Cliente();

        if (dto.getId() != null && dto.getId() > 0) {
            cliente.setId(dto.getId());
        }
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        
        return cliente;
    }
    
    public List<ClienteDTO> toClienteDTOList(List<Cliente> clientes) {
        return clientes.stream().map(this::toClienteDTO).collect(Collectors.toList());
    }

    // Usuario mappings
    public UsuarioDTO toUsuarioDTO(Usuario usuario) {
        if (usuario == null) return null;
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getEmail()); // Use email as username since entity doesn't have username field
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRol().name());
        dto.setActivo(true); // Default to true since entity doesn't have activo field
        
        return dto;
    }
    
    public Usuario toUsuario(UsuarioDTO dto) {
        if (dto == null) return null;
        
        Usuario usuario = new Usuario();

        if (dto.getId() != null && dto.getId() > 0) {
            usuario.setId(dto.getId());
        }
        usuario.setEmail(dto.getEmail());
        
        if (dto.getRole() != null) {
            usuario.setRol(Usuario.Role.valueOf(dto.getRole().toUpperCase()));
        }
        
        return usuario;
    }
    
    public List<UsuarioDTO> toUsuarioDTOList(List<Usuario> usuarios) {
        return usuarios.stream().map(this::toUsuarioDTO).collect(Collectors.toList());
    }
}
