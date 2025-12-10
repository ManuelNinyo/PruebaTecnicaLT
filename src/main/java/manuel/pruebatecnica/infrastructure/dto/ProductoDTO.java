package manuel.pruebatecnica.infrastructure.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    
    private Long id;
    
    @NotBlank(message = "Product code is required")
    private String codigo;
    
    @NotBlank(message = "Product name is required")
    private String nombre;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal precio;
    
    @Size(max = 3, message = "Currency code must not exceed 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be 3 uppercase letters (e.g., USD, EUR)")
    private String moneda = "USD";
    
    @Size(max = 1000, message = "Characteristics must not exceed 1000 characters")
    private String caracteristicas;
    
    @NotBlank(message = "Company NIT is required")
    private String empresaNit;
    
    private Long categoriaId;
}
