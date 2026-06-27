<div align="center">
  <h1>👓 Óptica VISUM</h1>
  <p><b>Plataforma de Gestión y E-commerce basada en Microservicios</b></p>
  
  ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
  ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
  ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
  ![JavaScript](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)
  ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
  ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
</div>

---

## 📖 1. Resumen del Proyecto

**Óptica VISUM** es una aplicación web empresarial escalable desarrollada bajo una **arquitectura de microservicios**. La solución está orientada a la gestión integral de una óptica, permitiendo administrar de manera fluida y centralizada la venta de productos (monturas y lentes de sol) junto con el agendamiento y control de citas visuales.

---

## 🏗️ 2. Arquitectura del Sistema

La plataforma ha sido construida para ser completamente independiente y escalable. Consta de cinco microservicios en Spring Boot, cada uno con su propia base de datos, garantizando una arquitectura robusta y separación de responsabilidades.

* 🌐 **API Gateway:** Enrutamiento centralizado de todas las solicitudes (`Puerto 8080`).
* 🔍 **Service Registry (Eureka):** Descubrimiento y registro dinámico de servicios (`Puerto 8761`).

### 📦 Módulos Funcionales (Microservicios)

| Microservicio | Puerto | Descripción Funcional |
| :--- | :--- | :--- |
| `ms-seguridad` | `8081` | Gestión de accesos, login (BCrypt), CRUD de usuarios/roles y validación de permisos. |
| `ms-catalogo` | `8082` | Inventario de monturas/lentes, control de stock, CRUD y dashboard interactivo. |
| `ms-carrito` | `8083` | Gestión de compras (añadir, ver, eliminar). Comunicación REST directa con Catálogo. |
| `ms-ventas` | `8084` | Creación de órdenes de compra, historial de pedidos y reducción automática de stock. |
| `ms-citas` | `8085` | Administración de sucursales, calendario interactivo para agendar/cancelar citas. |

---

## 💻 3. Stack Tecnológico

* **Backend:** Java, Spring Boot, Spring Data JPA.
* **Seguridad:** Spring Security, BCryptPasswordEncoder.
* **Base de Datos:** PostgreSQL.
* **Integración:** APIs REST (GET, POST, PUT, DELETE), manejo estructurado de estados HTTP.
* **Documentación de API:** Swagger / OpenAPI.
* **Frontend:** HTML5 puro y consumo asíncrono de APIs mediante JavaScript (Fetch API).

---

## ⚙️ 4. Configuración y Despliegue

Para ejecutar el entorno localmente, es indispensable tener un servidor **PostgreSQL** activo y crear las siguientes bases de datos antes de levantar los microservicios:

```sql
CREATE DATABASE optica_seguridad;
CREATE DATABASE optica_catalogo;
CREATE DATABASE optica_carrito;
CREATE DATABASE optica_ventas;
CREATE DATABASE optica_citas;

🚀 5. Uso y Pruebas
El sistema permite interactuar tanto a través del enrutador central como de forma aislada por cada servicio.

Plataforma Principal (Gateway): http://localhost:8080/index.html

Accesos Aislados: * Registro: http://localhost:8081/register.html

Catálogo: http://localhost:8082/catalogo.html

Pruebas Backend (Postman / Swagger): Puedes atacar directamente los endpoints, por ejemplo: http://localhost:8082/productos

👨‍💻 6. Equipo de Desarrollo
Desarrollado para el curso Desarrollo de Servicios Web II (Período 2026) — Instituto de Educación Superior Privado CIBERTEC.

Docente Titular: Franklin Silvestre Cappa Ticona
