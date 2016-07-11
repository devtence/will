package com.devtence.will.dev.models.users;

import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Class that models the User Password Reset data and maps it's structure to the persistence layer,
 * it also defines and implements the functions that can be performed.
 *
 * @author plessmann
 * @since 2015-06-03
 *
 */
@Entity
public class UserPasswordReset extends BaseModel<UserPasswordReset> implements Serializable {

	/**
	 * User id
	 */
    @Index
    private Long idUser;

    /**
     * Json web token stored
     */
    @Index
    private String webToken;

    /**
     * Secret to decode the JWT
     */
    private String secret;

    /**
     * Default constructor
     */
    public UserPasswordReset() {
    }

    /**
     * Recomended constructor
     * @param idUser
     * @param webToken
     * @param secret
     */
    public UserPasswordReset(Long idUser, String webToken, String secret) {
        this.idUser = idUser;
        this.webToken = webToken;
        this.secret = secret;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getWebToken() {
        return webToken;
    }

    public void setWebToken(String webToken) {
        this.webToken = webToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Validates the required fields and Stores the object to the database
     * @throws Exception
     */
    @Override
    public void validate() throws Exception {
        if(idUser == null){
            throw new MissingFieldException("invalid idUser");
        }
        if(webToken == null || webToken.isEmpty()){
            throw new MissingFieldException("invalid webToken");
        }
        if(secret == null || secret.isEmpty()){
            throw new MissingFieldException("invalid secret");
        }
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

    @Override
    public void update(UserPasswordReset data) throws Exception {
        //do nothing
    }

    @Override
    public void load(long id) {
        //do nothing
    }

    /**
     * Returns the object queried by the Json Web Token string
     * @param webToken
     * @return
     * @throws Exception
     */
    public static UserPasswordReset getByToken(String webToken) throws Exception {
        return DbObjectify.ofy().load().type(UserPasswordReset.class).filter("webToken", webToken).first().now();
    }

    /**
     * Returns the object queried by the user id
     * @param user
     * @return
     * @throws Exception
     */
    public static UserPasswordReset getUser(Long user) throws Exception {
        return DbObjectify.ofy().load().type(UserPasswordReset.class).filter("idUser", user).first().now();
    }

}
