package pe.edu.cibertec.ventas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "direcciones_envio")
public class DireccionEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long idDireccion;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "nombre_destinatario", nullable = false, length = 150)
    private String nombreDestinatario;

    @Column(name = "telefono_destinatario", nullable = false, length = 20)
    private String telefonoDestinatario;

    @Column(nullable = false, length = 100)
    private String departamento;

    @Column(nullable = false, length = 100)
    private String provincia;

    @Column(nullable = false, length = 100)
    private String distrito;

    @Column(name = "direccion_linea1", nullable = false, length = 255)
    private String direccionLinea1;

    @Column(name = "direccion_linea2", length = 255)
    private String direccionLinea2;

    @Column(columnDefinition = "TEXT")
    private String referencia;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    @Column(nullable = false)
    private Boolean estado = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}