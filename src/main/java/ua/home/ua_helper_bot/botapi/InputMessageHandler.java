package ua.home.ua_helper_bot.botapi;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.home.ua_helper_bot.cache.UserDataCache;

/**Обработчик сообщений
 */
public interface InputMessageHandler {
    BotApiMethod<?> handle(Message message);

    BotApiMethod<?> handle(CallbackQuery callbackQuery, UserDataCache userDataCache);

    BotState getHandlerName();
}
