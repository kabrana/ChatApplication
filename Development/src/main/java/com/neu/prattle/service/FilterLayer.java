package com.neu.prattle.service;

import com.neu.prattle.model.Filter;

import java.util.List;

/**
 * Interface represents all possible queries able to be made to the database in regards to filter modifications.
 */
public interface FilterLayer {

    /**
     * Adds a list of filter words to the database.
     *
     * @param lst A list of type Filter.
     */
    void addBatchWords(List<Filter> lst);

    /**
     * Adds a single filter word to the database.
     *
     * @param word A word of type Filter.
     */
    void addWord(Filter word);

    /**
     * Deletes a filter word from the database.
     *
     * @param word A word of type Filter.
     */
    void deleteWord(Filter word);

    /**
     * Queries the database to see if the filter word exists in the backend.
     *
     * @param filter A word of type Filter.
     * @return True if word exists, false if not.
     */
    boolean wordExist(Filter filter);

}
