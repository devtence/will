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
 * Created by sorcerer on 6/9/16.
 */
@Api(name = Constants.JAZZ_API_NAME,
        version = Constants.API_MASTER_VERSION)
public class AuthorAPI extends BaseController<Author>{

    private static final Logger log = Logger.getLogger(AuthorAPI.class.getName());

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

    @Override
    @ApiMethod(name = "author.list", path = "authors")
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("limit") @Nullable @DefaultValue("100") Integer limit, @Named("sortFields") @Nullable List<String> sortFields, @Named("sortDirection") @Nullable List<Boolean> sortDirections, @Named("cursor") @Nullable String cursor, User user) throws InternalServerErrorException, UnauthorizedException {
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
