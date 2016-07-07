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
 * sample API that uses Google SearchAPI
 * Created by sorcerer on 6/22/16.
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
     * this api method adds documents to the data store and the search api
     * @param toAdd object to add
     * @return object added
     */
    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.POST,
            name = "search.add",
            path = "search")
    public Search testAdd(Search toAdd){
        try {
            //saving
            toAdd.validate();
            //adding data to the SearchAPI
            toAdd.addIndex();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return toAdd;
    }

    /**
     * this api method searches the SearchAPI Index and only returns documents with value < 100 and
     * finds the document in the GDS
     * @param id index to search
     * @return list of objects that matches the index and are no bigger than 100
     */
    @ApiMethod(
            httpMethod = ApiMethod.HttpMethod.GET,
            name = "search.search",
            path = "search/{id}")
    public List<Search> testSearch(@Named("id") String id){
        ArrayList tr = new ArrayList();
        try {
            String query = "value < 100";
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
