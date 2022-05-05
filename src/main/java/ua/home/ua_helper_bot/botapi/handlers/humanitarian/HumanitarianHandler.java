package ua.home.ua_helper_bot.botapi.handlers.humanitarian;

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
public class HumanitarianHandler implements InputMessageHandler {

    private ReplyMessagesService replyMessagesService;

    public HumanitarianHandler(ReplyMessagesService replyMessagesService) {
        this.replyMessagesService = replyMessagesService;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HUMANITARIAN;
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

        editMessageText.setText(replyMessagesService.getReplyText("reply.goodLinkToHumanitarian"));
        editMessageText.setReplyMarkup(getInlineMsgButtons());

        return editMessageText;
    }

    //create buttons
    private InlineKeyboardMarkup getInlineMsgButtons() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonInstructionForRefugrees = new InlineKeyboardButton(); //+1
        buttonInstructionForRefugrees.setText("Інструкція для біженців");
        buttonInstructionForRefugrees.setUrl("https://helsinki.org.ua/articles/instruktsiia-dlia-bizhentsiv-shchodo-poriadku-vyizdu-za-kordon/");
        buttonInstructionForRefugrees.setCallbackData("DataButtonInstructionForRefugrees");

        InlineKeyboardButton buttonSeekingHelp = new InlineKeyboardButton(); //+2
        buttonSeekingHelp.setText("Пошук допомоги");
        buttonSeekingHelp.setUrl("https://t.me/saveua_bot");
        buttonSeekingHelp.setCallbackData("DataButtonSeekingHelp");

        InlineKeyboardButton buttonHandbookOfWartime = new InlineKeyboardButton(); //+3
        buttonHandbookOfWartime.setText("Довідник у часи війни");
        buttonHandbookOfWartime.setUrl("https://t.me/DovidkaInfo_bot");
        buttonHandbookOfWartime.setCallbackData("DataButtonHandbookOfWartime");

        InlineKeyboardButton buttonHelpForCaritasThird = new InlineKeyboardButton(); //+4
        buttonHelpForCaritasThird.setText("Допомога від Caritas");
        buttonHelpForCaritasThird.setUrl("https://caritas.ua/news/merezha-karitasu-dopomagaye-lyudyam-yaki-postrazhdaly-vid-rosijskoyi-agresiyi/");
        buttonHelpForCaritasThird.setCallbackData("DataButtonHelpForCaritasThird");

        InlineKeyboardButton buttonCoordinationCenterInVolynSecond = new InlineKeyboardButton(); //+5
        buttonCoordinationCenterInVolynSecond.setText("Координаційний центр на Волині");
        buttonCoordinationCenterInVolynSecond.setUrl("https://voladm.gov.ua/new/pri-volinskiy-oda-na-period-voyennogo-stanu-diye-koordinaciyniy-centr-volini/");
        buttonCoordinationCenterInVolynSecond.setCallbackData("DataButtonCoordinationCenterInVolynSecond");

        InlineKeyboardButton buttonSupportInUkraineAndAboroad = new InlineKeyboardButton(); //+6
        buttonSupportInUkraineAndAboroad.setText("Підтримка в Україні і за кордоном");
        buttonSupportInUkraineAndAboroad.setUrl("https://ukrainesupport.com.ua/");
        buttonSupportInUkraineAndAboroad.setCallbackData("DataButtonSupportInUkraineAndAboroad");

        InlineKeyboardButton buttonHumanitarianHealpInKyiv = new InlineKeyboardButton(); //+7
        buttonHumanitarianHealpInKyiv.setText("Гуманітарні штаби Києва");
        buttonHumanitarianHealpInKyiv.setUrl("https://cutt.ly/0AjSQya");
        buttonHumanitarianHealpInKyiv.setCallbackData("DataButtonHumanitarianHealpInKyiv");

        InlineKeyboardButton buttonDataAssistanceToRetirees = new InlineKeyboardButton(); //+8
        buttonDataAssistanceToRetirees.setText("Допомога пенсіонерам");
        buttonDataAssistanceToRetirees.setUrl("https://starenki.com.ua/");
        buttonDataAssistanceToRetirees.setCallbackData("DataAssistanceToRetirees");

        InlineKeyboardButton buttonHelpInLviv = new InlineKeyboardButton(); //+9
        buttonHelpInLviv.setText("Допомога у Львові");
        buttonHelpInLviv.setUrl("https://t.me/DopomohtyUALviv?fbclid=IwAR23_Ny3kMdd0xIaM3CQOL_anAI_K7lzOIwVDQpVwOncQdTUMdott2ygtGY");
        buttonHelpInLviv.setCallbackData("DataButtonHelpInLviv");

        InlineKeyboardButton buttonAssistanceInKharkiv = new InlineKeyboardButton(); //+10
        buttonAssistanceInKharkiv.setText("Допомога у Харкові");
        buttonAssistanceInKharkiv.setUrl("https://www.foodforlife.kh.ua/");
        buttonAssistanceInKharkiv.setCallbackData("DataButtonAssistanceInKharkiv");

        InlineKeyboardButton buttonCharitableFoodDistribution = new InlineKeyboardButton(); //+11
        buttonCharitableFoodDistribution.setText("Благодійна роздача їжі");
        buttonCharitableFoodDistribution.setUrl("https://foodforlife.org.ua/");
        buttonCharitableFoodDistribution.setCallbackData("DataButtonCharitableFoodDistribution");

        InlineKeyboardButton buttonAssistanceInKyiv = new InlineKeyboardButton(); //+12
        buttonAssistanceInKyiv.setText("Допомога у Києві");
        buttonAssistanceInKyiv.setUrl("https://potreby.kyivcity.gov.ua/");
        buttonAssistanceInKyiv.setCallbackData("DataButtonAssistanceInKyiv");

        InlineKeyboardButton buttonAssistanceInKyivObl = new InlineKeyboardButton(); //+13
        buttonAssistanceInKyivObl.setText("Допомога у Київській області");
        buttonAssistanceInKyivObl.setUrl("https://www.koda.gov.ua/u-kyyivskij-oblasnij-vijskovij-administracziyi-praczyuye-koll-czentr-dlya-dopomogy/");
        buttonAssistanceInKyivObl.setCallbackData("DataButtonAssistanceInKyivObl");

        InlineKeyboardButton buttonAdministrativeAssistance = new InlineKeyboardButton(); //+14
        buttonAdministrativeAssistance.setText("Адміністративна допомога");
        buttonAdministrativeAssistance.setUrl("https://ukrainesupport.com.ua/ua/administrative-support");
        buttonAdministrativeAssistance.setCallbackData("DataButtonAdministrativeAssistance");

        InlineKeyboardButton buttonOffersOfHelp = new InlineKeyboardButton(); //+15
        buttonOffersOfHelp.setText("Пропозиції допомоги");
        buttonOffersOfHelp.setUrl("https://uahelpers.com/volunteers/search?location=%D0%92%D1%81%D1%8F%20%D0%A3%D0%BA%D1%80%D0%B0%D1%97%D0%BD%D0%B0&category=any");
        buttonOffersOfHelp.setCallbackData("DataButtonOffersOfHelp");

        InlineKeyboardButton buttonRetry = new InlineKeyboardButton(); //+16
        buttonRetry.setText(Emojis.RETURN + " Повернутись");
        buttonRetry.setCallbackData("DataButtonReturnHumanitarian");

        InlineKeyboardButton buttonAtTheStart = new InlineKeyboardButton(); //+17
        buttonAtTheStart.setText(Emojis.START + " На початок");
        buttonAtTheStart.setCallbackData("DataButtonAtTheStartHumanitarian");

        List<InlineKeyboardButton> keyboardButtonRow1 = new ArrayList<>();
        keyboardButtonRow1.add(buttonInstructionForRefugrees);

        List<InlineKeyboardButton> keyboardButtonRow2 = new ArrayList<>();
        keyboardButtonRow2.add(buttonSeekingHelp);

        List<InlineKeyboardButton> keyboardButtonRow3 = new ArrayList<>();
        keyboardButtonRow3.add(buttonHandbookOfWartime);

        List<InlineKeyboardButton> keyboardButtonRow4 = new ArrayList<>();
        keyboardButtonRow4.add(buttonHelpForCaritasThird);

        List<InlineKeyboardButton> keyboardButtonRow5 = new ArrayList<>();
        keyboardButtonRow5.add(buttonCoordinationCenterInVolynSecond);

        List<InlineKeyboardButton> keyboardButtonRow6 = new ArrayList<>();
        keyboardButtonRow6.add(buttonSupportInUkraineAndAboroad);

        List<InlineKeyboardButton> keyboardButtonRow7 = new ArrayList<>();
        keyboardButtonRow7.add(buttonHumanitarianHealpInKyiv);

        List<InlineKeyboardButton> keyboardButtonRow8 = new ArrayList<>();
        keyboardButtonRow8.add(buttonDataAssistanceToRetirees);

        List<InlineKeyboardButton> keyboardButtonRow9 = new ArrayList<>();
        keyboardButtonRow9.add(buttonHelpInLviv);

        List<InlineKeyboardButton> keyboardButtonRow10 = new ArrayList<>();
        keyboardButtonRow10.add(buttonAssistanceInKharkiv);

        List<InlineKeyboardButton> keyboardButtonRow11 = new ArrayList<>();
        keyboardButtonRow11.add(buttonCharitableFoodDistribution);

        List<InlineKeyboardButton> keyboardButtonRow12 = new ArrayList<>();
        keyboardButtonRow12.add(buttonAssistanceInKyiv);

        List<InlineKeyboardButton> keyboardButtonRow13 = new ArrayList<>();
        keyboardButtonRow13.add(buttonAssistanceInKyivObl);

        List<InlineKeyboardButton> keyboardButtonRow14 = new ArrayList<>();
        keyboardButtonRow14.add(buttonAdministrativeAssistance);

        List<InlineKeyboardButton> keyboardButtonRow15 = new ArrayList<>();
        keyboardButtonRow15.add(buttonOffersOfHelp);

        List<InlineKeyboardButton> keyboardButtonRow16 = new ArrayList<>();
        keyboardButtonRow16.add(buttonRetry);
        keyboardButtonRow16.add(buttonAtTheStart);

        List<List<InlineKeyboardButton>> rowsList = new ArrayList<>();
        rowsList.add(keyboardButtonRow1);
        rowsList.add(keyboardButtonRow2);
        rowsList.add(keyboardButtonRow3);
        rowsList.add(keyboardButtonRow4);
        rowsList.add(keyboardButtonRow5);
        rowsList.add(keyboardButtonRow6);
        rowsList.add(keyboardButtonRow7);
        rowsList.add(keyboardButtonRow8);
        rowsList.add(keyboardButtonRow9);
        rowsList.add(keyboardButtonRow10);
        rowsList.add(keyboardButtonRow11);
        rowsList.add(keyboardButtonRow12);
        rowsList.add(keyboardButtonRow13);
        rowsList.add(keyboardButtonRow14);
        rowsList.add(keyboardButtonRow15);
        rowsList.add(keyboardButtonRow16);

        inlineKeyboardMarkup.setKeyboard(rowsList);

        return inlineKeyboardMarkup;
    }
}
