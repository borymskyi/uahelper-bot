package ua.home.ua_helper_bot.cache;

import ua.home.ua_helper_bot.botapi.BotState;
import ua.home.ua_helper_bot.model.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    UserProfileData getUserProfileData(int userId);

    void saveUserProfileData(int userId, UserProfileData userProfileData);
}
