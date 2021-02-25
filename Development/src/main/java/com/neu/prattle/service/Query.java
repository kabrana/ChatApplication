package com.neu.prattle.service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.neu.prattle.model.Message;

import java.util.*;

/**
 * Sole purpose of the class is to query all messages for a given groupID. This is what loads the message history.
 */
public class Query {

    private Query() {
    }

    public static List<Message> messages(String groupID) {
        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":value", new AttributeValue().withS(groupID));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName("Messages").withFilterExpression("Recipient = :value").withExpressionAttributeValues(attributeValues);

        ScanResult result = DynamoDBCredentials.client.scan(scanRequest);

        List<Message> messageList = new ArrayList<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            Message message = new Message(item.get("From").getS(), item.get("Recipient").getS(), item.get("Content").getS());
            message.setMessageId(item.get("MessageId").getS());
            message.setRecordCreateDate(item.get("RecordCreateDate").getS());
            messageList.add(message);
        }
        Collections.sort(messageList);
        return messageList;
    }
}
