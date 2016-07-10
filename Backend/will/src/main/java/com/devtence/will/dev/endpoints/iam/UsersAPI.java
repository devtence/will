package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.PushQueue;
import com.devtence.will.dev.commons.wrappers.AuthorizationWrapper;
import com.devtence.will.dev.commons.wrappers.BooleanWrapper;
import com.devtence.will.dev.endpoints.AuthenticableController;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.exceptions.UserExistException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.users.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.*;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.jasypt.util.password.BasicPasswordEncryptor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Google Endpoint Class that implements the API methods to operate on the User model.
 * It also provides the services to authenticate, recover and update the password.
 * All the methods of this class are secured by the default Authenticator
 *
 * @author plessmann
 * @since 2016-06-02
 * @see User
 * @see ListItem
 */
@Api(
    name = Constants.IAM_API_NAME,
    version = Constants.API_MASTER_VERSION
)
public class UsersAPI extends BaseController<User> implements AuthenticableController<User> {

    private static final Logger log = Logger.getLogger(UsersAPI.class.getName());

    /**
     * Adds a new User to the Google Cloud Datastore.
     *
     * @param data  BaseModel child containing the data to insert
     * @param user  user provided by authentication to restrict access to this operation
     * @return the created object
     * @throws BadRequestException
     * @throws ConflictException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     *
     * TODO: check hardcoded values and change them to constants or configs.
     */
    @Override
    @ApiMethod(name = "user.create", path = "user")
    public User create(User data, com.google.api.server.spi.auth.common.User user)
            throws BadRequestException, ConflictException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        try {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            data.setPassword(passwordEncryptor.encryptPassword(data.getPassword()));
            data.setStatus(2);
            data.setFailedLoginCounter(0);
            data.setLastLoginStatus(false);
            data.setPasswordRecoveryStatus(0);
            data.validate();
        } catch (MissingFieldException e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
        } catch (UserExistException e){
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new ConflictException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return data;
    }

    /**
     * Search the Users database using the id specified.
     *
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return object that matches the id
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "user.read", path = "user/{id}")
    public User read(@Named("id") Long id, com.google.api.server.spi.auth.common.User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        User userDevtence = null;
        try {
            userDevtence = User.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(userDevtence == null){
            throw new NotFoundException(String.format(Constants.USER_ERROR_NOT_FOUND, id));
        }
        return userDevtence;
    }

    /**
     * Updates the current User with the new data.
     *
     * @param id    id of the instance of type T to e updated
     * @param data  instance of the same type that holds the new values
     * @param user  user provided by authentication to restrict access to this operation
     * @return updated object
     * @throws BadRequestException
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "user.update", path = "user/{id}")
    public User update(@Named("id") Long id,
                       User data,
                       com.google.api.server.spi.auth.common.User user)
            throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        User userDevtence = null;
        try {
            userDevtence = User.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(userDevtence == null){
            throw new NotFoundException(String.format(Constants.USER_ERROR_NOT_FOUND, id));
        }
        try {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            data.setPassword(passwordEncryptor.encryptPassword(data.getPassword()));
            userDevtence.update(data);
        } catch (UserExistException e){
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return userDevtence;
    }

    /**
     * Removes a User from the Google Cloud Datastore.
     *
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return deleted object
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "user.delete", path = "user/{id}")
    public User delete(@Named("id") Long id, com.google.api.server.spi.auth.common.User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        User userDevtence = null;
        try {
            userDevtence = User.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(userDevtence == null){
            throw new NotFoundException(String.format(Constants.USER_ERROR_NOT_FOUND, id));
        }
        try {
            userDevtence.destroy();
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return userDevtence;
    }

    /**
     * Returns a paginated and sorted list of Users.
     *
     * @param index initial point of the segment
     * @param limit max elements for the segment
     * @param sortFields    array of strings with the names of the fields to be used to sort the data
     * @param sortDirections    array of booleans that define wether the sortings is DEC or not
     * @param cursor        index of the previous segment
     * @param user  user provided by authentication to restrict access to this operation
     * @return list of User objects, sorted and paginated
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "user.list", path = "users")
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index,
                         @Named("limit") @Nullable @DefaultValue("100") Integer limit,
                         @Named("sortFields") @Nullable List<String> sortFields,
                         @Named("sortDirection") @Nullable List<Boolean> sortDirections,
                         @Named("cursor") @Nullable String cursor, com.google.api.server.spi.auth.common.User user)
            throws InternalServerErrorException, UnauthorizedException {
            validateUser(user);
        ListItem list = null;
        try {
            list = User.getList(cursor, limit, User.class, sortFields, sortDirections);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return list;
    }

    /**
     * Checks the received user credentials and the grants access to the user.
     *
     * @param data  user and password of the AuthenticableEntity
     * @param user  user that accepts the client being used to for the authentication operation
     * @return
     * @throws BadRequestException
     * @throws InternalServerErrorException
     * @throws NotFoundException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "user.authenticate", path = "user/authenticate")
    public AuthorizationWrapper authenticate(User data, com.google.api.server.spi.auth.common.User user)
            throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException {
        validateUser(user);
        User userDevtence;
        try {
            userDevtence = User.getByUser(data.getUser());
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
        }
        if(userDevtence != null){
            boolean allow = false;
            try {
                allow = userDevtence.goodLogin(data.getPassword());
            } catch (Exception e) {
                log.log(Level.WARNING, Constants.ERROR, e);
                throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
            }
            if (allow) {
                try {
                    return userDevtence.authorize();
                } catch (Exception e) {
                    log.log(Level.WARNING, Constants.ERROR, e);
                    throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
                }
            } else {
                throw new UnauthorizedException(Constants.INVALID_PASSWORD);
            }
        } else {
            throw new NotFoundException(String.format(Constants.USER_NOT_FOUND, data.getUser()));
        }
    }

    /**
     * Starts the process for password recovery, and creates the event that will send the user the password recovery
     * email notification.
     *
     * @param data  data to identify the user starting the process
     * @param user  user that accpets the client being used to for the authentication operation
     * @return
     * @throws BadRequestException
     * @throws InternalServerErrorException
     * @throws NotFoundException
     * @throws UnauthorizedException
     *
     * TODO: Check endpoint route and implementation
     */
    @Override
    @ApiMethod(name = "user.password.recover", path = "user/password/recover")
    public BooleanWrapper recoverPassword(User data, com.google.api.server.spi.auth.common.User user)
            throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException {
        validateUser(user);
        User userDevtence;
        try {
            userDevtence = User.getByUser(data.getUser());
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
        }
        if(userDevtence != null){
            try {
                userDevtence.setPasswordRecoveryStatus(1);
                userDevtence.update();
                TaskOptions taskOptions = TaskOptions.Builder.withUrl(Constants.NOTIFY).param(Constants.ID, userDevtence.getId().toString()).param(Constants.NOTIFICATION_MNEMONIC, Constants.PASSWORD_RECOVERY_NOTIFICATION).param(Constants.NOTIFICATOR_KEY, Constants.USER_PASSWORD_RECOVERY);
                PushQueue.enqueueMail(taskOptions);
                return new BooleanWrapper(true);
            } catch (Exception e) {
                log.log(Level.WARNING, Constants.ERROR, e);
                throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
            }
        } else {
            throw new NotFoundException(String.format(Constants.USER_NOT_FOUND, data.getUser()));
        }
    }

    /**
     * Checks if the user has initiated the password recovery process and changes the user password.
     *
     * @param data  data to identify the user starting the process
     * @param user  user that accepts the client being used to for the authentication operation
     * @return
     * @throws BadRequestException
     * @throws InternalServerErrorException
     * @throws NotFoundException
     * @throws UnauthorizedException
     *
     * TODO: Check the endpoint route and implementation
     */
    @Override
    @ApiMethod(name = "user.password.update", path = "user/password/update")
    public BooleanWrapper updatePassword(User data, com.google.api.server.spi.auth.common.User user)
            throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException {
        validateUser(user);
        User userDevtence;
        try {
            userDevtence = User.getByUser(data.getUser());
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
        }
        if(userDevtence != null){
            if(userDevtence.getPasswordRecoveryStatus() == 3) {
                try {
                    BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
                    data.setPassword(passwordEncryptor.encryptPassword(data.getPassword()));
                    data.setPasswordRecoveryStatus(0);
                    userDevtence.update(data);
                    return new BooleanWrapper(true);
                } catch (Exception e) {
                    log.log(Level.WARNING, Constants.ERROR, e);
                    throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
                }
            } else {
                throw new BadRequestException(Constants.NO_PASSWORD_RESET_PROCESS_PENDING);
            }
        } else {
            throw new NotFoundException(String.format(Constants.USER_NOT_FOUND, data.getUser()));
        }
    }

    /**
     * Checks if the queried username exists on the user Collection.
     *
     * @param data  element of T containing the field to check
     * @param user  user that accepts the client being used to for the authentication operation
     * @return
     * @throws BadRequestException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     *
     * TODO: check this endpoint implementation
     */
    @Override
    @ApiMethod(name = "user.username", path = "user/username")
    public BooleanWrapper checkUser(User data, com.google.api.server.spi.auth.common.User user)
            throws BadRequestException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        User userDevtence = null;
        if(data.getUser() != null){
            try {
                userDevtence = User.getByUser(data.getUser());
            } catch (Exception e) {
                log.log(Level.WARNING, Constants.ERROR, e);
                throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
            }
            if(userDevtence == null){
                return new BooleanWrapper(true);
            }
            return new BooleanWrapper(false);
        } else {
            throw new BadRequestException(Constants.USER_ERROR_CREATE);
        }
    }
}
