package com.devtence.will.dev.endpoints;

import com.devtence.will.dev.commons.wrappers.AuthorizationWrapper;
import com.devtence.will.dev.models.BaseModel;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

/**
 * Created by plessmann on 03/06/16.
 */
public interface AuthenticableController<T extends BaseModel> {

	public AuthorizationWrapper authenticate(T data) throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException;

}
