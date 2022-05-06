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

    public void deleteLastMessage(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        try {
            execute(deleteMessage);
        }catch(TelegramApiException tae) {
            throw new RuntimeException(tae);
        }
    }

    public void sendEndMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Дякуємо за користування чат-ботом.\n Якщо у вас залишилися питання, скористайтеся командою /start");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /*
        Каждые 5 минут пинговать этот метод:

        Деконструкция:
        - каждые 5 минут пинговать метод.

        - каждые 5 минут пинговать метод.
        - в методе проверяется unix время прихода апдейта чата айди.
        - в методе сверяется с unix текущим времинем.
        - если это время больше чем 5 минут:
        = екзекют (удалить паследнее сообщение).
        = укзекют (выслать новое сообщение).

        До метода сохраняем в userDataCache юзера:
        - String chatId;
        - int userId;
        - long unixTimeUserUpdate (время unix время прихода апдейта)
        - Integer messageId
        - String inlineMessageId

        //каждые 5 минут пингуем метод
        void Метод(UserDataCache userDataCache) {
            long date_now (unix время которое сейчас).

            forEach метод {
            - обходим Весь Map<Integer(userId), UserProfileData(обьект с данными)> usersProfileData = new HashMap<>();
            - проверяем: unixTimeUserUpdate
            - long endTime = unixTimeUserUpdate + 5 min;
            - if ( endTime < date_now ) {
                execute(methodDeleteMessage((i).userId, (i).chatId, (i).messageId, (.)inlineMessageId)
                execute(methodEndMessage(i).userId, (i).chatId, (i).messageId, (.)inlineMessageId))
            }
        }
    */
}
