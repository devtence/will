package com.devtence.will.dev.endpoints.jazz;

import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.jazz.Language;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.*;

/**
 * Created by sorcerer on 6/9/16.
 */
public class LanguageAPI extends BaseController<Language>{

    @Override
    public Language create(Language data, User user) throws BadRequestException, ConflictException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public Language read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public Language update(@Named("id") Long id, Language data, User user) throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public Language delete(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
        return null;
    }

    @Override
    public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("offset") @Nullable @DefaultValue("100") Integer offset, @Named("sortField") @Nullable String sortField, @Named("sortDirection") @Nullable @DefaultValue("ASC") String sortDirection, @Named("cursor") @Nullable String cursor, User user) throws InternalServerErrorException, UnauthorizedException {
        return null;
    }
}
