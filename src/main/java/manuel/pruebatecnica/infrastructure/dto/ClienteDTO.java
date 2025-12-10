package manuel.pruebatecnica.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    
    private Long id;
    
    @NotBlank(message = "Client name is required")
    @Size(max = 200, message = "Client name must not exceed 200 characters")
    private String nombre;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String telefono;
    
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String direccion;
    
    private LocalDate fechaRegistro;
}
