package ru.home.mywizard_bot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Данные анкеты пользователя
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData {

    String ProfileChatId;
    int ProfileUserId;

    @Override
    public String toString() {
        return "UserProfileData{" +
                "ProfileChatId='" + ProfileChatId + '\'' +
                ", ProfileUserId=" + ProfileUserId +
                '}';
    }
}