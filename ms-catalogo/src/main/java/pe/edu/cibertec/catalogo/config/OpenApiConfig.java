package pe.edu.cibertec.catalogo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación automática de la API
 *
 * Acceso a la documentación:
 * - Swagger UI: http://localhost:8082/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8082/v3/api-docs
 * - OpenAPI YAML: http://localhost:8082/v3/api-docs.yaml
 *
 * @author VISUM Team
 * @version 1.0
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8082}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VISUM - API de Catálogo de Productos")
                        .version("1.0.0")
                        .description("""
                                API REST para la gestión del catálogo de productos de la óptica VISUM.
                                
                                **Funcionalidades:**
                                - Gestión de productos (lentes, monturas, accesorios)
                                - Gestión de categorías y marcas
                                - Búsqueda y filtrado de productos
                                - Gestión de stock e inventario
                                - Reseñas y valoraciones
                                - Lista de favoritos
                                
                                **Características técnicas:**
                                - Validaciones con Bean Validation
                                - Manejo centralizado de errores
                                - Códigos HTTP semánticos
                                - Paginación y ordenamiento
                                - Búsqueda full-text
                                """)
                        .contact(new Contact()
                                .name("Equipo VISUM")
                                .email("soporte@visum.com")
                                .url("https://visum.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("http://localhost:8080/api/catalogo")
                                .description("A través de API Gateway")
                ));
    }
}