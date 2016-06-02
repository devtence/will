package com.devtence.api.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.authenticators.UserAuthenticator;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.users.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

import javax.inject.Named;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by plessmann on 02/06/16.
 */
@Api(
		name = Constants.IAM_API_NAME,
		version = Constants.API_MASTER_VERSION,
		scopes = {Constants.EMAIL_SCOPE},
		clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
		audiences = {Constants.ANDROID_AUDIENCE},
		authenticators = {UserAuthenticator.class}
)
public class UsersAPI extends BaseController<User> {

	private static final Logger log = Logger.getLogger(UsersAPI.class.getName());

	@Override
	public User create(User data, com.google.api.server.spi.auth.common.User user) throws BadRequestException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		try {
			data.validate();
		} catch (MissingFieldException e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return data;
	}

	@Override
	public User read(@Named("id") Long id, com.google.api.server.spi.auth.common.User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		User userDevtence = null;
		try {
			userDevtence = User.getById(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(userDevtence == null){
			throw new NotFoundException(String.format(Constants.USER_ERROR_NOT_FOUND, id));
		}
		return userDevtence;
	}

	@Override
	public User update(@Named("id") Long id, User data, com.google.api.server.spi.auth.common.User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		User userDevtence = null;
		try {
			userDevtence = User.getById(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(userDevtence == null){
			throw new NotFoundException(String.format(Constants.USER_ERROR_NOT_FOUND, id));
		}
		try {

		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return null;
	}

	@Override
	public User delete(@Named("id") Long id, com.google.api.server.spi.auth.common.User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		User userDevtence = null;
		try {
			userDevtence = User.getById(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(userDevtence == null){
			throw new NotFoundException(String.format(Constants.USER_ERROR_NOT_FOUND, id));
		}
		try {

		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return null;
	}

	@Override
	public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("offset") @Nullable @DefaultValue("100") Integer offset, @Named("sortField") @Nullable String sortField, @Named("sortDirection") @Nullable String sortDirection, @Named("cursor") @Nullable String cursor, com.google.api.server.spi.auth.common.User user) throws InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		try {

		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return null;
	}
}
