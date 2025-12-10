package manuel.pruebatecnica.application.service;

import lombok.RequiredArgsConstructor;
import manuel.pruebatecnica.domain.model.Empresa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final EmpresaService empresaService;
    private final PdfService pdfService;
    private final EmailService emailService;

    public void sendInventoryReport(String toEmail) {
        List<Empresa> empresas = empresaService.findAll();
        
        byte[] pdfContent = pdfService.generateInventoryReport(empresas);
        
        String subject = "Inventory Report - " + java.time.LocalDateTime.now().toLocalDate();
        String body = "Please find attached the inventory report with all products by company.";
        
        emailService.sendEmailWithAttachment(toEmail, subject, body, pdfContent, "inventory_report.pdf");
    }

    public void sendInventoryReport(String toEmail, String subject, String body, String empresaNit) {
        List<Empresa> empresas;
        
        if (empresaNit != null && !empresaNit.trim().isEmpty()) {
            Empresa empresa = empresaService.findByNit(empresaNit)
                    .orElseThrow(() -> new RuntimeException("Company not found with NIT: " + empresaNit));
            empresas = List.of(empresa);
        } else {
            empresas = empresaService.findAll();
        }
        
        byte[] pdfContent = pdfService.generateInventoryReport(empresas);
        
        emailService.sendEmailWithAttachment(toEmail, subject, body, pdfContent, "inventory_report.pdf");
    }
}
