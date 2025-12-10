package manuel.pruebatecnica.infrastructure.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import manuel.pruebatecnica.application.service.ProductoService;
import manuel.pruebatecnica.domain.model.Producto;
import manuel.pruebatecnica.infrastructure.dto.ApiResponse;
import manuel.pruebatecnica.infrastructure.dto.ProductoDTO;
import manuel.pruebatecnica.infrastructure.mapper.EntityMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductoController {

    private final ProductoService productoService;
    private final EntityMapper entityMapper;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> findAll() {
        List<Producto> productos = productoService.findAll();
        List<ProductoDTO> productoDTOs = entityMapper.toProductoDTOList(productos);
        return ResponseEntity.ok(ApiResponse.success(productoDTOs));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<ProductoDTO>> findById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(producto -> ResponseEntity.ok(ApiResponse.success(entityMapper.toProductoDTO(producto))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/empresa/{empresaNit}")
    @Operation(summary = "Get products by company", description = "Retrieve all products for a specific company", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> findByEmpresaNit(@PathVariable String empresaNit) {
        List<Producto> productos = productoService.findByEmpresaNit(empresaNit);
        List<ProductoDTO> productoDTOs = entityMapper.toProductoDTOList(productos);
        return ResponseEntity.ok(ApiResponse.success(productoDTOs));
    }

    @PostMapping
    @Operation(summary = "Create product", description = "Create a new product", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<ProductoDTO>> create(@Valid @RequestBody ProductoDTO productoDTO) {
        try {
            Producto producto = entityMapper.toProducto(productoDTO);
            Producto createdProducto = productoService.save(producto);
            return ResponseEntity.ok(ApiResponse.success("Product created successfully", entityMapper.toProductoDTO(createdProducto)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to create product: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Update an existing product", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<ProductoDTO>> update(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        try {
            Producto producto = entityMapper.toProducto(productoDTO);
            Producto updatedProducto = productoService.update(id, producto);
            return ResponseEntity.ok(ApiResponse.success("Product updated successfully", entityMapper.toProductoDTO(updatedProducto)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Delete a product by its ID", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            productoService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
