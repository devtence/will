package com.devtence.will.dev.models;

import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.commons.Notification;
import com.devtence.will.dev.models.jazz.Author;
import com.devtence.will.dev.models.jazz.Category;
import com.devtence.will.dev.models.jazz.Content;
import com.devtence.will.dev.models.jazz.Language;
import com.devtence.will.dev.samples.search.Search;
import com.devtence.will.dev.models.users.Client;
import com.devtence.will.dev.models.users.Role;
import com.devtence.will.dev.models.users.User;
import com.devtence.will.dev.models.users.UserPasswordReset;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Class that specifies the Models to be connected to the Google Cloud Datastore, every new model needs to be
 * manually registered in this Class.
 *
 * This Class also makes public the ofy() method that creates a bridge between the Model an the Google Cloud Datastore.
 *
 * @author sorcerer
 * @since 2015-7-25
 */
public class DbObjectify {

    // Every Model Class intended to be registered must be added here
    static {
        //IAM Model Classes
        ObjectifyService.register(Configuration.class);
        ObjectifyService.register(Notification.class);
        ObjectifyService.register(Client.class);
        ObjectifyService.register(Role.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(UserPasswordReset.class);
        //Jazz Model Classes
        ObjectifyService.register(Author.class);
        ObjectifyService.register(Category.class);
        ObjectifyService.register(Content.class);
        ObjectifyService.register(Language.class);
        //Sample Classes
        ObjectifyService.register(Search.class);
        //new classes
        //add your new classes here
    }

    /**
     * Method that gives access to the functionality of the Google Cloud Datastore.
     */
    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

}
