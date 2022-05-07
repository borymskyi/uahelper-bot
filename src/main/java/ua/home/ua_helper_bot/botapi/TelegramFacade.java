package ua.home.ua_helper_bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.home.ua_helper_bot.MyWizardTelegramBot;
import ua.home.ua_helper_bot.model.UserProfileData;
import ua.home.ua_helper_bot.cache.UserDataCache;
import ua.home.ua_helper_bot.service.ReplyMessagesService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    //Catch an update from the user
    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> replyMessage = null;
        Message message = null;
        CallbackQuery callbackQuery = null;
        UserProfileData user = new UserProfileData();

        //Update Button
        if (update.hasCallbackQuery()) {
            //save UserData
            user.setProfileChatId(update.getCallbackQuery().getMessage().getChatId().toString());
            user.setUserId(update.getCallbackQuery().getFrom().getId().intValue());
            user.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            user.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
            userDataCache.saveUserProfileData(user.getUserId(), user);

            callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}",
                    callbackQuery.getFrom().getUserName(),
                    callbackQuery.getFrom().getId(),
                    callbackQuery.getData());

            //process update
            replyMessage = handleInputCallbackQuery(userDataCache, callbackQuery);
        }

        //Update message/command
        if (update.getMessage() != null && update.getMessage().hasText()) {
            //save UserData
            user.setProfileChatId(update.getMessage().getChatId().toString());
            user.setUserId(update.getMessage().getFrom().getId().intValue());
            user.setMessageId(update.getMessage().getMessageId());
            userDataCache.saveUserProfileData(user.getUserId(), user);

            message = update.getMessage();
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(),
                    message.getFrom().getId(),
                    message.getChatId(),
                    message.getText());

            //process update
            replyMessage = handleInputMessage(userDataCache, message);
        }

        //save UserData
        user.setUpdateUserTime(LocalDateTime.now());
        userDataCache.saveUserProfileData(user.getUserId(), user);

        return replyMessage;
    }

    //Processing update (message/command)
    private BotApiMethod<?> handleInputMessage(UserDataCache userDataCache, Message message) {
        BotApiMethod<?> replyMessage;

        String inputText = message.getText();
        int userId = message.getFrom().getId().intValue();
        BotState botState;

        //Проверка message / команды
        switch (inputText) {
            case "/start":
                botState = BotState.SHOW_START;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        //save BotState
        userDataCache.setUsersCurrentBotState(userId, botState);

        //next processing update message(command)
        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    //Processing update (button)
    private BotApiMethod<?> handleInputCallbackQuery(UserDataCache userDataCache, CallbackQuery callbackQuery) {
        BotApiMethod<?> callBackAnswer;

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
                botState = BotState.SHOW_START_RETURN;
                break;
            //DeathHandler
            case "DataButtonReturnDeath":
                botState = BotState.SHOW_START_RETURN;
                break;
            //EvacuationHandler
            case "DataButtonLeaveTheCity":
                botState = BotState.SHOW_EVACUATION_CITY;
                break;
            case "DataButtonWhereToStay":
                botState = BotState.SHOW_EVACUATION_ROAD;
                break;
            case "DataButtonReturnEvacuationFirst":
                botState = BotState.SHOW_START_RETURN;
                break;
            case "DataButtonReturnEvacuationSecond":
                botState = BotState.SHOW_EVACUATION;
                break;
            case "DataButtonAtTheStartEvacuationSecond":
                botState = BotState.SHOW_START_RETURN;
                break;
            case "DataButtonReturnEvacuationThird":
                botState = BotState.SHOW_EVACUATION;
                break;
            case "DataButtonAtTheStartEvacuationThird":
                botState = BotState.SHOW_START_RETURN;
                break;
            //HumanitarianHandler
            case "DataButtonReturnHumanitarian":
                botState = BotState.SHOW_START_RETURN;
                break;
            case "DataButtonAtTheStartHumanitarian":
                botState = BotState.SHOW_START_RETURN;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        //save BotState
        userDataCache.setUsersCurrentBotState(userId, botState);

        //next processing update (button)
        callBackAnswer = botStateContext.processInputCallbackQuery(botState, callbackQuery, userDataCache);

        return callBackAnswer;
    }

    @Scheduled(fixedRateString = "${pingTaskMethod.period}")
    public void deleteMessage() {
        System.out.println("▄■▀■▄■▀■▄ User activity ▄■▀■▄■▀■▄");

        LocalDateTime timeNow = LocalDateTime.now();

        Map<Integer, UserProfileData> usersProfileData = new HashMap<>(userDataCache.getUsersProfileData());
        for(Map.Entry<Integer, UserProfileData> item : usersProfileData.entrySet()){
            System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue());

            LocalDateTime timeUser = item.getValue().getUpdateUserTime();
            LocalDateTime timeEnd = timeUser.plusMinutes(4);

            if (timeNow.isAfter(timeUser) && timeNow.isAfter(timeEnd)) {
                myWizardBot.deleteLastMessage(item.getValue().getProfileChatId(), item.getValue().getMessageId());
                myWizardBot.sendEndMessage(item.getValue().getProfileChatId());
                userDataCache.getUsersProfileData().remove(item.getKey());
                System.out.println("Stream closed");
                System.out.println();
            } else {
                System.out.println("Stream active");
                System.out.println();
            }

        }
    }
}
