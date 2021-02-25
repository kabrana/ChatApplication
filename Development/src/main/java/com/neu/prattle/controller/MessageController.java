package com.neu.prattle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.prattle.service.Query;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * A controller for the message object. This controller handles message specific functionality
 * between the front end and back end.
 */
@Path(value = "/message")
public class MessageController {

    /**
     * Run a query on the database and return all messages that are part of the specified group.
     * @param groupId the ID of the group that the messages are a part of.
     * @return a string representation of all of the messages queried against the specific group.
     * @throws JsonProcessingException if an error occurs while writing the object to a string.
     */
    @GET
    @Path("/group/{groupID}")
    @Produces("text/plain")
    public String getMessagesByGroupID(@PathParam("groupID") String groupId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(Query.messages(groupId));
    }
}