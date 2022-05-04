package ru.home.mywizard_bot.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.mywizard_bot.cache.UserDataCache;

/**Обработчик сообщений
 */
public interface InputMessageHandler {
    SendMessage handle(Message message);

    SendMessage handle(CallbackQuery callbackQuery, UserDataCache userDataCache);

    BotState getHandlerName();
}
