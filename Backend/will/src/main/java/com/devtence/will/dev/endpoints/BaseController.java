package com.devtence.will.dev.endpoints;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.authenticators.UserAuthenticator;
import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.ListItem;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.*;

import java.util.List;


/**
 * Created by plessmann on 02/06/16.
 */
@ApiClass(
	scopes = {Constants.EMAIL_SCOPE},
	clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
	audiences = {Constants.ANDROID_AUDIENCE},
	authenticators = {UserAuthenticator.class}
)
public abstract class BaseController<T extends BaseModel> {

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.POST)
	public abstract T create(T data, User user) throws BadRequestException, ConflictException,InternalServerErrorException, UnauthorizedException;

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.GET)
	public abstract T read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException;

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.PUT)
	public abstract T update(@Named("id") Long id, T data, User user) throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException;

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.DELETE)
	public abstract T delete(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException;

	@ApiMethod(httpMethod = ApiMethod.HttpMethod.GET)
	public abstract ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("limit") @Nullable @DefaultValue("100") Integer limit, @Named("sortFields") @Nullable List<String> sortFields, @Named("sortDirection") @Nullable List<Boolean> sortDirections, @Named("cursor") @Nullable String cursor, User user) throws InternalServerErrorException, UnauthorizedException;

	public static void validateUser(User user) throws UnauthorizedException {
		if(user == null){
			throw new UnauthorizedException(Constants.INVALID_USER);
		}
	}

}
