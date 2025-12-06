# --------------------------------------------------------
# ETAPA 1: BUILD (Compilación)
# Usamos una imagen con Maven y Java 21 para crear el .jar
# --------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Primero copiamos solo el pom.xml para aprovechar el caché de Docker
COPY pom.xml .
# Descargamos dependencias (esto se guarda en caché si no cambias el pom)
RUN mvn dependency:go-offline

# Ahora copiamos el código fuente y compilamos
COPY src ./src
# -DskipTests evita errores de conexión a DB durante la construcción
RUN mvn clean package -DskipTests

# --------------------------------------------------------
# ETAPA 2: RUN (Ejecución)
# Usamos una imagen ligera de Java 21 solo para correr la app
# --------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos solo el .jar construido en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto 8080
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]