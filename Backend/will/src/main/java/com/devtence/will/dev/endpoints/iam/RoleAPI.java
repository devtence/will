package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.users.Role;
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
public class RoleAPI extends BaseController<Role> {

	private static final Logger log = Logger.getLogger(RoleAPI.class.getName());

	@Override
	@ApiMethod(name = "role.create", path = "role")
	public Role create(Role data, User user) throws BadRequestException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		try {
			data.validate();
		} catch (MissingFieldException e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(String.format(Constants.ROLE_ERROR_CREATE, e.getMessage()));
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return data;
	}

	@Override
	@ApiMethod(name = "role.read", path = "role/{id}")
	public Role read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		Role role = null;
		try {
			role = Role.get(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(role == null){
			throw new NotFoundException(String.format(Constants.ROLE_ERROR_NOT_FOUND, id));
		}
		return role;
	}

	@Override
	@ApiMethod(name = "role.update", path = "role/{id}")
	public Role update(@Named("id") Long id, Role data, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		Role role = null;
		try {
			role = Role.get(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(role == null){
			throw new NotFoundException(String.format(Constants.ROLE_ERROR_NOT_FOUND, id));
		}
		try {
			role.update(data);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return role;
	}

	@Override
	@ApiMethod(name = "role.delete", path = "role/{id}")
	public Role delete(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		Role role = null;
		try {
			role = Role.get(id);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		if(role == null){
			throw new NotFoundException(String.format(Constants.ROLE_ERROR_NOT_FOUND, id));
		}
		try {
			role.destroy();
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return role;
	}

	@Override
	@ApiMethod(name = "role.list", path = "roles")
	public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("offset") @Nullable @DefaultValue("100") Integer offset, @Named("sortField") @Nullable String sortField, @Named("sortDirection") @Nullable @DefaultValue("ASC") String sortDirection, @Named("cursor") @Nullable String cursor, User user) throws InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		ListItem list = null;
		try {
			list = Role.getList(cursor, offset, Role.class, sortField, sortDirection);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return list;
	}
}
