package manuel.pruebatecnica.infrastructure.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import manuel.pruebatecnica.application.service.PdfService;
import manuel.pruebatecnica.domain.model.Empresa;
import manuel.pruebatecnica.domain.model.Producto;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OpenPdfServiceImpl implements PdfService {

    @Override
    public byte[] generateInventoryReport(List<Empresa> empresas) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        
        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        
        Paragraph title = new Paragraph("Inventory Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        Paragraph date = new Paragraph("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), normalFont);
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(20);
        document.add(date);
        
        for (Empresa empresa : empresas) {
            Paragraph companyTitle = new Paragraph("Company: " + empresa.getNombre() + " (NIT: " + empresa.getNit() + ")", headerFont);
            companyTitle.setSpacingBefore(20);
            companyTitle.setSpacingAfter(10);
            document.add(companyTitle);
            
            if (empresa.getProductos() == null || empresa.getProductos().isEmpty()) {
                Paragraph noProducts = new Paragraph("No products found for this company.", normalFont);
                document.add(noProducts);
            } else {
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.setSpacingAfter(20);
                
                addTableHeader(table, headerFont);
                addProductRows(table, empresa.getProductos(), normalFont);
                
                document.add(table);
            }
            
            document.add(new Paragraph(" "));
        }
        
        document.close();
        
        return outputStream.toByteArray();
    }
    
    private void addTableHeader(PdfPTable table, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase("Code", font));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Name", font));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Price", font));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Characteristics", font));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(cell);
    }
    
    private void addProductRows(PdfPTable table, List<Producto> productos, Font font) {
        for (Producto producto : productos) {
            table.addCell(new Phrase(producto.getCodigo(), font));
            table.addCell(new Phrase(producto.getNombre(), font));
            table.addCell(new Phrase(producto.getPrecio() + " " + producto.getMoneda(), font));
            table.addCell(new Phrase(producto.getCaracteristicas() != null ? producto.getCaracteristicas() : "", font));
        }
    }
}
