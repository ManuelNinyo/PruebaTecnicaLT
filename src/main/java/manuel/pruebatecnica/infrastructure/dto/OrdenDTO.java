package manuel.pruebatecnica.infrastructure.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDTO {
    
    private Long id;
    
    @NotNull(message = "Client ID is required")
    private Long clienteId;
    
    private String estado = "PENDIENTE";
    
    private LocalDateTime fechaOrden;
    
    private List<OrdenDetalleDTO> detalles;
    
    private Double total;
}
