FROM node:20-alpine AS frontend
WORKDIR /app

RUN npm install -g pnpm

COPY frontend/package.json frontend/pnpm-lock.yaml ./

RUN pnpm install

COPY frontend/ .

RUN pnpm run build

FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY --from=frontend /app/dist /app/src/main/resources/static

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]