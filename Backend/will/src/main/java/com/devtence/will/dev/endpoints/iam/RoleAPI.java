package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.users.Role;
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
 * Google Endpoint Class that implements the API methods to operate on the Role model.
 * All the methods on this class are secured by the default Authenticator.
 *
 * @author plessmann
 * @since 2016-06-02
 * @see Role
 * @see ListItem
 *
 */
@Api(
    name = Constants.IAM_API_NAME,
    version = Constants.API_MASTER_VERSION
)
public class RoleAPI extends BaseController<Role> {

    private static final Logger log = Logger.getLogger(RoleAPI.class.getName());

    /**
     * Adds a new Role to the Google Cloud Datastore.
     *
     * @param data  BaseModel child containing the data to insert
     * @param user  user provided by authentication to restrict access to this operation
     * @return object created
     * @throws BadRequestException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "role.create", path = "role")
    public Role create(Role data, User user) throws BadRequestException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        try {
            data.validate();
        } catch (MissingFieldException e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new BadRequestException(String.format(Constants.ROLE_ERROR_CREATE, e.getMessage()));
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return data;
    }

    /**
     * Search the Roles database using the id specified.
     *
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return tho object found
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "role.read", path = "role/{id}")
    public Role read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Role role = null;
        try {
            role = Role.get(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(role == null){
            throw new NotFoundException(String.format(Constants.ROLE_ERROR_NOT_FOUND, id));
        }
        return role;
    }

    /**
     * Updates an Existing Role with the new Data.
     *
     * @param id    id of the instance of type T to e updated
     * @param data  instance of the same type that holds the new values
     * @param user  user provided by authentication to restrict access to this operation
     * @return the updated object
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "role.update", path = "role/{id}")
    public Role update(@Named("id") Long id, Role data, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Role role = null;
        try {
            role = Role.get(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(role == null){
            throw new NotFoundException(String.format(Constants.ROLE_ERROR_NOT_FOUND, id));
        }
        try {
            role.update(data);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return role;
    }

    /**
     * Removes a Role from the Google Cloud Datastore.
     *
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return object deleted
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "role.delete", path = "role/{id}")
    public Role delete(@Named("id") Long id, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Role role = null;
        try {
            role = Role.get(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(role == null){
            throw new NotFoundException(String.format(Constants.ROLE_ERROR_NOT_FOUND, id));
        }
        try {
            role.destroy();
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return role;
    }

    /**
     * Returns a paginated and sorted list of Roles with their permissions.
     *
     * @param index initial point of the segment
     * @param limit max elements for the segment
     * @param sortFields    array of strings with the names of the fields to be used to sort the data
     * @param sortDirections    array of booleans that define the direction of each sortFields. true if DEC.
     * @param cursor        index of the previous segment
     * @param user  user provided by authentication to restrict access to this operation
     * @return list of Role objects, sorted and paginated
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "role.list", path = "roles")
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index,
                         @Named("limit") @Nullable @DefaultValue("100") Integer limit,
                         @Named("sortFields") @Nullable List<String> sortFields,
                         @Named("sortDirection") @Nullable List<Boolean> sortDirections,
                         @Named("cursor") @Nullable String cursor, User user)
            throws InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        ListItem list = null;
        try {
            list = Role.getList(cursor, limit, Role.class, sortFields, sortDirections);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return list;
    }
}
