package com.devtence.will.dev.endpoints.jazz;

import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.jazz.Creator;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.*;

import javax.inject.Named;

/**
 * Created by sorcerer on 6/9/16.
 */
public class AuthorAPI extends BaseController<Creator>{

    @Override
    public Creator create(Creator data, User user) throws BadRequestException, ConflictException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public Creator read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public Creator update(@Named("id") Long id, Creator data, User user) throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public Creator delete(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("offset") @Nullable @DefaultValue("100") Integer offset, @Named("sortField") @Nullable String sortField, @Named("sortDirection") @Nullable @DefaultValue("ASC") String sortDirection, @Named("cursor") @Nullable String cursor, User user) throws InternalServerErrorException, UnauthorizedException {
        return null;
    }
}
