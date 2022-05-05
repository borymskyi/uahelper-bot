package ua.home.ua_helper_bot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.home.ua_helper_bot.cache.UserDataCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines message handlers for each state.
 */
@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    //Message/Command
    public BotApiMethod<?> processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    //Button
    public BotApiMethod<?> processInputCallbackQuery(BotState currentState, CallbackQuery callbackQuery, UserDataCache userDataCache) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(callbackQuery, userDataCache);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isStartProfileState(currentState)) {
            return messageHandlers.get(BotState.SHOW_START);
        }
        else if (isEvacuationProfileState(currentState)) {
            return messageHandlers.get(BotState.SHOW_EVACUATION);
        }
        return messageHandlers.get(currentState);
    }

    private boolean isStartProfileState(BotState currentState) {
        switch (currentState) {
            case SHOW_START:
            case SHOW_START_RETURN:
                return true;
            default:
                return false;
        }
    }

    private boolean isEvacuationProfileState(BotState currentState) {
        switch (currentState) {
            case SHOW_EVACUATION:
            case SHOW_EVACUATION_POST:
            case SHOW_EVACUATION_CITY:
            case SHOW_EVACUATION_ROAD:
                return true;
            default:
                return false;
        }
    }
}





