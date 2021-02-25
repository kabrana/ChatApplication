package com.neu.prattle.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;

/**
 * Class implementation for all translation done within the client. Messages translated are not altered within the
 * database, only the message history on the front end is altered.
 */
public class TranslationImpl implements TranslationLayer {

    private static final TranslationLayer accountService = new TranslationImpl();

    /**
     * The database service.
     *
     * @return TranslationLayer ADT.
     */
    public static TranslationLayer getInstance() {
        return accountService;
    }

    @Override
    public String translate(String target, String content) {

        AmazonTranslate translate = AmazonTranslateClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(DynamoDBCredentials.awsCreds))
                .withRegion("us-east-2")
                .build();

        TranslateTextRequest request = new TranslateTextRequest()
                .withText(content)
                .withSourceLanguageCode("auto")
                .withTargetLanguageCode(target);
        TranslateTextResult result = translate.translateText(request);

        return result.getTranslatedText();
    }
}
