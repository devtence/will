package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.users.Client;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by plessmann on 02/06/16.
 */
@Api(
	name = Constants.IAM_API_NAME,
	version = Constants.API_MASTER_VERSION
)
public class ClientsAPI extends BaseController<Client> {

	private static final Logger log = Logger.getLogger(ClientsAPI.class.getName());

	@Override
	@ApiMethod(name = "client.create", path = "client")
	public Client create(Client data, User user) throws BadRequestException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		try {
			data.validate();
		} catch (MissingFieldException e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(String.format(Constants.CLIENT_ERROR_CREATE, e.getMessage()));
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return data;
	}

	@Override
	@ApiMethod(name = "client.read", path = "client/{id}")
	public Client read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		Client client = null;
		try {
			client = Client.getById(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(client == null){
			throw new NotFoundException(String.format(Constants.CLIENT_ERROR_NOT_FOUND, id));
		}
		return client;
	}

	@Override
	@ApiMethod(name = "client.update", path = "client/{id}")
	public Client update(@Named("id") Long id, Client data, User user)
			throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		Client client = null;
		try {
			client = Client.getById(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(client == null){
			throw new NotFoundException(String.format(Constants.CLIENT_ERROR_NOT_FOUND, id));
		}
		try {
			client.update(data);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return client;
	}

	@Override
	@ApiMethod(name = "client.delete", path = "client/{id}")
	public Client delete(@Named("id") Long id,
						 User user)
			throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		Client client = null;
		try {
			client = Client.getById(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}

		if(client == null){
			throw new NotFoundException(String.format(Constants.CLIENT_ERROR_NOT_FOUND, id));
		}

		try {
			client.destroy();
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}

		return client;
	}

	@Override
	@ApiMethod(name = "client.list", path = "clients")
	public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index,
						 @Named("offset") @Nullable @DefaultValue("100") Integer offset,
						 @Named("sortField") @Nullable String sortField,
						 @Named("sortDirection") @Nullable @DefaultValue("ASC") String sortDirection,
						 @Named("cursor") @Nullable String cursor,
						 User user)
			throws InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		ListItem list = null;
		try {
			list = Client.getList(cursor, offset, Client.class, sortField, sortDirection);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return list;
	}


}
