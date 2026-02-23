package pe.edu.cibertec.ventas.config;

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
                        .title("VISUM - API de Ventas y Pedidos")
                        .version("1.0.0")
                        .description("Gestión de órdenes de compra, pagos y envíos."));
    }
}