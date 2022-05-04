package ru.home.mywizard_bot.botapi.handlers.evacuation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.InputMessageHandler;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.service.ReplyMessagesService;
import ru.home.mywizard_bot.utils.Emojis;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class EvacuationHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public EvacuationHandler(UserDataCache userDataCache,
                             ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId().intValue()).equals(BotState.SHOW_EVACUATION)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId().intValue(), BotState.SHOW_EVACUATION_POST);
        }
        return processUsersInput(message);
    }

    @Override
    public SendMessage handle(CallbackQuery callbackQuery, UserDataCache userDataCache) {
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_EVACUATION;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        Integer userId = inputMsg.getFrom().getId().intValue();
        String chatId = inputMsg.getChatId().toString();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.SHOW_EVACUATION_POST)) {
            String startMessage = messagesService.getReplyText("reply.start", Emojis.SHAKING_HANDS);
            String startCategory = messagesService.getReplyText("reply.category");
            replyToUser = new SendMessage(chatId, String.format("%s%n%n%s", startMessage, startCategory));
            replyToUser.setReplyMarkup(getStartButtonsMarkup());
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }

    private InlineKeyboardMarkup getStartButtonsMarkup() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> buttons = new ArrayList<InlineKeyboardButton>();

        InlineKeyboardButton buttonTrO = new InlineKeyboardButton();
        buttonTrO.setText("Доброволець ТрО" /*+ Emojis.MUSCLE*/);
        InlineKeyboardButton buttonDeath = new InlineKeyboardButton();
        buttonDeath.setText("Член родини загиблого");
        InlineKeyboardButton buttonEvacuation = new InlineKeyboardButton();
        buttonEvacuation.setText("Пам'ятка з евакуації");
        InlineKeyboardButton buttonHumanitarian = new InlineKeyboardButton();
        buttonHumanitarian.setText("Гуманітарна допомога");
        InlineKeyboardButton buttonPsychological = new InlineKeyboardButton();
        buttonPsychological.setText("Психологічна допомога");
        buttonPsychological.setUrl("https://t.me/LisovaPolianaBot?start");

        buttons.add(buttonTrO);
        buttons.add(buttonDeath);
        buttons.add(buttonEvacuation);
        buttons.add(buttonHumanitarian);
        buttons.add(buttonPsychological);

        keyboard.add(buttons);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }
}