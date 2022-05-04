package ru.home.mywizard_bot.botapi.handlers.tro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.InputMessageHandler;
import ru.home.mywizard_bot.service.ReplyMessagesService;
import ru.home.mywizard_bot.utils.Emojis;

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
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message message) {
        String chatId = message.getChatId().toString();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.goodLink");
        replyToUser.setReplyMarkup(getInlineMsgButtons());

        return replyToUser;
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
        buttonReturn.setCallbackData("DatabuttonReturn");

        InlineKeyboardButton buttonAtTheBeginning = new InlineKeyboardButton();
        buttonAtTheBeginning.setText(Emojis.BEGINNING + " На початок");
        buttonAtTheBeginning.setCallbackData("DatabuttonAtTheBeginning");

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
        keyboardButtonsRow5.add(buttonAtTheBeginning);

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
