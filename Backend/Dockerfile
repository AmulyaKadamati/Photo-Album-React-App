# =========================
# 1. Build Stage
# =========================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom first for caching
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copy source
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# =========================
# 2. Run Stage
# =========================
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Expose port (Render will override but it's ok)
EXPOSE 8080

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Set environment PORT for Render
ENV PORT=8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
