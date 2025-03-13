FROM openjdk:21-jdk
WORKDIR /app
COPY build/libs/post-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
