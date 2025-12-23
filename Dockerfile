FROM eclipse-temurin:21-jdk
LABEL author="your_name"

WORKDIR /app
COPY target/buchladen-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]



