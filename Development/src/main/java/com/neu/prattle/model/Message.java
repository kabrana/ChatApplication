package com.neu.prattle.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * An object representation of a message. This message class contains all the relative data and
 * methods that represent messages.
 */
@DynamoDBTable(tableName = "Messages")
public class Message implements Comparable<Message>{
    /***
     * Unique id of the message being created.
     */
    private String messageId;
    /***
     * The name of the user who sent this message.
     */
    private String from;
    /***
     * The name of the group receiving the message.
     */
    private String recipient;
    /***
     * It represents the contents of the message.
     */
    private String content;
    /***
     * The UTC time of when the message was created.
     */
    private String recordCreateDate;

    /**
     * A boolean to determine if the message is active or not.
     * Inactive messages will not be rendered on the front end.
     */
    private Boolean active;

    /**
     * Create a new message object with the given information. a new random ID is
     * assigned upon creation.
     * @param from who the message is from.
     * @param recipient who the message is going to.
     * @param content the textual content of this message.
     */
    public Message(String from, String recipient, String content) {
        this.messageId = UUID.randomUUID().toString();
        this.from = from;
        this.recipient = recipient;
        this.content = content;
        this.active = true;
        this.recordCreateDate = String.valueOf(java.time.Clock.systemUTC().instant());
    }

    /**
     * Empty constructor.
     */
    public Message() {}

    /**
     * Get the message ID for this message.
     * @return the string ID for this message.
     */
    @DynamoDBHashKey(attributeName = "MessageId")
    public String getMessageId() { return messageId; }

    /**
     * Set the message ID for this message.
     * @param messageId the new ID for this message.
     */
    public void setMessageId(String messageId) { this.messageId = messageId;}

    /**
     * Get who this message is from.
     * @return a string representing who this message is from.
     */
    @DynamoDBAttribute(attributeName = "From")
    public String getFrom() { return from; }

    /**
     * Set the from field for this message.
     * @param from who this message is from.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Get the recipient of this message.
     * @return a string representing who this messaged is going to.
     */
    @DynamoDBAttribute(attributeName = "Recipient")
    public String getRecipient() { return recipient; }

    /**
     * Set the recipient of this message.
     * @param recipient the new recipient that this message is going to.
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * Get the content of this message.
     * @return the textual body/content of this message.
     */
    @DynamoDBAttribute(attributeName = "Content")
    public String getContent() { return content; }

    /**
     * Set the content of this message.
     * @param content the new textual body of this message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the active status of this message.
     * @return true if the message is active, false otherwise.
     */
    @DynamoDBAttribute(attributeName = "Active")
    public Boolean getActive() { return active; }

    /**
     * Set the active status of this message.
     * @param active the new status of this message.
     */
    public void setActive(Boolean active) { this.active = active; }

    /**
     * Get the date this message was created.
     * @return a string for the date this message was created.
     */
    @DynamoDBAttribute(attributeName = "RecordCreateDate")
    public String getRecordCreateDate() { return recordCreateDate; }

    /**
     * Set the date for when this message was created.
     * @param recordCreateDate the new date to set for when this message was created.
     */
    public void setRecordCreateDate(String recordCreateDate) {this.recordCreateDate = recordCreateDate;}

    /**
     * Get a string representation of this message object.
     * @return the string representation of this message object.
     */
    @Override
    public String toString() {
        return "From: " + from + " Recipient: " + recipient + " Content: " + content + " CreateDate: " + recordCreateDate + " MessageId: " + messageId;}

    /**
     * Compare this message to another message.
     * @param message the message this message is being compared to.
     * @return determine the creation date of the messages and return appropriately, -1 if this
     * message is after the other message or 1 if it is before.
     */
    @Override
    public int compareTo(Message message) {
        Instant ins1 = Instant.parse(message.getRecordCreateDate());
        Instant ins2 = Instant.parse(this.getRecordCreateDate());
        if (ins1.isAfter(ins2)) {
            return -1;
        } else return 1;
    }

    /**
     * Determine if this message is equal to another object by comparing the message IDs.
     * @param o the object to be compared to.
     * @return false if o is not an instance of message or if the message IDs are not equal.
     * True otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message)) {
            return false;
        }
        Message m = (Message) o;
        return this.messageId.equals(m.messageId);
    }

    /**
     * Return the hashcode for this message object.
     * @return the hashcode for this message object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(from, recipient, content);
    }
}