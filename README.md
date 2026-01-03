## Práctica 1 DSS - Programación de servicio web RESTful con EJB y ORM

### Requisitos del Sistema
- **Java**: JDK 17 (mínimo recomendado para compatibilidad con STS 4.0)
- **Maven**: 3.6.0+
- **STS 4.0**: Spring Tool Suite 4.0
- **Base de Datos**: H2 (embebida, sin configuración externa necesaria)

### Importación en STS 4.0

1. Descomprimir el ZIP del proyecto
2. En STS: `File → Import → Maven → Existing Maven Projects`
3. Seleccionar la carpeta raíz donde esté el `pom.xml`
4. Hacer clic en `Next` y luego `Finish`
5. Ejecutar: `Maven → Update Project (Force Update)` - sin errores debe terminar

### Ejecución del Proyecto

**Desde STS:**
- Click derecho en el proyecto
- `Run As → Spring Boot App`
- La aplicación arrancará en `http://localhost:8080`

**Desde terminal Maven:**
```bash
mvn clean spring-boot:run
```

### Endpoints Principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Página principal |
| GET | `/login` | Página de login |
| POST | `/login` | Autenticación de usuario |
| GET | `/products` | Listado de productos |
| GET | `/cart` | Carrito de compras |
| POST | `/cart/add` | Añadir producto al carrito |
| GET | `/admin` | Panel de administración |
| POST | `/admin/export` | Exportar datos |

### Credenciales de Acceso

- **Usuario administrador**: `admin` / `admin`
- **Base de datos**: H2 embebida (configurada automáticamente)
- **Consola H2**: Disponible en `http://localhost:8080/h2-console`

### Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/practica1/practica1/
│   │   ├── controller/     # Controladores REST
│   │   ├── service/        # Servicios de negocio
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Repositorios Spring Data
│   │   └── config/         # Configuración de seguridad
│   └── resources/
│       ├── application.properties  # Configuración de la app
│       ├── static/                # Recursos estáticos (CSS, JS, imágenes)
│       └── templates/             # Templates Thymeleaf
└── test/
    └── java/...            # Tests unitarios
```

### Tecnologías Utilizadas

- **Spring Boot 3.5.6**: Framework web
- **Spring Security 6**: Autenticación y autorización
- **Spring Data JPA**: Persistencia de datos
- **H2 Database**: Base de datos embebida
- **Thymeleaf**: Plantillas HTML
- **Lombok**: Generación de código boilerplate
- **Maven**: Gestión de dependencias

### Solución de Problemas

**Error al actualizar Maven:**
```bash
Maven → Update Project (Force Update)
```

**Errores de compilación:**
- Verificar que Java 17 está configurado: `Window → Preferences → Java → Installed JREs`
- Hacer clic derecho en proyecto → `Maven → Update Project`

**Puerto 8080 en uso:**
- Cambiar en `application.properties`: `server.port=8081`
