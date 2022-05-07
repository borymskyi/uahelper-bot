package ua.home.ua_helper_bot.botapi.handlers.evacuation;

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
public class EvacuationHandler implements InputMessageHandler {

    private ReplyMessagesService replyMessagesService;

    public EvacuationHandler(ReplyMessagesService replyMessagesService) {
        this.replyMessagesService = replyMessagesService;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_EVACUATION;
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

        if (userDataCache.getUsersBotState(callbackQuery.getFrom().getId().intValue()).equals(BotState.SHOW_EVACUATION)) {
            editMessageText.setText(replyMessagesService.getReplyText("reply.category"));
            editMessageText.setReplyMarkup(getEvacuationFirstInlineKeyboardButtons());
        }
        else if ((userDataCache.getUsersBotState(callbackQuery.getFrom().getId().intValue()).equals(BotState.SHOW_EVACUATION_CITY))) {
            editMessageText.setText(replyMessagesService.getReplyText("reply.goodLinkForLeaveCity"));
            editMessageText.setReplyMarkup(getEvacuationSecondInlineKeyboardButtons());
        }
        else if ((userDataCache.getUsersBotState(callbackQuery.getFrom().getId().intValue()).equals(BotState.SHOW_EVACUATION_ROAD))) {
            editMessageText.setText(replyMessagesService.getReplyText("reply.goodLinkToFindHousing"));
            editMessageText.setReplyMarkup(getEvacuationThirdInlineKeyboardButtons());
        }

        return editMessageText;
    }

    //Памятка з евакуації
    private InlineKeyboardMarkup getEvacuationFirstInlineKeyboardButtons() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonLeaveTheCity = new InlineKeyboardButton();
        buttonLeaveTheCity.setText("Як мені виїхати з міста " + Emojis.BUS);
        buttonLeaveTheCity.setCallbackData("DataButtonLeaveTheCity");

        InlineKeyboardButton buttonWhereToStay = new InlineKeyboardButton();
        buttonWhereToStay.setText("Де зупинитись по дорозі евакуації " + Emojis.HOUSE);
        buttonWhereToStay.setCallbackData("DataButtonWhereToStay");

        InlineKeyboardButton buttonReturn = new InlineKeyboardButton();
        buttonReturn.setText(Emojis.RETURN + " Повернутись");
        buttonReturn.setCallbackData("DataButtonReturnEvacuationFirst");

        List<InlineKeyboardButton> keyboardButtonRow1 = new ArrayList<>();
        keyboardButtonRow1.add(buttonLeaveTheCity);

        List<InlineKeyboardButton> keyboardButtonRow2 = new ArrayList<>();
        keyboardButtonRow2.add(buttonWhereToStay);

        List<InlineKeyboardButton> keyboardButtonRow3 = new ArrayList<>();
        keyboardButtonRow3.add(buttonReturn);

        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        buttonRows.add(keyboardButtonRow1);
        buttonRows.add(keyboardButtonRow2);
        buttonRows.add(keyboardButtonRow3);

        inlineKeyboardMarkup.setKeyboard(buttonRows);

        return inlineKeyboardMarkup;
    }

    //Як мені виїхати з міста
    private InlineKeyboardMarkup getEvacuationSecondInlineKeyboardButtons() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonLogisticCenter = new InlineKeyboardButton();
        buttonLogisticCenter.setText("Логістичний центр");
        buttonLogisticCenter.setUrl("https://pomich.org/shippers");
        buttonLogisticCenter.setCallbackData("DataButtonLogisticCenter");

        InlineKeyboardButton buttonCarsFree = new InlineKeyboardButton();
        buttonCarsFree.setText("Авто з вільними місцями");
        buttonCarsFree.setUrl("https://t.me/nampodorozi_bot");
        buttonCarsFree.setCallbackData("DataButtonCarsFree");

        InlineKeyboardButton buttonSearchForTransport = new InlineKeyboardButton();
        buttonSearchForTransport.setText("Пошук транспорту");
        buttonSearchForTransport.setUrl("https://www.ukrainenow.org/refuge#need-transfer");
        buttonSearchForTransport.setCallbackData("DataButtonSearchForTransport");

        InlineKeyboardButton buttonWebOfCarriers = new InlineKeyboardButton();
        buttonWebOfCarriers.setText("Сайт перевізників");
        buttonWebOfCarriers.setUrl("https://sharry.cc/");
        buttonWebOfCarriers.setCallbackData("DataButtonWebOfCarriers");

        InlineKeyboardButton buttonMonitoringOfQueues = new InlineKeyboardButton();
        buttonMonitoringOfQueues.setText("Моніторинг черг на кордонах");
        buttonMonitoringOfQueues.setUrl("https://docs.google.com/spreadsheets/d/e/2PACX-1vTmKNAxZn2cPpBqPHnRx9Hc_GP" +
                                        "zfi7U92h05hkNuES6pA8l7IcbfdRELMkTBWGcBFoRkUdwlnfX889X/pubhtml?gid=0&single" +
                                        "=true&fbclid=IwAR1qTqmfhHYDYFB_N14kCXweiK3BWVGzSfIYlpnD1UhYU33c-Tm0LJQYSuw");
        buttonMonitoringOfQueues.setCallbackData("DataButtonMonitoringOfQueues");

        InlineKeyboardButton buttonUkrz = new InlineKeyboardButton();
        buttonUkrz.setText("Укрзалізниця");
        buttonUkrz.setUrl("https://t.me/UkrzalInfo");
        buttonUkrz.setCallbackData("DataButtonUkrz");

        InlineKeyboardButton buttonGuideToUkraine = new InlineKeyboardButton();
        buttonGuideToUkraine.setText("Путівник Україною");
        buttonGuideToUkraine.setUrl("https://t.me/help_onroad_ua_bot");
        buttonGuideToUkraine.setCallbackData("DataButtonGuideToUkraine");

        InlineKeyboardButton buttonGreenHallway = new InlineKeyboardButton();
        buttonGreenHallway.setText("Зелений коридор");
        buttonGreenHallway.setUrl("https://t.me/zeleniy_coridor_bot");
        buttonGreenHallway.setCallbackData("DataButtonGreenHallway");

        InlineKeyboardButton buttonHelpFromCaritas = new InlineKeyboardButton();
        buttonHelpFromCaritas.setText("Допомога від Caritas");
        buttonHelpFromCaritas.setUrl("https://caritas.ua/news/merezha-karitasu-dopomagaye-lyudyam-yaki-postrazhdaly-vid-rosijskoyi-agresiyi/");
        buttonHelpFromCaritas.setCallbackData("DataButtonHelpFromCaritas");

        InlineKeyboardButton buttonHelpByCountry = new InlineKeyboardButton();
        buttonHelpByCountry.setText("Довідка по країнах");
        buttonHelpByCountry.setUrl("https://allhelpua.notion.site/30c476ad371347e1a14786f96000e270");
        buttonHelpByCountry.setCallbackData("DataButtonHelpByCountry");

        InlineKeyboardButton buttonMutualAssistance = new InlineKeyboardButton();
        buttonMutualAssistance.setText("Взаємодопомога");
        buttonMutualAssistance.setUrl("https://helplist.io/#filter/0/0/all");
        buttonMutualAssistance.setCallbackData("DataButtonMutualAssistance");

        InlineKeyboardButton buttonEvacuation = new InlineKeyboardButton();
        buttonEvacuation.setText("Евакуація");
        buttonEvacuation.setUrl("https://viyna.net/czentri-dopomogi-bizhenczyam");
        buttonEvacuation.setCallbackData("DataButtonEvacuation");

        InlineKeyboardButton buttonRetry = new InlineKeyboardButton();
        buttonRetry.setText(Emojis.RETURN + " Повернутись");
        buttonRetry.setCallbackData("DataButtonReturnEvacuationSecond");

        InlineKeyboardButton buttonAtTheStart = new InlineKeyboardButton();
        buttonAtTheStart.setText(Emojis.START + " На початок");
        buttonAtTheStart.setCallbackData("DataButtonAtTheStartEvacuationSecond");

        List<InlineKeyboardButton> keyboardButtonRow1 = new ArrayList<>();
        keyboardButtonRow1.add(buttonLogisticCenter);

        List<InlineKeyboardButton> keyboardButtonRow2 = new ArrayList<>();
        keyboardButtonRow2.add(buttonCarsFree);

        List<InlineKeyboardButton> keyboardButtonRow3 = new ArrayList<>();
        keyboardButtonRow3.add(buttonSearchForTransport);

        List<InlineKeyboardButton> keyboardButtonRow4 = new ArrayList<>();
        keyboardButtonRow4.add(buttonWebOfCarriers);

        List<InlineKeyboardButton> keyboardButtonRow5 = new ArrayList<>();
        keyboardButtonRow5.add(buttonMonitoringOfQueues);

        List<InlineKeyboardButton> keyboardButtonRow6 = new ArrayList<>();
        keyboardButtonRow6.add(buttonUkrz);

        List<InlineKeyboardButton> keyboardButtonRow7 = new ArrayList<>();
        keyboardButtonRow7.add(buttonGuideToUkraine);

        List<InlineKeyboardButton> keyboardButtonRow8 = new ArrayList<>();
        keyboardButtonRow8.add(buttonGreenHallway);

        List<InlineKeyboardButton> keyboardButtonRow9 = new ArrayList<>();
        keyboardButtonRow9.add(buttonHelpFromCaritas);

        List<InlineKeyboardButton> keyboardButtonRow10 = new ArrayList<>();
        keyboardButtonRow10.add(buttonHelpByCountry);

        List<InlineKeyboardButton> keyboardButtonRow11 = new ArrayList<>();
        keyboardButtonRow11.add(buttonMutualAssistance);

        List<InlineKeyboardButton> keyboardButtonRow12 = new ArrayList<>();
        keyboardButtonRow12.add(buttonEvacuation);

        List<InlineKeyboardButton> keyboardButtonRow13 = new ArrayList<>();
        keyboardButtonRow13.add(buttonRetry);
        keyboardButtonRow13.add(buttonAtTheStart);


        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        buttonRows.add(keyboardButtonRow1);
        buttonRows.add(keyboardButtonRow2);
        buttonRows.add(keyboardButtonRow3);
        buttonRows.add(keyboardButtonRow4);
        buttonRows.add(keyboardButtonRow5);
        buttonRows.add(keyboardButtonRow6);
        buttonRows.add(keyboardButtonRow7);
        buttonRows.add(keyboardButtonRow8);
        buttonRows.add(keyboardButtonRow9);
        buttonRows.add(keyboardButtonRow10);
        buttonRows.add(keyboardButtonRow11);
        buttonRows.add(keyboardButtonRow12);
        buttonRows.add(keyboardButtonRow13);

        inlineKeyboardMarkup.setKeyboard(buttonRows);

        return inlineKeyboardMarkup;
    }

    //Де зупинитись по дорозі евакуації
    private InlineKeyboardMarkup getEvacuationThirdInlineKeyboardButtons() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonSearchForHousingInTheWorld = new InlineKeyboardButton(); //+
        buttonSearchForHousingInTheWorld.setText("Пошук житла у світі");
        buttonSearchForHousingInTheWorld.setUrl("https://icanhelp.host/");
        buttonSearchForHousingInTheWorld.setCallbackData("DataButtonSearchForHousingInTheWorld");

        InlineKeyboardButton buttonHousingInLvivAndCarpathi = new InlineKeyboardButton(); //+2
        buttonHousingInLvivAndCarpathi.setText("Житло у Львівській області і в Карпатах");
        buttonHousingInLvivAndCarpathi.setUrl("https://booking.karpaty.ua/uk");
        buttonHousingInLvivAndCarpathi.setCallbackData("DataButtonHousingInLvivAndCarpathi");

        InlineKeyboardButton buttonCarpathiInfo = new InlineKeyboardButton(); //+3
        buttonCarpathiInfo.setText("Карпати Info");
        buttonCarpathiInfo.setUrl("https://www.karpaty.info/ua/");
        buttonCarpathiInfo.setCallbackData("DataButtonCarpathiInfo");

        InlineKeyboardButton buttonShelterInTheWorld = new InlineKeyboardButton(); //+4
        buttonShelterInTheWorld.setText("Прихисток для українців у світі");
        buttonShelterInTheWorld.setUrl("https://shelter4ua.com/ua");
        buttonShelterInTheWorld.setCallbackData("DataButtonShelterInTheWorld");

        InlineKeyboardButton buttonShelterInUkraine = new InlineKeyboardButton(); //+5
        buttonShelterInUkraine.setText("Прихисток в Україні");
        buttonShelterInUkraine.setUrl("https://prykhystok.gov.ua/");
        buttonShelterInUkraine.setCallbackData("DataButtonShelterInUkraine");

        InlineKeyboardButton buttonShelterInEurope = new InlineKeyboardButton(); //+6
        buttonShelterInEurope.setText("Прихисток у Європі");
        buttonShelterInEurope.setUrl("https://ua.eu4ua.org/");
        buttonShelterInEurope.setCallbackData("DataButtonShelterInEurope");

        InlineKeyboardButton buttonHousingAbroad = new InlineKeyboardButton(); //+7
        buttonHousingAbroad.setText("Житло за кордоном");
        buttonHousingAbroad.setUrl("https://refugees.ro/");
        buttonHousingAbroad.setCallbackData("DataButtonHousingAbroad");

        InlineKeyboardButton buttonRentAndSaleOfHousing = new InlineKeyboardButton(); //+8
        buttonRentAndSaleOfHousing.setText("Оренда і продаж житла");
        buttonRentAndSaleOfHousing.setUrl("https://flatfy.ua/ru/");
        buttonRentAndSaleOfHousing.setCallbackData("DataButtonRentAndSaleOfHousing");

        InlineKeyboardButton buttonTemporaryHousing = new InlineKeyboardButton(); //+9
        buttonTemporaryHousing.setText("Тимчасове житло");
        buttonTemporaryHousing.setUrl("https://www.host4ukraine.com/");
        buttonTemporaryHousing.setCallbackData("DataButtonTemporaryHousing");

        InlineKeyboardButton buttonFindingASafeHome = new InlineKeyboardButton(); //+10
        buttonFindingASafeHome.setText("Пошук безпечного дому");
        buttonFindingASafeHome.setUrl("https://docs.google.com/forms/d/e/1FAIpQLSerQV8PufaIagMPTZ36mlNeVGOzElh_G-sXOAmNvZQUUXS9GQ/viewform");
        buttonFindingASafeHome.setCallbackData("DataButtonFindingASafeHome");

        InlineKeyboardButton buttonMutualAssistance = new InlineKeyboardButton(); //+11
        buttonMutualAssistance.setText("Взаємодопомога");
        buttonMutualAssistance.setUrl("https://dopomagai.org/");
        buttonMutualAssistance.setCallbackData("DatabuttonMutualAssistanceSecond");

        InlineKeyboardButton buttonContactsInUkraine = new InlineKeyboardButton(); //+12
        buttonContactsInUkraine.setText("Контакти по Україні");
        buttonContactsInUkraine.setUrl("https://life.pravda.com.ua/society/2022/02/26/247573/");
        buttonContactsInUkraine.setCallbackData("DataButtonContactsInUkraine");

        InlineKeyboardButton buttonFindingHousingInEurope = new InlineKeyboardButton(); //+13
        buttonFindingHousingInEurope.setText("Пошук житла в Європі");
        buttonFindingHousingInEurope.setUrl("https://welcome-ukraine.eu/?fbclid=IwAR3XPMI_D-aHytGzrayWmitRAd8PWFNW52syEU7zSLvKbHTyDuggkRN7Klc");
        buttonFindingHousingInEurope.setCallbackData("DataButtonFindingHousingInEurope");

        InlineKeyboardButton buttonHousingAssistance = new InlineKeyboardButton(); //+14
        buttonHousingAssistance.setText("Допомога з житлом");
        buttonHousingAssistance.setUrl("https://helprequest.aidaform.com/request");
        buttonHousingAssistance.setCallbackData("DataButtonHousingAssistance");

        InlineKeyboardButton buttonRequestForHousingChatbot = new InlineKeyboardButton(); //+15
        buttonRequestForHousingChatbot.setText("Запит на житло через чат-бот");
        buttonRequestForHousingChatbot.setUrl("https://t.me/shelter_for_ukrainians_bot");
        buttonRequestForHousingChatbot.setCallbackData("DataButtonRequestForHousingChatbot");

        InlineKeyboardButton buttonHelpFromCaritas = new InlineKeyboardButton(); //+16
        buttonHelpFromCaritas.setText("Допомога від Caritas");
        buttonHelpFromCaritas.setUrl("https://caritas.ua/news/merezha-karitasu-dopomagaye-lyudyam-yaki-postrazhdaly-vid-rosijskoyi-agresiyi/");
        buttonHelpFromCaritas.setCallbackData("DatabuttonHelpFromCaritasSecond");

        InlineKeyboardButton buttonMutualAssistanceOfUkr = new InlineKeyboardButton(); //+17
        buttonMutualAssistanceOfUkr.setText("Взаємодопомога Українців");
        buttonMutualAssistanceOfUkr.setUrl("https://helplist.io/");
        buttonMutualAssistanceOfUkr.setCallbackData("DataButtonMutualAssistanceOfUkr");

        InlineKeyboardButton buttonCoordinationCenterInVolun = new InlineKeyboardButton(); //+18
        buttonCoordinationCenterInVolun.setText("Координаційний центр на Волині");
        buttonCoordinationCenterInVolun.setUrl("https://voladm.gov.ua/new/pri-volinskiy-oda-na-period-voyennogo-stanu-diye-koordinaciyniy-centr-volini/");
        buttonCoordinationCenterInVolun.setCallbackData("DataButtonCoordinationCenterInVolun");

        InlineKeyboardButton buttonRefugeesAbroad = new InlineKeyboardButton(); //+19
        buttonRefugeesAbroad.setText("Біженцям за кордоном");
        buttonRefugeesAbroad.setUrl("https://ukrainesupport.com.ua/ua/");
        buttonRefugeesAbroad.setCallbackData("DataButtonRefugeesAbroad");

        InlineKeyboardButton buttonRefugeesInUkraine = new InlineKeyboardButton(); //+20
        buttonRefugeesInUkraine.setText("Біженцям в Україні");
        buttonRefugeesInUkraine.setUrl("https://ukrainesupport.com.ua/ua/refugees-ukraine");
        buttonRefugeesInUkraine.setCallbackData("DataButtonRefugeesInUkraine");

        InlineKeyboardButton buttonHousingInUkraine = new InlineKeyboardButton(); //+21
        buttonHousingInUkraine.setText("Житло в Україні");
        buttonHousingInUkraine.setUrl("https://viyna.net/dopomoga-u-poshuczi-zhitla");
        buttonHousingInUkraine.setCallbackData("DataButtonHousingInUkraine");

        InlineKeyboardButton buttonHousingInTheWorld = new InlineKeyboardButton(); //+22
        buttonHousingInTheWorld.setText("Житло у світі");
        buttonHousingInTheWorld.setUrl("https://viyna.net/dopomoga-z-pereyizdom");
        buttonHousingInTheWorld.setCallbackData("DataButtonHousingInTheWorld");

        InlineKeyboardButton buttonRefugeeGuide = new InlineKeyboardButton(); //+23
        buttonRefugeeGuide.setText("Порадник для біженців");
        buttonRefugeeGuide.setUrl("https://prjctr.notion.site/3fff7ad01d9648a8af299771b4e1b319");
        buttonRefugeeGuide.setCallbackData("DataButtonRefugeeGuide");

        InlineKeyboardButton buttonRetry = new InlineKeyboardButton();
        buttonRetry.setText(Emojis.RETURN + " Повернутись");
        buttonRetry.setCallbackData("DataButtonReturnEvacuationThird");

        InlineKeyboardButton buttonAtTheStart = new InlineKeyboardButton();
        buttonAtTheStart.setText(Emojis.START + " На початок");
        buttonAtTheStart.setCallbackData("DataButtonAtTheStartEvacuationThird");

        List<InlineKeyboardButton> keyboardButtonRow1 = new ArrayList<>();
        keyboardButtonRow1.add(buttonSearchForHousingInTheWorld);

        List<InlineKeyboardButton> keyboardButtonRow2 = new ArrayList<>();
        keyboardButtonRow2.add(buttonHousingInLvivAndCarpathi);

        List<InlineKeyboardButton> keyboardButtonRow3 = new ArrayList<>();
        keyboardButtonRow3.add(buttonCarpathiInfo);

        List<InlineKeyboardButton> keyboardButtonRow4 = new ArrayList<>();
        keyboardButtonRow4.add(buttonShelterInTheWorld);

        List<InlineKeyboardButton> keyboardButtonRow5 = new ArrayList<>();
        keyboardButtonRow5.add(buttonShelterInUkraine);

        List<InlineKeyboardButton> keyboardButtonRow6 = new ArrayList<>();
        keyboardButtonRow6.add(buttonShelterInEurope);

        List<InlineKeyboardButton> keyboardButtonRow7 = new ArrayList<>();
        keyboardButtonRow7.add(buttonHousingAbroad);

        List<InlineKeyboardButton> keyboardButtonRow8 = new ArrayList<>();
        keyboardButtonRow8.add(buttonRentAndSaleOfHousing);

        List<InlineKeyboardButton> keyboardButtonRow9 = new ArrayList<>();
        keyboardButtonRow9.add(buttonTemporaryHousing);

        List<InlineKeyboardButton> keyboardButtonRow10 = new ArrayList<>();
        keyboardButtonRow10.add(buttonFindingASafeHome);

        List<InlineKeyboardButton> keyboardButtonRow11 = new ArrayList<>();
        keyboardButtonRow11.add(buttonMutualAssistance);

        List<InlineKeyboardButton> keyboardButtonRow12 = new ArrayList<>();
        keyboardButtonRow12.add(buttonContactsInUkraine);

        List<InlineKeyboardButton> keyboardButtonRow13 = new ArrayList<>();
        keyboardButtonRow13.add(buttonFindingHousingInEurope);

        List<InlineKeyboardButton> keyboardButtonRow14 = new ArrayList<>();
        keyboardButtonRow14.add(buttonHousingAssistance);

        List<InlineKeyboardButton> keyboardButtonRow15 = new ArrayList<>();
        keyboardButtonRow15.add(buttonRequestForHousingChatbot);

        List<InlineKeyboardButton> keyboardButtonRow16 = new ArrayList<>();
        keyboardButtonRow16.add(buttonHelpFromCaritas);

        List<InlineKeyboardButton> keyboardButtonRow17 = new ArrayList<>();
        keyboardButtonRow17.add(buttonMutualAssistanceOfUkr);

        List<InlineKeyboardButton> keyboardButtonRow18 = new ArrayList<>();
        keyboardButtonRow18.add(buttonCoordinationCenterInVolun);

        List<InlineKeyboardButton> keyboardButtonRow19 = new ArrayList<>();
        keyboardButtonRow19.add(buttonRefugeesAbroad);

        List<InlineKeyboardButton> keyboardButtonRow20 = new ArrayList<>();
        keyboardButtonRow20.add(buttonRefugeesInUkraine);

        List<InlineKeyboardButton> keyboardButtonRow21 = new ArrayList<>();
        keyboardButtonRow21.add(buttonHousingInUkraine);

        List<InlineKeyboardButton> keyboardButtonRow22 = new ArrayList<>();
        keyboardButtonRow22.add(buttonHousingInTheWorld);

        List<InlineKeyboardButton> keyboardButtonRow23 = new ArrayList<>();
        keyboardButtonRow23.add(buttonRefugeeGuide);

        List<InlineKeyboardButton> keyboardButtonRow24 = new ArrayList<>();
        keyboardButtonRow24.add(buttonRetry);
        keyboardButtonRow24.add(buttonAtTheStart);

        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        buttonRows.add(keyboardButtonRow1);
        buttonRows.add(keyboardButtonRow2);
        buttonRows.add(keyboardButtonRow3);
        buttonRows.add(keyboardButtonRow4);
        buttonRows.add(keyboardButtonRow5);
        buttonRows.add(keyboardButtonRow6);
        buttonRows.add(keyboardButtonRow7);
        buttonRows.add(keyboardButtonRow8);
        buttonRows.add(keyboardButtonRow9);
        buttonRows.add(keyboardButtonRow10);
        buttonRows.add(keyboardButtonRow11);
        buttonRows.add(keyboardButtonRow12);
        buttonRows.add(keyboardButtonRow13);
        buttonRows.add(keyboardButtonRow14);
        buttonRows.add(keyboardButtonRow15);
        buttonRows.add(keyboardButtonRow16);
        buttonRows.add(keyboardButtonRow17);
        buttonRows.add(keyboardButtonRow18);
        buttonRows.add(keyboardButtonRow19);
        buttonRows.add(keyboardButtonRow20);
        buttonRows.add(keyboardButtonRow21);
        buttonRows.add(keyboardButtonRow22);
        buttonRows.add(keyboardButtonRow23);
        buttonRows.add(keyboardButtonRow24);

        inlineKeyboardMarkup.setKeyboard(buttonRows);

        return inlineKeyboardMarkup;
    }
}