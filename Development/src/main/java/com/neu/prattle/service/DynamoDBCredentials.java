package com.neu.prattle.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

/**
 * Credentials class to access AWS database for the project.
 */

class DynamoDBCredentials {

    static BasicAWSCredentials awsCreds =
            new BasicAWSCredentials("AKIAZVVVPHSI377XNGMC",
                    "X+BrfJcWRyl+E4Qagmdtnpry2AAo239DulLQqR9H");
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-east-2")
            .build();

    private DynamoDBCredentials() {
    }

    /**
     * Mapper constant used by the classes in the services package.
     *
     * @return DynamoDBMapper.
     */
    static DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(DynamoDBCredentials.client);
    }

    /**
     * MapperS3 constant used by the classes in the services package.
     *
     * @return DynamoDBS3Mapper.
     */
    static DynamoDBMapper dynamoDBS3Mapper() {
        return new DynamoDBMapper(DynamoDBCredentials.client,
                new AWSStaticCredentialsProvider(DynamoDBCredentials.awsCreds));
    }


}
