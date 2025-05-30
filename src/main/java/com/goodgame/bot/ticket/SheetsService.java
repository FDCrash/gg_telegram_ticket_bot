package com.goodgame.bot.ticket;

import com.goodgame.bot.ticket.config.BotConfig;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SheetsService {
    private final BotConfig botConfig;

    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
        try (InputStream inputStream = SheetsService.class.getResourceAsStream(botConfig.getCredentialsPath())) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));
            return new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)
            ).setApplicationName("Telegram Bot").build();
        }
    }

    public void appendToSheet(List<Object> values) throws IOException, GeneralSecurityException {
        Sheets sheetsService = getSheetsService();
        ValueRange body = new ValueRange().setValues(Collections.singletonList(values));
        sheetsService.spreadsheets().values()
                .append(botConfig.getSheetId(), botConfig.getSheetName(), body)
                .setValueInputOption("RAW")
                .execute();
    }
}
