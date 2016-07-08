package com.devtence.will.dev.samples.search;

import com.devtence.will.dev.models.BaseModel;
import com.google.appengine.api.search.*;
import com.googlecode.objectify.annotation.Entity;

/**
 * Class that implements the sample Data Model used in the TestAPI Google Cloud Search API example.
 *
 * @author sorcerer
 * @since 2016-06-22
 *
 * @see TestAPI
 */
@Entity
public class Search extends BaseModel<Search>{

    // Hardcoded index identifier for the Search API
    private static final String INDEX = "TEST-INDEX";

    private String content;

    private Integer value;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * Validate and save a document on the Google Cloud Datastore
     */
    @Override
    public void validate() throws Exception {
        this.save();
    }

    /**
     * Delete a document on the Google Cloud Datastore
     */
    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    @Override
    public void update(Search data) throws Exception {
        //not needed
    }

    @Override
    public void load(long id) {
        //not needed
    }

    /**
     * This method adds a document to the index on the Google Cloud Search API
     */
    public void addIndex(){
        Document doc = Document.newBuilder()
                .setId("" + this.getId())
                .addField(Field.newBuilder().setName("content").setText(this.getContent()))
                .addField(Field.newBuilder().setName("value").setNumber(this.getValue()))
                .build();
        try {
            indexADocument(INDEX, doc);
        }catch (Exception ex){

        }
    }

    /**
     * Query over a specific Search API index
     * @param docId Search API index used for the query
     * @param query Query used to retrieve items from the Seach API index
     * @return A list of results from the index
     */
    public static Results<ScoredDocument> getDocument(String docId, String query){
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(docId).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        Results<ScoredDocument> results = index.search(query);
        return results;
    }

    /**
     * Insert a document to a specific index on the Google Cloud Search API
     *
     * @param indexName Search API index where the document is to be indexed
     * @param document Document object to be indexed
     * @throws InterruptedException
     */
    public static void indexADocument(String indexName, Document document)
            throws InterruptedException {
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

        final int maxRetry = 3;
        int attempts = 0;
        int delay = 2;
        while (true) {
            try {
                index.put(document);
            } catch (PutException e) {
                if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())
                        && ++attempts < maxRetry) { // retrying
                    Thread.sleep(delay * 1000);
                    delay *= 2; // easy exponential backoff
                    continue;
                } else {
                    throw e; // otherwise throw
                }
            }
            break;
        }
    }
}
