package com.goodgame.bot.ticket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bot")
@Data
public class BotConfig {
    private String token;
    private String name;
    private String sheetId;
    private String credentialsPath;
}
