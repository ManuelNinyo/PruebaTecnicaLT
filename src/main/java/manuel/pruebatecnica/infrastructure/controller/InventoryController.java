package manuel.pruebatecnica.infrastructure.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import manuel.pruebatecnica.application.service.InventoryService;
import manuel.pruebatecnica.infrastructure.dto.ApiResponse;
import manuel.pruebatecnica.infrastructure.dto.InventoryReportRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Endpoints for inventory management")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/report/send")
    @Operation(summary = "Send inventory report", description = "Generate and send inventory report via email", security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<ApiResponse<String>> sendInventoryReport(@Valid @RequestBody InventoryReportRequest request) {
        try {
            inventoryService.sendInventoryReport(
                request.getToEmail(), 
                request.getSubject(), 
                request.getBody(),
                request.getEmpresaNit()
            );
            return ResponseEntity.ok(ApiResponse.success("Inventory report sent successfully to " + request.getToEmail()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to send inventory report: " + e.getMessage()));
        }
    }
}
