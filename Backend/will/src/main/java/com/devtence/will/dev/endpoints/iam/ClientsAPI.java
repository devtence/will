package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.users.Client;
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
 * Google Endpoint Class that implements the API methods to operate on the Clients model.
 * All the methods on this class are secured by the default Authenticator.
 *
 * @author plessmann
 * @since 2016-06-02
 * @see Client
 * @see ListItem
 *
 */
@Api(
    name = Constants.IAM_API_NAME,
    version = Constants.API_MASTER_VERSION
)
public class ClientsAPI extends BaseController<Client> {

    private static final Logger log = Logger.getLogger(ClientsAPI.class.getName());

    /**
     * Adds a new Client to the Google Cloud Datastore.
     *
     * @param data  BaseModel child containing the data to insert
     * @param user  user provided by authentication to restrict access to this operation
     * @return object created
     * @throws BadRequestException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "client.create", path = "client")
    public Client create(Client data, User user)
            throws BadRequestException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        try {
            data.validate();
        } catch (MissingFieldException e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new BadRequestException(String.format(Constants.CLIENT_ERROR_CREATE, e.getMessage()));
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return data;
    }

    /**
     * Search the Clients database using the id specified.
     *
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return the object found
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "client.read", path = "client/{id}")
    public Client read(@Named("id") Long id, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Client client = null;
        try {
            client = Client.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(client == null){
            throw new NotFoundException(String.format(Constants.CLIENT_ERROR_NOT_FOUND, id));
        }
        return client;
    }

    /**
     * Updates the current Client with the new data.
     *
     * @param id    id of the instance of type T to e updated
     * @param data  instance of the same type that holds the new values
     * @param user  user provided by authentication to restrict access to this operation
     * @return the object persisted
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "client.update", path = "client/{id}")
    public Client update(@Named("id") Long id, Client data, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Client client = null;
        try {
            client = Client.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        if(client == null){
            throw new NotFoundException(String.format(Constants.CLIENT_ERROR_NOT_FOUND, id));
        }
        try {
            client.update(data);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return client;
    }

    /**
     * Removes an Existing Client from the Google Cloud Datastore
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return deleted object
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "client.delete", path = "client/{id}")
    public Client delete(@Named("id") Long id,
                         User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Client client = null;
        try {
            client = Client.getById(id);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if(client == null){
            throw new NotFoundException(String.format(Constants.CLIENT_ERROR_NOT_FOUND, id));
        }

        try {
            client.destroy();
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        return client;
    }

    /**
     * Returns a paginated and sorted list of clients.
     *
     * @param index initial point of the segment
     * @param limit max elements for the segment
     * @param sortFields    array of strings with the names of the fields to be used to sort the data
     * @param sortDirections    array of booleans that define the direction of each sortFields. true if DEC.
     * @param cursor        index of the previous segment
     * @param user  user provided by authentication to restrict access to this operation
     * @return list of Client objects, sorted and paginated
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "client.list", path = "clients")
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index,
                         @Named("limit") @Nullable @DefaultValue("100") Integer limit,
                         @Named("sortFields") @Nullable List<String> sortFields,
                         @Named("sortDirection") @Nullable List<Boolean> sortDirections,
                         @Named("cursor") @Nullable String cursor, User user)
            throws InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        ListItem list = null;
        try {
            list = Client.getList(cursor, limit, Client.class, sortFields, sortDirections);
        } catch (Exception e) {
            log.log(Level.WARNING, Constants.ERROR, e);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return list;
    }


}
