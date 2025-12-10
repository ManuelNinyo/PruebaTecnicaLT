package manuel.pruebatecnica.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo", nullable = false, length = 50, unique = true)
    private String codigo;
    
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "caracteristicas", columnDefinition = "TEXT")
    private String caracteristicas;
    
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    
    @Column(name = "moneda", nullable = false, length = 3)
    private String moneda = "USD";
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_nit", nullable = false)
    @JsonIgnore
    private Empresa empresa;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "producto_categorias",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias;
    
    @ManyToMany(mappedBy = "productos", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Orden> ordenes;
}
