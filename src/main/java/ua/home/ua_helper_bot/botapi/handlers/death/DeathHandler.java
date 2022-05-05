package ua.home.ua_helper_bot.botapi.handlers.death;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.home.ua_helper_bot.botapi.BotState;
import ua.home.ua_helper_bot.botapi.InputMessageHandler;
import ua.home.ua_helper_bot.cache.UserDataCache;
import ua.home.ua_helper_bot.service.ReplyMessagesService;
import ua.home.ua_helper_bot.utils.Emojis;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DeathHandler implements InputMessageHandler {

    ReplyMessagesService replyMessagesService;

    public DeathHandler(ReplyMessagesService replyMessagesService) {
        this.replyMessagesService = replyMessagesService;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_DEATH;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        return null;
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery, UserDataCache userDataCache) {
        return processUsersInput(callbackQuery, userDataCache);
    }

    //process update (button)
    private BotApiMethod<?> processUsersInput(CallbackQuery callbackQuery, UserDataCache userDataCache) {
        EditMessageText editMessageText = new EditMessageText();

        editMessageText.setChatId((userDataCache.getUserProfileData(callbackQuery.getFrom().getId().intValue()).getProfileChatId()));
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setInlineMessageId(callbackQuery.getInlineMessageId());

        editMessageText.setText(replyMessagesService.getReplyText("reply.death"));
        editMessageText.setReplyMarkup(getInlineMsgButtons());

        return editMessageText;
    }

    //create buttons
    private InlineKeyboardMarkup getInlineMsgButtons() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonReturn = new InlineKeyboardButton();
        buttonReturn.setText(Emojis.RETURN + " Повернутись");
        buttonReturn.setCallbackData("DataButtonReturnDeath");

        List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
        keyboardButtonRow.add(buttonReturn);

        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        buttonRows.add(keyboardButtonRow);

        inlineKeyboardMarkup.setKeyboard(buttonRows);

        return inlineKeyboardMarkup;
    }
}
