package ua.home.ua_helper_bot;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.home.ua_helper_bot.botapi.TelegramFacade;
import ua.home.ua_helper_bot.service.ReplyMessagesService;
import ua.home.ua_helper_bot.utils.Emojis;

import java.util.List;

public class MyWizardTelegramBot extends TelegramWebhookBot {

    private String webHookPath;
    private String botUserName;
    private String botToken;

    private ReplyMessagesService replyMessagesService;
    private TelegramFacade telegramFacade;

    public MyWizardTelegramBot(TelegramBotsApi botOptions, TelegramFacade telegramFacade) {
        this.telegramFacade = telegramFacade;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        final BotApiMethod<?> replyMessageToUser = telegramFacade.handleUpdate(update);

        return replyMessageToUser;
    }

    // Method that deletes the last message
    public void deleteLastMessage(String chatId, Integer messageIdUser) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageIdUser);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("Stream closed");
    }

    // Method that send last message
    public void sendEndMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Дякуємо за користування чат-ботом.\nЯкщо у вас залишилися питання, скористайтеся командою /start");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
