package com.devtence.will.dev.models;

import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.commons.Notification;
import com.devtence.will.dev.models.users.Client;
import com.devtence.will.dev.models.users.Role;
import com.devtence.will.dev.models.users.User;
import com.devtence.will.dev.models.users.UserPasswordReset;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * base class to create conections to google data storage
 * Created by sorcerer on 7/25/15.
 */
public class DbObjectify {
    //must add every model here to guaranty that is registered to use
    static {
        ObjectifyService.register(Configuration.class);
        ObjectifyService.register(Notification.class);
        ObjectifyService.register(Client.class);
        ObjectifyService.register(Role.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(UserPasswordReset.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

}
