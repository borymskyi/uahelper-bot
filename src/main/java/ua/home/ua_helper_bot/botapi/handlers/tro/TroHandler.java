package ua.home.ua_helper_bot.botapi.handlers.tro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.EditChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageAutoDeleteTimerChanged;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.home.ua_helper_bot.botapi.BotState;
import ua.home.ua_helper_bot.botapi.InputMessageHandler;
import ua.home.ua_helper_bot.cache.UserDataCache;
import ua.home.ua_helper_bot.service.ReplyMessagesService;
import ua.home.ua_helper_bot.utils.Emojis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TroHandler implements InputMessageHandler {

    private ReplyMessagesService messagesService;

    public TroHandler(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_TRO;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        return null;
    }

    public BotApiMethod<?> handle(CallbackQuery callbackQuery, UserDataCache userDataCache) {
        return processUsersInput(callbackQuery, userDataCache);
    }

    private BotApiMethod<?> processUsersInput(CallbackQuery callbackQuery, UserDataCache userDataCache) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId((userDataCache.getUserProfileData(callbackQuery.getFrom().getId().intValue()).getProfileChatId()));
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setInlineMessageId(callbackQuery.getInlineMessageId());
        editMessageText.setText(messagesService.getReplyText("reply.goodLink"));
        editMessageText.setReplyMarkup(getInlineMsgButtons());
        return editMessageText;
    }

    private InlineKeyboardMarkup getInlineMsgButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonInstruction = new InlineKeyboardButton();
        buttonInstruction.setText("Як вступити до ТрО");
        buttonInstruction.setCallbackData("DatabuttonInstruction");
        buttonInstruction.setUrl("https://t.me/Hotovyi_do_vsioho_bot");

        InlineKeyboardButton buttonDirectoryWar = new InlineKeyboardButton();
        buttonDirectoryWar.setText("Довідник у час війни");
        buttonDirectoryWar.setCallbackData("DatabuttonDirectoryWar");
        buttonDirectoryWar.setUrl("https://dovidka.info/");

        InlineKeyboardButton buttonInformationTrO = new InlineKeyboardButton();
        buttonInformationTrO.setText("Інформація для ТрО");
        buttonInformationTrO.setCallbackData("DatabuttonInformationTrO");
        buttonInformationTrO.setUrl("https://viyna.net/teritorialna-oborona");

        InlineKeyboardButton buttonContactsTrOandPolice = new InlineKeyboardButton();
        buttonContactsTrOandPolice.setText("Контакти ТрО і поліції");
        buttonContactsTrOandPolice.setCallbackData("DatabuttonContactsTrOandPolice");
        buttonContactsTrOandPolice.setUrl("https://prjctr.notion.site/a811db34b55f4855bdf6521d485d0194");

        InlineKeyboardButton buttonReturn = new InlineKeyboardButton();
        buttonReturn.setText(Emojis.RETURN + " Повернутись");
        buttonReturn.setCallbackData("DatabuttonReturn_TRO");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonInstruction);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonDirectoryWar);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(buttonInformationTrO);

        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();
        keyboardButtonsRow4.add(buttonContactsTrOandPolice);

        List<InlineKeyboardButton> keyboardButtonsRow5 = new ArrayList<>();
        keyboardButtonsRow5.add(buttonReturn);

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
