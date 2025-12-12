FROM maven:3.9.11-amazoncorretto-25 AS build
WORKDIR /app

# copy only what is needed for dependency resolution first (leverages layer caching)
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

RUN mvn -B -DskipTests package

FROM eclipse-temurin:25-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
