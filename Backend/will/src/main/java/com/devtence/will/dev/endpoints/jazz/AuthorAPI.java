package com.devtence.will.dev.endpoints.jazz;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.jazz.Author;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Google Endpoint Class that implements the API methods to operate on the Author model.
 * All the methods on this class are secured by the default Authenticator.
 *
 * @author sorcerer
 * @since 2016-06-09
 * @see Author
 * @see ListItem
 *
 */
@Api(name = Constants.JAZZ_API_NAME,
        version = Constants.API_MASTER_VERSION)
public class AuthorAPI extends BaseController<Author> {

    private static final Logger log = Logger.getLogger(AuthorAPI.class.getName());

    /**
     * Adds a new Author to the Google Cloud Storage.
     * @param data  BaseModel child containing the data to insert
     * @param user  user provided by authentication to restrict access to this operation
     * @return object created
     * @throws BadRequestException
     * @throws ConflictException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "author.create",
            path = "author")
    public Author create(Author data, User user)
            throws BadRequestException, ConflictException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        try {
            data.validate();
        }catch (MissingFieldException ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new BadRequestException(String.format(Constants.AUTHOR_ERROR_CREATE, ex.getMessage()));
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return data;
    }

    /**
     * Queries the Author collection and retrieves the object with the specified id.
     *
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return the object found
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "author.read",
            path = "author/{id}")
    public Author read(@Named("id") Long id, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Author tr = null;
        try {
            tr = Author.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (tr == null){
            throw new NotFoundException(String.format(Constants.AUTHOR_ERROR_NOT_FOUND, id));
        }

        return tr;
    }

    /**
     * Updates the current Author with the new Data.
     *
     * @param id    id of the instance of type T to e updated
     * @param data  instance of the same type that holds the new values
     * @param user  user provided by authentication to restrict access to this operation
     * @return the updated object
     * @throws BadRequestException
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "author.update",
            path = "author/{id}")
    public Author update(@Named("id") Long id, Author data, User user)
            throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Author exist = null;
        try {
            exist = Author.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (exist == null){
            throw new NotFoundException(String.format(Constants.AUTHOR_ERROR_NOT_FOUND, id));
        }

        try {
            exist.update(data);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        return exist;
    }

    /**
     * Removes the specified Author from the Google Cloud Datastore.
     *
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return object deleted
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "author.delete",
            path = "author/{id}")
    public Author delete(@Named("id") Long id, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Author exist = null;
        try {
            exist = Author.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (exist == null){
            throw new NotFoundException(String.format(Constants.AUTHOR_ERROR_NOT_FOUND, id));
        }

        try {
            exist.destroy();
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        return exist;
    }

    /**
     * Returns a paginated and sorted list of Authors.
     *
     * @param index initial point of the segment
     * @param limit max elements for the segment
     * @param sortFields    array of strings with the names of the fields to be used to sort the data
     * @param sortDirections    array of booleans that define the direction of each sortField. true if DEC.
     * @param cursor        index of the previous segment
     * @param user  user provided by authentication to restrict access to this operation
     * @return list of notification objects, sorted and paginated
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "author.list", path = "authors")
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index,
                         @Named("limit") @Nullable @DefaultValue("100") Integer limit,
                         @Named("sortFields") @Nullable List<String> sortFields,
                         @Named("sortDirection") @Nullable List<Boolean> sortDirections,
                         @Named("cursor") @Nullable String cursor, User user)
            throws InternalServerErrorException, UnauthorizedException {
        ListItem list = null;
        try {
            list = Author.getList(cursor, limit, Author.class, sortFields, sortDirections);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return list;
    }
}
