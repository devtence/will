package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.users.Role;
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
public class RoleAPITest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final RoleAPI roleAPI = new RoleAPI();
	private final User user = new User("ok", "email");
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
		role = roleAPI.create(role, user);
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
		role = roleAPI.create(role, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
		role = roleAPI.read(role.getId(), user);
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
		Role role = new Role(name, permissions);
		role = roleAPI.create(role, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
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
		assertTrue(String.format(Constants.NAME_MUST_BE_VALUE, name), role.getName().equalsIgnoreCase(name));
		List<Permission> result = role.getPermissions();
		assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, permissions.size() == result.size());
		for (int i = 0; i < permissions.size(); i++) {
			assertTrue(String.format(Constants.PERMISSION_MUST_BE_VALUE, permissions.get(i).getRoute()), result.get(i).getRoute().equalsIgnoreCase(permissions.get(i).getRoute()));
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
		role = roleAPI.create(role, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
		role = roleAPI.delete(role.getId(), user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
		role = Role.getById(role.getId());
		assertNull(Constants.RESULT_MUST_BE_NULL, role);
	}

	@Test
	public void list() throws Exception {
		ListItem list = roleAPI.list(Constants.INDEX, Constants.OFFSET, Constants.NAME, Constants.ASC, null, user);
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

		String name = "0app";
		Role role = new Role(name, permissions);
		role = roleAPI.create(role, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());

		name = "1app";
		role = new Role(name, permissions);
		role = roleAPI.create(role, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());

		name = "2app";
		role = new Role(name, permissions);
		role = roleAPI.create(role, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());

		name = "3app";
		role = new Role(name, permissions);
		role = roleAPI.create(role, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());

		name = "4app";
		role = new Role(name, permissions);
		role = roleAPI.create(role, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());

		name = "5app";
		role = new Role(name, permissions);
		role = roleAPI.create(role, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());

		name = "6app";
		role = new Role(name, permissions);
		role = roleAPI.create(role, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());

		//</editor-fold>

		list = roleAPI.list(0, 100, Constants.NAME, Constants.ASC, null, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, list);
		assertNotNull(Constants.LIST_MUST_NOT_BE_NULL, list.getItems());
		assertFalse(Constants.LIST_MUST_NOT_BE_EMPTY, list.getItems().isEmpty());
		assertTrue(Constants.LIST_SIZE_MUST_BE_SEVEN, list.getItems().size() == 7);
	}

}