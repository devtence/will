package com.devtence.will.dev.endpoints.commons;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.caches.NotificationsCache;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.commons.Configuration;
import com.devtence.will.dev.models.commons.Notification;
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
public class NotificationsAPITest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final static NotificationsAPI notificationsAPI = new NotificationsAPI();
	private final static User user = new User("ok", "email");
	protected Closeable session;

	@BeforeClass
	public static void setUpBeforeClass() {
		ObjectifyService.setFactory(new ObjectifyFactory());
		ObjectifyService.register(Configuration.class);
		ObjectifyService.register(Notification.class);
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

	public static Notification createNotification(String sender, String recipients, String subject, String message, String mnemonic) throws Exception {
		Notification notification = new Notification(sender, recipients, subject, message, mnemonic);
		notification = notificationsAPI.create(notification, user);
		validateNotification(notification, sender, recipients, subject, message, mnemonic);
		return notification;
	}

	private static void validateNotification(Notification notification, String sender, String recipients, String subject, String message, String mnemonic) throws Exception {
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, notification);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, notification.getId());
		assertNotNull(Constants.SENDER_MUST_NOT_BE_NULL, notification.getSender());
		assertNotNull(Constants.RECIPIENTS_MUST_NOT_BE_NULL, notification.getRecipients());
		assertNotNull(Constants.SUBJECT_MUST_NOT_BE_NULL, notification.getSubject());
		assertNotNull(Constants.MESSAGE_MUST_NOT_BE_NULL, notification.getMessage());
		assertNotNull(Constants.MNEMONIC_MUST_NOT_BE_NULL, notification.getMnemonic());
		assertFalse(Constants.SENDER_MUST_NOT_BE_EMPTY, notification.getSender().isEmpty());
		assertFalse(Constants.RECIPIENTS_MUST_NOT_BE_EMPTY, notification.getRecipients().isEmpty());
		assertFalse(Constants.SUBJECT_MUST_NOT_BE_EMPTY, notification.getSubject().isEmpty());
		assertFalse(Constants.MESSAGE_MUST_NOT_BE_EMPTY, notification.getMessage().isEmpty());
		assertFalse(Constants.MNEMONIC_MUST_NOT_BE_EMPTY, notification.getMnemonic().isEmpty());
		assertTrue(String.format(Constants.SENDER_MUST_BE_VALUE, sender), notification.getSender().equalsIgnoreCase(sender));
		String[] result = notification.getRecipients().split(Constants.SEPARATOR);
		String[] base = recipients.split(Constants.SEPARATOR);
		assertTrue(Constants.ARRAYS_MUST_HAVE_SAME_SIZE, base.length == result.length);
		for (int i = 0; i < base.length; i++) {
			assertTrue(String.format(Constants.RECIPIENTS_MUST_BE_VALUE, base[i]), result[i].equalsIgnoreCase(base[i]));
		}
		assertTrue(String.format(Constants.SUBJECT_MUST_BE_VALUE, subject), notification.getSubject().equalsIgnoreCase(subject));
		assertTrue(String.format(Constants.MESSAGE_MUST_BE_VALUE, message), notification.getMessage().equalsIgnoreCase(message));
		assertTrue(String.format(Constants.MNEMONIC_MUST_BE_VALUE, mnemonic), notification.getMnemonic().equalsIgnoreCase(mnemonic));
		Configuration configuration = ConfigurationsAPITest.createConfiguration("cache-timeout", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		configuration = ConfigurationsAPITest.createConfiguration("use-cache", "1", "Test String");
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, configuration);
		notification = NotificationsCache.getInstance().getElement(mnemonic);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, notification);
	}

	@Test
	public void create() throws Exception {
		String sender = "test@test.test";
		String recipients = "1test@test.test;2test@test.test;3test@test.test";
		String subject = "Test";
		String message = "This is a test for notifications";
		String mnemonic = "TEST";
		Notification notification = createNotification(sender, recipients, subject, message, mnemonic);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, notification);
	}

	@Test
	public void read() throws Exception {
		String sender = "test@test.test";
		String recipients = "1test@test.test;2test@test.test;3test@test.test";
		String subject = "Test";
		String message = "This is a test for notifications";
		String mnemonic = "TEST";
		Notification notification = createNotification(sender, recipients, subject, message, mnemonic);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, notification);
		notification = notificationsAPI.read(notification.getId(), user);
		validateNotification(notification, sender,recipients,subject, message, mnemonic);
	}

	@Test
	public void update() throws Exception {
		String sender = "test@test.test";
		String recipients = "1test@test.test;2test@test.test;3test@test.test";
		String subject = "Test";
		String message = "This is a test for notifications";
		String mnemonic = "TEST";
		Notification notification = createNotification(sender, recipients, subject, message, mnemonic);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, notification);

		Long id = notification.getId();
		sender = "changedtest@test.test";
		recipients = "changed1test@test.test;changed2test@test.test;changed3test@test.test";
		subject = "changedTest";
		message = "changedThis is a test for notifications";
		mnemonic = "CHANGEDTEST";
		notification = new Notification(sender, recipients, subject, message, mnemonic);
		notification = notificationsAPI.update(id, notification, user);
		validateNotification(notification, sender, recipients, subject, message, mnemonic);
	}

	@Test
	public void delete() throws Exception {
		String sender = "test@test.test";
		String recipients = "1test@test.test;2test@test.test;3test@test.test";
		String subject = "Test";
		String message = "This is a test for notifications";
		String mnemonic = "TEST";
		Notification notification = createNotification(sender, recipients, subject, message, mnemonic);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, notification);
		notification = notificationsAPI.delete(notification.getId(), user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, notification);
		assertNotNull(Constants.ID_MUST_NOT_BE_NULL, notification.getId());
		notification = Notification.getById(notification.getId());
		assertNull(Constants.RESULT_MUST_BE_NULL, notification);
	}

	@Test
	public void list() throws Exception {
		List<String> sortFields = new ArrayList<>();
		sortFields.add(Constants.MNEMONIC);
		List<Boolean> sortDirections = new ArrayList<>();
		sortDirections.add(true);

		ListItem list = notificationsAPI.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
		assertNull(Constants.LIST_MUST_BE_NULL, list.getItems());

		String sender = "test@test.test";
		String recipients = "test@test.test;2test@test.test;3test@test.test";
		String subject = "Test";
		String message = "This is a test for notifications";
		String mnemonic = "TEST";
		Notification notification;
		for (int i = 0; i < 7; i++) {
			notification = createNotification(i + "_" + sender, i + "_" + recipients, i + "_" + subject, i + "_" + message, i + "_" + mnemonic);
			assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, notification);
		}

		list = notificationsAPI.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
		assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, list);
		assertNotNull(Constants.LIST_MUST_NOT_BE_NULL, list.getItems());
		assertFalse(Constants.LIST_MUST_NOT_BE_EMPTY, list.getItems().isEmpty());
		assertTrue(Constants.LIST_SIZE_MUST_BE_SEVEN, list.getItems().size() == 7);
	}

}