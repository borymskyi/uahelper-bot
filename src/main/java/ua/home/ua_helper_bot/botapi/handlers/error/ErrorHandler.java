package ua.home.ua_helper_bot.botapi.handlers.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.home.ua_helper_bot.botapi.BotState;
import ua.home.ua_helper_bot.botapi.InputMessageHandler;
import ua.home.ua_helper_bot.cache.UserDataCache;
import ua.home.ua_helper_bot.service.ReplyMessagesService;
import ua.home.ua_helper_bot.utils.Emojis;

@Slf4j
@Component
public class ErrorHandler implements InputMessageHandler {

    private ReplyMessagesService replyMessagesService;

    public ErrorHandler(ReplyMessagesService replyMessagesService) {
        this.replyMessagesService = replyMessagesService;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_ERROR;
    }

    //process message(error)
    @Override
    public BotApiMethod<?> handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery, UserDataCache userDataCache) {
        return processUsersInput(callbackQuery);
    }

    //process message(error)
    private BotApiMethod<?> processUsersInput(Message inputMsg) {
        String chatId = inputMsg.getChatId().toString();

        SendMessage replyToUser = new SendMessage(chatId, replyMessagesService.getReplyText("reply.errorMessage", Emojis.ROBOT));

        return replyToUser;
    }

    //process callbackQuery(error)
    private BotApiMethod<?> processUsersInput(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();

        SendMessage replyToUser = new SendMessage(chatId, replyMessagesService.getReplyText("reply.errorMessage", Emojis.ROBOT));

        return replyToUser;
    }
}
