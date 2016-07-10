package com.devtence.will.dev.endpoints.commons;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.commons.Notification;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Google Endpoint Class that implements the API methods to operate on the Notifications model.
 * All the methods on this class are secured by the default Authenticator.
 *
 * @author plessmann
 * @since 2016-06-06
 * @see Notification
 * @see ListItem
 */
@Api(
    name = Constants.COMMON_API_NAME,
    version = Constants.API_MASTER_VERSION
)
public class NotificationsAPI extends BaseController<Notification> {

    private static final Logger log = Logger.getLogger(NotificationsAPI.class.getName());

	/**
	 * Creates a Notification object on the Google Cloud Datastore
	 * @param data  BaseModel child containing the data to insert
	 * @param user  user provided by authentication to restrict access to this operation
	 * @return the object created
	 * @throws BadRequestException
	 * @throws InternalServerErrorException
	 * @throws UnauthorizedException
	 */
    @Override
    @ApiMethod(name = "notification.create", path = "notification")
    public Notification create(Notification data, User user) throws BadRequestException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        try {
            data.validate();
        } catch (MissingFieldException e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new BadRequestException(String.format(Constants.CONFIGURATION_ERROR_CREATE, e.getMessage()));
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return data;
    }

    /**
     * Queries the notification database using the id specified.
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return found object
     * @throws NotFoundException in case the id doesn't exist
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "notification.read", path = "notification/{id}")
    public Notification read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Notification notification = null;
        try {
            notification = Notification.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(notification == null){
            throw new NotFoundException(String.format(Constants.CONFIGURATION_ERROR_NOT_FOUND, id));
        }
        return notification;
    }

    /**
     * Updates the current notification with the new data.
     *
     * @param id    id of the instance of type T to e updated
     * @param data  instance of the same type that holds the new values
     * @param user  user provided by authentication to restrict access to this operation
     * @return updated object
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "notification.update", path = "notification/{id}")
    public Notification update(@Named("id") Long id, Notification data, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Notification notification = null;
        try {
            notification = Notification.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(notification == null){
            throw new NotFoundException(String.format(Constants.CONFIGURATION_ERROR_NOT_FOUND, id));
        }
        try {
            notification.update(data);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return notification;
    }

    /**
     * Removes a notification from the Google Cloud Datastore.
     *
     * @param id    id of the object to be deleted
     * @param user  user provided by authentication to restrict access to this operation
     * @return object deleted
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "notification.delete", path = "notification/{id}")
    public Notification delete(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Notification notification = null;
        try {
            notification = Notification.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(notification == null){
            throw new NotFoundException(String.format(Constants.CONFIGURATION_ERROR_NOT_FOUND, id));
        }
        try {
            notification.destroy();
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return notification;
    }

    /**
     * Returns a sorted list of the notifications stored in the Google Cloud Datastore.
     *
     * @param index initial point of the segment
     * @param limit max elements for the segment
     * @param sortFields    array of strings with the names of the fields to be used to sort the data
     * @param sortDirections    array of booleans that define the direction of each sortFields. true if DEC.
     * @param cursor        index of the previous segments
     * @param user  user provided by authentication to restrict access to this operation
     * @return list of notification objects, sorted and paginated
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "notification.list", path = "notifications")
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index,
                         @Named("limit") @Nullable @DefaultValue("100") Integer limit,
                         @Named("sortFields") @Nullable List<String> sortFields,
                         @Named("sortDirection") @Nullable List<Boolean> sortDirections,
                         @Named("cursor") @Nullable String cursor, User user)
            throws InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        ListItem list = null;
        try {
            list = Notification.getList(cursor, limit, Notification.class, sortFields, sortDirections);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return list;
    }
}
