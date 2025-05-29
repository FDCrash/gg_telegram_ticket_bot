FROM bellsoft/liberica-openjdk-alpine-musl:23

WORKDIR /app

COPY target/gg_telegram_ticket_bot-0.0.1-SNAPSHOT.jar /gg_telegram_ticket_bot.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/gg_telegram_ticket_bot.jar"]