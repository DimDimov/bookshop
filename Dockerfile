FROM eclipse-temurin:21-jdk
LABEL author="Vadym"

WORKDIR /app
COPY target/buchladen-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8078

ENTRYPOINT ["java", "-jar", "/app/app.jar"]



