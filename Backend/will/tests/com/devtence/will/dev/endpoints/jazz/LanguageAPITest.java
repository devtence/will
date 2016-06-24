package com.devtence.will.dev.endpoints.jazz;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.ListItem;
import com.devtence.will.dev.models.jazz.Author;
import com.devtence.will.dev.models.jazz.Category;
import com.devtence.will.dev.models.jazz.Content;
import com.devtence.will.dev.models.jazz.Language;
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
 * Created by sorcerer on 6/15/16.
 */
public class LanguageAPITest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private final static LanguageAPI actual = new LanguageAPI();
    private final static User user = new User("ok", "email");
    protected Closeable session;

    @BeforeClass
    public static void setUpBeforeClass() {
        ObjectifyService.setFactory(new ObjectifyFactory());
        ObjectifyService.register(Language.class);
    }

    @Before
    public void setUp() throws Exception {
        this.session = ObjectifyService.begin();
        helper.setUp();
    }

    @After
    public void tearDown() throws Exception {
        AsyncCacheFilter.complete();
        this.session.close();
        this.helper.tearDown();
    }

    @Test
    public void testCreate() throws Exception {
        Long id = 1l;
        String language = "test-language";
        String shortName = "test shortName";
        
        assertNotNull(id);
        assertFalse(id == 0);

        Language toCreate = new Language(id, language, shortName);
        Language wsResponse = actual.create(toCreate, user);
        Language testObj = Language.get(id);

        assertNotNull(wsResponse);
        assertTrue(wsResponse.getId() == id);
        assertTrue(wsResponse.getLanguage().equals(language));
        assertTrue(wsResponse.getShortName().equals(shortName));

        assertNotNull(testObj);
        assertTrue(testObj.getId() == id);
        assertTrue(testObj.getLanguage().equals(language));
        assertTrue(testObj.getShortName().equals(shortName));

        assertTrue(wsResponse.getId() == testObj.getId());
        assertTrue(wsResponse.getLanguage().equals(testObj.getLanguage()));
        assertTrue(wsResponse.getShortName().equals(testObj.getShortName()));
    }

    @Test
    public void testRead() throws Exception {
        Long id = 1l;
        String language = "test-language";
        String shortName = "test shortName";

        assertNotNull(id);
        assertFalse(id == 0);

        Language toRead = new Language(id, language, shortName);
        toRead.validate();

        Language testObj  = actual.read(id, user);

        assertNotNull(testObj);
        assertTrue(testObj.getId() == id);
        assertTrue(testObj.getLanguage().equals(language));
        assertTrue(testObj.getShortName().equals(shortName));
    }

    @Test
    public void testUpdate() throws Exception {
        Long id = 1l;
        String language = "test-language";
        String shortName = "test shortName";

        assertNotNull(id);
        assertFalse(id == 0);

        Language toCreate = new Language(id, language, shortName);
        toCreate.validate();

        String languageUpdate = "test-mod-language";
        String shortNameUpdate = "new shortName in test shortName";
        Integer statusUpdate = 1;
        //change
        List<Language> languagesUpdate = new ArrayList<>();

        Language toUpdate = new Language(id, languageUpdate, shortNameUpdate);

        Language wsResponse = actual.update(id, toUpdate, user);
        Language testObj = Language.get(id);

        assertNotNull(wsResponse);
        assertNotNull(testObj);
        assertEquals(wsResponse, testObj);
    }

    @Test
    public void testDelete() throws Exception {
        Long id = 1l;
        String language = "test-language";
        String shortName = "test shortName";

        assertNotNull(id);
        assertFalse(id == 0);

        Language toCreate = new Language(id, language, shortName);
        toCreate.validate();

        Language testObj = Language.get(id);
        assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, testObj);

        //validate response
        assertNotNull(actual.delete(id, user));

        testObj = Language.get(id);
        assertNull(testObj);
    }

    @Test
    public void testList() throws Exception {
        List<String> sortFields = new ArrayList<>();
        sortFields.add("language");
        List<Boolean> sortDirections = new ArrayList<>();
        sortDirections.add(true);

        ListItem list = actual.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
        assertNull(Constants.LIST_MUST_BE_NULL, list.getItems());

        Long id = 1l;
        String language = "test-language";
        String shortName = "test shortName";

        assertNotNull(id);
        assertFalse(id == 0);

        Language toCreate = new Language(id, language, shortName);
        toCreate.validate();

        list = actual.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
        assertNotNull(list.getItems());

    }
}