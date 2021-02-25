package com.neu.prattle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.prattle.service.TranslationImpl;
import com.neu.prattle.service.TranslationLayer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * A controller for the handling translation of messages between the front end and back end.
 */
@Path(value = "/translation")
public class TranslationController {

    /**
     * A database layer service object for handling translating messages from the database.
     */
    private TranslationLayer accountService = TranslationImpl.getInstance();

    /**
     * Translate a set of text to a specific language.
     * @param targetLang the target language that the content will be translated to.
     * @param content the text to be translated.
     * @return a string of the translated text.
     * @throws JsonProcessingException if an error occurs while writing the object to a string.
     */
    @GET
    @Path("/translation/{targetLang}/{content}")
    @Produces("text/plain")
    public String translate(@PathParam("targetLang") String targetLang,
                            @PathParam("content") String content) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(accountService.translate(targetLang, content));
    }
}