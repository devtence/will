package com.devtence.will.dev.endpoints;

import com.devtence.will.dev.commons.wrappers.AuthorizationWrapper;
import com.devtence.will.dev.commons.wrappers.BooleanWrapper;
import com.devtence.will.dev.models.BaseModel;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

/**
 * Created by plessmann on 03/06/16.
 */
public interface AuthenticableController<T extends BaseModel> {

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.POST)
	public AuthorizationWrapper authenticate(T data, User user) throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException;

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.POST)
	public BooleanWrapper recoverPassword (T data, User user) throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException;

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.PUT)
	public BooleanWrapper updatePassword(T data, User user) throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException;

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.POST)
	public BooleanWrapper checkUser(T data, User user) throws BadRequestException, InternalServerErrorException, UnauthorizedException;

}
