# Imagen base con JDK para ejecutar tu app
FROM openjdk:21-ea-24-oracle
# Directorio de trabajo dentro del contenedor
WORKDIR /app
# Copia el archivo JAR generado al contenedor
COPY build/libs/*.jar app.jar
COPY Wallet_OZARZE8P1JOTCP4N /Wallet_OZARZE8P1JOTCP4N
# Expón el puerto que usa Spring Boot (por defecto es 8080)
EXPOSE 8080

# Comando para ejecutar tu aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]