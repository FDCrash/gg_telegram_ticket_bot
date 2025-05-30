#!/bin/sh
export GOOGLE_CREDENTIALS=$(cat /etc/secrets/google_credentials)
export TELEGRAM_TOKEN=$(cat /etc/secrets/telegram_token)

exec java -jar /gg_telegram_ticket_bot.jar