package com.devtence.will.dev.models.users;

import com.devtence.will.dev.commons.caches.AuthorizationCache;
import com.devtence.will.dev.commons.caches.ConfigurationsCache;
import com.devtence.will.dev.commons.wrappers.AuthorizationWrapper;
import com.devtence.will.dev.commons.wrappers.CacheAuthWrapper;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.exceptions.UserExistException;
import com.devtence.will.dev.models.AuthenticableEntity;
import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.commons.codec.binary.Base64;
import org.jasypt.util.password.BasicPasswordEncryptor;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Class that models the User data and maps it's structure to the persistence layer,
 * it also defines and implements the functions that can be performed with the Users.
 *
 * @author plessmann
 * @since 2015-06-02
 *
 */
@Entity
public class User extends BaseModel<User> implements AuthenticableEntity{

    /**
     * user status
     * todo: change to enum
     */
    private Integer status;

    private Boolean lastLoginStatus;

    /**
     * Counter with the amount of consecutive failed login attempts
     */
    private Integer failedLoginCounter;

    /**
     * flag to represent when the user is in password recovery status
     */
    private Integer passwordRecoveryStatus;

    /**
     * user email
     */
    @Index
    private String email;

    /**
     * username
     */
    @Index
    private String user;

    /**
     * user password stored, encrypted value
     */
    private String password;

    /**
     * Json web token with access keys
     */
    @Index
    private String jwt;

    /**
     * secret key to decode the Json Web Token of the user
     */
    private String secret;

    /**
     * List of the roles the user has
     */
    @Index
    private List<Long> roles;


    public User() {
    }

    public User(String email, String user, String password) {
        this.email = email;
        this.user = user;
        this.password = password;
    }

    public User(String email, String user, String password, List<Long> roles) {
        this.email = email;
        this.user = user;
        this.password = password;
        this.roles = roles;
    }

    public User(String user, String password) {
        this.user = user;
        this.password = password;
    }

    /**
     * Recommended constructor
     * @param status
     * @param lastLoginStatus
     * @param failedLoginCounter
     * @param passwordRecoveryStatus
     * @param email
     * @param user
     * @param password
     * @param jwt
     * @param secret
     * @param roles
     */
    public User(Integer status, Boolean lastLoginStatus, Integer failedLoginCounter, Integer passwordRecoveryStatus, String email, String user, String password, String jwt, String secret, List<Long> roles) {
        this.status = status;
        this.lastLoginStatus = lastLoginStatus;
        this.failedLoginCounter = failedLoginCounter;
        this.passwordRecoveryStatus = passwordRecoveryStatus;
        this.email = email;
        this.user = user;
        this.password = password;
        this.jwt = jwt;
        this.secret = secret;
        this.roles = roles;
    }

    public User(String user) {
        this.user = user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getLastLoginStatus() {
        return lastLoginStatus;
    }

    public void setLastLoginStatus(Boolean lastLoginStatus) {
        this.lastLoginStatus = lastLoginStatus;
    }

    public Integer getFailedLoginCounter() {
        return failedLoginCounter;
    }

    public void setFailedLoginCounter(Integer failedLoginCounter) {
        this.failedLoginCounter = failedLoginCounter;
    }

    public Integer getPasswordRecoveryStatus() {
        return passwordRecoveryStatus;
    }

    public void setPasswordRecoveryStatus(Integer passwordRecoveryStatus) {
        this.passwordRecoveryStatus = passwordRecoveryStatus;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }

    /***
     * Compares the inputPassword to the stored password using jasypt libraries, the password must be passed to this
     * method in plain text
     * @param inputPassword password in plain text and does the encrypted comparison
     * @return
     * @throws Exception
     */
    @Override
    public boolean goodLogin(String inputPassword) throws Exception {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        lastLoginStatus = passwordEncryptor.checkPassword(inputPassword, password);
        if (lastLoginStatus) {
            failedLoginCounter = 0;
            passwordRecoveryStatus = 0;
        } else {
            failedLoginCounter++;
        }
        this.save();
        return lastLoginStatus;
    }

    /**
     * Generates and returns the keys to authorize user access, this keys are stored on the cache, and on the persistence layer
     * @return
     * @throws Exception
     * TODO: check the method implementation, and change hardcoded variables
     */
    @Override
    public AuthorizationWrapper authorize() throws Exception{
        int authTimeout = 60 * 60000;
        try {
            authTimeout = Integer.parseInt(ConfigurationsCache.getInstance().getElement("auth-timeout").getValue());
        } catch (Exception e) {}
        Key key = MacProvider.generateKey();
        jwt = Jwts.builder().setSubject(user).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + authTimeout)).signWith(SignatureAlgorithm.HS512, key).compact();
        secret = Base64.encodeBase64String(key.getEncoded());
        AuthorizationCache.getInstance().setAuth(new CacheAuthWrapper(getId(), jwt, secret, roles));
        this.save();
        return new AuthorizationWrapper(jwt, getId(), 0);
    }

    /**
     * Validates the required fields, and stores the object to the database.
     *
     * @throws Exception
     */
    @Override
    public void validate() throws Exception {
        if(email == null || email.isEmpty()){
            throw new MissingFieldException("invalid email");
        }
        if(user == null || user.isEmpty()){
            throw new MissingFieldException("invalid user");
        }
        if(password == null || password.isEmpty()){
            throw new MissingFieldException("invalid password");
        }
        //verify if user exist
        User exist = User.getByUser(this.getUser());
        if (exist != null) {
            throw new UserExistException("username already exists");
        }
        this.save();
    }

    /**
     * Method to find an Partner in the database
     * @param id	id of the Partner to find
     * @return	An Partner
     * @throws Exception an error occurred
     */
    public static User getById(Long id) throws Exception {
        return DbObjectify.ofy().load().type(User.class).id(id).now();
    }

    /**
     * Return the user with a Query the database using the user as parameter
     * @param user	username to search
     * @return	the associated Partner data
     * @throws Exception	an error occurred
     */
    public static User getByUser(String user) throws Exception {
        return DbObjectify.ofy().load().type(User.class).filter("user", user).first().now();
    }

    /**
     * Compares the object to the new data, executes the update if necessary
     * @param data new data to insert
     * @throws Exception
     */
    @Override
    public void update(User data) throws Exception {
        boolean mod = false;

        if (data.getStatus() != null){
            setStatus(data.getStatus());
            mod |= true;
        }

        if (data.getLastLoginStatus() != null){
            setLastLoginStatus(data.getLastLoginStatus());
            mod |= true;
        }

        if (data.getFailedLoginCounter() != null){
            setFailedLoginCounter(data.getFailedLoginCounter());
            mod |= true;
        }

        if (data.getPasswordRecoveryStatus() != null){
            setPasswordRecoveryStatus(data.getPasswordRecoveryStatus());
            mod |= true;
        }

        if (data.getEmail() != null){
            setEmail(data.getEmail());
            mod |= true;
        }

        if (data.getUser() != null){
            setUser(data.getUser());
            mod |= true;
        }

        if (data.getPassword() != null){
            setPassword(data.getPassword());
            mod |= true;
        }

        if (data.getJwt() != null){
            setUser(data.getJwt());
            mod |= true;
        }

        if (data.getSecret() != null){
            setUser(data.getSecret());
            mod |= true;
        }

        if (data.getRoles() != null){
            setRoles(data.getRoles());
            mod |= true;
        }

        if (mod){
            this.update();
        }
    }

    /**
     * Loads the object with the User from the database
     * @param id
     */
    @Override
    public void load(long id) {
        User me  = (User) get(id, User.class);
        setId(me.getId());
        setStatus(me.getStatus());
        setLastLoginStatus(me.getLastLoginStatus());
        setFailedLoginCounter(me.getFailedLoginCounter());
        setPasswordRecoveryStatus(me.getPasswordRecoveryStatus());
        setEmail(me.getEmail());
        setUser(me.getUser());
        setJwt(me.getJwt());
        setSecret(me.getSecret());
        setRoles(me.getRoles());
    }

    /**
     * Removes the object from the database
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        this.delete();
    }
}
