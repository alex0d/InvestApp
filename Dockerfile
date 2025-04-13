FROM eclipse-temurin:21 AS build
WORKDIR /app
COPY . .
EXPOSE 80
CMD ["./gradlew", ":composeApp:wasmJsBrowserDevelopmentRun"]