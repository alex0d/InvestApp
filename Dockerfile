FROM eclipse-temurin:21 AS build
WORKDIR /app
COPY . .
EXPOSE 8080
CMD ["./gradlew", ":composeApp:wasmJsBrowserDevelopmentRun"]