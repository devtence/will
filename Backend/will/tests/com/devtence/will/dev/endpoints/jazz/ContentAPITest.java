package com.devtence.will.dev.endpoints.jazz;

import com.devtence.will.Constants;
import com.devtence.will.dev.models.jazz.Author;
import com.devtence.will.dev.models.jazz.Content;
import com.devtence.will.dev.models.jazz.JazzFile;
import com.devtence.will.dev.models.jazz.Label;
import com.google.api.server.spi.auth.common.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Index;
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
public class ContentAPITest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private final static ContentAPI actual = new ContentAPI();
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
        String title = "test-title";
        String description = "test description";
        Integer status = 0;

        List<Author> authors = null;
        List<Long> categories = null;
        List<Label> labels = null;
        List<JazzFile> files = null;
        Long idLanguage = null;

        assertNotNull(id);
        assertFalse(id == 0);

        Content toCreate = new Content(id, title, description, authors, categories, labels, files, idLanguage);
        Content wsResponse = actual.create(toCreate, user);
        Content testObj = Content.get(id);

        assertNotNull(wsResponse);
        assertNotNull(testObj);

    }

    @Test
    public void testRead() throws Exception {
        Long id = 1l;
        String title = "test-title";
        String description = "test description";
        Integer status = 0;

        List<Author> authors = null;
        List<Long> categories = null;
        List<Label> labels = null;
        List<JazzFile> files = null;
        Long idLanguage = null;

        assertNotNull(id);
        assertFalse(id == 0);

        Content toRead = new Content(id, title, description, authors, categories, labels, files, idLanguage);
        toRead.validate();

        Content testObj  = actual.read(id, user);

        assertNotNull(testObj);
    }

    @Test
    public void testUpdate() throws Exception {
        Long id = 1l;
        String title = "test-title";
        String description = "test description";
        Integer status = 0;

        List<Author> authors = null;
        List<Long> categories = null;
        List<Label> labels = null;
        List<JazzFile> files = null;
        Long idLanguage = null;

        assertNotNull(id);
        assertFalse(id == 0);

        Content toCreate = new Content(id, title, description, authors, categories, labels, files, idLanguage);
        toCreate.validate();

        Content toUpdate = new Content(id, title, description, authors, categories, labels, files, idLanguage);

        Content wsResponse = actual.update(id, toUpdate, user);
        Content testObj = Content.get(id);

        assertNotNull(wsResponse);
        assertNotNull(testObj);
        assertEquals(wsResponse, testObj);
    }

    @Test
    public void testDelete() throws Exception {
        Long id = 1l;
        String title = "test-title";
        String description = "test description";
        Integer status = 0;

        List<Author> authors = null;
        List<Long> categories = null;
        List<Label> labels = null;
        List<JazzFile> files = null;
        Long idLanguage = null;

        assertNotNull(id);
        assertFalse(id == 0);

        Content toCreate = new Content(id, title, description, authors, categories, labels, files, idLanguage);
        toCreate.validate();

        Content testObj = Content.get(id);
        assertNotNull(Constants.RESULT_MUST_NOT_BE_NULL, testObj);

        //validate response
        assertNotNull(actual.delete(id, user));

        testObj = Content.get(id);
        assertNull(testObj);
    }

    @Test
    public void testList() throws Exception {

    }
}