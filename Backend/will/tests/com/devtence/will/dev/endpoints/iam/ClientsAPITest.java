package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.caches.ClientsCache;
import com.devtence.will.dev.endpoints.commons.ConfigurationsAPITest;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.users.Client;
import com.devtence.will.dev.models.users.Permission;
import com.google.api.server.spi.auth.common.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by plessmann on 09/06/16.
 */
public class ClientsAPITest {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final static ClientsAPI clientsAPI = new ClientsAPI();
	private final static User user = new User("ok", "email");
	protected Closeable session;

	@BeforeClass
	public static void setUpBeforeClass() {
		ObjectifyService.setFactory(new ObjectifyFactory());
	}

	@Before
	public void setUp() {
		this.session = ObjectifyService.begin();
		helper.setUp();
	}

	@After
	public void tearDown() {
		AsyncCacheFilter.complete();
		this.session.close();
		this.helper.tearDown();
	}

	public static Client createClient(String name, List<Permission> permissions) throws Exception {
		Client client = new Client(name, permissions);
		client = clientsAPI.create(client, user);
		validateClient(client, name, permissions);
		return client;
	}

	private static void validateClient(Client client, String name, List<Permission> permissions) throws Exception {
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, client.getId());
		assertNotNull(Constants.NAME_MUST_NOT_BE_NULL, client.getName());
		assertNotNull(Constants.PERMISSION_MUST_NOT_BE_NULL, client.getPermissions());
		assertFalse(Constants.NAME_MUST_NOT_BE_EMPTY, client.getName().isEmpty());
		assertFalse(Constants.PERMISSION_MUST_NOT_BE_EMPTY, client.getPermissions().isEmpty());
		assertTrue(String.format(Constants.NAME_MUST_BE_VALUE, name), client.getName().equalsIgnoreCase(name));
		List<Permission> result = client.getPermissions();
		assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, permissions.size() == result.size());
		for (int i = 0; i < permissions.size(); i++) {
			assertTrue(String.format(Constants.PERMISSION_MUST_BE_VALUE, permissions.get(i).getRoute()), result.get(i).getRoute().equalsIgnoreCase(permissions.get(i).getRoute()));
		}
		Configuration configuration = ConfigurationsAPITest.createConfiguration("cache-timeout", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		configuration = ConfigurationsAPITest.createConfiguration("use-cache", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		client = ClientsCache.getInstance().getElement(client.getId());
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
	}

	@Test
	public void create() throws Exception {
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		permissions.add(new Permission("ClientsAPI.create"));
		permissions.add(new Permission("ClientsAPI.read", true));
		permissions.add(new Permission("ClientsAPI.update", true));
		permissions.add(new Permission("ClientsAPI.authenticate"));
		permissions.add(new Permission("ClientsAPI.recoverPassword"));
		permissions.add(new Permission("ClientsAPI.updatePassword"));
		permissions.add(new Permission("ClientsAPI.checkUser"));
		String name = "app";
		Client client = createClient(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
	}

	@Test
	public void read() throws Exception {
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		permissions.add(new Permission("ClientsAPI.create"));
		permissions.add(new Permission("ClientsAPI.read", true));
		permissions.add(new Permission("ClientsAPI.update", true));
		permissions.add(new Permission("ClientsAPI.authenticate"));
		permissions.add(new Permission("ClientsAPI.recoverPassword"));
		permissions.add(new Permission("ClientsAPI.updatePassword"));
		permissions.add(new Permission("ClientsAPI.checkUser"));
		String name = "app";
		Client client = createClient(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		client = clientsAPI.read(client.getId(), user);
		validateClient(client, name, permissions);
	}

	@Test
	public void update() throws Exception {
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		permissions.add(new Permission("ClientsAPI.create"));
		permissions.add(new Permission("ClientsAPI.read", true));
		permissions.add(new Permission("ClientsAPI.update", true));
		permissions.add(new Permission("ClientsAPI.authenticate"));
		permissions.add(new Permission("ClientsAPI.recoverPassword"));
		permissions.add(new Permission("ClientsAPI.updatePassword"));
		permissions.add(new Permission("ClientsAPI.checkUser"));
		String name = "app";
		Client client = createClient(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		Long id = client.getId();
		permissions.clear();
		permissions.add(new Permission("changedClientsAPI.read"));
		permissions.add(new Permission("changedClientsAPI.create"));
		permissions.add(new Permission("changedClientsAPI.read", true));
		permissions.add(new Permission("changedClientsAPI.update", true));
		permissions.add(new Permission("changedClientsAPI.authenticate"));
		permissions.add(new Permission("changedClientsAPI.recoverPassword"));
		permissions.add(new Permission("changedClientsAPI.updatePassword"));
		permissions.add(new Permission("changedClientsAPI.checkUser"));
		name = "changedapp";
		client = new Client(name, permissions);
		client = clientsAPI.update(id, client, user);
		validateClient(client, name, permissions);
	}

	@Test
	public void delete() throws Exception {
		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		permissions.add(new Permission("ClientsAPI.create"));
		permissions.add(new Permission("ClientsAPI.read", true));
		permissions.add(new Permission("ClientsAPI.update", true));
		permissions.add(new Permission("ClientsAPI.authenticate"));
		permissions.add(new Permission("ClientsAPI.recoverPassword"));
		permissions.add(new Permission("ClientsAPI.updatePassword"));
		permissions.add(new Permission("ClientsAPI.checkUser"));
		String name = "app";
		Client client = createClient(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		client = clientsAPI.delete(client.getId(), user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, client.getId());
		client = Client.getById(client.getId());
		assertNull(Constants.RESULT_MUST_BE_NULL, client);
	}

	@Test
	public void list() throws Exception {
		ListItem list = clientsAPI.list(Constants.INDEX, Constants.OFFSET, Constants.NAME, Constants.ASC, null, user);
		assertNull(Constants.LIST_MUST_BE_NULL, list.getItems());

		List<Permission> permissions = new ArrayList<>(8);
		permissions.add(new Permission("ClientsAPI.read"));
		permissions.add(new Permission("ClientsAPI.create"));
		permissions.add(new Permission("ClientsAPI.read", true));
		permissions.add(new Permission("ClientsAPI.update", true));
		permissions.add(new Permission("ClientsAPI.authenticate"));
		permissions.add(new Permission("ClientsAPI.recoverPassword"));
		permissions.add(new Permission("ClientsAPI.updatePassword"));
		permissions.add(new Permission("ClientsAPI.checkUser"));
		String name = "app";
		Client client;

		for (int i = 0; i < 7; i++) {
			client = createClient(i + "_" + name, permissions);
			assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, client);
		}

		list = clientsAPI.list(0, 100, Constants.NAME, Constants.ASC, null, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, list);
		assertNotNull(Constants.LIST_MUST_NOT_BE_NULL, list.getItems());
		assertFalse(Constants.LIST_MUST_NOT_BE_EMPTY, list.getItems().isEmpty());
		assertTrue(Constants.LIST_SIZE_MUST_BE_SEVEN, list.getItems().size() == 7);
	}

}