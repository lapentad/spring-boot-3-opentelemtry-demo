FROM openjdk:17-ea-slim-buster
WORKDIR /app
EXPOSE 8090 8090
ADD target/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]