package com.devtence.will.dev.endpoints.commons;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.commons.Configuration;
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
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by plessmann on 09/06/16.
 */
public class ConfigurationsAPITest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final static ConfigurationsAPI configurationsAPI = new ConfigurationsAPI();
	private final static User user = new User("ok", "email");
	protected Closeable session;

	@BeforeClass
	public static void setUpBeforeClass() {
		ObjectifyService.setFactory(new ObjectifyFactory());
		ObjectifyService.register(Configuration.class);
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
	public static Configuration createConfiguration(String configKey, String configValue, String description) throws Exception {
		Configuration configuration = new Configuration(configKey, configValue, description);
		configuration = configurationsAPI.create(configuration, user);
		validateConfiguration(configuration, configKey, configValue, description);
		return configuration;
	}

	private static void validateConfiguration(Configuration configuration, String configKey, String configValue, String description){
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		assertNotNull(Constants.CONFIG_KEY_MUST_NOT_BE_NULL, configuration.getConfigKey());
		assertNotNull(Constants.DESCRIPTION_MUST_NOT_BE_NULL, configuration.getDescription());
		assertNotNull(Constants.VALUE_MUST_NOT_BE_NULL, configuration.getValue());
		assertFalse(Constants.CONFIG_KEY_MUST_NOT_BE_EMPTY, configuration.getConfigKey().isEmpty());
		assertFalse(Constants.DESCRIPTION_MUST_NOT_BE_EMPTY, configuration.getDescription().isEmpty());
		assertFalse(Constants.VALUE_MUST_NOT_BE_EMPTY, configuration.getValue().isEmpty());
		assertTrue(String.format(Constants.CONFIG_KEY_MUST_BE_VALUE, configKey), configuration.getConfigKey().equalsIgnoreCase(configKey));
		assertTrue(String.format(Constants.DESCRIPTION_MUST_BE_VALUE, description), configuration.getDescription().equalsIgnoreCase(description));
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, configValue), configuration.getValue().equalsIgnoreCase(configValue));
	}

	@Test
	public void create() throws Exception {
		String configKey = "test-config-String";
		String configValue = "String";
		String description = "Test String";
		Configuration configuration = createConfiguration(configKey, configValue, description);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);

		configKey = "test-config-Int";
		configValue = "1";
		description = "Test Int";
		configuration = createConfiguration(configKey, configValue, description);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, configValue), configuration.getInt() == Integer.parseInt(configValue));

		configKey = "test-config-Long";
		configValue = ""+System.currentTimeMillis();
		description = "Test Long";
		configuration = createConfiguration(configKey, configValue, description);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, configValue), configuration.getLong() == Long.parseLong(configValue));

		configKey = "test-config-Array";
		configValue = "Strign;array;of;strings";
		description = "Test Array";
		configuration = createConfiguration(configKey, configValue, description);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		String[] result = configuration.getStringArray(Constants.SEPARATOR);
		String[] base = configValue.split(Constants.SEPARATOR);
		assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, base.length == result.length);
		for (int i = 0; i < base.length; i++) {
			assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, base[i]), result[i].equalsIgnoreCase(base[i]));
		}

		configKey = "test-config-Long";
		configValue = "0.8";
		description = "Test Float";
		configuration = createConfiguration(configKey, configValue, description);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, configValue), configuration.getFloat() == Float.parseFloat(configValue));

		configKey = "test-config-Long";
		configValue = "1.89665654";
		description = "Test Double";
		configuration = createConfiguration(configKey, configValue, description);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, configValue), configuration.getDouble() == Double.parseDouble(configValue));

		configKey = "test-config-Long";
		configValue = "1";
		description = "Test Boolean";
		configuration = createConfiguration(configKey, configValue, description);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, configValue), configuration.getBoolean());
	}

	@Test
	public void read() throws Exception {
		String configKey = "test-config-String";
		String configValue = "String";
		String description = "Test String";
		Configuration configuration = createConfiguration(configKey, configValue, description);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		configuration = configurationsAPI.read(configuration.getId(), user);
		validateConfiguration(configuration, configKey, configValue, description);
	}

	@Test
	public void update() throws Exception {
		String configKey = "test-config-String";
		String configValue = "String";
		String description = "Test String";
		Configuration configuration = createConfiguration(configKey, configValue, description);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		Long id = configuration.getId();

		configKey = "test-config-Changed";
		configValue = "Changed";
		description = "Test Changed";
		configuration = new Configuration(configKey, configValue, description);
		configuration = configurationsAPI.update(id, configuration, user);
		validateConfiguration(configuration, configKey, configValue, description);
	}

	@Test
	public void delete() throws Exception {
		String configKey = "test-config-String";
		String configValue = "String";
		String description = "Test String";
		Configuration configuration = createConfiguration(configKey, configValue, description);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		configuration = configurationsAPI.delete(configuration.getId(), user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		configuration = Configuration.getById(configuration.getId());
		assertNull(Constants.RESULT_MUST_BE_NULL, configuration);
	}

	@Test
	public void list() throws Exception {
		List<String> sortFields = new ArrayList<>();
		sortFields.add(Constants.VALUE);
		sortFields.add(Constants.CONFIG_KEY);
		List<Boolean> sortDirections = new ArrayList<>();
		sortDirections.add(false);
		sortDirections.add(false);

		ListItem list = configurationsAPI.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
		assertNull(Constants.LIST_MUST_BE_NULL, list.getItems());
		String description = "Test String";
		Configuration configuration;
		Random r = new Random();
		for (int i = 0; i < 7; i++) {
			configuration = createConfiguration(String.valueOf(i), String.valueOf(r.nextInt(10)), description);
			assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		}
		list = configurationsAPI.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, list);
		assertNotNull(Constants.LIST_MUST_NOT_BE_NULL, list.getItems());
		assertFalse(Constants.LIST_MUST_NOT_BE_EMPTY, list.getItems().isEmpty());
		assertTrue(Constants.LIST_SIZE_MUST_BE_SEVEN, list.getItems().size() == 7);
		int pivotVal = Integer.parseInt(((Configuration) list.getItems().get(0)).getValue());
		int pivotKey = Integer.parseInt(((Configuration) list.getItems().get(0)).getConfigKey());
		int key = 0, val = 0;
		for (int i = 0; i < list.getItems().size(); i++) {
			val = Integer.parseInt(((Configuration) list.getItems().get(i)).getValue());
			key = Integer.parseInt(((Configuration) list.getItems().get(i)).getConfigKey());
			assertTrue(Constants.VALUE_MUST_BE_MAJOR_OR_EQUAL, val >= pivotVal);
			if (val == pivotVal) {
				assertTrue(Constants.VALUE_MUST_BE_MAJOR_OR_EQUAL, key >= pivotKey);
			}
			pivotKey = key;
			pivotVal = val;
		}

		sortDirections.clear();
		sortDirections.add(true);
		sortDirections.add(false);
		list = configurationsAPI.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, list);
		assertNotNull(Constants.LIST_MUST_NOT_BE_NULL, list.getItems());
		assertFalse(Constants.LIST_MUST_NOT_BE_EMPTY, list.getItems().isEmpty());
		assertTrue(Constants.LIST_SIZE_MUST_BE_SEVEN, list.getItems().size() == 7);
		pivotVal = Integer.parseInt(((Configuration) list.getItems().get(0)).getValue());
		pivotKey = Integer.parseInt(((Configuration) list.getItems().get(0)).getConfigKey());
		for (int i = 0; i < list.getItems().size(); i++) {
			val = Integer.parseInt(((Configuration) list.getItems().get(i)).getValue());
			key = Integer.parseInt(((Configuration) list.getItems().get(i)).getConfigKey());
			assertTrue(Constants.VALUE_MUST_BE_MINOR_OR_EQUAL, val <= pivotVal);
			if (val == pivotVal) {
				assertTrue(Constants.VALUE_MUST_BE_MAJOR_OR_EQUAL, key >= pivotKey);
			}
			pivotKey = key;
			pivotVal = val;
		}
	}

}