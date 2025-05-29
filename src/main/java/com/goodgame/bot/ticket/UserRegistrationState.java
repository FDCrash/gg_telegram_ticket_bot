package com.goodgame.bot.ticket;

import lombok.Data;

@Data
public class UserRegistrationState {
    private UserState state;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String socialNetwork = "";
    private int ticketCount;
}
