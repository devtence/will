package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.PushQueue;
import com.devtence.will.dev.commons.wrappers.AuthorizationWrapper;
import com.devtence.will.dev.commons.wrappers.BooleanWrapper;
import com.devtence.will.dev.endpoints.AuthenticableController;
import com.devtence.will.dev.endpoints.BaseController;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.exceptions.UserExistException;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.users.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.*;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.jasypt.util.password.BasicPasswordEncryptor;

import javax.inject.Named;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by plessmann on 02/06/16.
 */
@Api(
	name = Constants.IAM_API_NAME,
	version = Constants.API_MASTER_VERSION
)
public class UsersAPI extends BaseController<User> implements AuthenticableController<User> {

	private static final Logger log = Logger.getLogger(UsersAPI.class.getName());

	@Override
	@ApiMethod(name = "user.create", path = "user")
	public User create(User data, com.google.api.server.spi.auth.common.User user) throws BadRequestException, ConflictException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		try {
			BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
			data.setPassword(passwordEncryptor.encryptPassword(data.getPassword()));
			data.setStatus(2);
			data.setFailedLoginCounter(0);
			data.setLastLoginStatus(false);
			data.setPasswordRecoveryStatus(0);
			data.validate();
		} catch (MissingFieldException e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
		} catch (UserExistException e){
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new ConflictException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return data;
	}

	@Override
	@ApiMethod(name = "user.read", path = "user/{id}")
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
	@ApiMethod(name = "user.update", path = "user/{id}")
	public User update(@Named("id") Long id,
					   User data,
					   com.google.api.server.spi.auth.common.User user)
			throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException {
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
			BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
			data.setPassword(passwordEncryptor.encryptPassword(data.getPassword()));
			userDevtence.update(data);
		} catch (UserExistException e){
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return userDevtence;
	}

	@Override
	@ApiMethod(name = "user.delete", path = "user/{id}")
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
			userDevtence.destroy();
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return userDevtence;
	}

	@Override
	@ApiMethod(name = "user.list", path = "users")
	public ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("offset") @Nullable @DefaultValue("100") Integer offset, @Named("sortField") @Nullable String sortField, @Named("sortDirection") @Nullable @DefaultValue("ASC") String sortDirection, @Named("cursor") @Nullable String cursor, com.google.api.server.spi.auth.common.User user) throws InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		ListItem list = null;
		try {
			list = User.getList(cursor, offset, User.class, sortField, sortDirection);
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
		}
		return list;
	}

	@Override
	@ApiMethod(name = "user.authenticate", path = "user/authenticate")
	public AuthorizationWrapper authenticate(User data, com.google.api.server.spi.auth.common.User user) throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException {
		validateUser(user);
		User userDevtence;
		try {
			userDevtence = User.getByUser(data.getUser());
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
		}
		if(userDevtence != null){
			boolean allow = false;
			try {
				allow = userDevtence.goodLogin(data.getPassword());
			} catch (Exception e) {
				log.log(Level.WARNING, Constants.ERROR, e);
				throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
			}
			if (allow) {
				try {
					return userDevtence.authorize();
				} catch (Exception e) {
					log.log(Level.WARNING, Constants.ERROR, e);
					throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
				}
			} else {
				throw new UnauthorizedException(Constants.INVALID_PASSWORD);
			}
		} else {
			throw new NotFoundException(String.format(Constants.USER_NOT_FOUND, data.getUser()));
		}
	}

	@Override
	@ApiMethod(name = "user.password.recover", path = "user/password/recover")
	public BooleanWrapper recoverPassword(User data, com.google.api.server.spi.auth.common.User user) throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException {
		validateUser(user);
		User userDevtence;
		try {
			userDevtence = User.getByUser(data.getUser());
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
		}
		if(userDevtence != null){
			try {
				userDevtence.setPasswordRecoveryStatus(1);
				userDevtence.update();
				TaskOptions taskOptions = TaskOptions.Builder.withUrl(Constants.NOTIFY).param(Constants.ID, userDevtence.getId().toString()).param(Constants.NOTIFICATION_MNEMONIC, Constants.PASSWORD_RECOVERY_NOTIFICATION).param(Constants.NOTIFICATOR_KEY, Constants.USER_PASSWORD_RECOVERY);
				PushQueue.enqueueMail(taskOptions);
				return new BooleanWrapper(true);
			} catch (Exception e) {
				log.log(Level.WARNING, Constants.ERROR, e);
				throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
			}
		} else {
			throw new NotFoundException(String.format(Constants.USER_NOT_FOUND, data.getUser()));
		}
	}

	@Override
	@ApiMethod(name = "user.password.update", path = "user/password/update")
	public BooleanWrapper updatePassword(User data, com.google.api.server.spi.auth.common.User user) throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException {
		validateUser(user);
		User userDevtence;
		try {
			userDevtence = User.getByUser(data.getUser());
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
			throw new BadRequestException(String.format(Constants.USER_ERROR_CREATE, e.getMessage()));
		}
		if(userDevtence != null){
			if(userDevtence.getPasswordRecoveryStatus() == 3) {
				try {
					data.setPasswordRecoveryStatus(0);
					userDevtence.update(data);
					return new BooleanWrapper(true);
				} catch (Exception e) {
					log.log(Level.WARNING, Constants.ERROR, e);
					throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
				}
			} else {
				throw new BadRequestException(Constants.NO_PASSWORD_RESET_PROCESS_PENDING);
			}
		} else {
			throw new NotFoundException(String.format(Constants.USER_NOT_FOUND, data.getUser()));
		}
	}

	@Override
	@ApiMethod(name = "user.username", path = "user/username")
	public BooleanWrapper checkUser(User data, com.google.api.server.spi.auth.common.User user) throws BadRequestException, InternalServerErrorException, UnauthorizedException {
		validateUser(user);
		User userDevtence = null;
		if(data.getUser() != null){
			try {
				userDevtence = User.getByUser(data.getUser());
			} catch (Exception e) {
				log.log(Level.WARNING, Constants.ERROR, e);
				throw new InternalServerErrorException(Constants.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE);
			}
			if(userDevtence == null){
				return new BooleanWrapper(true);
			}
			return new BooleanWrapper(false);
		} else {
			throw new BadRequestException(Constants.USER_ERROR_CREATE);
		}
	}
}
