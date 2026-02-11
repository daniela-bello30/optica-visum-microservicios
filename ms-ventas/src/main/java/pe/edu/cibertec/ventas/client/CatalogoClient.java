package pe.edu.cibertec.ventas.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CatalogoClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String CATALOGO_URL = "http://ms-catalogo/productos";

    public void actualizarStock(Long idProducto, Integer cantidad) {
        try {
            restTemplate.put(CATALOGO_URL + "/" + idProducto + "/stock?cantidad=" + cantidad, null);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar stock del producto");
        }
    }
}