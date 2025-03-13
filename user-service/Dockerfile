FROM openjdk:21-jdk
WORKDIR /app
COPY build/libs/user-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
