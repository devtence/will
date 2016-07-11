package com.devtence.will.dev.models.jazz;

import com.devtence.will.dev.models.BaseModel;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.util.List;

/**
 * Class that models the Content data and maps it's structure to the persistence layer,
 * it also defines and implements the functions that can be performed with the Contents.
 *
 * <p> This class is the central component for the Content Management System defined by
 * the Jazz Classes.
 *
 * @author sorcerer
 * @since 2015-06-09
 * @see Author
 * @see Category
 * @see Label
 * @see JazzFile
 * @see Language
 *
 */
@Entity
public class Content extends BaseModel<Content> {

    /**
     * Content title
     */
    @Index
    private String title;

    /**
     * Small description for the content
     */
    private String description;

    /**
     * Body of the content
     */
    private String body;

    /**
     * List of Authors that created the content
     */
    private List<Author> authors;

    /**
     * List of categories
     */
    private List<Long> categories;

    /**
     * List of labels for content categorization
     */
    private List<Label> labels;

    /**
     * List of files attached to the Content
     */
    private List<JazzFile> files;

    /**
     * Language id used for queries
     */
    @Index
    private Long idLanguage;

    /*getter and setters*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<JazzFile> getFiles() {
        return files;
    }

    public void setFiles(List<JazzFile> files) {
        this.files = files;
    }

    public Long getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(Long idLanguage) {
        this.idLanguage = idLanguage;
    }

    /**
     * Default constructor
     */
    public Content() {
    }

    /**
     * Recommended constructor
     * @param id
     * @param title
     * @param description
     * @param authors
     * @param categories
     * @param labels
     * @param files
     * @param idLanguage
     */
    public Content(Long id, String title, String description, List<Author> authors, List<Long> categories,
                   List<Label> labels, List<JazzFile> files, Long idLanguage) {
        setId(id);
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.categories = categories;
        this.labels = labels;
        this.files = files;
        this.idLanguage = idLanguage;
    }

    /**
     * Stores the object to the database
     * @throws Exception
     */
    @Override
    public void validate() throws Exception {
        //validate fields
        this.save();
    }

    /**
     * Removes the object from the database
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    /**
     * Compares the new Data with the object and executes the update if necessary
     * @param data new data to insert
     * @throws Exception
     */
    @Override
    public void update(Content data) throws Exception {
        boolean mod = false;

        if (data.getTitle() != null){
            setTitle(data.getTitle());
            mod |= true;
        }

        if (data.getDescription() != null){
            setDescription(data.getDescription());
            mod |= true;
        }

        if (data.getAuthors() != null){
            setAuthors(data.getAuthors());
            mod |= true;
        }

        if (data.getCategories() != null){
            setCategories(data.getCategories());
            mod |= true;
        }

        if (data.getLabels() != null){
            setLabels(data.getLabels());
            mod |= true;
        }

        if (data.getFiles() != null){
            setFiles(data.getFiles());
            mod |= true;
        }

        if (data.getIdLanguage() != null){
            setIdLanguage(data.getIdLanguage());
            mod |= true;
        }


        if (mod){
            this.validate();
        }

    }

    /**
     * Loads the object with the data from the database.
     * @param id
     */
    @Override
    public void load(long id) {
        Content me  = get(id);
        setTitle(me.getTitle());
        setDescription(me.getDescription());
        setAuthors(me.getAuthors());
        setCategories(me.getCategories());
        setLabels(me.getLabels());
        setFiles(me.getFiles());
        setIdLanguage(me.getIdLanguage());
    }

    /**
     * Returns a Content object using the get function from parent
     * @param id of the object to get
     * @return
     */
    public static Content get(long id){
        return (Content) get(id, Content.class);
    }
}
