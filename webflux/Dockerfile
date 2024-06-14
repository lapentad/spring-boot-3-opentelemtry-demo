FROM openjdk:17-ea-slim-buster
WORKDIR /app
EXPOSE 8091 8091
ADD target/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]