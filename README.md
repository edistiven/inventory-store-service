# Inventory Store Service - Backend API

Backend REST API para el sistema de gestión de inventario desarrollado con Spring Boot y PostgreSQL.

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 4.0.5**
- **Spring Data JPA**
- **Spring Security** con autenticación JWT
- **PostgreSQL**
- **Lombok**
- **JWT** para tokens de autenticación

## Configuración de la Base de Datos

### 1. Instalar PostgreSQL

Asegúrate de tener PostgreSQL instalado y en ejecución en tu sistema.

### 2. Crear la Base de Datos

```sql
CREATE DATABASE inventory_store;
CREATE USER postgres WITH PASSWORD 'admin123';
GRANT ALL PRIVILEGES ON DATABASE inventory_store TO postgres;
```

### 3. Configurar Conexión

La configuración de la base de datos está en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventory_store
spring.datasource.username=postgres
spring.datasource.password=admin123
```

## Instalación y Ejecución

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd inventory-store/inventory-store-service
```

### 2. Construir el Proyecto

```bash
./gradlew build
```

### 3. Ejecutar la Aplicación

```bash
./gradlew bootRun
```

La aplicación estará disponible en `http://localhost:8080`

## Endpoints de la API

### Autenticación

- `POST /api/auth/login` - Iniciar sesión
- `GET /api/auth/me` - Obtener usuario actual
- `POST /api/auth/validate` - Validar token

### Productos

- `GET /api/inventario/productoEntities` - Obtener todos los productoEntities
- `GET /api/inventario/productoEntities/{codigo}` - Obtener productoEntity por código
- `POST /api/inventario/productoEntities` - Crear nuevo productoEntity
- `PUT /api/inventario/productoEntities/{codigo}` - Actualizar productoEntity
- `DELETE /api/inventario/productoEntities/{codigo}` - Eliminar productoEntity
- `GET /api/inventario/productoEntities/buscar` - Buscar productoEntities con filtros

### Ventas

- `POST /api/inventario/ventas` - Registrar venta

### Estadísticas y Reportes

- `GET /api/inventario/estadisticas` - Obtener estadísticas del inventario
- `GET /api/inventario/reportes/bajo-stock` - Reporte de productoEntities con bajo stock
- `GET /api/inventario/reportes/por-vencer` - Reporte de productoEntities por vencer
- `GET /api/inventario/resumen` - Resumen del inventario

### Health Check

- `GET /api/inventario/health` - Verificar estado del servicio

## Usuarios de Prueba

El sistema se inicializa con los siguientes usuarios:

### Administrador
- **Usuario**: admin
- **Contraseña**: admin123
- **Rol**: admin

### Usuario Normal
- **Usuario**: usuario
- **Contraseña**: admin123
- **Rol**: user

## Ejemplos de Uso

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### Obtener Productos

```bash
curl -X GET http://localhost:8080/api/inventario/productoEntities \
  -H "Authorization: Bearer <token>"
```

### Crear Producto

```bash
curl -X POST http://localhost:8080/api/inventario/productoEntities \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "nombre": "Nuevo Producto",
    "descripcion": "Descripción del productoEntity",
    "precio": 100.50,
    "cantidad": 25,
    "fechaCaducidad": "2024-12-31"
  }'
```

## Estructura del Proyecto

```
src/
|-- main/
|   |-- java/
|   |   |-- com/ec/inventory/store/inventory_store_service/
|   |   |   |-- config/          # Configuración de seguridad y JWT
|   |   |   |-- controller/      # Controladores REST
|   |   |   |-- dto/            # Data Transfer Objects
|   |   |   |-- model/
|   |   |   |   |-- entity/     # Entidades JPA
|   |   |   |-- repository/     # Repositorios JPA
|   |   |   |-- service/        # Servicios de negocio
|   |   |   |-- InventoryStoreServiceApplication.java
|   |-- resources/
|   |   |-- application.properties  # Configuración de la aplicación
|   |   |-- data.sql               # Datos iniciales
```

## Configuración Adicional

### JWT

La configuración de JWT se encuentra en `application.properties`:

```properties
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400000  # 24 horas
```

### CORS

La API está configurada para permitir solicitudes desde:

- `http://localhost:4200` (Angular)
- `http://localhost:3000` (React/Node.js)

## Desarrollo

### Logs

La aplicación está configurada para mostrar logs en consola con el siguiente formato:

```
2024-01-01 12:00:00 - Mensaje de log
```

### Base de Datos

- **DDL Auto**: `update` - Actualiza el esquema automáticamente
- **Show SQL**: `true` - Muestra las consultas SQL en consola
- **Format SQL**: `true` - Formatea las consultas SQL para mejor legibilidad

## Notas Importantes

1. **Contraseñas**: Las contraseñas en la base de datos están encriptadas con BCrypt
2. **JWT Tokens**: Los tokens expiran después de 24 horas
3. **Roles**: El sistema soporta roles `admin` y `user`
4. **Validación**: Todos los endpoints están protegidos con JWT excepto `/api/auth/login` y `/api/inventario/health`
5. **CORS**: Configurado para desarrollo local con Angular en puerto 4200

## Problemas Comunes

### Conexión a Base de Datos

Si tienes problemas de conexión, verifica:
1. Que PostgreSQL esté en ejecución
2. Que la base de datos `inventory_store` exista
3. Que las credenciales en `application.properties` sean correctas

### Errores de JWT

Si recibes errores de autenticación:
1. Verifica que el token sea válido
2. Revisa que el token no haya expirado
3. Asegúrate de incluir el token en el header `Authorization: Bearer <token>`
