package com.devtence.will.dev.models.users;

import com.devtence.will.dev.commons.caches.AuthorizationCache;
import com.devtence.will.dev.commons.caches.ConfigurationsCache;
import com.devtence.will.dev.commons.caches.RolesCache;
import com.devtence.will.dev.commons.wrappers.AuthorizationWrapper;
import com.devtence.will.dev.commons.wrappers.CacheAuthWrapper;
import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.AuthenticableEntity;
import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.commons.codec.binary.Base64;
import org.jasypt.util.password.BasicPasswordEncryptor;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by plessmann on 02/06/16.
 */
@Entity
public class User extends BaseModel implements AuthenticableEntity{

    private Integer status;
    private Boolean lastLoginStatus;
    private Integer failedLoginCounter;
    private Integer passwordRecoveryStatus;
    @Index
    private String email;
    @Index
    private String user;
    private String password;
    @Index
    private String jwt;
    private String secret;

	@Index
    private List<Long> roles;

    public User() {
    }

    public User(String email, String user, String password) {
        this.email = email;
        this.user = user;
        this.password = password;
    }

    public User(String user, String password) {
        this.user = user;
        this.password = password;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonIgnore
    public Boolean getLastLoginStatus() {
        return lastLoginStatus;
    }

    public void setLastLoginStatus(Boolean lastLoginStatus) {
        this.lastLoginStatus = lastLoginStatus;
    }

    @JsonIgnore
    public Integer getFailedLoginCounter() {
        return failedLoginCounter;
    }

    public void setFailedLoginCounter(Integer failedLoginCounter) {
        this.failedLoginCounter = failedLoginCounter;
    }

    @JsonIgnore
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

    @JsonIgnore
    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @JsonIgnore
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<Long> getRolesKeys() {
        return roles;
    }

    public void setRolesKeys(List<Long> roles) {
        this.roles = roles;
    }

    public void setRoles(List<Role> roles) {
		if (this.roles == null) {
			this.roles = new ArrayList<>();
		}
		if(!this.roles.isEmpty()){
			this.roles.clear();
		}
		for (Role role : roles) {
			this.roles.add(role.getId());
		}
    }

	@JsonIgnore
	public List<Role> getRoles() throws Exception {
		List<Role> roles = new ArrayList<>(this.roles.size());
		for (Long role : this.roles) {
			roles.add(RolesCache.getInstance().getRole(role));
		}
		return roles;
	}

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

	@Override
    public AuthorizationWrapper authorize() throws Exception{
        int authTimeout = 60 * 60000;
        try {
            authTimeout = Integer.parseInt(ConfigurationsCache.getInstance().getConfiguration("auth-timeout").getValue());
        } catch (Exception e) {}
        Key key = MacProvider.generateKey();
        jwt = Jwts.builder().setSubject(user).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + authTimeout)).signWith(SignatureAlgorithm.HS512, key).compact();
        secret = Base64.encodeBase64String(key.getEncoded());
        AuthorizationCache.getInstance().setAuth(new CacheAuthWrapper(getId(), jwt, secret, roles));
		this.save();
        return new AuthorizationWrapper(jwt, getId(), 0);
    }

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
        status = 2;
        failedLoginCounter = 0;
        lastLoginStatus = false;
        passwordRecoveryStatus = 0;
        this.save();
    }

    /**
     * Method to find an Partner in the databse
     * @param id	id of the Partner to find
     * @return	An Partner
     * @throws Exception an error ocurred
     */
    public static User getById(Long id) throws Exception {
        return DbObjectify.ofy().load().type(User.class).id(id).now();
    }

    /**
     * Query the database using the user as parameter
     * @param user	username to search
     * @return	the associated Partner data
     * @throws Exception	an error ocurred
     */
    public static User getByUser(String user) throws Exception {
        return DbObjectify.ofy().load().type(User.class).filter("user", user).first().now();
    }

    public void update(User toUpdate) throws Exception {
        boolean mod = false;

        if (toUpdate.getStatus() != null){
            setStatus(toUpdate.getStatus());
            mod |= true;
        }

        if (toUpdate.getLastLoginStatus() != null){
            setLastLoginStatus(toUpdate.getLastLoginStatus());
            mod |= true;
        }

        if (toUpdate.getFailedLoginCounter() != null){
            setFailedLoginCounter(toUpdate.getFailedLoginCounter());
            mod |= true;
        }

        if (toUpdate.getPasswordRecoveryStatus() != null){
            setPasswordRecoveryStatus(toUpdate.getPasswordRecoveryStatus());
            mod |= true;
        }

        if (toUpdate.getEmail() != null){
            setEmail(toUpdate.getEmail());
            mod |= true;
        }

        if (toUpdate.getUser() != null){
            setUser(toUpdate.getUser());
            mod |= true;
        }

        if (toUpdate.getPassword() != null){
            setPassword(toUpdate.getPassword());
            mod |= true;
        }

        if (toUpdate.getJwt() != null){
            setUser(toUpdate.getJwt());
            mod |= true;
        }

        if (toUpdate.getSecret() != null){
            setUser(toUpdate.getSecret());
            mod |= true;
        }

        if (toUpdate.getRoles() != null){
            setRoles(toUpdate.getRoles());
            mod |= true;
        }

        if (mod){
            this.validate();
        }
    }

    @Override
    public void destroy() throws Exception {
        this.delete();
    }
}
