package manuel.pruebatecnica.infrastructure.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import manuel.pruebatecnica.application.service.EmpresaService;
import manuel.pruebatecnica.domain.model.Empresa;
import manuel.pruebatecnica.infrastructure.dto.ApiResponse;
import manuel.pruebatecnica.infrastructure.dto.EmpresaDTO;
import manuel.pruebatecnica.infrastructure.mapper.EntityMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Endpoints for managing companies")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final EntityMapper entityMapper;

    @GetMapping
    @Operation(summary = "Get all companies", description = "Retrieve all companies", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<List<EmpresaDTO>>> findAll() {
        List<Empresa> empresas = empresaService.findAll();
        List<EmpresaDTO> empresaDTOs = entityMapper.toEmpresaDTOList(empresas);
        return ResponseEntity.ok(ApiResponse.success(empresaDTOs));
    }

    @GetMapping("/{nit}")
    @Operation(summary = "Get company by NIT", description = "Retrieve a specific company by its NIT", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<EmpresaDTO>> findByNit(@PathVariable String nit) {
        return empresaService.findByNit(nit)
                .map(empresa -> ResponseEntity.ok(ApiResponse.success(entityMapper.toEmpresaDTO(empresa))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create company", description = "Create a new company", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<EmpresaDTO>> create(@Valid @RequestBody EmpresaDTO empresaDTO) {
        try {
            Empresa empresa = entityMapper.toEmpresa(empresaDTO);
            Empresa createdEmpresa = empresaService.save(empresa);
            return ResponseEntity.ok(ApiResponse.success("Company created successfully", entityMapper.toEmpresaDTO(createdEmpresa)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to create company: " + e.getMessage()));
        }
    }

    @PutMapping("/{nit}")
    @Operation(summary = "Update company", description = "Update an existing company", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<EmpresaDTO>> update(@PathVariable String nit, @Valid @RequestBody EmpresaDTO empresaDTO) {
        try {
            Empresa empresa = entityMapper.toEmpresa(empresaDTO);
            Empresa updatedEmpresa = empresaService.update(nit, empresa);
            return ResponseEntity.ok(ApiResponse.success("Company updated successfully", entityMapper.toEmpresaDTO(updatedEmpresa)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{nit}")
    @Operation(summary = "Delete company", description = "Delete a company by its NIT", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String nit) {
        try {
            empresaService.deleteByNit(nit);
            return ResponseEntity.ok(ApiResponse.success("Company deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
