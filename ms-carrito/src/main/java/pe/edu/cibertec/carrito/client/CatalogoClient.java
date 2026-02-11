package pe.edu.cibertec.carrito.client;

import pe.edu.cibertec.carrito.dto.ProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CatalogoClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String CATALOGO_URL = "http://ms-catalogo/productos";

    public ProductoDTO obtenerProducto(Long idProducto) {
        try {
            return restTemplate.getForObject(CATALOGO_URL + "/" + idProducto, ProductoDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener el producto del catálogo");
        }
    }

    public void actualizarStock(Long idProducto, Integer cantidad) {
        try {
            restTemplate.put(CATALOGO_URL + "/" + idProducto + "/stock?cantidad=" + cantidad, null);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar el stock");
        }
    }
}