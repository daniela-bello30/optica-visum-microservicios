package pe.edu.cibertec.carrito.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pe.edu.cibertec.carrito.dto.ApiResponse;
import pe.edu.cibertec.carrito.dto.ProductoDTO;
import pe.edu.cibertec.carrito.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CatalogoClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String CATALOGO_URL = "http://ms-catalogo/productos";
//cambie el metodo
    public ProductoDTO obtenerProducto(Long idProducto) {
        try {
            ResponseEntity<ProductoDTO> response = restTemplate.getForEntity(
                    CATALOGO_URL + "/" + idProducto,
                    ProductoDTO.class
            );

            return response.getBody();

        } catch (Exception e) {
            throw new BusinessException("Error al conectar con el catálogo: " + e.getMessage(), "COMMUNICATION_ERROR");
        }
    }

    public void actualizarStock(Long idProducto, Integer cantidad) {
        try {
            restTemplate.put(CATALOGO_URL + "/" + idProducto + "/stock?cantidad=" + cantidad, null);
        } catch (Exception e) {
            throw new BusinessException("No se pudo actualizar el stock en el catálogo", "STOCK_UPDATE_ERROR");
        }
    }
}