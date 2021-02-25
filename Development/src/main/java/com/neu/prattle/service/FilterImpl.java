package com.neu.prattle.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.neu.prattle.model.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * FilterImpl is the link between the Filter model and the database storage for filter words.
 */
public class FilterImpl implements FilterLayer {

    private static final FilterLayer accountService = new FilterImpl();

    /**
     * The database service.
     *
     * @return FilterLayer ADT.
     */
    public static FilterLayer getInstance() {
        return accountService;
    }

    @Override
    public void addBatchWords(List<Filter> lst) {
        for (Filter filter : lst) {
            DynamoDBCredentials.dynamoDBS3Mapper().save(filter);
        }
    }

    @Override
    public void addWord(Filter word) {
        DynamoDBCredentials.dynamoDBS3Mapper().save(word);
    }

    @Override
    public void deleteWord(Filter word) {
        DynamoDBCredentials.dynamoDBS3Mapper().delete(word);
    }

    @Override
    public boolean wordExist(Filter filter) {
        return DynamoDBCredentials.dynamoDBS3Mapper().load(Filter.class, filter.getWord()) != null;
    }

    /**
     * Gets all words from the database as a list for viewing.
     *
     * @return A list of strings representing all words in the database.
     */
    public static List<String> getList() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Filter> filter = DynamoDBCredentials.dynamoDBMapper().scan(Filter.class, scanExpression);
        List<String> stringList = new ArrayList<>();
        for (Filter f : filter) {
            stringList.add(f.getWord());
        }
        return stringList;
    }
}
