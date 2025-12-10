package manuel.pruebatecnica.application.service;

import lombok.RequiredArgsConstructor;
import manuel.pruebatecnica.domain.model.Producto;
import manuel.pruebatecnica.infrastructure.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> findByEmpresaNit(String empresaNit) {
        return productoRepository.findByEmpresaNit(empresaNit);
    }

    public Producto save(Producto producto) {
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new RuntimeException("Ya existe un producto con ese código");
        }
        return productoRepository.save(producto);
    }

    public Producto update(Long id, Producto productoDetails) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (!producto.getCodigo().equals(productoDetails.getCodigo()) && 
            productoRepository.existsByCodigo(productoDetails.getCodigo())) {
            throw new RuntimeException("Ya existe un producto con ese código");
        }
        
        producto.setCodigo(productoDetails.getCodigo());
        producto.setNombre(productoDetails.getNombre());
        producto.setCaracteristicas(productoDetails.getCaracteristicas());
        producto.setPrecio(productoDetails.getPrecio());
        producto.setMoneda(productoDetails.getMoneda());
        producto.setEmpresa(productoDetails.getEmpresa());
        producto.setCategorias(productoDetails.getCategorias());
        
        return productoRepository.save(producto);
    }

    public void deleteById(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }
}
