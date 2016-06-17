package com.devtence.will.dev.endpoints.jazz;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.jazz.Category;
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
public class CategoryAPITest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private final static CategoryAPI actual = new CategoryAPI();
    private final static User user = new User("ok", "email");
    protected Closeable session;

    @BeforeClass
    public static void setUpBeforeClass() {
        ObjectifyService.setFactory(new ObjectifyFactory());
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
        String description = "test description";
        Long idLanguage = 0l;

        assertNotNull(id);
        assertFalse(id == 0);

        Category toCreate = new Category(id, name, description, idLanguage);
        Category wsResponse = actual.create(toCreate, user);
        Category testObj = Category.get(id);

        assertNotNull(wsResponse);
        assertTrue(wsResponse.getId() == id);
        assertTrue(wsResponse.getName().equals(name));
        assertTrue(wsResponse.getDescription().equals(description));

        assertNotNull(testObj);
        assertTrue(testObj.getId() == id);
        assertTrue(testObj.getName().equals(name));
        assertTrue(testObj.getDescription().equals(description));
    }

    @Test
    public void testRead() throws Exception {
        Long id = 1l;
        String name = "test-name";
        String description = "test description";
        Long idLanguage = 0l;

        assertNotNull(id);
        assertFalse(id == 0);

        Category toRead = new Category(id, name, description, idLanguage);
        toRead.validate();

        Category testObj  = actual.read(id, user);

        assertNotNull(testObj);
        assertTrue(testObj.getId() == id);
        assertTrue(testObj.getName().equals(name));
        assertTrue(testObj.getDescription().equals(description));
    }

    @Test
    public void testUpdate() throws Exception {
        Long id = 1l;
        String name = "test-name";
        String description = "test description";
        Long idLanguage = 0l;

        assertNotNull(id);
        assertFalse(id == 0);

        Category toCreate = new Category(id, name, description, idLanguage);
        toCreate.validate();

        String nameUpdate = "test-mod-name";
        String descriptionUpdate = "new description in test description";
        Integer statusUpdate = 1;
        //change
        List<Language> languagesUpdate = new ArrayList<>();

        Category toUpdate = new Category(id, nameUpdate, descriptionUpdate, idLanguage);

        Category wsResponse = actual.update(id, toUpdate, user);
        Category testObj = Category.get(id);

        assertNotNull(wsResponse);
        assertNotNull(testObj);
        assertEquals(wsResponse, testObj);
    }

    @Test
    public void testDelete() throws Exception {
        Long id = 1l;
        String name = "test-name";
        String description = "test description";
        Long idLanguage = 0l;

        assertNotNull(id);
        assertFalse(id == 0);

        Category toCreate = new Category(id, name, description, idLanguage);
        toCreate.validate();

        Category testObj = Category.get(id);
        assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, testObj);

        //validate response
        assertNotNull(actual.delete(id, user));

        testObj = Category.get(id);
        assertNull(testObj);
    }

    @Test
    public void testList() throws Exception {

    }
}