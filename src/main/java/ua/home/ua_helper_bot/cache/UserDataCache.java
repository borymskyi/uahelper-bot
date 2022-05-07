package ua.home.ua_helper_bot.cache;

import org.springframework.stereotype.Component;
import ua.home.ua_helper_bot.botapi.BotState;
import ua.home.ua_helper_bot.model.UserProfileData;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory cache.
 * usersBotStates: user_id and user's bot state
 * usersProfileData: user_id  and user's profile data.
 */
@Component
public class UserDataCache implements DataCache {
    private Map<Integer, BotState> usersBotStates = new HashMap<>();
    private Map<Integer, UserProfileData> usersProfileData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = BotState.SHOW_ERROR;
        return botState;
    }

    public BotState getUsersBotState(int userId) {
        return usersBotStates.get(userId);
    }

    @Override
    public UserProfileData getUserProfileData(int userId) {
        UserProfileData userProfileData = usersProfileData.get(userId);
        if (userProfileData == null) {
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(int userId, UserProfileData inputUserProfileData) {

        if (usersProfileData.get(userId) == null ) {
            usersProfileData.put(userId, inputUserProfileData);
        }
        else {
            UserProfileData userProfileDataNew = usersProfileData.get(userId);
            userProfileDataNew.setProfileChatId(inputUserProfileData.getProfileChatId());
            userProfileDataNew.setUserId(inputUserProfileData.getUserId());
            userProfileDataNew.setUpdateUserTime(inputUserProfileData.getUpdateUserTime());
            userProfileDataNew.setMessageId(inputUserProfileData.getMessageId());
            userProfileDataNew.setAllMessageIdUser(inputUserProfileData.getMessageId());
        }

    }

    public Map<Integer, UserProfileData> getUsersProfileData() {
        return usersProfileData;
    }
}
