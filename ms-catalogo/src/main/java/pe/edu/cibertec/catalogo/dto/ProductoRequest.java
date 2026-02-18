package pe.edu.cibertec.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para la creación y actualización de productos
 * Incluye todas las validaciones necesarias según reglas de negocio
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear o actualizar un producto")
public class ProductoRequest {

    @Schema(description = "Nombre del producto", example = "Ray-Ban Aviator Clásico")
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    private String nombre;

    @Schema(description = "Descripción detallada del producto",
            example = "Lentes de sol icónicos con lentes de cristal verde...")
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 2000, message = "La descripción debe tener entre 10 y 2000 caracteres")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "299.90", minimum = "0")
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @DecimalMax(value = "99999.99", message = "El precio no puede exceder 99,999.99")
    @Digits(integer = 5, fraction = 2, message = "El precio debe tener máximo 5 enteros y 2 decimales")
    private BigDecimal precio;

    @Schema(description = "Porcentaje de descuento", example = "15.00", minimum = "0", maximum = "100")
    @DecimalMin(value = "0.00", message = "El descuento no puede ser negativo")
    @DecimalMax(value = "100.00", message = "El descuento no puede exceder 100%")
    @Digits(integer = 3, fraction = 2, message = "El descuento debe tener máximo 3 enteros y 2 decimales")
    private BigDecimal descuento;

    @Schema(description = "Cantidad en stock", example = "50", minimum = "0")
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 9999, message = "El stock no puede exceder 9,999 unidades")
    private Integer stock;

    @Schema(description = "ID de la categoría", example = "1")
    @NotNull(message = "La categoría es obligatoria")
    @Positive(message = "El ID de categoría debe ser positivo")
    private Long idCategoria;

    @Schema(description = "ID de la marca", example = "2")
    @NotNull(message = "La marca es obligatoria")
    @Positive(message = "El ID de marca debe ser positivo")
    private Long idMarca;

    @Schema(description = "Color del producto", example = "Negro")
    @Size(max = 50, message = "El color no puede exceder 50 caracteres")
    private String color;

    @Schema(description = "Forma del producto", example = "Rectangular")
    @Size(max = 50, message = "La forma no puede exceder 50 caracteres")
    private String forma;

    @Schema(description = "Material del producto", example = "Acetato")
    @Size(max = 100, message = "El material no puede exceder 100 caracteres")
    private String material;

    @Schema(description = "Género al que está dirigido",
            example = "Unisex",
            allowableValues = {"Hombre", "Mujer", "Unisex"})
    @Pattern(regexp = "^(Hombre|Mujer|Unisex)$",
            message = "El género debe ser: Hombre, Mujer o Unisex")
    private String genero;

    @Schema(description = "Indica si el producto está destacado", example = "true")
    private Boolean destacado;

    @Schema(description = "Indica si el producto es nuevo", example = "false")
    private Boolean esNuevo;

    @Schema(description = "SKU del producto", example = "RB-AV-001")
    @Size(max = 50, message = "El SKU no puede exceder 50 caracteres")
    private String sku;

    @Schema(description = "Código de barras", example = "7501234567890")
    @Size(max = 50, message = "El código de barras no puede exceder 50 caracteres")
    private String codigoBarras;
}