package com.neu.prattle.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * A filter object. A filter object represents words that are to be filtered on the front end.
 */
@DynamoDBTable(tableName = "Filter")
public class Filter {

    /**
     * The word to be filtered.
     */
    String filterWord;

    /**
     * Construct a new filter object for a word that will be filtered on the front end.
     * @param filterWord the word to be filtered for this filter object.
     */
    public Filter(String filterWord) {
        this.filterWord = filterWord;
    }

    /**
     * Empty constructor.
     */
    public Filter(){}

    /**
     * Get the word for this filter object.
     * @return the word for this filter object.
     */
    @DynamoDBHashKey(attributeName = "Word")
    public String getWord() { return filterWord; }

    /**
     * Set the word for this filter object.
     * @param word the word to set for this filter object.
     */
    public void setWord(String word){this.filterWord = word;}
}
