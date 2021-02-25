package com.neu.prattle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.prattle.service.FilterImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * A controller for the filter model object.
 * This controller allows the front end to interact with the database for all filter functionality.
 */
@Path(value = "/filter")
public class FilterController {

    /**
     * Get the the list of filterable words that is currently stored in the database. This is
     * returned as a string to the calling client that will handle the parsing of information.
     * @return A string object representing the list of filter words stored in the database for use
     * on the front end.
     * @throws JsonProcessingException if an error occurs while writing the object to a string.
     */
    @GET
    @Path("/filter")
    @Produces("text/plain")
    public String getFilterWordList() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(FilterImpl.getList());
    }

}
