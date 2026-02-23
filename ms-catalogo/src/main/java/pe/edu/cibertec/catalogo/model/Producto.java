package pe.edu.cibertec.catalogo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    // AQUI ARREGLAMOS EL NOMBRE
    @Column(name = "nombre_producto", nullable = false, length = 200)
    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    // AQUI ARREGLAMOS EL PRECIO UNITARIO
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    // AQUI ARREGLAMOS EL PRECIO CON DESCUENTO
    @Column(name = "precio_descuento", precision = 5, scale = 2)
    private BigDecimal descuento;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    @Column(length = 50)
    private String color;

    @Column(length = 50)
    private String forma;

    @Column(length = 100)
    private String material;

    @Column(length = 20)
    private String genero;

    // AQUI ARREGLAMOS EL CAMPO DESTACADO
    @Column(name = "es_destacado")
    private Boolean destacado = false;

    @Column(name = "es_nuevo")
    private Boolean esNuevo = false;

    @Column(length = 50, unique = true)
    private String sku;

    @Column(name = "codigo_barras", length = 50, unique = true)
    private String codigoBarras;

    // AQUI ARREGLAMOS EL ESTADO (ACTIVO)
    @Column(name = "estado", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // AQUI ARREGLAMOS LA FECHA DE ACTUALIZACION
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaModificacion;
}