package ru.home.mywizard_bot.botapi.handlers.start;

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
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String chatId = inputMsg.getChatId().toString();

        String startMessage = messagesService.getReplyText("reply.start", Emojis.SHAKING_HANDS);
        String startCategory = messagesService.getReplyText("reply.category");
        SendMessage replyToUser = new SendMessage(chatId, String.format("%s%n%n%s", startMessage, startCategory));
        replyToUser.setReplyMarkup(getInlineMsgButtons());

        return replyToUser;
    }

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
