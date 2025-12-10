package manuel.pruebatecnica.application.service;

import manuel.pruebatecnica.domain.model.Empresa;

import java.util.List;

public interface PdfService {
    byte[] generateInventoryReport(List<Empresa> empresas);
}
