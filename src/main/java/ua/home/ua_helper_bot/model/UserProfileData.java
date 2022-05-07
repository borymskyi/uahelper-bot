package ua.home.ua_helper_bot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData {

    String profileChatId;
    int userId;
    LocalDateTime updateUserTime;
    Integer messageId;
    String inlineMessageId;
    List<Integer> allMessageIdUser = new ArrayList<>();

    public void setAllMessageIdUser(Integer messageId) {

        if (allMessageIdUser.contains(messageId)) {

        } else {
            this.allMessageIdUser.add(messageId);
        }

        for (int i = 0; i < allMessageIdUser.size(); i++) {

            if (allMessageIdUser.get(i) == null && allMessageIdUser.get(i) != messageId) {
                this.allMessageIdUser.remove(allMessageIdUser.get(i));
            }
        }

    }

    public List<Integer> getAllMessageIdUser() {
        return this.allMessageIdUser;
    }

    @Override
    public String toString() {
        return "UserProfileData{" +
                "profileChatId='" + profileChatId + '\'' +
                ", userId=" + userId +
                ", updateUserTime=" + updateUserTime +
                ", messageId=" + messageId +
                ", inlineMessageId='" + inlineMessageId + '\'' +
                ", allMessageIdUser=" + allMessageIdUser +
                '}';
    }
}