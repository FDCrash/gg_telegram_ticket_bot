package com.goodgame.bot.ticket;

import com.goodgame.bot.ticket.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TicketBot extends TelegramLongPollingBot {
    private final SheetsService sheetsService;
    private final Map<Long, UserRegistrationState> userStates = new HashMap<>();

    public TicketBot(BotConfig botConfig, SheetsService sheetsService) {
        super(botConfig.getToken());
        this.sheetsService = sheetsService;
    }

    @Override
    public String getBotUsername() {
        return "GG ticket bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        try {
            if (messageText.equals("/start")) {
                sendMessage(chatId, "Привет! Используйте /register для регистрации билетов.");
            } else if (messageText.equals("/register")) {
                userStates.put(chatId, new UserRegistrationState());
                userStates.get(chatId).setState(UserState.WAITING_FOR_NAME);
                sendMessage(chatId, "Пожалуйста, введите ваше имя.");
            } else if (userStates.containsKey(chatId)) {
                handleRegistration(chatId, messageText);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleRegistration(long chatId, String messageText) throws TelegramApiException {
        UserRegistrationState state = userStates.get(chatId);
        UserState currentState = state.getState();

        switch (currentState) {
            case WAITING_FOR_NAME:
                state.setFirstName(messageText);
                state.setState(UserState.WAITING_FOR_LAST_NAME);
                sendMessage(chatId, "Введите вашу фамилию.");
                break;
            case WAITING_FOR_LAST_NAME:
                state.setLastName(messageText);
                state.setState(UserState.WAITING_FOR_SOCIAL_NETWORK);
                sendMessage(chatId, "Введите ваш номер телефона.");
                break;
            case WAITING_FOR_SOCIAL_NETWORK:
                state.setPhoneNumber(messageText);
                state.setState(UserState.WAITING_FOR_PHONE);
                sendMessage(chatId, "Введите соц сеть (instagram/telegram).");
                break;
            case WAITING_FOR_PHONE:
                state.setSocialNetwork(messageText);
                state.setState(UserState.WAITING_FOR_TICKET_COUNT);
                sendMessage(chatId, "Сколько билетов вы хотите зарегистрировать?");
                break;
            case WAITING_FOR_TICKET_COUNT:
                int ticketCount = Integer.parseInt(messageText);
                state.setTicketCount(ticketCount);

                for (int i = 0; i < ticketCount; i++) {
                    List<Object> row = List.of(
                            state.getFirstName(),
                            state.getLastName(),
                            state.getPhoneNumber(),
                            state.getSocialNetwork(),
                            Instant.now().toString()
                    );
                    try {
                        sheetsService.appendToSheet(row);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendMessage(chatId, "Ошибка при сохранении данных. Попробуйте снова.");
                        return;
                    }
                }

                sendMessage(chatId, "Регистрация завершена. Зарегистрировано " + ticketCount + " билетов.");
                userStates.remove(chatId);
                break;
        }
    }

    private void sendMessage(long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        execute(message);
    }
}
