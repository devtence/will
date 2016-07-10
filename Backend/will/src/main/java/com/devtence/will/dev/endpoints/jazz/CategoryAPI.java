package com.devtence.will.dev.endpoints.jazz;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.jazz.Category;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Google Endpoint Class that implements the API methods to operate on the Category model.
 * all the methods of this class are secured by the default Authenticator
 *
 * @author sorcerer
 * @since 2016-06-09
 * @see Category
 * @see ListItem
 *
 */
@Api(
    name = Constants.JAZZ_API_NAME,
    version = Constants.API_MASTER_VERSION
)
public class CategoryAPI extends BaseController<Category>{

    private static final Logger log = Logger.getLogger(AuthorAPI.class.getName());

    /**
     * Adds a new Category to the Google Cloud Datastore
     * @param data  BaseModel child containing the data to insert
     * @param user  user provided by authentication to restrict access to this operation
     * @return
     * @throws BadRequestException
     * @throws ConflictException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "category.create",
            path = "category")
    public Category create(Category data, User user)
            throws BadRequestException, ConflictException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        try {
            data.validate();
        }catch (MissingFieldException ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new BadRequestException(String.format(Constants.CATEGORY_ERROR_CREATE, ex.getMessage()));
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return data;
    }

    /**
     * Returns the queried Category
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict acces to this operation
     * @return
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "category.read",
            path = "category")
    public Category read(@Named("id") Long id, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Category tr = null;
        try {
            tr = Category.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (tr == null){
            throw new NotFoundException(String.format(Constants.CATEGORY_ERROR_NOT_FOUND, id));
        }
        return tr;
    }

    /**
     * Updates the current Category with the new Data
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
    @ApiMethod(name = "category.update",
            path = "category/{id}")
    public Category update(@Named("id") Long id, Category data, User user)
            throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Category exist = null;
        try {
            exist = Category.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (exist == null){
            throw new NotFoundException(String.format(Constants.CATEGORY_ERROR_NOT_FOUND, id));
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
     * Removes a Category from the Google Cloud Datastore
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict acces to this operation
     * @return
     * @throws NotFoundException
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "category.delete",
            path = "category/{id}")
    public Category delete(@Named("id") Long id, User user)
            throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        validateUser(user);
        Category exist = null;
        try {
            exist = Category.get(id);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }

        if (exist == null){
            throw new NotFoundException(String.format(Constants.CATEGORY_ERROR_NOT_FOUND, id));
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
     * Returns a sorted list of Categories
     * @param index initial point of the segment
     * @param limit max elements for the segment
     * @param sortFields    array of strings with the names of the fields to be used to sort the data
     * @param sortDirections    array of booleans that define wether the sortings is DEC or not
     * @param cursor        index of the previous segmente required using this method
     * @param user  user provided by authentication to restrict acces to this operation
     * @return
     * @throws InternalServerErrorException
     * @throws UnauthorizedException
     */
    @Override
    @ApiMethod(name = "category.list", path = "categories")
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index,
                         @Named("limit") @Nullable @DefaultValue("100") Integer limit,
                         @Named("sortFields") @Nullable List<String> sortFields,
                         @Named("sortDirection") @Nullable List<Boolean> sortDirections,
                         @Named("cursor") @Nullable String cursor, User user)
            throws InternalServerErrorException, UnauthorizedException {
        ListItem list = null;
        try {
            list = Category.getList(cursor, limit, Category.class, sortFields, sortDirections);
        }catch (Exception ex){
            log.log(Level.WARNING, Constants.ERROR, ex);
            throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
        }
        return list;
    }
}
