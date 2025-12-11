package manuel.pruebatecnica.application.service;

import lombok.RequiredArgsConstructor;
import manuel.pruebatecnica.domain.model.Empresa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {


    public void sendInventoryReport(String toEmail) {

        
        String subject = "Inventory Report - " + java.time.LocalDateTime.now().toLocalDate();
        String body = "Please find attached the inventory report with all products by company.";
        

    }

    public void sendInventoryReport(String toEmail, String subject, String body, String empresaNit) {

    }
}
