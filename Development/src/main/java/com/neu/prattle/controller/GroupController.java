package com.neu.prattle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupDatabaseImpl;
import com.neu.prattle.service.GroupDatabaseLayer;
import com.neu.prattle.service.UserDatabaseLayer;
import com.neu.prattle.service.UserDatabaseLayerImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * A filter for the group model object. This controller handles all functionality related
 * to groups when interacting between the front end and back end.
 */
@Path(value = "/group")
public class GroupController {

    /**
     * Group service object for interacting with the database.
     */
    private GroupDatabaseLayer groupService = GroupDatabaseImpl.getInstance();

    /**
     * User service object for interacting with the database.
     */
    private UserDatabaseLayer accountService = UserDatabaseLayerImpl.getInstance();

    /**
     * Create a new group as specified by the client attempting to create the group. A new
     * group object is created from the information pulled in from the front end. All users
     * that are being added to the new group are updated accordingly in the backend. After
     * all the updating has finishing, the group is created using the groupService.
     * @param group the new group to be created in the database.
     * @return an "ok" response to signify to the client that the group was created.
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGroup(Group group) {
        Set<String> setHash = group.getModeratorList();
        Group groupTemp = new Group(group.getGroupName(), group.getGroupList(), group.getType());
        groupTemp.setModeratorList(setHash);

        for (String userTemp : groupTemp.getGroupList()) {
            User user = accountService.getUser(userTemp);
            user.addGroup(groupTemp.getGroupID());
            accountService.addUser(user);
        }
        groupService.createGroup(groupTemp);
        return Response.ok().build();
    }

    /**
     * Get an existing group object from the database and return it as a string for the front end
     * to handle parsing of data.
     * @param groupID the ID of the existing group in the database.
     * @return a string representation of an existing group as requested by the calling client.
     * @throws JsonProcessingException if an error occurs while writing the object to a string.
     */
    @GET
    @Path("group/{groupID}")
    @Produces("text/plain")
    public String getGroupObject(@PathParam("groupID") String groupID) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(groupService.getGroup(groupID));
    }

    /**
     * Takes in a messageID and finds the resulting group that the message was sent over.
     * @param messageId the ID for the message.
     * @return a string representation of the group object that the message was sent over.
     * @throws JsonProcessingException if an error occurs while writing the object to a string.
     */
    @GET
    @Path("groupName/{groupID}")
    @Produces("text/plain")
    public String getGroupName(@PathParam("groupID") String messageId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(groupService.getGroup(messageId).getGroupName());
    }

    /**
     * Delete an existing group from the database. For each user in the group, their information
     * will be updated accordingly in the backend to represent that they are no longer in the
     * group.
     * @param groupID the ID of the group that will be deleted from the database.
     * @return an "ok" status after all the updates have been processed. Returns an error message
     * if the group does not exist in the backend.
     */
    @POST
    @Path("/delete/{groupID}")
    @Produces("text/plain")
    public Response deleteGroup(@PathParam("groupID") String groupID) {
        Group group = new Group();
        group.setGroupID(groupID);
        if (groupService.groupExists(group)) {
            Set<String> groupMember = groupService.getGroup(groupID).getGroupList();
            for (String s : groupMember) {
                User user = accountService.getUser(s);
                user.removeGroup(groupID);
                accountService.addUser(user);
            }
            groupService.deleteGroup(group);
            return Response.ok().build();
        }
        return Response.status(409).build();
    }

    /**
     * Get a list of all of the moderators for an existing specific group.
     * @param groupID the ID for the specific group.
     * @return a string representation of the list of users who are moderators of the
     * specific group.
     * @throws JsonProcessingException if an error occurs while writing the object to a string.
     */
    @GET
    @Path("moderators/{groupID}")
    @Produces("text/plain")
    public String getGroupModerators(@PathParam("groupID") String groupID) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(groupService.getModerators(groupService.getGroup(groupID)));
    }

    /**
     * Get a list of all of the members of an existing group.
     * @param groupID the ID for the specific group.
     * @return a string representation of the list of users who are a part of the existing group.
     * @throws JsonProcessingException if an error occurs while writing the object to a string.
     */
    @GET
    @Path("members/{groupID}")
    @Produces("text/plain")
    public String getGroupMemberList(@PathParam("groupID") String groupID) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(groupService.getMembers(groupService.getGroup(groupID)));
    }

    /**
     * Adds a new member to the existing group. The members information will also be updated
     * to reflect the new group they are in.
     * @param groupID the ID for the group the user is being added to.
     * @param username the username for the new user being added to the group.
     * @return an "ok" status after all processing has completed.
     */
    @POST
    @Path("/add/{groupID}/{username}")
    @Produces("text/plain")
    public Response addGroupMember(@PathParam("groupID") String groupID, @PathParam("username") String username) {
        Group group = groupService.getGroup(groupID);
        User user = accountService.getUser(username);
        group.addUser(user);
        user.addGroup(group.getGroupID());
        groupService.createGroup(group);
        accountService.addUser(user);
        return Response.ok().build();
    }

    /**
     * Removes a group member from an existing group in the databsae. Both the group object and the
     * user object will be updated to reflect the user no longer being a part of the specific group.
     * @param groupID the ID for the group the member is being removed from.
     * @param username the username of the member being removed from the group.
     * @return an "ok" status after all processing has completed.
     */
    @POST
    @Path("/remove/{groupID}/{username}")
    @Produces("text/plain")
    public Response removeGroupMember(@PathParam("groupID") String groupID, @PathParam("username") String username) {
        Group group = groupService.getGroup(groupID);
        User user = accountService.getUser(username);
        group.removeUser(user);
        user.removeGroup(group.getGroupID());
        groupService.createGroup(group);
        accountService.addUser(user);
        return Response.ok().build();
    }

    /**
     * Promote a member of an existing group to a moderator for that group. The user and group
     * objects will both be updated in the backend.
     * @param groupID the ID for the group that the user will be made a moderator of.
     * @param username the username of the existing member in the group that will be promoted.
     * @return an "ok" status after all processing has completed.
     */
    @POST
    @Path("/promote/{groupID}/{username}")
    @Produces("text/plain")
    public Response promoteGroupMember(@PathParam("groupID") String groupID, @PathParam("username") String username) {
        Group group = groupService.getGroup(groupID);
        User user = accountService.getUser(username);

        Set<String> list = group.getModeratorList();
        list.add(user.getName());
        group.setModeratorList(list);
        groupService.createGroup(group);
        return Response.ok().build();
    }

    /**
     * Check to see if a user is a moderator of a specific group. Query the backend for the
     * existing group and get the list of moderators for that specific group.
     * Return true if the user is present in the list, false otherwise.
     * @param groupID the ID for the specific group that is being queried.
     * @param username the username of the user that is being queried.
     * @return a string representing true or false, depending on whether the user is a
     * current moderator.
     */
    @GET
    @Path("isModerator/{groupID}/{username}")
    @Produces("text/plain")
    public String isModerators(@PathParam("groupID") String groupID, @PathParam("username") String username) {
        Set<String> set = groupService.getModerators(groupService.getGroup(groupID));
        if(set.contains(username)){
            return "true";
        }
        else{
            return "false";
        }
    }

    /**
     * Leave a group as a user. The database is queried for the specific group object, and then
     * the group object is updated accordingly. The user object is also queried and updated.
     * @param groupID the ID of the group that the user is leaving.
     * @param username the username of the user that is leaving the group.
     * @return an "ok" status after all processing has completed.
     */
    @POST
    @Path("leave/{groupID}/{username}")
    @Produces("text/plain")
    public Response leaveGroup(@PathParam("groupID") String groupID, @PathParam("username") String username) {
        Group tempGroup = groupService.getGroup(groupID);
        User tempUser = accountService.getUser(username);
        tempUser.removeGroup(groupID);
        tempGroup.removeUser(tempUser);
        groupService.createGroup(tempGroup);
        accountService.addUser(tempUser);
        return Response.ok().build();
    }
}
