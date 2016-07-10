package com.devtence.will.dev.models.commons;


import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Class that model the Configuration data and map it's structure to the persistence layer,
 * it also defines and implements the functions that can be performed with the Configurations.
 *
 * <p> This objects are used on the ConfigurationCache </p>
 *
 * @author plessmann
 * @since 2015-03-16
 * @see com.devtence.will.dev.commons.caches.ConfigurationsCache
 *
 */
@Entity
public class Configuration extends BaseModel<Configuration> implements Serializable {

    /**
     * key to map the configuration
     */
    @Index
    private String configKey;

    /**
     * configuration Value
     */
    @Index
    private String value;

    /**
     * configuration description
     */
    private String description;

    /**
     * Default Constructor
     */
    public Configuration() {
    }

    /**
     * Recommended Constructor method
     * @param configKey
     * @param value
     * @param description
     */
    public Configuration(String configKey, String value, String description) {
        this.configKey = configKey;
        this.value = value;
        this.description = description;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Read a configuration by its name
     * @param key the name of the configuration
     * @return	the configuration object
     * @throws Exception	if there was an error querying the database
     */
    private static Configuration getByKey(String key) throws Exception {
        return DbObjectify.ofy().load().type(Configuration.class).filter("configKey", key).first().now();
    }

    /**
     * Read a configuration by its name
     * @param key the name of the configuration
     * @return	the configuration object
     * @throws Exception	if there was an error querying the database
     */
    public static Configuration getConfigByKey(String key) throws Exception {
        return getByKey(key);
    }

    /**
     * Read a configuration by its name expecting it to be a String
     * @param key the name of the configuration
     * @return	the configuration String value
     * @throws Exception	if there was an error querying the database
     */
    public static String getString(String key) throws Exception {
        Configuration c = getByKey(key);
        return c.getValue();
    }

    /**
     * Read a configuration by its name expecting it to be a Long
     * @param key the name of the configuration
     * @return	the configuration Long value
     * @throws Exception	if there was an error querying the database
     */
    public static Long getLong(String key) throws Exception {
        Configuration c = getByKey(key);
        return Long.parseLong(c.getValue());
    }

    /**
     * Read a configuration by its name expecting it to be a String[]
     * @param key the name of the configuration
     * @return	the configuration String[] value
     * @throws Exception	if there was an error querying the database
     */
    public static String[] getStringArray(String key, String separator) throws Exception {
        Configuration c = getByKey(key);
        return c.getValue().split(separator);
    }

    /**
     * Read a configuration by its name expecting it to be a int
     * @param key the name of the configuration
     * @return	the configuration int value
     * @throws Exception	if there was an error querying the database
     */
    public static int getInt(String key) throws Exception {
        Configuration c = getByKey(key);
        return Integer.parseInt(c.getValue());
    }

    /**
     * Read a configuration by its name expecting it to be a float
     * @param key the name of the configuration
     * @return	the configuration float value
     * @throws Exception	if there was an error querying the database
     */
    public static float getFloat(String key) throws Exception {
        Configuration c = getByKey(key);
        return Float.parseFloat(c.getValue());
    }

    /**
     * Read a configuration by its name expecting it to be a double
     * @param key the name of the configuration
     * @return	the configuration double value
     * @throws Exception	if there was an error querying the database
     */
    public static double getDouble(String key) throws Exception {
        Configuration c = getByKey(key);
        return Double.parseDouble(c.getValue());
    }

    /**
     * Read a configuration by its name expecting it to be a boolean
     * @param key the name of the configuration
     * @return	the configuration boolean value
     * @throws Exception	if there was an error querying the database
     */
    public static boolean getBoolean(String key) throws Exception {
        Configuration c = getByKey(key);
        int value = Integer.parseInt(c.getValue());
        return value == 0 ? false : true;
    }

    public static Configuration getById(Long id) throws Exception {
        return DbObjectify.ofy().load().type(Configuration.class).id(id).now();
    }

    @JsonIgnore
    public Long getLong() throws Exception {
        return Long.parseLong(value);
    }

    @JsonIgnore
    public String[] getStringArray(String separator) throws Exception {
        return value.split(separator);
    }

    @JsonIgnore
    public int getInt() throws Exception {
        return Integer.parseInt(value);
    }

    @JsonIgnore
    public float getFloat() throws Exception {
        return Float.parseFloat(value);
    }

    @JsonIgnore
    public double getDouble() throws Exception {
        return Double.parseDouble(value);
    }

    @JsonIgnore
    public boolean getBoolean() throws Exception {
        return Integer.parseInt(value) != 0;
    }

    /**
     * Validates the object data and adds the new Configuration to the DB
     * @throws Exception
     */
    @Override
    public void validate() throws Exception {
        if(configKey == null || configKey.isEmpty()){
            throw new MissingFieldException("invalid configKey");
        }
        if(value == null || value.isEmpty()){
            throw new MissingFieldException("invalid value");
        }
        if(description == null || description.isEmpty()){
            throw new MissingFieldException("invalid description");
        }
        this.save();
    }

    /**
     * Deletes the Configuration
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        this.delete();
    }

    /**
     * Checks the new Data with the data inside de the object, it performs the update if new data is set
     * @param data data to be updated on the persistence layer.
     * @throws Exception
     */
    @Override
    public void update(Configuration data) throws Exception {
        boolean mod = false;

        if (data.getConfigKey() != null){
            setConfigKey(data.getConfigKey());
            mod |= true;
        }

        if (data.getDescription() != null){
            setDescription(data.getDescription());
            mod |= true;
        }

        if (data.getValue() != null){
            setValue(data.getValue());
            mod |= true;
        }

        if (mod){
            this.validate();
        }
    }

    /**
     * Loads the object with the data from the Google Cloud Datastore using the id queried
     * @param id value to query in the DB
     */
    @Override
    public void load(long id) {
        Configuration me = DbObjectify.ofy().load().type(Configuration.class).id(id).now();
        this.setId(me.getId());
        this.setConfigKey(me.getConfigKey());
        this.setValue(me.getValue());
        this.setDescription(me.getDescription());
    }
}
