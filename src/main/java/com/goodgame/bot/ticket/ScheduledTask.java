package com.goodgame.bot.ticket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTask {

    @Scheduled(fixedRate = 600000)
    public void logEveryTenMinutes() {
        log.info("Прошло 10 минут");
    }
}
