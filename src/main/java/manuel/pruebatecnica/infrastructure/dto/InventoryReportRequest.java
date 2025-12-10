package manuel.pruebatecnica.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReportRequest {
    
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String toEmail;
    
    @NotBlank(message = "Subject is required")
    private String subject = "Inventory Report";
    
    private String body = "Please find attached the inventory report.";
    
    private String empresaNit;
}
