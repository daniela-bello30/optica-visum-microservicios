package pe.edu.cibertec.catalogo.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pe.edu.cibertec.catalogo.dto.ApiResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para toda la aplicación
 * Proporciona respuestas consistentes y códigos HTTP apropiados
 *
 * @author VISUM Team
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de validación de @Valid
     * HTTP 400 - Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        log.error("Error de validación en {}: {}", request.getRequestURI(), ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse.ErrorDetails errorDetails = ApiResponse.ErrorDetails.builder()
                .code("VALIDATION_ERROR")
                .details("Errores de validación en los campos del request")
                .path(request.getRequestURI())
                .validationErrors(errors)
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                "Error de validación. Por favor revise los campos enviados.",
                errorDetails
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja violaciones de constraints (@NotNull, @Size, etc.)
     * HTTP 400 - Bad Request
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        log.error("Violación de constraints en {}: {}", request.getRequestURI(), ex.getMessage());

        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        ApiResponse.ErrorDetails errorDetails = ApiResponse.ErrorDetails.builder()
                .code("CONSTRAINT_VIOLATION")
                .details("Violación de restricciones de validación")
                .path(request.getRequestURI())
                .validationErrors(errors)
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                "Error en las restricciones de los datos",
                errorDetails
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones cuando un recurso no es encontrado
     * HTTP 404 - Not Found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        log.error("Recurso no encontrado en {}: {}", request.getRequestURI(), ex.getMessage());

        ApiResponse.ErrorDetails errorDetails = ApiResponse.ErrorDetails.builder()
                .code("RESOURCE_NOT_FOUND")
                .details(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                "Recurso no encontrado",
                errorDetails
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de lógica de negocio
     * HTTP 422 - Unprocessable Entity
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        log.error("Error de negocio en {}: {}", request.getRequestURI(), ex.getMessage());

        ApiResponse.ErrorDetails errorDetails = ApiResponse.ErrorDetails.builder()
                .code(ex.getErrorCode())
                .details(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                "Error en la lógica de negocio",
                errorDetails
        );

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Maneja IllegalArgumentException
     * HTTP 400 - Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.error("Argumento ilegal en {}: {}", request.getRequestURI(), ex.getMessage());

        ApiResponse.ErrorDetails errorDetails = ApiResponse.ErrorDetails.builder()
                .code("ILLEGAL_ARGUMENT")
                .details(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                "Argumento inválido en la petición",
                errorDetails
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja todas las excepciones no capturadas específicamente
     * HTTP 500 - Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Error interno del servidor en {}: ", request.getRequestURI(), ex);

        ApiResponse.ErrorDetails errorDetails = ApiResponse.ErrorDetails.builder()
                .code("INTERNAL_SERVER_ERROR")
                .details("Ha ocurrido un error inesperado. Por favor contacte al administrador.")
                .path(request.getRequestURI())
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                "Error interno del servidor",
                errorDetails
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}