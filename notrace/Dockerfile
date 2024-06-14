FROM openjdk:17-ea-slim-buster
WORKDIR /app
EXPOSE 8092 8092
ADD target/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]