# Guía de despliegue — ASST Backend

Este documento describe las variables de entorno requeridas para desplegar el backend en Render con base de datos PostgreSQL en Neon.

## Variables de entorno requeridas en producción

### Base de datos (Neon)

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_URL` | URL JDBC completa de Neon | `jdbc:postgresql://ep-xxx.neon.tech/asst_db?sslmode=require` |
| `DB_USERNAME` | Usuario de la BD | `neondb_owner` |
| `DB_PASSWORD` | Contraseña de la BD | (valor secreto) |

### Spring Boot

| Variable | Descripción | Valor recomendado |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Perfil activo de Spring | `prod` |
| `PORT` | Puerto del servidor | (Render lo asigna automáticamente) |

### JPA (opcional, ya tienen valores recomendados en application-prod.properties)

| Variable | Descripción | Valor recomendado en producción |
|---|---|---|
| `JPA_DDL_AUTO` | Estrategia de schema de Hibernate | `validate` |
| `JPA_SHOW_SQL` | Mostrar SQL en logs | `false` |

### JWT

| Variable | Descripción |
|---|---|
| `JWT_SECRET` | Clave secreta para firmar JWT (mínimo 256 bits, base64) |
| `JWT_ACCESS_EXPIRATION` | Expiración access token en ms (default: 3600000 = 1h) |
| `JWT_REFRESH_EXPIRATION` | Expiración refresh token en ms (default: 86400000 = 24h) |

### Google OAuth

| Variable | Descripción |
|---|---|
| `GOOGLE_CLIENT_ID` | Client ID de Google OAuth |
| `GOOGLE_ALLOWED_DOMAIN` | Dominio permitido (default: `unicauca.edu.co`) |

### CORS y Swagger

| Variable | Descripción | Ejemplo |
|---|---|---|
| `CORS_ALLOWED_ORIGINS` | URLs del frontend separadas por coma | `https://asst.vercel.app,https://asst-preview.vercel.app` |
| `SWAGGER_ENABLED` | Habilitar/deshabilitar Swagger UI | `true` o `false` |

## Generación de JWT_SECRET para producción

Se recomienda generar un nuevo secreto distinto al de desarrollo. Opciones:

- Linux/Mac: `openssl rand -base64 64`
- Windows (Git Bash): `openssl rand -base64 64`
- Online: https://generate-random.org/encryption-key-generator (mínimo 256 bits)

## Activación del perfil de producción

El perfil `prod` se activa automáticamente cuando `SPRING_PROFILES_ACTIVE=prod`. Esto sobrescribe valores de `application.properties` con los de `application-prod.properties`:

- `spring.jpa.hibernate.ddl-auto=validate` (no modifica el schema, solo lo valida)
- `spring.jpa.show-sql=false` (no expone SQL en logs)
- Logging de Hibernate reducido a WARN

## Verificación post-despliegue

1. Revisar logs en Render: debe aparecer `The following 1 profile is active: "prod"`.
2. Verificar que el log diga `Tomcat started on port {PORT}` sin errores.
3. Acceder a `https://{tu-app}.onrender.com/swagger-ui/index.html` (si `SWAGGER_ENABLED=true`).
4. Probar el login con Google desde el frontend en Vercel.
