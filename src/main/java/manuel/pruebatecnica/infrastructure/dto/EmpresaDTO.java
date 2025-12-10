package manuel.pruebatecnica.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    
    private String nit;
    
    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name must not exceed 200 characters")
    private String nombre;
    
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String direccion;
    
    @Size(max = 50, message = "Phone must not exceed 50 characters")
    private String telefono;
    
    private List<ProductoDTO> productos;
}
