# =========================================
# Stage 1 — Build Stage (Gradle Build)
# =========================================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy Gradle files first (for better caching)
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Download Gradle dependencies (cached)
RUN ./gradlew dependencies --no-daemon || true

# Copy full project
COPY . .

# Build the Spring Boot application
RUN ./gradlew clean build -x test --no-daemon

# =========================================
# Stage 2 — Runtime Stage (Slim JDK)
# =========================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy built JAR from the builder stage
COPY --from=builder /app/build/libs/app.jar app.jar

# Expose port (default Spring Boot port)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]