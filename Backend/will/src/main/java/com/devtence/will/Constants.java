package com.devtence.will;

/**
 * Contains the client IDs and scopes for allowed clients consuming your API.
 */
public class Constants {
	public static final String WEB_CLIENT_ID = "replace this with your web client ID";
	public static final String ANDROID_CLIENT_ID = "replace this with your Android client ID";
	public static final String IOS_CLIENT_ID = "replace this with your iOS client ID";
	public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;

	public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";

	public static final String AUTHORIZATION = "Authorization";
	public static final String AUTHORIZATION_KEY = "Authorization-key";
	public static final String AUTHORIZATION_CLIENT = "Authorization-client";
	public static final String ERROR = "Error";
	public static final String INVALID_JWT_OR_SECRET = "Invalid jwt or secret";
	public static final String INVALID_ID = "Invalid id = %d";

	public static final String IAM_API_NAME = "iam";
	public static final String COMMON_API_NAME = "commons";
	public static final String API_MASTER_VERSION = "v1";
	public static final String INVALID_USER = "Invalid user";
	public static final String INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE = "there was an error processing the request";

	public static final String CONFIGURATION_ERROR_CREATE = "Error creating configuration: %s";
	public static final String CONFIGURATION_ERROR_NOT_FOUND = "Configuration not found with index: %d";

	public static final String CLIENT_ERROR_CREATE = "Error creating client: %s";
	public static final String CLIENT_ERROR_NOT_FOUND = "Client not found with index: %d";

	public static final String ROLE_ERROR_CREATE = "Error creating role: %s";
	public static final String ROLE_ERROR_NOT_FOUND = "Role not found with index: %d";

	public static final String USER_ERROR_CREATE = "Error creating user: %s";
	public static final String USER_ERROR_NOT_FOUND = "User not found with index: %d";
	public static final String USER_NOT_FOUND = "User not found with user: %s";


	public static final String INVALID_PASSWORD = "Invalid password";

}
