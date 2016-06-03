package com.devtence.will.dev.endpoints;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.authenticators.UserAuthenticator;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.commons.Configuration;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
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
 * Created by plessmann on 03/06/16.
 */
@Api(
		name = Constants.COMMON_API_NAME,
		version = Constants.API_MASTER_VERSION,
		scopes = {Constants.EMAIL_SCOPE},
		clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
		audiences = {Constants.ANDROID_AUDIENCE},
		authenticators = {UserAuthenticator.class}
)
public class ConfigurationsAPI extends BaseController<Configuration> {

	private static final Logger log = Logger.getLogger(ConfigurationsAPI.class.getName());

	@Override
	@ApiMethod(httpMethod = ApiMethod.HttpMethod.POST, name = "configuration.create", path = "configuration")
	public Configuration create(Configuration data, User user) throws BadRequestException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		try {
			data.validate();
		} catch (MissingFieldException e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(String.format(Constants.CONFIGURATION_ERROR_CREATE, e.getMessage()));
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return data;
	}

	@Override
	@ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, name = "configuration.read", path = "configuration/{id}")
	public Configuration read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		Configuration configuration = null;
		try {
			configuration = Configuration.getById(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(configuration == null){
			throw new NotFoundException(String.format(Constants.CONFIGURATION_ERROR_NOT_FOUND, id));
		}
		return configuration;
	}

	@Override
	@ApiMethod(httpMethod = ApiMethod.HttpMethod.PUT, name = "configuration.update", path = "configuration/{id}")
	public Configuration update(@Named("id") Long id, Configuration data, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		Configuration configuration = null;
		try {
			configuration = Configuration.getById(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(configuration == null){
			throw new NotFoundException(String.format(Constants.CONFIGURATION_ERROR_NOT_FOUND, id));
		}
		try {

		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return null;
	}

	@Override
	@ApiMethod(httpMethod = ApiMethod.HttpMethod.DELETE, name = "configuration.delete", path = "configuration/{id}")
	public Configuration delete(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		Configuration configuration = null;
		try {
			configuration = Configuration.getById(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(configuration == null){
			throw new NotFoundException(String.format(Constants.CONFIGURATION_ERROR_NOT_FOUND, id));
		}
		try {

		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return null;
	}

	@Override
	@ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, name = "configuration.list", path = "configurations")
	public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("offset") @Nullable @DefaultValue("100") Integer offset, @Named("sortField") @Nullable String sortField, @Named("sortDirection") @Nullable @DefaultValue("ASC") String sortDirection, @Named("cursor") @Nullable String cursor, User user) throws InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		ListItem list = null;
		try {
			list = Configuration.getList(cursor, offset, Configuration.class, sortField, sortDirection);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return list;
	}
}
