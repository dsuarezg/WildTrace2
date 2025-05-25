<div style="text-align: center;">
  <img src="banner.png" alt="WildTrace" width="100%">
</div>

# WildTrace – Plataforma distribuida para el registro de biodiversidad

## 🌿 Descripción general

**WildTrace** es un sistema distribuido de microservicios diseñado para registrar avistamientos de fauna en zonas naturalizadas. Su objetivo es proporcionar una herramienta extensible, modular y profesional para técnicos, voluntarios y gestores ambientales interesados en el seguimiento de la biodiversidad.

Cada avistamiento queda registrado como una combinación única de especie, zona geográfica y fecha, con detalles del observador y método usado.

---

## 🧩 Arquitectura basada en microservicios

WildTrace está compuesto por los siguientes servicios:

| Servicio           | Puerto | Descripción                                           |
|--------------------|--------|--------------------------------------------------------|
| `discovery-server` | 8761   | Eureka Server para descubrimiento de servicios        |
| `gateway-service`  | 8080   | API Gateway que enruta las solicitudes a los servicios |
| `species-service`  | 8081   | Gestión del catálogo de especies                      |
| `zone-service`     | 8082   | Gestión de zonas naturalizadas                        |
| `sighting-service` | 8083   | Registro de avistamientos de especies                 |

Todos los microservicios están registrados en Eureka y se comunican a través de **Feign Clients**.

---

## 🗺️ Diagrama de arquitectura

```text
          +------------------------+
          |   discovery-server     | (Eureka)
          +------------------------+
                     ▲
                     |
         +-----------+------------+
         |                        |
+------------------+    +------------------+    +------------------+
|  species-service |    |   zone-service   |    | sighting-service |
+------------------+    +------------------+    +------------------+
         ▲                        ▲                     ▲
         |                        |                     |
         +----------+-------------+---------------------+
                    |
            +------------------+
            |  gateway-service |
            +------------------+  <-- Expuesto en el puerto 8080
```

---

## 🚀 Instalación local (multi-servicio)

### 1. Clonar el repositorio

```bash
git clone https://github.com/dsuarezg/WildTrace.git
cd WildTrace
```

### 2. Crear las bases de datos en MySQL:

```sql
CREATE DATABASE wildtrace_species;
CREATE DATABASE wildtrace_zones;
CREATE DATABASE wildtrace_sightings;
```

### 3. Configurar `application.properties` en cada microservicio (`species-service`, `zone-service`, `sighting-service`)

Asegúrate de definir el nombre de la aplicación y la URL de Eureka:

```properties
spring.application.name=species-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

### 4. Compilar y ejecutar todos los servicios:

En cada subcarpeta:
```bash
mvn clean install
mvn spring-boot:run
```

Orden recomendado:
1. `discovery-server`
2. `gateway-service`
3. `species-service`
4. `zone-service`
5. `sighting-service`

---

## 📚 Microservicios documentados

### 🔬 [Species Service](species-service/README.md)
- Gestión de especies (CRUD)
- Swagger UI: `http://localhost:8081/swagger-ui/index.html`

### 🗺️ [Zone Service](zone-service/README.md)
- Gestión de zonas naturalizadas (CRUD + coordenadas)
- Swagger UI: `http://localhost:8082/swagger-ui/index.html`

### 👁️ [Sighting Service](sighting-service/README.md)
- Registro de avistamientos (con validación cruzada)
- Swagger UI: `http://localhost:8083/swagger-ui/index.html`

---

## 🔐 API Gateway

`gateway-service` escucha en el puerto `8080` y enruta las peticiones a los microservicios según los siguientes patrones:

| Ruta                  | Servicio destino        |
|-----------------------|-------------------------|
| `/api/species/**`     | `species-service`       |
| `/api/zones/**`       | `zone-service`          |
| `/api/sightings/**`   | `sighting-service`      |

---

## 🔧 Tecnologías utilizadas

- Java 21
- Spring Boot 3.4.x
- Spring Cloud Eureka / OpenFeign / Gateway
- Spring Data JPA
- Swagger / OpenAPI 3
- JUnit 5 / Mockito / MockMvc / TestRestTemplate
- ArchUnit / JaCoCo
- MySQL / MariaDB
- Lombok
- Maven

---

## ✅ Pruebas y cobertura

Todos los servicios cuentan con:

- Tests unitarios con Mockito
- Tests de integración con MockMvc
- Tests funcionales con TestRestTemplate
- Validación de arquitectura con ArchUnit
- Cobertura con JaCoCo (visualizable en `target/site/jacoco/index.html`)

---

## 📦 Por hacer (próximos pasos)

- [ ] Cachear entidades más consultadas (especies, zonas)
- [ ] Filtros avanzados en Sightings por fecha, especie, zona
- [ ] Enriquecer respuestas con datos relacionados (DTOs anidados)
- [ ] Exponer métricas o estadísticas (frecuencia, zonas calientes)
- [ ] Refactor hacia arquitectura hexagonal (por puertos y adaptadores)# WildTrace
