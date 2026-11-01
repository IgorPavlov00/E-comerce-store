# One image holds everything: Spring Boot serves the API and the built Angular app.

FROM node:18-alpine AS web
WORKDIR /web
COPY front/package.json front/package-lock.json ./
RUN npm ci --no-audit --no-fund
COPY front/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-17 AS api
WORKDIR /api
COPY ecomerce/pom.xml ./
RUN mvn -B -q dependency:go-offline
COPY ecomerce/src ./src
COPY --from=web /web/dist/front ./src/main/resources/static
RUN mvn -B -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN useradd --system --no-create-home store
COPY --from=api /api/target/*.jar app.jar
USER store
ENV PORT=10000
# Tuned for small instances such as Render's free plan (512 MB RAM, 0.1 CPU).
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70 -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -Xss512k"
ENV SPRING_MAIN_LAZY_INITIALIZATION=true
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "app.jar"]
