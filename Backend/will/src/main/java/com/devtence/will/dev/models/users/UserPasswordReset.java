package com.devtence.will.dev.models.users;

import com.devtence.will.dev.exceptions.MissingFieldException;
import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.DbObjectify;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Created by plessmann on 03/06/16.
 */
@Entity
public class UserPasswordReset extends BaseModel<UserPasswordReset> implements Serializable {

	private Long idUser;
	@Index
	private String webToken;
	private String secret;

	public UserPasswordReset() {
	}

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

	@Override
	public void destroy() throws Exception {
		this.delete();
	}

	@Override
	public void update(UserPasswordReset data) throws Exception {

	}

	public static UserPasswordReset getByToken(String webToken) throws Exception {
		return DbObjectify.ofy().load().type(UserPasswordReset.class).filter("webToken", webToken).first().now();
	}
}
