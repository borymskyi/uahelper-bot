package ru.home.mywizard_bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.mywizard_bot.MyWizardTelegramBot;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.service.ReplyMessagesService;

@Component
@Slf4j
public class TelegramFacade {

    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MyWizardTelegramBot myWizardBot;
    private ReplyMessagesService messagesService;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache,
                          @Lazy MyWizardTelegramBot myWizardBot, ReplyMessagesService messagesService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.myWizardBot = myWizardBot;
        this.messagesService = messagesService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        Message message = null;
        CallbackQuery callbackQuery = null;
        SendMessage replyMessage = null;

        //Save UserData
        UserProfileData user = new UserProfileData();
        user.setProfileChatId(update.getMessage().getChatId().toString());
        user.setProfileUserId(update.getMessage().getFrom().getId().intValue());
        userDataCache.saveUserProfileData(update.getMessage().getFrom().getId().intValue(), user);

        if (update.hasCallbackQuery()) {
            callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}",
                    update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(),
                    update.getCallbackQuery().getData());
            replyMessage = handleInputMessage(userDataCache, message, callbackQuery);
        }

        if (update.getMessage() != null && update.getMessage().hasText()) {
            message = update.getMessage();
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(),
                    message.getFrom().getId(),
                    message.getChatId(),
                    message.getText());
            replyMessage = handleInputMessage(userDataCache, message, callbackQuery);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(UserDataCache userDataCache, Message message, CallbackQuery callbackQuery) {
        String inputMsg = message.getText();
        UserDataCache
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.SHOW_START;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        // установили / сохранили состояние(botstate) конкретного(user_id) юзера.
        userDataCache.setUsersCurrentBotState(userId, botState);

        // обробатываю входящее сообщение /
        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        String inputButtonData = buttonQuery.getData();
        int userId = buttonQuery.getFrom().getId().intValue();
        BotState botState;
        BotApiMethod<?> callBackAnswer;

        if (inputButtonData.equals("DataButtonTrO")) {
            botState = BotState.SHOW_TRO;
        }
        else {
            botState = userDataCache.getUsersCurrentBotState(userId);
            /*callBackAnswer = sendAnswerCallbackQuery("Данная кнопка не поддерживается", true, buttonQuery);*/
        }

        // установили / сохранили состояние(botstate) конкретного(user_id) юзера.
        userDataCache.setUsersCurrentBotState(userId, botState);
        userDataCache.saveUserProfileData(userId, );

        callBackAnswer = botStateContext.processInputMessage(botState);

        return callBackAnswer;
    }

    /*private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }*/
}
