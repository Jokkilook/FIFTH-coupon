FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/app.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]