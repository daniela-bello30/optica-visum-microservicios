package pe.edu.cibertec.ventas.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class CarritoClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String CARRITO_URL = "http://ms-carrito/carrito";

    public Map<String, Object> obtenerCarrito(Long idUsuario) {
        try {
            return restTemplate.exchange(
                    CARRITO_URL + "/" + idUsuario,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            ).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener carrito del usuario");
        }
    }

    public void vaciarCarrito(Long idUsuario) {
        try {
            restTemplate.delete(CARRITO_URL + "/" + idUsuario + "/vaciar");
        } catch (Exception e) {
            throw new RuntimeException("Error al vaciar carrito");
        }
    }
}