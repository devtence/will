package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.authenticators.UserAuthenticator;
import com.devtence.will.dev.commons.wrappers.AuthorizationWrapper;
import com.devtence.will.dev.commons.wrappers.BooleanWrapper;
import com.devtence.will.dev.endpoints.commons.ConfigurationsAPI;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.users.*;
import com.devtence.will.dev.servlets.PasswordRedirect;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by plessmann on 09/06/16.
 */
public class UsersAPITest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final LocalServiceTestHelper taskQueueHelper = new LocalServiceTestHelper(new LocalTaskQueueTestConfig());
	private final UsersAPI usersAPI = new UsersAPI();
	private final RoleAPI roleAPI = new RoleAPI();
	private final ClientsAPI clientsAPI = new ClientsAPI();
	private final ConfigurationsAPI configurationsAPI = new ConfigurationsAPI();
	private final com.google.api.server.spi.auth.common.User userAuth = new com.google.api.server.spi.auth.common.User("ok", "email");
	protected Closeable session;

	@BeforeClass
	public static void setUpBeforeClass() {
		ObjectifyService.setFactory(new ObjectifyFactory());
	}

	@Before
	public void setUp() {
		session = ObjectifyService.begin();
		helper.setUp();
		taskQueueHelper.setUp();
	}

	@After
	public void tearDown() {
		AsyncCacheFilter.complete();
		session.close();
		helper.tearDown();
		taskQueueHelper.tearDown();
	}

	@Test
	public void create() throws Exception {
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("RolesAPI.read"));
		permissions.add(new Permission("RolesAPI.create"));
		permissions.add(new Permission("RolesAPI.read", true));
		permissions.add(new Permission("RolesAPI.update", true));
		permissions.add(new Permission("RolesAPI.authenticate"));
		permissions.add(new Permission("RolesAPI.recoverPassword"));
		permissions.add(new Permission("RolesAPI.updatePassword"));
		permissions.add(new Permission("RolesAPI.checkUser"));
		String name = "app";
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
		assertNotNull(Constants.USER_MUST_NOT_BE_NULL, user.getUser());
		assertNotNull(Constants.EMAIL_MUST_NOT_BE_NULL, user.getEmail());
		assertNotNull(Constants.PASSWORD_MUST_NOT_BE_NULL, user.getPassword());
		assertNotNull(Constants.ROLES_MUST_NOT_BE_NULL, user.getRoles());
		assertFalse(Constants.USER_MUST_NOT_BE_EMPTY, user.getUser().isEmpty());
		assertFalse(Constants.EMAIL_MUST_NOT_BE_EMPTY, user.getEmail().isEmpty());
		assertFalse(Constants.PASSWORD_MUST_NOT_BE_EMPTY, user.getPassword().isEmpty());
		assertFalse(Constants.ROLES_MUST_NOT_BE_EMPTY, user.getRoles().isEmpty());
		assertTrue(String.format(Constants.USER_MUST_BE_VALUE, name), user.getUser().equalsIgnoreCase(userName));
		assertTrue(String.format(Constants.EMAIL_MUST_BE_VALUE, name), user.getEmail().equalsIgnoreCase(email));
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		assertTrue(String.format(Constants.PASSWORD_MUST_BE_VALUE, name), passwordEncryptor.checkPassword(password, user.getPassword()));
		List<Long> result = user.getRoles();
		assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, roles.size() == result.size());
		for (int i = 0; i < roles.size(); i++) {
			assertTrue(String.format(Constants.ROLE_MUST_BE_VALUE, roles.get(i)), result.get(i) == roles.get(i));
		}

	}

	@Test
	public void read() throws Exception {
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("RolesAPI.read"));
		permissions.add(new Permission("RolesAPI.create"));
		permissions.add(new Permission("RolesAPI.read", true));
		permissions.add(new Permission("RolesAPI.update", true));
		permissions.add(new Permission("RolesAPI.authenticate"));
		permissions.add(new Permission("RolesAPI.recoverPassword"));
		permissions.add(new Permission("RolesAPI.updatePassword"));
		permissions.add(new Permission("RolesAPI.checkUser"));
		String name = "app";
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
		user = usersAPI.read(user.getId(), userAuth);
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
		assertTrue(String.format(Constants.USER_MUST_BE_VALUE, name), user.getUser().equalsIgnoreCase(userName));
		assertTrue(String.format(Constants.EMAIL_MUST_BE_VALUE, name), user.getEmail().equalsIgnoreCase(email));
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		assertTrue(String.format(Constants.PASSWORD_MUST_BE_VALUE, name), passwordEncryptor.checkPassword(password, user.getPassword()));
		List<Long> result = user.getRoles();
		assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, roles.size() == result.size());
		for (int i = 0; i < roles.size(); i++) {
			assertTrue(String.format(Constants.ROLE_MUST_BE_VALUE, roles.get(i)), result.get(i) == roles.get(i));
		}
	}

	@Test
	public void update() throws Exception {
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("RolesAPI.read"));
		permissions.add(new Permission("RolesAPI.create"));
		permissions.add(new Permission("RolesAPI.read", true));
		permissions.add(new Permission("RolesAPI.update", true));
		permissions.add(new Permission("RolesAPI.authenticate"));
		permissions.add(new Permission("RolesAPI.recoverPassword"));
		permissions.add(new Permission("RolesAPI.updatePassword"));
		permissions.add(new Permission("RolesAPI.checkUser"));
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
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());
		permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		permissions.add(new Permission("ClientsAPI.create"));
		permissions.add(new Permission("ClientsAPI.read", true));
		permissions.add(new Permission("ClientsAPI.update", true));
		permissions.add(new Permission("ClientsAPI.authenticate"));
		permissions.add(new Permission("ClientsAPI.recoverPassword"));
		permissions.add(new Permission("ClientsAPI.updatePassword"));
		permissions.add(new Permission("ClientsAPI.checkUser"));
		name = "user";
		role = new Role(name, permissions);
		role = roleAPI.create(role, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
		roles.add(role.getId());
		email = "changeduser@user.com";
		userName = "changeduser";
		password = "changed1234*";
		Long id = user.getId();
		user = new User(email, userName, password, roles);
		user = usersAPI.update(id, user, userAuth);
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
		assertTrue(String.format(Constants.USER_MUST_BE_VALUE, name), user.getUser().equalsIgnoreCase(userName));
		assertTrue(String.format(Constants.EMAIL_MUST_BE_VALUE, name), user.getEmail().equalsIgnoreCase(email));
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		assertTrue(String.format(Constants.PASSWORD_MUST_BE_VALUE, name), passwordEncryptor.checkPassword(password, user.getPassword()));
		List<Long> result = user.getRoles();
		assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, roles.size() == result.size());
		for (int i = 0; i < roles.size(); i++) {
			assertTrue(String.format(Constants.ROLE_MUST_BE_VALUE, roles.get(i)), result.get(i) == roles.get(i));
		}

	}

	@Test
	public void delete() throws Exception {
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("RolesAPI.read"));
		permissions.add(new Permission("RolesAPI.create"));
		permissions.add(new Permission("RolesAPI.read", true));
		permissions.add(new Permission("RolesAPI.update", true));
		permissions.add(new Permission("RolesAPI.authenticate"));
		permissions.add(new Permission("RolesAPI.recoverPassword"));
		permissions.add(new Permission("RolesAPI.updatePassword"));
		permissions.add(new Permission("RolesAPI.checkUser"));
		String name = "app";
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
		user = usersAPI.delete(user.getId(), userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());
		user = User.getById(user.getId());
		assertNull(Constants.RESULT_MUST_BE_NULL, user);
	}

	@Test
	public void list() throws Exception {
		ListItem list = usersAPI.list(Constants.INDEX, Constants.OFFSET, Constants.USERNAME, Constants.ASC, null, userAuth);
		assertNull(Constants.LIST_MUST_BE_NULL, list.getItems());

		//<editor-fold desc="Notifications creation">
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("RolesAPI.read"));
		permissions.add(new Permission("RolesAPI.create"));
		permissions.add(new Permission("RolesAPI.read", true));
		permissions.add(new Permission("RolesAPI.update", true));
		permissions.add(new Permission("RolesAPI.authenticate"));
		permissions.add(new Permission("RolesAPI.recoverPassword"));
		permissions.add(new Permission("RolesAPI.updatePassword"));
		permissions.add(new Permission("RolesAPI.checkUser"));
		String name = "app";
		Role role = new Role(name, permissions);
		role = roleAPI.create(role, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
		List<Long> roles = new ArrayList<>(1);
		roles.add(role.getId());

		String email = "0user@user.com";
		String userName = "0user";
		String password = "1234*";
		User user = new User(email, userName, password, roles);
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		email = "1user@user.com";
		userName = "1user";
		user = new User(email, userName, password, roles);
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		email = "2user@user.com";
		userName = "2user";
		user = new User(email, userName, password, roles);
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		email = "3user@user.com";
		userName = "3user";
		user = new User(email, userName, password, roles);
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		email = "4user@user.com";
		userName = "4user";
		user = new User(email, userName, password, roles);
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		email = "5user@user.com";
		userName = "5user";
		user = new User(email, userName, password, roles);
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		email = "6user@user.com";
		userName = "6user";
		user = new User(email, userName, password, roles);
		user = usersAPI.create(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, user);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, user.getId());

		//</editor-fold>

		list = usersAPI.list(Constants.INDEX, Constants.OFFSET, Constants.USERNAME, Constants.ASC, null, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, list);
		assertNotNull(Constants.LIST_MUST_NOT_BE_NULL, list.getItems());
		assertFalse(Constants.LIST_MUST_NOT_BE_EMPTY, list.getItems().isEmpty());
		assertTrue(Constants.LIST_SIZE_MUST_BE_SEVEN, list.getItems().size() == 7);
	}

	@Test
	public void authenticateSuccess() throws Exception {
		Configuration configuration = new Configuration("cache-timeout", "1", "Test String");
		configuration = configurationsAPI.create(configuration, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = new Client(name, permissions);
		client = clientsAPI.create(client, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, client.getId());
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
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, authenticate);
		assertNotNull(Constants.AUTHORIZATION_MUST_NOT_BE_NULL, authenticate.getAuthorization());
		assertNotNull(Constants.AUTHORIZATION_KEY_MUST_NOT_BE_NULL, authenticate.getAuthorizationKey());
		assertFalse(Constants.AUTHORIZATION_MUST_NOT_BE_EMPTY, authenticate.getAuthorization().isEmpty());
		assertFalse(Constants.AUTHORIZATION_KEY_MUST_NOT_BE_EMPTY, authenticate.getAuthorization().isEmpty());
		UserAuthenticator userAuthenticator = new UserAuthenticator();
		com.google.api.server.spi.auth.common.User authUser = userAuthenticator.authProduction(client.getId().toString(), permissions.get(0).getRoute(), authenticate.getAuthorization(), authenticate.getAuthorizationKey().toString());
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, authUser);
	}

	@Test(expected=NotFoundException.class)
	public void authenticateWrongUser() throws Exception {
		Configuration configuration = new Configuration("cache-timeout", "1", "Test String");
		configuration = configurationsAPI.create(configuration, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = new Client(name, permissions);
		client = clientsAPI.create(client, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, client.getId());
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
		user = new User("wrongUser", password);
		usersAPI.authenticate(user, userAuth);
	}

	@Test(expected=UnauthorizedException.class)
	public void authenticateWrongPassword() throws Exception {
		Configuration configuration = new Configuration("cache-timeout", "1", "Test String");
		configuration = configurationsAPI.create(configuration, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = new Client(name, permissions);
		client = clientsAPI.create(client, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, client.getId());
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
		user = new User(userName, "wrongPassword");
		usersAPI.authenticate(user, userAuth);
	}

	@Test
	public void authenticateUnauthorizedOperationForClient() throws Exception {
		Configuration configuration = new Configuration("cache-timeout", "1", "Test String");
		configuration = configurationsAPI.create(configuration, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		List<Permission> permissions = new ArrayList<>();
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = new Client(name, permissions);
		client = clientsAPI.create(client, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, client.getId());
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
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, authenticate);
		assertNotNull(Constants.AUTHORIZATION_MUST_NOT_BE_NULL, authenticate.getAuthorization());
		assertNotNull(Constants.AUTHORIZATION_KEY_MUST_NOT_BE_NULL, authenticate.getAuthorizationKey());
		assertFalse(Constants.AUTHORIZATION_MUST_NOT_BE_EMPTY, authenticate.getAuthorization().isEmpty());
		assertFalse(Constants.AUTHORIZATION_KEY_MUST_NOT_BE_EMPTY, authenticate.getAuthorization().isEmpty());
		UserAuthenticator userAuthenticator = new UserAuthenticator();
		com.google.api.server.spi.auth.common.User authUser = userAuthenticator.authProduction(client.getId().toString(), permissions.get(0).getRoute(), authenticate.getAuthorization(), authenticate.getAuthorizationKey().toString());
		assertNull(Constants.RESULT_MUST_BE_NULL, authUser);
	}

	@Test
	public void authenticateUnauthorizedOperationForRole() throws Exception {
		Configuration configuration = new Configuration("cache-timeout", "1", "Test String");
		configuration = configurationsAPI.create(configuration, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		List<Permission> permissions = new ArrayList<>();
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = new Client(name, permissions);
		client = clientsAPI.create(client, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, client.getId());
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
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, authenticate);
		assertNotNull(Constants.AUTHORIZATION_MUST_NOT_BE_NULL, authenticate.getAuthorization());
		assertNotNull(Constants.AUTHORIZATION_KEY_MUST_NOT_BE_NULL, authenticate.getAuthorizationKey());
		assertFalse(Constants.AUTHORIZATION_MUST_NOT_BE_EMPTY, authenticate.getAuthorization().isEmpty());
		assertFalse(Constants.AUTHORIZATION_KEY_MUST_NOT_BE_EMPTY, authenticate.getAuthorization().isEmpty());
		UserAuthenticator userAuthenticator = new UserAuthenticator();
		com.google.api.server.spi.auth.common.User authUser = userAuthenticator.authProduction(client.getId().toString(), "ClientsAPI.read", authenticate.getAuthorization(), authenticate.getAuthorizationKey().toString());
		assertNull(Constants.RESULT_MUST_BE_NULL, authUser);
	}

	@Ignore
	@Test
	public void recoverPassword() throws Exception {
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("RolesAPI.read"));
		permissions.add(new Permission("RolesAPI.create"));
		permissions.add(new Permission("RolesAPI.read", true));
		permissions.add(new Permission("RolesAPI.update", true));
		permissions.add(new Permission("RolesAPI.authenticate"));
		permissions.add(new Permission("RolesAPI.recoverPassword"));
		permissions.add(new Permission("RolesAPI.updatePassword"));
		permissions.add(new Permission("RolesAPI.checkUser"));
		String name = "app";
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
		BooleanWrapper booleanWrapper = usersAPI.recoverPassword(user, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, booleanWrapper);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, booleanWrapper.getResult());
		assertTrue(Constants.RESULT_MUST_BE_TRUE, booleanWrapper.getResult());

		UserPasswordReset userPasswordReset = UserPasswordReset.getUser(user.getId());
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, userPasswordReset);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, userPasswordReset.getId());
		assertNotNull(Constants.WEB_TOKEN_MUST_NOT_BE_NULL, userPasswordReset.getWebToken());
		assertNotNull(Constants.SECRET_MUST_NOT_BE_NULL, userPasswordReset.getSecret());
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, userPasswordReset.getIdUser());
		assertFalse(Constants.WEB_TOKEN_MUST_NOT_BE_EMPTY, userPasswordReset.getWebToken().isEmpty());
		assertFalse(Constants.SECRET_MUST_NOT_BE_EMPTY, userPasswordReset.getSecret().isEmpty());

		PasswordRedirect passwordRedirect = new PasswordRedirect();
		boolean processRequest = passwordRedirect.processRequest(userPasswordReset.getWebToken());
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, processRequest);
		assertTrue(Constants.RESULT_MUST_BE_TRUE, processRequest);
	}

	@Test
	public void updatePassword() throws Exception {

	}

	@Test
	public void checkUser() throws Exception {
		Configuration configuration = new Configuration("cache-timeout", "1", "Test String");
		configuration = configurationsAPI.create(configuration, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		List<Permission> permissions = new ArrayList<>();
		permissions.add(new Permission("ClientsAPI.read"));
		String name = "app";
		Client client = new Client(name, permissions);
		client = clientsAPI.create(client, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, client.getId());
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