package com.devtence.will.dev.endpoints.commons;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.commons.Configuration;
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
 * Google Endpoint Class that implements the API methods to operate on the Configuration model.
 * All the methods on this class are secured by the default Authenticator.
 *
 * @author plessmann
 * @since 2016-03-06
 * @see Configuration
 * @see ListItem
 */
@Api(
    name = Constants.COMMON_API_NAME,
    version = Constants.API_MASTER_VERSION
)
public class ConfigurationsAPI extends BaseController<Configuration> {

    private static final Logger log = Logger.getLogger(ConfigurationsAPI.class.getName());

    /**
     * Adds a new configuration object to the Google Cloud Datastore, returns the object inserted if OK.
     *
     * @param data  BaseModel child containing the data to insert
     * @param user  user provided by authentication to restrict access to this operation
     * @return object inserted
     * @throws BadRequestException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "configuration.create", path = "configuration")
    public Configuration create(Configuration data, User user) throws BadRequestException, InternalServerErrorException, UnauthorizedException {
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
     * Returns the configuration queried using the id provided.
     *
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return object that belogns to the ID specified
     * @throws NotFoundException if its not found
     * @throws InternalServerErrorException if something fails
     * @throws UnauthorizedException in case the user its not authorized
     */
    @Override
    @ApiMethod(name = "configuration.read", path = "configuration/{id}")
    public Configuration read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Configuration configuration = null;
        try {
            configuration = Configuration.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(configuration == null){
            throw new NotFoundException(String.format(Constants.CONFIGURATION_ERROR_NOT_FOUND, id));
        }
        return configuration;
    }

    /**
     * Modifies the selected configuration with the new data sent.
     *
     * @param id    id of the instance of type T to e updated
     * @param data  instance of the same type that holds the new values
     * @param user  user provided by authentication to restrict access to this operation
     * @return modified object
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "configuration.update", path = "configuration/{id}")
    public Configuration update(@Named("id") Long id, Configuration data, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Configuration configuration = null;
        try {
            configuration = Configuration.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(configuration == null){
            throw new NotFoundException(String.format(Constants.CONFIGURATION_ERROR_NOT_FOUND, id));
        }
        try {
            configuration.update(data);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return configuration;
    }

    /**
     * Deletes a configuration record from the Google Cloud Datastore.
     *
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return deleted object
     * @throws NotFoundException if the configurations doesnt exist
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "configuration.delete", path = "configuration/{id}")
    public Configuration delete(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Configuration configuration = null;
        try {
            configuration = Configuration.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(configuration == null){
            throw new NotFoundException(String.format(Constants.CONFIGURATION_ERROR_NOT_FOUND, id));
        }
        try {
            configuration.destroy();
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return configuration;
    }

    /**
     * Returns a paginated and sorted list of all the configurations stored in the Google Cloud Datastore.
     *
     * @param index initial point of the segment
     * @param limit max elements for the segment
     * @param sortFields    array of strings with the names of the fields to be used to sort the data
     * @param sortDirections    array of booleans that define the direction of each sortFields. true if DEC.
     * @param cursor        index of the previous segment
     * @param user  user provided by authentication to restrict access to this operation
     * @return list of configuration objects, sorted and paginated
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "configuration.list", path = "configurations")
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index,
                         @Named("limit") @Nullable @DefaultValue("100") Integer limit,
                         @Named("sortFields") @Nullable List<String> sortFields,
                         @Named("sortDirection") @Nullable List<Boolean> sortDirections,
                         @Named("cursor") @Nullable String cursor, User user)
            throws InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        ListItem list = null;
        try {
            list = Configuration.getList(cursor, limit, Configuration.class, sortFields, sortDirections);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return list;
    }
}
