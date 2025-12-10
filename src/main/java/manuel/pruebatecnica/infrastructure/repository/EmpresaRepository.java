package manuel.pruebatecnica.infrastructure.repository;

import manuel.pruebatecnica.domain.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, String> {
    Optional<Empresa> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
