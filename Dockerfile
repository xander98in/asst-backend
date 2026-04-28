# ============================================================
# Stage 1: Build con Maven + JDK 17
# ============================================================
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copiar wrapper de Maven y archivos de configuración primero
# (esto permite a Docker cachear las dependencias si solo cambia el código)
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Dar permisos de ejecución al wrapper de Maven (necesario en Linux)
RUN chmod +x mvnw

# Descargar dependencias (capa cacheable)
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente
COPY src src

# Compilar el JAR (saltamos los tests porque ya se ejecutaron localmente)
RUN ./mvnw clean package -DskipTests -B

# ============================================================
# Stage 2: Runtime con solo JRE 17
# ============================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar el JAR construido en el stage 1
COPY --from=builder /app/target/*.jar app.jar

# Render usa la variable PORT, ya configurada en application.properties
EXPOSE 8080

# Ejecutar el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
