package manuel.pruebatecnica.application.service;

import lombok.RequiredArgsConstructor;
import manuel.pruebatecnica.domain.model.Empresa;
import manuel.pruebatecnica.infrastructure.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    public Optional<Empresa> findByNit(String nit) {
        return empresaRepository.findById(nit);
    }

    public Empresa save(Empresa empresa) {
        if (empresaRepository.existsByNombre(empresa.getNombre())) {
            throw new RuntimeException("Ya existe una empresa con ese nombre");
        }
        return empresaRepository.save(empresa);
    }

    public Empresa update(String nit, Empresa empresaDetails) {
        Empresa empresa = empresaRepository.findById(nit)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        
        if (!empresa.getNombre().equals(empresaDetails.getNombre()) && 
            empresaRepository.existsByNombre(empresaDetails.getNombre())) {
            throw new RuntimeException("Ya existe una empresa con ese nombre");
        }
        
        empresa.setNombre(empresaDetails.getNombre());
        empresa.setDireccion(empresaDetails.getDireccion());
        empresa.setTelefono(empresaDetails.getTelefono());
        
        return empresaRepository.save(empresa);
    }

    public void deleteByNit(String nit) {
        if (!empresaRepository.existsById(nit)) {
            throw new RuntimeException("Empresa no encontrada");
        }
        empresaRepository.deleteById(nit);
    }
}
