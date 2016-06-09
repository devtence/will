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

import static org.junit.Assert.*;

/**
 * Created by plessmann on 09/06/16.
 */
public class ConfigurationsAPITest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final ConfigurationsAPI configurationsAPI = new ConfigurationsAPI();
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
		String configKey = "test-config-String";
		String stringValue = "String";
		String description = "Test String";
		Configuration configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getValue().equalsIgnoreCase(stringValue));

		configKey = "test-config-Int";
		stringValue = "1";
		description = "Test Int";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getInt() == Integer.parseInt(stringValue));

		configKey = "test-config-Long";
		stringValue = ""+System.currentTimeMillis();
		description = "Test Long";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getLong() == Long.parseLong(stringValue));

		configKey = "test-config-Array";
		stringValue = "Strign;array;of;strings";
		description = "Test Array";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		String[] result = configuration.getStringArray(Constants.SEPARATOR);
		String[] base = stringValue.split(Constants.SEPARATOR);
		assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, base.length == result.length);
		for (int i = 0; i < base.length; i++) {
			assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, base[i]), result[i].equalsIgnoreCase(base[i]));
		}

		configKey = "test-config-Long";
		stringValue = "0.8";
		description = "Test Float";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getFloat() == Float.parseFloat(stringValue));

		configKey = "test-config-Long";
		stringValue = "1.89665654";
		description = "Test Double";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getDouble() == Double.parseDouble(stringValue));

		configKey = "test-config-Long";
		stringValue = "1";
		description = "Test Boolean";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getBoolean());
	}

	@Test
	public void read() throws Exception {
		String configKey = "test-config-String";
		String stringValue = "String";
		String description = "Test String";
		Configuration configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		configuration = configurationsAPI.read(configuration.getId(), user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getValue().equalsIgnoreCase(stringValue));

	}

	@Test
	public void update() throws Exception {
		String configKey = "test-config-String";
		String stringValue = "String";
		String description = "Test String";
		Configuration configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		Long id = configuration.getId();

		configKey = "test-config-Changed";
		stringValue = "Changed";
		description = "Test Changed";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.update(id, configuration, user);
		assertTrue(String.format(Constants.CONFIG_KEY_MUST_BE_VALUE, configKey), configuration.getConfigKey().equalsIgnoreCase(configKey));
		assertTrue(String.format(Constants.DESCRIPTION_MUST_BE_VALUE, description), configuration.getDescription().equalsIgnoreCase(description));
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getValue().equalsIgnoreCase(stringValue));

	}

	@Test
	public void delete() throws Exception {
		String configKey = "test-config-String";
		String stringValue = "String";
		String description = "Test String";
		Configuration configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		configuration = configurationsAPI.delete(configuration.getId(), user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, configuration.getId());
		configuration = Configuration.getById(configuration.getId());
		assertNull(Constants.RESULT_MUST_BE_NULL, configuration);
	}

	@Test
	public void list() throws Exception {
		ListItem list = configurationsAPI.list(Constants.INDEX, Constants.OFFSET, Constants.CONFIG_KEY, Constants.ASC, null, user);
		assertNull(Constants.LIST_MUST_BE_NULL, list.getItems());

		//<editor-fold desc="Configs creation">
		String configKey = "test-config-String";
		String stringValue = "String";
		String description = "Test String";
		Configuration configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getValue().equalsIgnoreCase(stringValue));

		configKey = "test-config-Int";
		stringValue = "1";
		description = "Test Int";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getInt() == Integer.parseInt(stringValue));

		configKey = "test-config-Long";
		stringValue = ""+System.currentTimeMillis();
		description = "Test Long";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getLong() == Long.parseLong(stringValue));

		configKey = "test-config-Array";
		stringValue = "Strign;array;of;strings";
		description = "Test Array";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		String[] result = configuration.getStringArray(Constants.SEPARATOR);
		String[] base = stringValue.split(Constants.SEPARATOR);
		assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, base.length == result.length);
		for (int i = 0; i < base.length; i++) {
			assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, base[i]), result[i].equalsIgnoreCase(base[i]));
		}

		configKey = "test-config-Long";
		stringValue = "0.8";
		description = "Test Float";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getFloat() == Float.parseFloat(stringValue));

		configKey = "test-config-Long";
		stringValue = "1.89665654";
		description = "Test Double";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getDouble() == Double.parseDouble(stringValue));

		configKey = "test-config-Long";
		stringValue = "1";
		description = "Test Boolean";
		configuration = new Configuration(configKey, stringValue, description);
		configuration = configurationsAPI.create(configuration, user);
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
		assertTrue(String.format(Constants.VALUE_MUST_BE_VALUE, stringValue), configuration.getBoolean());
		//</editor-fold>

		list = configurationsAPI.list(0, 100, Constants.CONFIG_KEY, Constants.ASC, null, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, list);
		assertNotNull(Constants.LIST_MUST_NOT_BE_NULL, list.getItems());
		assertFalse(Constants.LIST_MUST_NOT_BE_EMPTY, list.getItems().isEmpty());
		assertTrue(Constants.LIST_SIZE_MUST_BE_SEVEN, list.getItems().size() == 7);
	}

}