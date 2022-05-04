package ua.home.ua_helper_bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.home.ua_helper_bot.MyWizardTelegramBot;
import ua.home.ua_helper_bot.model.UserProfileData;
import ua.home.ua_helper_bot.cache.UserDataCache;
import ua.home.ua_helper_bot.service.ReplyMessagesService;

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
        update.hasCallbackQuery();
        Message message = null;
        CallbackQuery callbackQuery = null;
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}",
                    update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(),
                    update.getCallbackQuery().getData());
            replyMessage = handleInputCallbackQuery(userDataCache, callbackQuery);
        }

        if (update.getMessage() != null && update.getMessage().hasText()) {
            //Save UserData
            UserProfileData user = new UserProfileData();
            user.setProfileChatId(update.getMessage().getChatId().toString());
            userDataCache.saveUserProfileData(update.getMessage().getFrom().getId().intValue(), user);

            message = update.getMessage();
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(),
                    message.getFrom().getId(),
                    message.getChatId(),
                    message.getText());
            replyMessage = handleInputMessage(userDataCache, message);
        }

        return replyMessage;
    }

    //Processing an incoming Message
    private SendMessage handleInputMessage(UserDataCache userDataCache, Message message) {
        SendMessage replyMessage;

        String inputMsg = message.getText();
        int userId = message.getFrom().getId().intValue();
        BotState botState;

        //Проверка message / команды
        switch (inputMsg) {
            case "/start":
                botState = BotState.SHOW_START;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        //save BotState
        userDataCache.setUsersCurrentBotState(userId, botState);

        //обробатываю входящий Message.
        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    //Processing an incoming CallbackQuery
    private SendMessage handleInputCallbackQuery(UserDataCache userDataCache, CallbackQuery callbackQuery) {
        SendMessage callBackAnswer;

        String inputCallbackQuery = callbackQuery.getData();
        int userId = callbackQuery.getFrom().getId().intValue();
        BotState botState;

        switch (inputCallbackQuery) {
            //StartHandler
            case "DataButtonTrO":
                botState = BotState.SHOW_TRO;
                break;
            case "DataButtonDeath":
                botState = BotState.SHOW_DEATH;
                break;
            case "DataButtonEvacuation":
                botState = BotState.SHOW_EVACUATION;
                break;
            case "DataButtonHumanitarian":
                botState = BotState.SHOW_HUMANITARIAN;
                break;
            //TroHandler
            case "DatabuttonReturn_TRO":
                botState = BotState.SHOW_START;
                break;
            case "DatabuttonAtTheBeginning_TRO":
                botState = BotState.SHOW_START;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        //save BotState
        userDataCache.setUsersCurrentBotState(userId, botState);

        //обробатываю входящий CallbackQuery.
        callBackAnswer = botStateContext.processInputCallbackQuery(botState, callbackQuery, userDataCache);

        return callBackAnswer;
    }
}
