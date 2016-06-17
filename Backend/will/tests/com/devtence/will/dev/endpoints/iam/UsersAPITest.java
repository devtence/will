package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.authenticators.UserAuthenticator;
import com.devtence.will.dev.commons.wrappers.AuthorizationWrapper;
import com.devtence.will.dev.commons.wrappers.BooleanWrapper;
import com.devtence.will.dev.endpoints.commons.ConfigurationsAPITest;
import com.devtence.will.dev.endpoints.commons.NotificationsAPI;
import com.devtence.will.dev.endpoints.commons.NotificationsAPITest;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.commons.Notification;
import com.devtence.will.dev.models.users.*;
import com.devtence.will.dev.notificators.UserPasswordRecovery;
import com.devtence.will.dev.servlets.PasswordRedirect;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by plessmann on 09/06/16.
 */
public class UsersAPITest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final UsersAPI usersAPI = new UsersAPI();
	private final RoleAPI roleAPI = new RoleAPI();
	private final NotificationsAPI notificationsAPI = new NotificationsAPI();
	private final com.google.api.server.spi.auth.common.User userAuth = new com.google.api.server.spi.auth.common.User("ok", "email");
	protected Closeable session;

	@BeforeClass
	public static void setUpBeforeClass() {
		ObjectifyService.setFactory(new ObjectifyFactory());
		ObjectifyService.register(Configuration.class);
		ObjectifyService.register(Notification.class);
		ObjectifyService.register(Client.class);
		ObjectifyService.register(Role.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(UserPasswordReset.class);
	}

	@Before
	public void setUp() {
		session = ObjectifyService.begin();
		helper.setUp();
	}

	@After
	public void tearDown() {
		AsyncCacheFilter.complete();
		session.close();
		helper.tearDown();
	}

	private User createUser(String email, String userName, String password, String apiPermission) throws Exception {
		List<Permission> permissions = new ArrayList<>(5);
		permissions.add(new Permission(apiPermission + ".read"));
		permissions.add(new Permission(apiPermission + ".create"));
		permissions.add(new Permission(apiPermission + ".read", true));
		permissions.add(new Permission(apiPermission + ".update", true));
		permissions.add(new Permission(apiPermission + ".list"));
		String name = apiPermission + "_" + System.currentTimeMillis();
		Role role = RoleAPITest.createRole(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		List<Long> roles = new ArrayList<>(1);
		roles.add(role.getId());
		User user = new User(email, userName, password, roles);
		user = usersAPI.create(user, userAuth);
		validateUserFields(user, email, userName, password, roles);
		return user;
	}

	private void validateUserFields(User user, String email, String userName, String password, List<Long> roles) {
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());
		assertNotNull(Constants.USER_MUST_NOT_BE_NULL, user.getUser());
		assertNotNull(Constants.EMAIL_MUST_NOT_BE_NULL, user.getEmail());
		assertNotNull(Constants.PASSWORD_MUST_NOT_BE_NULL, user.getPassword());
		assertNotNull(Constants.ROLES_MUST_NOT_BE_NULL, user.getRoles());
		assertFalse(Constants.USER_MUST_NOT_BE_EMPTY, user.getUser().isEmpty());
		assertFalse(Constants.EMAIL_MUST_NOT_BE_EMPTY, user.getEmail().isEmpty());
		assertFalse(Constants.PASSWORD_MUST_NOT_BE_EMPTY, user.getPassword().isEmpty());
		assertFalse(Constants.ROLES_MUST_NOT_BE_EMPTY, user.getRoles().isEmpty());
		assertTrue(String.format(Constants.USER_MUST_BE_VALUE, userName), user.getUser().equalsIgnoreCase(userName));
		assertTrue(String.format(Constants.EMAIL_MUST_BE_VALUE, email), user.getEmail().equalsIgnoreCase(email));
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		assertTrue(String.format(Constants.PASSWORD_MUST_BE_VALUE, password), passwordEncryptor.checkPassword(password, user.getPassword()));
		if(roles != null) {
			List<Long> result = user.getRoles();
			assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, roles.size() == result.size());
			for (int i = 0; i < roles.size(); i++) {
				assertTrue(String.format(Constants.ROLE_MUST_BE_VALUE, roles.get(i)), result.get(i) == roles.get(i));
			}
		}
	}

	public void validateAuthorization(AuthorizationWrapper authenticate, Client client, List<Permission> permissions, boolean success){
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, authenticate);
		assertNotNull(Constants.AUTHORIZATION_MUST_NOT_BE_NULL, authenticate.getAuthorization());
		assertNotNull(Constants.AUTHORIZATION_KEY_MUST_NOT_BE_NULL, authenticate.getAuthorizationKey());
		assertFalse(Constants.AUTHORIZATION_MUST_NOT_BE_EMPTY, authenticate.getAuthorization().isEmpty());
		assertFalse(Constants.AUTHORIZATION_KEY_MUST_NOT_BE_EMPTY, authenticate.getAuthorization().isEmpty());
		UserAuthenticator userAuthenticator = new UserAuthenticator();
		com.google.api.server.spi.auth.common.User authUser = userAuthenticator.authProduction(client.getId().toString(), permissions.get(0).getRoute(), authenticate.getAuthorization(), authenticate.getAuthorizationKey().toString());
		if (success) {
			assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, authUser);
		} else {
			assertNull(Constants.RESULT_MUST_BE_NULL, authUser);
		}
	}

	@Test
	public void create() throws Exception {
		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = createUser(email, userName, password, "User");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
	}

	@Test
	public void read() throws Exception {
		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = createUser(email, userName, password, "User");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		user = usersAPI.read(user.getId(), userAuth);
		validateUserFields(user, email, userName, password, null);
	}

	@Test
	public void update() throws Exception {
		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = createUser(email, userName, password, "User");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());
		List<Permission> permissions = new ArrayList<>(5);
		permissions.add(new Permission("ClientsAPI.read"));
		permissions.add(new Permission("ClientsAPI.create"));
		permissions.add(new Permission("ClientsAPI.read", true));
		permissions.add(new Permission("ClientsAPI.update", true));
		permissions.add(new Permission("ClientsAPI.list"));
		String name = "update";
		Role role = RoleAPITest.createRole(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		List<Long> roles = new ArrayList<>(1);
		roles.add(role.getId());
		email = "changeduser@user.com";
		userName = "changeduser";
		password = "changed1234*";
		Long id = user.getId();
		user = new User(email, userName, password, roles);
		user = usersAPI.update(id, user, userAuth);
		validateUserFields(user, email, userName, password, roles);
	}

	@Test
	public void delete() throws Exception {
		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = createUser(email, userName, password, "User");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());
		user = usersAPI.delete(user.getId(), userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());
		user = User.getById(user.getId());
		assertNull(Constants.RESULT_MUST_BE_NULL, user);
	}

	@Test
	public void list() throws Exception {
		List<String> sortFields = new ArrayList<>();
		sortFields.add(Constants.USERNAME);
		List<Boolean> sortDirections = new ArrayList<>();
		sortDirections.add(true);

		ListItem list = usersAPI.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, userAuth);
		assertNull(Constants.LIST_MUST_BE_NULL, list.getItems());
		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user;
		for (int i = 0; i < 7; i++) {
			user = createUser(i + "_" + email, i + "_" + userName, password, "User");
			assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
			assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());
		}
		list = usersAPI.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, list);
		assertNotNull(Constants.LIST_MUST_NOT_BE_NULL, list.getItems());
		assertFalse(Constants.LIST_MUST_NOT_BE_EMPTY, list.getItems().isEmpty());
		assertTrue(Constants.LIST_SIZE_MUST_BE_SEVEN, list.getItems().size() == 7);
	}

	@Test
	public void authenticateSuccess() throws Exception {
		Configuration configuration = ConfigurationsAPITest.createConfiguration("cache-timeout", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);

		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = ClientsAPITest.createClient(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);

		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = createUser(email, userName, password, "ClientsAPI");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		user = new User(userName, password);
		AuthorizationWrapper authenticate = usersAPI.authenticate(user, userAuth);
		validateAuthorization(authenticate, client, permissions, true);
	}

	@Test(expected=NotFoundException.class)
	public void authenticateWrongUser() throws Exception {
		Configuration configuration = ConfigurationsAPITest.createConfiguration("cache-timeout", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);

		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = ClientsAPITest.createClient(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);

		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = createUser(email, userName, password, "ClientsAPI");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		user = new User("wrongUser", password);
		usersAPI.authenticate(user, userAuth);
	}

	@Test(expected=UnauthorizedException.class)
	public void authenticateWrongPassword() throws Exception {
		Configuration configuration = ConfigurationsAPITest.createConfiguration("cache-timeout", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);

		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = ClientsAPITest.createClient(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);

		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = createUser(email, userName, password, "ClientsAPI");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		user = new User(userName, "wrongPassword");
		usersAPI.authenticate(user, userAuth);
	}

	@Test
	public void authenticateUnauthorizedOperationForClient() throws Exception {
		Configuration configuration = ConfigurationsAPITest.createConfiguration("cache-timeout", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		List<Permission> permissions = new ArrayList<>();
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = ClientsAPITest.createClient(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);

		permissions.clear();
		permissions = new ArrayList<>();
		permissions.add(new Permission("ClientsAPI.create"));
		name = "admin";
		Role role = new Role(name, permissions);
		role = roleAPI.create(role, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		List<Long> roles = new ArrayList<>(1);
		roles.add(role.getId());
		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = new User(email, userName, password, roles);
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		user = new User(userName, password);
		AuthorizationWrapper authenticate = usersAPI.authenticate(user, userAuth);
		validateAuthorization(authenticate, client, permissions, false);
	}

	@Test
	public void authenticateUnauthorizedOperationForRole() throws Exception {
		Configuration configuration = ConfigurationsAPITest.createConfiguration("cache-timeout", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		List<Permission> permissions = new ArrayList<>();
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = ClientsAPITest.createClient(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);

		permissions.clear();
		permissions = new ArrayList<>();
		permissions.add(new Permission("ClientsAPI.create"));
		name = "admin";
		Role role = new Role(name, permissions);
		role = roleAPI.create(role, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
		List<Long> roles = new ArrayList<>(1);
		roles.add(role.getId());
		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = new User(email, userName, password, roles);
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());
		user = new User(userName, password);
		AuthorizationWrapper authenticate = usersAPI.authenticate(user, userAuth);
		validateAuthorization(authenticate, client, permissions, false);
	}

	@Test
	public void recoverPassword() throws Exception {
		Configuration configuration = ConfigurationsAPITest.createConfiguration("cache-timeout", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);

		configuration = ConfigurationsAPITest.createConfiguration("password_redirect_servlet", "http://localhost:8080/pwd?token=%s", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);

		String sender = "test@test.test";
		String recipients = "1test@test.test;2test@test.test;3test@test.test";
		String subject = "URL recovery";
		String message = "Visite %URL% para seguir el proceso";

		Notification notification = NotificationsAPITest.createNotification(sender, recipients, subject, message, Constants.PASSWORD_RECOVERY_NOTIFICATION);
		notification = notificationsAPI.create(notification, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, notification);


		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = createUser(email, userName, password, "ClientsAPI");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);

		Long id = user.getId();
		user = new User(userName);
		BooleanWrapper booleanWrapper = usersAPI.recoverPassword(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, booleanWrapper);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, booleanWrapper.getResult());
		assertTrue(Constants.RESULT_MUST_BE_TRUE, booleanWrapper.getResult());

		UserPasswordRecovery userPasswordRecovery = new UserPasswordRecovery();
		Map<String, String[]> recoveryParams = new HashMap();
		String[] idArray = {id.toString()};
		String[] notificationArray = {Constants.PASSWORD_RECOVERY_NOTIFICATION};
		recoveryParams.put(Constants.ID, idArray);
		recoveryParams.put(Constants.NOTIFICATION_MNEMONIC, notificationArray);
		userPasswordRecovery.notify(recoveryParams);

		Thread.sleep(2000);

		user = User.getById(id);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());
		assertTrue("status must be 2", user.getPasswordRecoveryStatus() == 2);

		UserPasswordReset userPasswordReset = UserPasswordReset.getUser(user.getId());
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, userPasswordReset);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, userPasswordReset.getId());
		assertNotNull(Constants.WEB_TOKEN_MUST_NOT_BE_NULL, userPasswordReset.getWebToken());
		assertNotNull(Constants.SECRET_MUST_NOT_BE_NULL, userPasswordReset.getSecret());
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, userPasswordReset.getIdUser());
		assertFalse(Constants.WEB_TOKEN_MUST_NOT_BE_EMPTY, userPasswordReset.getWebToken().isEmpty());
		assertFalse(Constants.SECRET_MUST_NOT_BE_EMPTY, userPasswordReset.getSecret().isEmpty());

		userPasswordReset = UserPasswordReset.getByToken(userPasswordReset.getWebToken());
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, userPasswordReset);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, userPasswordReset.getId());
		assertNotNull(Constants.WEB_TOKEN_MUST_NOT_BE_NULL, userPasswordReset.getWebToken());
		assertNotNull(Constants.SECRET_MUST_NOT_BE_NULL, userPasswordReset.getSecret());
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, userPasswordReset.getIdUser());
		assertFalse(Constants.WEB_TOKEN_MUST_NOT_BE_EMPTY, userPasswordReset.getWebToken().isEmpty());
		assertFalse(Constants.SECRET_MUST_NOT_BE_EMPTY, userPasswordReset.getSecret().isEmpty());

		PasswordRedirect passwordRedirect = new PasswordRedirect();
		boolean processRequest = passwordRedirect.processRequest(userPasswordReset.getWebToken());

		Thread.sleep(2000);

		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, processRequest);
		assertFalse(Constants.RESULT_MUST_BE_FALSE, processRequest);
		userPasswordReset = UserPasswordReset.getUser(user.getId());
		assertNull(Constants.RESULT_MUST_BE_NULL, userPasswordReset);

		user = new User(userName, password);
		booleanWrapper = usersAPI.updatePassword(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, booleanWrapper);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, booleanWrapper.getResult());
		assertTrue(Constants.RESULT_MUST_BE_TRUE, booleanWrapper.getResult());
		user = User.getById(id);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertTrue("Password staus must be 0", user.getPasswordRecoveryStatus() == 0);
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		assertTrue(String.format(Constants.PASSWORD_MUST_BE_VALUE, password), passwordEncryptor.checkPassword(password, user.getPassword()));
	}

	@Test
	public void checkUser() throws Exception {
		Configuration configuration = ConfigurationsAPITest.createConfiguration("cache-timeout", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);

		List<Permission> permissions = new ArrayList<>();
		permissions.clear();
		permissions = new ArrayList<>();
		permissions.add(new Permission("ClientsAPI.create"));
		String name = "admin";
		Role role = new Role(name, permissions);
		role = roleAPI.create(role, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
		List<Long> roles = new ArrayList<>(1);
		roles.add(role.getId());
		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = new User(email, userName, password, roles);
		BooleanWrapper booleanWrapper = usersAPI.checkUser(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, booleanWrapper);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, booleanWrapper.getResult());
		assertTrue(Constants.RESULT_MUST_BE_TRUE, booleanWrapper.getResult());
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());
		booleanWrapper = usersAPI.checkUser(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, booleanWrapper);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, booleanWrapper.getResult());
		assertFalse(Constants.RESULT_MUST_BE_FALSE, booleanWrapper.getResult());
	}

}