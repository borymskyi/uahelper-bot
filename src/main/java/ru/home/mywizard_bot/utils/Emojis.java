package ru.home.mywizard_bot.utils;

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
    BEGINNING(EmojiParser.parseToUnicode("⏮"));

    private String emojiName;

    @Override
    public String toString() {
        return emojiName;
    }
}