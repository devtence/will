package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.caches.RolesCache;
import com.devtence.will.dev.endpoints.commons.ConfigurationsAPITest;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.users.Permission;
import com.devtence.will.dev.models.users.Role;
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
public class RoleAPITest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final static RoleAPI roleAPI = new RoleAPI();
	private final static User user = new User("ok", "email");
	protected Closeable session;

	@BeforeClass
	public static void setUpBeforeClass() {
		ObjectifyService.setFactory(new ObjectifyFactory());
		ObjectifyService.register(Configuration.class);
		ObjectifyService.register(Role.class);
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

	public static Role createRole(String name, List<Permission> permissions) throws Exception {
		Role role = new Role(name, permissions);
		role = roleAPI.create(role, user);
		validateRole(role, name, permissions);
		return role;
	}

	private static void validateRole(Role role, String name, List<Permission> permissions) throws Exception {
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
		assertNotNull(Constants.NAME_MUST_NOT_BE_NULL, role.getName());
		assertNotNull(Constants.PERMISSION_MUST_NOT_BE_NULL, role.getPermissions());
		assertFalse(Constants.NAME_MUST_NOT_BE_EMPTY, role.getName().isEmpty());
		assertFalse(Constants.PERMISSION_MUST_NOT_BE_EMPTY, role.getPermissions().isEmpty());
		assertTrue(String.format(Constants.NAME_MUST_BE_VALUE, name), role.getName().equalsIgnoreCase(name));
		List<Permission> result = role.getPermissions();
		assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, permissions.size() == result.size());
		for (int i = 0; i < permissions.size(); i++) {
			assertTrue(String.format(Constants.PERMISSION_MUST_BE_VALUE, permissions.get(i).getRoute()), result.get(i).getRoute().equalsIgnoreCase(permissions.get(i).getRoute()));
		}
		Configuration configuration = ConfigurationsAPITest.createConfiguration("cache-timeout", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		configuration = ConfigurationsAPITest.createConfiguration("use-cache", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		role = RolesCache.getInstance().getElement(role.getId());
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
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
		Role role = createRole(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
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
		Role role = createRole(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		role = roleAPI.read(role.getId(), user);
		validateRole(role, name, permissions);
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
		String name = "app";
		Role role = createRole(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		Long id = role.getId();
		permissions.clear();
		permissions.add(new Permission("changedRolesAPI.read"));
		permissions.add(new Permission("changedRolesAPI.create"));
		permissions.add(new Permission("changedRolesAPI.read", true));
		permissions.add(new Permission("changedRolesAPI.update", true));
		permissions.add(new Permission("changedRolesAPI.authenticate"));
		permissions.add(new Permission("changedRolesAPI.recoverPassword"));
		permissions.add(new Permission("changedRolesAPI.updatePassword"));
		permissions.add(new Permission("changedRolesAPI.checkUser"));
		name = "changedapp";
		role = new Role(name, permissions);
		role = roleAPI.update(id, role, user);
		validateRole(role, name, permissions);
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
		Role role = createRole(name, permissions);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		role = roleAPI.delete(role.getId(), user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
		role = Role.getById(role.getId());
		assertNull(Constants.RESULT_MUST_BE_NULL, role);
	}

	@Test
	public void list() throws Exception {
		List<String> sortFields = new ArrayList<>();
		sortFields.add(Constants.NAME);
		List<Boolean> sortDirections = new ArrayList<>();
		sortDirections.add(true);
		ListItem list = roleAPI.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
		assertNull(Constants.LIST_MUST_BE_NULL, list.getItems());

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
		Role role;

		for (int i = 0; i < 7; i++) {
			role = createRole(i + "_" + name, permissions);
			assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		}

		list = roleAPI.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, list);
		assertNotNull(Constants.LIST_MUST_NOT_BE_NULL, list.getItems());
		assertFalse(Constants.LIST_MUST_NOT_BE_EMPTY, list.getItems().isEmpty());
		assertTrue(Constants.LIST_SIZE_MUST_BE_SEVEN, list.getItems().size() == 7);
	}

}