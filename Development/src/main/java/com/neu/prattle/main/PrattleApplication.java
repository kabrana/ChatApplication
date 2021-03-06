package com.neu.prattle.main;

import com.neu.prattle.controller.*;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/***
 * Sets up the resource classes for handling REST requests.
 * Refer {@link Application}
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public class PrattleApplication extends Application {
    private final Set<Class<?>> resourceClasses = new HashSet<>();

    @Override
    public Set<Class<?>> getClasses() {
        resourceClasses.add(MessageController.class);
        resourceClasses.add(UserController.class);
        resourceClasses.add(GroupController.class);
        resourceClasses.add(FilterController.class);
        resourceClasses.add(TranslationController.class);
        return resourceClasses;
    }
}
