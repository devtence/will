package com.devtence.will.dev.models.commons;


import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Configurations to be used in different apsects that can be changed real time
 * Created by plessmann on 10/03/16.
 */
@Entity
public class Configuration extends BaseModel implements Serializable {

	@Index
	private String configKey;
	private String value;
	private String description;

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


}
