FROM eclipse-temurin:23-jdk-jammy

WORKDIR /app

COPY target/gg_telegram_ticket_bot-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dserver.port=${PORT:-8080}", "-jar", "app.jar"]