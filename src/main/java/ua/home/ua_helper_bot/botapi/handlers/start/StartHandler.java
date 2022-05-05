package ua.home.ua_helper_bot.botapi.handlers.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
public class StartHandler implements InputMessageHandler {

    private ReplyMessagesService messagesService;

    public StartHandler(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_START;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery, UserDataCache userDataCache) {
        return processUsersInput(callbackQuery, userDataCache);
    }

    //process message (command)
    private BotApiMethod<?> processUsersInput(Message inputMsg) {
        String chatId = inputMsg.getChatId().toString();

        String startMessage = messagesService.getReplyText("reply.start", Emojis.SHAKING_HANDS);
        String startCategory = messagesService.getReplyText("reply.category");
        SendMessage replyToUser = new SendMessage(chatId, String.format("%s%n%n%s", startMessage, startCategory));
        replyToUser.setReplyMarkup(getInlineMsgButtons());

        return replyToUser;
    }

    //process update (button)
    private BotApiMethod<?> processUsersInput(CallbackQuery callbackQuery, UserDataCache userDataCache) {
        EditMessageText editMessageText = new EditMessageText();

        editMessageText.setChatId((userDataCache.getUserProfileData(callbackQuery.getFrom().getId().intValue()).getProfileChatId()));
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setInlineMessageId(callbackQuery.getInlineMessageId());

        editMessageText.setText(messagesService.getReplyText("reply.startBack", Emojis.SHAKING_HANDS));
        editMessageText.setReplyMarkup(getInlineMsgButtons());

        return editMessageText;
    }

    //create buttons
    private InlineKeyboardMarkup getInlineMsgButtons() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonTrO = new InlineKeyboardButton();
        buttonTrO.setText("Доброволець ТрО " + Emojis.MUSCLE);
        buttonTrO.setCallbackData("DataButtonTrO");

        InlineKeyboardButton buttonDeath = new InlineKeyboardButton();
        buttonDeath.setText("Член родини загиблого " + Emojis.DEATH);
        buttonDeath.setCallbackData("DataButtonDeath");

        InlineKeyboardButton buttonEvacuation = new InlineKeyboardButton();
        buttonEvacuation.setText("Пам'ятка з евакуації " + Emojis.CAR);
        buttonEvacuation.setCallbackData("DataButtonEvacuation");

        InlineKeyboardButton buttonHumanitarian = new InlineKeyboardButton();
        buttonHumanitarian.setText("Гуманітарна допомога " + Emojis.HUMANITARIAN);
        buttonHumanitarian.setCallbackData("DataButtonHumanitarian");

        InlineKeyboardButton buttonPsychological = new InlineKeyboardButton();
        buttonPsychological.setText("Психологічна допомога " + Emojis.PSYCHOLOGICAL);
        buttonPsychological.setUrl("https://t.me/LisovaPolianaBot?start");
        buttonPsychological.setCallbackData("DataButtonPsychological");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonTrO);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonDeath);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(buttonEvacuation);

        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();
        keyboardButtonsRow4.add(buttonHumanitarian);

        List<InlineKeyboardButton> keyboardButtonsRow5 = new ArrayList<>();
        keyboardButtonsRow5.add(buttonPsychological);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);
        rowList.add(keyboardButtonsRow5);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
