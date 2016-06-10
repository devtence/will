package com.devtence.will.dev.endpoints.jazz;

import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.jazz.Category;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.*;

/**
 * Created by sorcerer on 6/9/16.
 */
public class CategoryAPI extends BaseController<Category>{
    
    @Override
    public Category create(Category data, User user) throws BadRequestException, ConflictException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public Category read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public Category update(@Named("id") Long id, Category data, User user) throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public Category delete(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("limit") @Nullable @DefaultValue("100") Integer limit, @Named("sortField") @Nullable String sortField, @Named("sortDirection") @Nullable @DefaultValue("ASC") String sortDirection, @Named("cursor") @Nullable String cursor, User user) throws InternalServerErrorException, UnauthorizedException {
        return null;
    }
}
