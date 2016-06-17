package com.devtence.will.dev.endpoints.commons;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.commons.Configuration;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by plessmann on 03/06/16.
 */
@Api(
	name = Constants.COMMON_API_NAME,
	version = Constants.API_MASTER_VERSION
)
public class ConfigurationsAPI extends BaseController<Configuration> {

	private static final Logger log = Logger.getLogger(ConfigurationsAPI.class.getName());

	@Override
	@ApiMethod(name = "configuration.create", path = "configuration")
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
	@ApiMethod(name = "configuration.read", path = "configuration/{id}")
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
	@ApiMethod(name = "configuration.update", path = "configuration/{id}")
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
			configuration.update(data);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return configuration;
	}

	@Override
	@ApiMethod(name = "configuration.delete", path = "configuration/{id}")
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
			configuration.destroy();
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return configuration;
	}

	@Override
	@ApiMethod(name = "configuration.list", path = "configurations")
	public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("limit") @Nullable @DefaultValue("100") Integer limit, @Named("sortFields") @Nullable List<String> sortFields, @Named("sortDirection") @Nullable List<Boolean> sortDirections, @Named("cursor") @Nullable String cursor, User user) throws InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		ListItem list = null;
		try {
			list = Configuration.getList(cursor, limit, Configuration.class, sortFields, sortDirections);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return list;
	}
}
