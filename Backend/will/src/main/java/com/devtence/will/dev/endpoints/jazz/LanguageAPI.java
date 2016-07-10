package com.devtence.will.dev.endpoints.jazz;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.jazz.Language;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Google Endpoint Class that implements the API methods to operate on the Language model.
 * all the methods of this class are secured by the default Authenticator
 *
 * @author sorcerer
 * @since 2016-06-09
 * @see Language
 * @see ListItem
 *
 */
@Api(name = Constants.JAZZ_API_NAME,
        version = Constants.API_MASTER_VERSION)
public class LanguageAPI extends BaseController<Language>{

    private static final Logger log = Logger.getLogger(LanguageAPI.class.getName());

    /**
     * Adds a new Language to the Google Cloud Datastore.
     * @param data  BaseModel child containing the data to insert
     * @param user  user provided by authentication to restrict access to this operation
     * @return
     * @throws BadRequestException
     * @throws ConflictException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "language.create",
            path = "language")
    public Language create(Language data, User user)
            throws BadRequestException, ConflictException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        try {
            data.validate();
        }catch (MissingFieldException ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new BadRequestException(String.format(Constants.LANGUAGE_ERROR_CREATE, ex.getMessage()));
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return data;
    }

    /**
     * Returns the queried Language
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "language.read",
            path = "language/{id}")
    public Language read(@Named("id") Long id, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Language tr = null;
        try {
            tr = Language.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (tr == null){
            throw new NotFoundException(String.format(Constants.LANGUAGE_ERROR_NOT_FOUND, id));
        }

        return tr;
    }

    /**
     * Updates the current Language with the new Data
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
    @ApiMethod(name = "language.update",
            path = "language/{id}")
    public Language update(@Named("id") Long id, Language data, User user)
            throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Language exist = null;
        try {
            exist = Language.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (exist == null){
            throw new NotFoundException(String.format(Constants.LANGUAGE_ERROR_NOT_FOUND, id));
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
     * Removes the queried Language from the Google Cloud Datastore.
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict access to this operation
     * @return
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "language.delete",
            path = "language/{id}")
    public Language delete(@Named("id") Long id, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Language exist = null;
        try {
            exist = Language.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (exist == null){
            throw new NotFoundException(String.format(Constants.LANGUAGE_ERROR_NOT_FOUND, id));
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
     * Returns a sorted List of Languages
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
    @ApiMethod(name = "language.list", path = "languages")
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index,
                         @Named("limit") @Nullable @DefaultValue("100") Integer limit,
                         @Named("sortFields") @Nullable List<String> sortFields,
                         @Named("sortDirection") @Nullable List<Boolean> sortDirections,
                         @Named("cursor") @Nullable String cursor, User user)
            throws InternalServerErrorException, UnauthorizedException {
        ListItem list = null;
        try {
            list = Language.getList(cursor, limit, Language.class, sortFields, sortDirections);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return list;
    }
}
