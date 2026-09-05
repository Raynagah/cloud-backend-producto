# Pedidos360 - Microservicio de Productos (`ms-producto`)

Microservicio encargado de la gestión y persistencia del catálogo de productos para la plataforma **Pedidos360**. Expone los endpoints necesarios para consultar productos activos y validar inventarios.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3.2.x
* **Persistencia:** Spring Data JPA / PostgreSQL
* **Seguridad:** OAuth2 Resource Server (Validación JWT con Azure AD)
* **Contenedorización:** Docker

## ⚙️ Instalación y Ejecución

### Requisitos Previos

* JDK 17
* Maven 3.8+
* PostgreSQL (Base de datos `db_producto`)
* Docker

### Variables de Entorno

| Variable | Valor por Defecto / Descripción |
| :--- | :--- |
| `AZURE_TENANT_ID` | `78b145ef-56b9-4397-b87c-27b242a9fce5` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://<HOST_BD>:5432/db_producto` |
| `SPRING_DATASOURCE_USERNAME` | Credencial de base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Credencial de base de datos |

### Endpoints Principales

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/v1/productos` | Lista todos los productos activos |
| `GET` | `/api/v1/productos/{id}` | Obtiene el detalle de un producto por su ID |

### Compilación Local

```bash
mvn clean package -DskipTests
```

### Despliegue con Docker

1. **Construir la imagen:**

```bash
docker build -t pedidos360/ms-producto:v1 .
```

2. **Ejecutar contenedor:**

```bash
docker run -d \
  --name ms-producto \
  -p 8082:8082 \
  -e AZURE_TENANT_ID="78b145ef-56b9-4397-b87c-27b242a9fce5" \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://<HOST_BD>:5432/db_producto" \
  -e SPRING_DATASOURCE_USERNAME="postgres" \
  -e SPRING_DATASOURCE_PASSWORD="password" \
  pedidos360/ms-producto:v1
```

---

## 🔗 Ecosistema de Repositorios

### Backend

* [Microservicio Producto (Este repositorio)](https://github.com/Raynagah/cloud-backend-producto)
* [BFF Orchestrator](https://github.com/Raynagah/cloud-backend-bff)
* [Microservicio Carrito](https://github.com/Raynagah/cloud-backend-carrito)
* [Microservicio Usuarios](https://github.com/NBello26/ms-usuarios-cloud.git)

### Frontend

* [Frontend React](https://github.com/Raynagah/cloud-frontend.git)