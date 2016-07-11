package com.devtence.will.dev.samples.search;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.authenticators.UserAuthenticator;
import com.devtence.will.dev.samples.search.Search;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

import java.util.ArrayList;
import java.util.List;


/**
 * Sample Google Endpoint Class that implements the Search API from Google Cloud
 *
 * @author sorcerer
 * @since 2016-06-22
 *
 * @see Search
 */
@Api(
        scopes = {Constants.EMAIL_SCOPE},
        name = "search",
        version = Constants.API_MASTER_VERSION,
        clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE},
        authenticators = {UserAuthenticator.class}
)
public class TestAPI {


    /**
     * This API method adds documents to the Google Datastore and the Search API from Google Cloud
     * @param toAdd object to be added to the Datastore and to be indexed on the Search API
     * @return Object indexed and saved
     */
    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "search.add",
            path = "search")
    public Search testAdd(Search toAdd){
        try {
            // Saving to Datastore
            toAdd.validate();
            // Adding data to the SearchAPI index
            toAdd.addIndex();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return toAdd;
    }

    /**
     * This API method queries the Search API Index and only returns documents with value < 100 and
     * retrieves the document in the Datastore.
     *
     * @param id the Search API index identifier to be used for the search
     * @return list of objects that matches the Search API index and are no bigger than 100
     */
    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "search.search",
            path = "search/{id}")
    public List<Search> testSearch(@Named("id") String id){
        ArrayList tr = new ArrayList();
        try {
            // Hardcoded query for testing purposes
            String query = "value < 100";

            // Search using Google Cloud Search API
            Results<ScoredDocument> result = Search.getDocument(id , query);
            for (ScoredDocument doc : result.getResults()){
                System.out.println(doc.toString());
                System.out.println(doc.getFields("content"));
                Search add = (Search) Search.get(Long.parseLong(doc.getId()),Search.class);
                tr.add(add);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return tr;
    }
}
