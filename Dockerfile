# ==========================================
# 1. ETAPA DE CONSTRUCCIÓN (BUILD STAGE)
# ==========================================
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copiar configuración de dependencias para aprovechar la caché de capas de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el archivo JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# 2. ETAPA DE EJECUCIÓN (RUNTIME STAGE)
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear un usuario sin privilegios por seguridad y preparar directorio de logs
RUN addgroup -S appgroup && adduser -S appuser -G appgroup && mkdir -p /app/logs && chown -R appuser:appgroup /app
USER appuser

# Copiar el artefacto generado desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto del microservicio ms-auth
EXPOSE 8086

# Variables de entorno por defecto
ENV SERVER_PORT=8086 \
    JAVA_OPTS="-Xms256m -Xmx512m"

# Comando de inicio del contenedor
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
