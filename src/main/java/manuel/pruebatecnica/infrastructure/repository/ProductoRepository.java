package manuel.pruebatecnica.infrastructure.repository;

import manuel.pruebatecnica.domain.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEmpresaNit(String empresaNit);
    boolean existsByCodigo(String codigo);
    
    @Query("SELECT p FROM Producto p WHERE p.empresa.nit = :empresaNit ORDER BY p.nombre")
    List<Producto> findByEmpresaNitOrderByNombre(@Param("empresaNit") String empresaNit);
}
