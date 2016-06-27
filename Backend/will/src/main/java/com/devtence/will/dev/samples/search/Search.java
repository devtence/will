package com.devtence.will.dev.samples.search;

import com.devtence.will.dev.models.BaseModel;
import com.google.appengine.api.search.*;
import com.googlecode.objectify.annotation.Entity;

/**
 * Created by sorcerer on 6/22/16.
 */
@Entity
public class Search extends BaseModel<Search>{

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

    @Override
    public void validate() throws Exception {
        this.save();
    }

    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    @Override
    public void update(Search data) throws Exception {

    }

    @Override
    public void load(long id) {
        //no need
    }


    public void addIndex(){
        Document doc = Document.newBuilder()
                .setId("" + this.getId())
                .addField(Field.newBuilder().setName("content").setText(this.getContent()))
                .addField(Field.newBuilder().setName("value").setNumber(this.getValue()))
                .build();
        try {
            indexADocument("sorcerer",doc);
        }catch (Exception ex){

        }
    }

    public static Results<ScoredDocument> getDocument(String docId, String query){
        IndexSpec indexSpec = IndexSpec.newBuilder().setName(docId).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        Results<ScoredDocument> results = index.search(query);
        return results;
    }

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
