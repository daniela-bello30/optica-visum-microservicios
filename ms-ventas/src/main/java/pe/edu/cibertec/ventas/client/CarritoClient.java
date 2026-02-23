package pe.edu.cibertec.ventas.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.ventas.dto.ApiResponse;
import pe.edu.cibertec.ventas.exception.BusinessException;

import java.util.Map;

@Service
public class CarritoClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String CARRITO_URL = "http://ms-carrito/carrito";

    public Map<String, Object> obtenerCarrito(Long idUsuario) {
        try {
            ResponseEntity<ApiResponse<Map<String, Object>>> response = restTemplate.exchange(
                    CARRITO_URL + "/" + idUsuario,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<Map<String, Object>>>() {}
            );
            if (response.getBody() != null && response.getBody().getSuccess()) {
                return response.getBody().getData();
            }
            throw new BusinessException("No se pudo obtener el carrito del usuario", "CARRITO_ERROR");
        } catch (Exception e) {
            throw new BusinessException("Error de comunicación con MS-Carrito: " + e.getMessage(), "COMMUNICATION_ERROR");
        }
    }

    public void vaciarCarrito(Long idUsuario) {
        try {
            restTemplate.delete(CARRITO_URL + "/" + idUsuario + "/vaciar");
        } catch (Exception e) {
            throw new BusinessException("Error al vaciar carrito en MS-Carrito", "CARRITO_CLEAR_ERROR");
        }
    }
}