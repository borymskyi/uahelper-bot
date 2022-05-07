package ua.home.ua_helper_bot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emojis {
    SHAKING_HANDS(EmojiParser.parseToUnicode("\uD83E\uDD1D")),
    MUSCLE(EmojiParser.parseToUnicode(":muscle:")),
    DEATH(EmojiParser.parseToUnicode("\uD83E\uDEC2")),
    CAR(EmojiParser.parseToUnicode("\uD83D\uDE97")),
    HUMANITARIAN(EmojiParser.parseToUnicode("💊")),
    PSYCHOLOGICAL(EmojiParser.parseToUnicode("\uD83C\uDFDD")),
    RETURN(EmojiParser.parseToUnicode("⬅️")),
    START(EmojiParser.parseToUnicode("⏮")),
    BUS(EmojiParser.parseToUnicode("\uD83D\uDE8C")),
    HOUSE(EmojiParser.parseToUnicode("\uD83C\uDFD8")),
    ROBOT(EmojiParser.parseToUnicode("\uD83E\uDD16"));

    private String emojiName;

    @Override
    public String toString() {
        return emojiName;
    }
}