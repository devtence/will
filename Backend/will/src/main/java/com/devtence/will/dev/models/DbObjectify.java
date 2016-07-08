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
 * basic class to create connections to google data storage (GDS).
 * every new model defined by must be manually registered in this class.
 *
 * this class also publishes the ofy() method that is used to call on the GDS
 *
 * Created by sorcerer on 7/25/15.
 */
public class DbObjectify {

    //must add every model here to guaranty that is registered to use
    static {
        //iam classes
        ObjectifyService.register(Configuration.class);
        ObjectifyService.register(Notification.class);
        ObjectifyService.register(Client.class);
        ObjectifyService.register(Role.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(UserPasswordReset.class);
        //jazz classes
        ObjectifyService.register(Author.class);
        ObjectifyService.register(Category.class);
        ObjectifyService.register(Content.class);
        ObjectifyService.register(Language.class);
        //sample classes
        ObjectifyService.register(Search.class);
        //new classes
        //add your new classes here
    }

    /**
     * this method gives you access to the service that allows you to operate on the GDS
     * @return service to operate on the GDS
     */
    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

}
