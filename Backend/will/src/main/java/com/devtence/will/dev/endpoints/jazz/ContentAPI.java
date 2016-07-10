package com.devtence.will.dev.endpoints.jazz;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.jazz.Content;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Google Endpoint Class that implements the API methods to operate on the Content model.
 * all the methods of this class are secured by the default Authenticator
 *
 * @author sorcerer
 * @since 2016-06-09
 * @see Content
 * @see ListItem
 *
 */
@Api(name = Constants.JAZZ_API_NAME,version = Constants.API_MASTER_VERSION)
public class ContentAPI extends BaseController<Content>{

    private static final Logger log = Logger.getLogger(AuthorAPI.class.getName());

    /**
     * Adds a new Content to the Google Cloud Datastore.
     * @param data  BaseModel child containing the data to insert
     * @param user  user provided by authentication to restrict access to this operation
     * @return
     * @throws BadRequestException
     * @throws ConflictException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "content.create",
            path = "content")
    public Content create(Content data, User user) throws BadRequestException, ConflictException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        try {
            data.validate();
        }catch (MissingFieldException ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new BadRequestException(String.format(Constants.CONFIGURATION_ERROR_CREATE, ex.getMessage()));
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return data;
    }

    /**
     * Returns the queried  Content with the id parameter
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "content.read",
            path = "content/{id}")
    public Content read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Content tr = null;
        try {
            tr = Content.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (tr == null){
            throw new NotFoundException(String.format(Constants.CONTENT_ERROR_NOT_FOUND, id));
        }
        return tr;
    }

    /**
     * Updates the current Content with the new Data
     * @param id    id of the instance of type T to e updated
     * @param data  instance of the same type that holds the new values
     * @param user  user provided by authentication to restrict access to this operation
     * @return
     * @throws BadRequestException
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "content.update",
            path = "content/{id}")
    public Content update(@Named("id") Long id, Content data, User user)
            throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Content exist = null;
        try {
            exist = Content.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (exist == null){
            throw new NotFoundException(String.format(Constants.CONTENT_ERROR_NOT_FOUND, id));
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
     * Removes a Content from the Google Cloud Datastore
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "content.delete",
            path = "content/{id}")
    public Content delete(@Named("id") Long id, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Content exist = null;
        try {
            exist = Content.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (exist == null){
            throw new NotFoundException(String.format(Constants.CONTENT_ERROR_NOT_FOUND, id));
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
     * Returns a sorted list of Contents
     * @param index initial point of the segment
     * @param limit max elements for the segment
     * @param sortFields    array of strings with the names of the fields to be used to sort the data
     * @param sortDirections    array of booleans that define wether the sortings is DEC or not
     * @param cursor        index of the previous segment
     * @param user  user provided by authentication to restrict access to this operation
     * @return
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "content.list", path = "contents")
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index,
                         @Named("limit") @Nullable @DefaultValue("100") Integer limit,
                         @Named("sortFields") @Nullable List<String> sortFields,
                         @Named("sortDirection") @Nullable List<Boolean> sortDirections,
                         @Named("cursor") @Nullable String cursor, User user)
            throws InternalServerErrorException, UnauthorizedException {
        ListItem list = null;
        try {
            list = Content.getList(cursor, limit, Content.class, sortFields, sortDirections);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return list;
    }
}
