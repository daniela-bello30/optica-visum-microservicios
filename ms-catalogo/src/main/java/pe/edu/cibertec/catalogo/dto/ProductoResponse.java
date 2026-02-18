package pe.edu.cibertec.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para productos
 * Incluye información completa del producto con datos de categoria y marca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con información completa de un producto")
public class ProductoResponse {

    @Schema(description = "ID único del producto", example = "1")
    private Long idProducto;

    @Schema(description = "Nombre del producto", example = "Ray-Ban Aviator Clásico")
    private String nombre;

    @Schema(description = "Descripción del producto")
    private String descripcion;

    @Schema(description = "Precio actual", example = "299.90")
    private BigDecimal precio;

    @Schema(description = "Porcentaje de descuento aplicado", example = "15.00")
    private BigDecimal descuento;

    @Schema(description = "Precio final con descuento", example = "254.92")
    private BigDecimal precioFinal;

    @Schema(description = "Cantidad disponible en stock", example = "50")
    private Integer stock;

    @Schema(description = "Información de la categoría")
    private CategoriaDto categoria;

    @Schema(description = "Información de la marca")
    private MarcaDto marca;

    @Schema(description = "Color del producto", example = "Negro")
    private String color;

    @Schema(description = "Forma del producto", example = "Rectangular")
    private String forma;

    @Schema(description = "Material del producto", example = "Acetato")
    private String material;

    @Schema(description = "Género", example = "Unisex")
    private String genero;

    @Schema(description = "Indica si está destacado", example = "true")
    private Boolean destacado;

    @Schema(description = "Indica si es nuevo", example = "false")
    private Boolean esNuevo;

    @Schema(description = "SKU del producto", example = "RB-AV-001")
    private String sku;

    @Schema(description = "Código de barras", example = "7501234567890")
    private String codigoBarras;

    @Schema(description = "Calificación promedio", example = "4.5")
    private Double calificacionPromedio;

    @Schema(description = "Número de reseñas", example = "128")
    private Integer numeroResenas;

    @Schema(description = "Fecha de creación")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de última modificación")
    private LocalDateTime fechaModificacion;

    @Schema(description = "Indica si el producto está activo", example = "true")
    private Boolean activo;

    /**
     * DTO anidado para información de categoría
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Información básica de la categoría")
    public static class CategoriaDto {
        @Schema(description = "ID de la categoría", example = "1")
        private Long idCategoria;

        @Schema(description = "Nombre de la categoría", example = "Lentes de Sol")
        private String nombre;

        @Schema(description = "Descripción de la categoría")
        private String descripcion;
    }

    /**
     * DTO anidado para información de marca
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Información básica de la marca")
    public static class MarcaDto {
        @Schema(description = "ID de la marca", example = "2")
        private Long idMarca;

        @Schema(description = "Nombre de la marca", example = "Ray-Ban")
        private String nombre;

        @Schema(description = "País de origen", example = "Italia")
        private String paisOrigen;
    }
}