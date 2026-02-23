package pe.edu.cibertec.carrito.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VISUM - API de Carrito de Compras")
                        .version("1.0.0")
                        .description("Gestión de carritos temporales de los usuarios de la óptica."));
    }
}