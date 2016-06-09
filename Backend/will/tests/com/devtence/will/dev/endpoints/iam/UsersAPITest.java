package com.devtence.will.dev.endpoints.iam;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.users.Permission;
import com.devtence.will.dev.models.users.Role;
import com.devtence.will.dev.models.users.User;
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
public class UsersAPITest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final UsersAPI usersAPI = new UsersAPI();
	private final RoleAPI roleAPI = new RoleAPI();
	private final com.google.api.server.spi.auth.common.User userAuth = new com.google.api.server.spi.auth.common.User("ok", "email");
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
		role = roleAPI.create(role, userAuth);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, role);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, role.getId());
		List<Long> roles = new ArrayList<>(1);
		roles.add(role.getId());
		String email = "user@user.com";
		String userName = "user";
		String password = "1234*";
		User user = new User(email, userName, password, roles);

	}

	@Test
	public void read() throws Exception {

	}

	@Test
	public void update() throws Exception {

	}

	@Test
	public void delete() throws Exception {

	}

	@Test
	public void list() throws Exception {

	}

	@Test
	public void authenticate() throws Exception {

	}

	@Test
	public void recoverPassword() throws Exception {

	}

	@Test
	public void updatePassword() throws Exception {

	}

	@Test
	public void checkUser() throws Exception {

	}

}