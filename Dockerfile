# Etapa 1: Construcción (Build)
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
# Copiamos el pom y descargamos dependencias (aprovechando la caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copiamos el código fuente y compilamos
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Run)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos solo el .jar generado en la etapa anterior
COPY --from=builder /app/target/*-SNAPSHOT.jar ms-producto.jar
# Exponemos el puerto del microservicio
EXPOSE 8082
# Comando de inicio
ENTRYPOINT ["java", "-jar", "ms-producto.jar"]