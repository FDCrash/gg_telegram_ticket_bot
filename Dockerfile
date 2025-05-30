FROM bellsoft/liberica-openjdk-alpine-musl:23 AS builder
WORKDIR /app
COPY pom.xml ./
RUN apk add --no-cache maven && mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests && rm -rf /root/.m2

FROM bellsoft/liberica-openjdk-alpine-musl:23
WORKDIR /app
COPY --from=builder /app/target/telegram-gg-ticket-bot-1.0-SNAPSHOT.jar /gg_telegram_ticket_bot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/gg_telegram_ticket_bot.jar"]