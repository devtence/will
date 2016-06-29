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
public class AuthorAPITest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private final static AuthorAPI actual = new AuthorAPI();
    private final static User user = new User("ok", "email");
    protected Closeable session;

    @BeforeClass
    public static void setUpBeforeClass() {
        ObjectifyService.setFactory(new ObjectifyFactory());
        ObjectifyService.register(Author.class);
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
        String name = "test-name";
        String bio = "test bio";
        Integer status = 0;
        List<Language> languages = new ArrayList<>();

        assertNotNull(id);
        assertFalse(id == 0);

        Author toCreate = new Author(id, name, bio, status, languages);
        Author wsResponse = actual.create(toCreate, user);
        Author testObj = Author.get(id);

        assertNotNull(wsResponse);
        assertTrue(wsResponse.getId() == id);
        assertTrue(wsResponse.getName().equals(name));
        assertTrue(wsResponse.getBio().equals(bio));
        assertTrue(wsResponse.getStatus() == status);
        assertEquals(wsResponse.getLanguages(), languages);

        assertNotNull(testObj);
        assertTrue(testObj.getId() == id);
        assertTrue(testObj.getName().equals(name));
        assertTrue(testObj.getBio().equals(bio));
        assertTrue(testObj.getStatus() == status);
        assertEquals(testObj.getLanguages(), languages);

        assertTrue(wsResponse.getId() == testObj.getId());
        assertTrue(wsResponse.getName().equals(testObj.getName()));
        assertTrue(wsResponse.getBio().equals(testObj.getBio()));
        assertTrue(wsResponse.getStatus() == testObj.getStatus());
        assertEquals(wsResponse.getLanguages(), testObj.getLanguages());

    }

    @Test
    public void testRead() throws Exception {
        Long id = 1l;
        String name = "test-name";
        String bio = "test bio";
        Integer status = 0;
        List<Language> languages = new ArrayList<>();

        assertNotNull(id);
        assertFalse(id == 0);

        Author toRead = new Author(id, name, bio, status, languages);
        toRead.validate();

        Author testObj  = actual.read(id, user);

        assertNotNull(testObj);
        assertTrue(testObj.getId() == id);
        assertTrue(testObj.getName().equals(name));
        assertTrue(testObj.getBio().equals(bio));
        assertTrue(testObj.getStatus() == status);
        assertEquals(testObj.getLanguages(), languages);
    }

    @Test
    public void testUpdate() throws Exception {
        Long id = 1l;
        String name = "test-name";
        String bio = "test bio";
        Integer status = 0;
        List<Language> languages = new ArrayList<>();

        assertNotNull(id);
        assertFalse(id == 0);

        Author toCreate = new Author(id, name, bio, status, languages);
        toCreate.validate();

        String nameUpdate = "test-mod-name";
        String bioUpdate = "new bio in test bio";
        Integer statusUpdate = 1;
        //change
        List<Language> languagesUpdate = new ArrayList<>();

        Author toUpdate = new Author(id, nameUpdate, bioUpdate, statusUpdate, languagesUpdate);

        Author wsResponse = actual.update(id, toUpdate, user);
        Author testObj = Author.get(id);

        assertNotNull(wsResponse);
        assertNotNull(testObj);
        assertEquals(wsResponse, testObj);
    }

    @Test
    public void testDelete() throws Exception {
        Long id = 1l;
        String name = "test-name";
        String bio = "test bio";
        Integer status = 0;
        List<Language> languages = new ArrayList<>();

        assertNotNull(id);
        assertFalse(id == 0);

        Author toCreate = new Author(id, name, bio, status, languages);
        toCreate.validate();

        Author testObj = Author.get(id);
        assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, testObj);

        //validate response
        assertNotNull(actual.delete(id, user));

        testObj = Author.get(id);
        assertNull(testObj);
    }

    @Test
    public void testList() throws Exception {
        List<String> sortFields = new ArrayList<>();
        sortFields.add("name");
        List<Boolean> sortDirections = new ArrayList<>();
        sortDirections.add(true);

        ListItem list = actual.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
        assertNull(Constants.LIST_MUST_BE_NULL, list.getItems());

        //add one
        Long id = 1l;
        String name = "test-name";
        String bio = "test bio";
        Integer status = 0;
        List<Language> languages = new ArrayList<>();
        Author toCreate = new Author(id, name, bio, status, languages);
        toCreate.validate();

        //search again not null
        list = actual.list(Constants.INDEX, Constants.OFFSET, sortFields, sortDirections, null, user);
        assertNotNull(Constants.LIST_MUST_BE_NULL, list.getItems());

    }
}