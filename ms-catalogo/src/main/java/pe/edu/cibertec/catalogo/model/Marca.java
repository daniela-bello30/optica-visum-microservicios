package pe.edu.cibertec.catalogo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long idMarca;

    @Column(name = "nombre_marca", nullable = false, unique = true, length = 100)
    private String nombreMarca;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(nullable = false)
    private Boolean estado = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}