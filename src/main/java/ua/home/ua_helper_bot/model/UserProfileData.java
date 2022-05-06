package ua.home.ua_helper_bot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData {

    String profileChatId;
    int userId;
    LocalDateTime updateUserTime;
    Integer messageId;
    String inlineMessageId;

    @Override
    public String toString() {
        return "UserProfileData{" +
                "profileChatId='" + profileChatId + '\'' +
                ", userId=" + userId +
                ", updateUserTime=" + updateUserTime +
                ", messageId=" + messageId +
                ", inlineMessageId='" + inlineMessageId + '\'' +
                '}';
    }
}