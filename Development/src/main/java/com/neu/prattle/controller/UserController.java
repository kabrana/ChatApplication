package com.neu.prattle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupDatabaseImpl;
import com.neu.prattle.service.GroupDatabaseLayer;
import com.neu.prattle.service.UserDatabaseLayer;
import com.neu.prattle.service.UserDatabaseLayerImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/***
 * A controller object for the user model object. This controller is responsible for handling
 * all interactions between the front end and back end that pertain to user objects.
 */
@Path(value = "/user")
public class UserController {

    /**
     * A database layer service object for interacting with user objects in the database.
     */
    private UserDatabaseLayer accountService = UserDatabaseLayerImpl.getInstance();

    /**
     * A database layer service object for interacting with group objects in the database.
     */
    private final GroupDatabaseLayer groupDatabaseLayer = GroupDatabaseImpl.getInstance();

    /**
     * A static string for the general group ID. All users are entered in this group by default.
     */
    private static final String GENERAL_ID = "83cb9551-727c-4f3d-ab1d-edd5dc6d7c83";

    /***
     * Handles a HTTP POST request for user creation
     *
     *
     * @return -> A Response indicating the outcome of the requested operation.
     */
    @POST
    @Path("/create/{username}/{password}")
    public Response createUserAccount(@PathParam("username") String username, @PathParam("password") String password) {
        User newUser = new User(username, password);
        if (!accountService.userExist(newUser)) {
            accountService.addUser(newUser);
            groupDatabaseLayer.addUser(groupDatabaseLayer.getGroup(GENERAL_ID), newUser);
            return Response.ok().build();
        }
        return Response.status(409).build();
    }

    /**
     * Get request for a list of the groups that a user is a part of.
     * @param username the username for the user that will be queried against the database.
     * @return a string representation of the list of groups that the user is a part of.
     * @throws JsonProcessingException if an error occurs while writing the object to a string.
     */
    @GET
    @Path("groups/{username}")
    @Produces("text/plain")
    public String getGroupList(@PathParam("username") String username) throws JsonProcessingException {
        User newUser = accountService.getUser(username);
        Set<String> groupList = newUser.getGroups();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(groupList);
    }

    /**
     * Get request for the avatar of a specific user against the database.
     * @param username the username for the user that will be queried against the database.
     * @return a string representation of the URL for the avatar of the specific user.
     * @throws JsonProcessingException if an error occurs while writing the object to a string.
     * @throws MalformedURLException if an error occurs with retrieving the URL.
     */
    @GET
    @Path("avatar/{username}")
    @Produces("text/plain")
    public String getAvatar(@PathParam("username") String username) throws JsonProcessingException, MalformedURLException {
        User newUser = accountService.getUser(username);
        ObjectMapper mapper = new ObjectMapper();
        URL url = new URL("https://imagestestbenjaminsteele.s3.us-east-2.amazonaws.com/Users/Default/defaultImage.png");
        try {
            URL userURL = newUser.getAvatar().getUrl();
            return mapper.writeValueAsString(userURL);
        } catch (Exception e) {
            return mapper.writeValueAsString(url);
        }
    }

    /**
     * Post request to update the users status on the backend for the specific user object.
     * @param username the username of the user whose status will be updated.
     * @param status the status that will be set for the user.
     * @return an "ok" build response after all the processing has finished.
     */
    @POST
    @Path("/status/{username}/{status}")
    public Response changeStatus(@PathParam("username") String username, @PathParam("status") String status) {
        User newUser = new User(username, accountService.getUser(username).getPassword());
        newUser.setStatus(status);
        accountService.addUser(newUser);
        return Response.ok().build();
    }

    /**
     * Get Request to determine if the provided username and password are a valid combination to
     * login.
     * @param username the username to log in with.
     * @param password the password to log in with.
     * @return
     */
    @GET
    @Path("/login/{username}/{password}")
    @Produces("text/plain")
    public String loginAccount(@PathParam("username") String username, @PathParam("password") String password) {
        User temp = accountService.getUser(username);
        String tempPassword = temp.getPassword();
        if (tempPassword.equals(password)) {
            return "Success";
        }
        return "Failure";
    }

    /**
     * Get request to determine is a user exists in the database.
     * @param username the username to query against the database to determine if a user exists
     * @return a string representation of a boolean. True if a user exists, false if they do not.
     */
    @GET
    @Path("/exist/{username}")
    @Produces("text/plain")
    public String userExist(@PathParam("username") String username) {
        User testUser = new User();
        testUser.setName(username);
        if (accountService.userExist(testUser)) {
            return "True";
        }
        return "False";
    }

    /**
     * Get Request to update the password for a specific user in the backend.
     * @param username the username for the user whose password is being updated.
     * @param oldPassword the current password for the user
     * @param newPassword the new password for the user
     * @return a string representation of a boolean. True if the user successfully updates
     * their password, false otherwise.
     */
    @GET
    @Path("/changePassword/{username}/{oldPassword}/{newPassword}")
    @Produces("text/plain")
    public String changePassword(@PathParam("username") String username, @PathParam("oldPassword") String oldPassword,
                                 @PathParam("newPassword") String newPassword) {
        User temp = accountService.getUser(username);
        if (temp.getPassword().equals(oldPassword)) {
            temp.setPassword(newPassword);
            accountService.addUser(temp);
            return "True";
        } else {
            return "False";
        }
    }
}
