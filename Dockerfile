FROM bellsoft/liberica-openjdk-alpine-musl:23 AS builder
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN apk add --no-cache maven && mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine-musl:23
WORKDIR /app
COPY --from=builder /app/target/telegram-gg-ticket-bot-1.0-SNAPSHOT.jar /gg_telegram_ticket_bot.jar
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]