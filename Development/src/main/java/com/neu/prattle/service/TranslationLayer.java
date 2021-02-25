package com.neu.prattle.service;

/**
 * The translation interface requires only a target language, and the content to be translated. If nothing is specified
 * within the client, it defaults to English.
 */
public interface TranslationLayer {

    /**
     * Translates the content of all messages in your history.
     *
     * @param target  The target language you want to see.
     * @param content The content to be translated.
     * @return The translation as a string.
     */
    String translate(String target, String content);

}
