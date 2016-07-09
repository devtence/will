package com.devtence.will;

/**
 * Contains the client IDs and scopes for allowed clients consuming your API and other aplications constants.
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
	public static final String INVALID_KEY = "Invalid key = %s";

	public static final String IAM_API_NAME = "iam";
	public static final String JAZZ_API_NAME = "jazz";
	public static final String COMMON_API_NAME = "commons";
	public static final String FILES_API_NAME = "files";
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

	public static final String LANGUAGE_ERROR_CREATE = "Error creating language: %s";
	public static final String LANGUAGE_ERROR_NOT_FOUND = "Language not found with index: %d";

    public static final String AUTHOR_ERROR_CREATE = "Error creating author: %s";
    public static final String AUTHOR_ERROR_NOT_FOUND = "Author not found with index: %d";

    public static final String CATEGORY_ERROR_CREATE = "Error creating category: %s";
    public static final String CATEGORY_ERROR_NOT_FOUND = "Category not found with index: %d";

    public static final String CONTENT_ERROR_CREATE = "Error creating content: %d";
    public static final String CONTENT_ERROR_NOT_FOUND = "Content not found with index: %d";

	public static final String INVALID_PASSWORD = "Invalid password";

	public static final String NOTIFY = "/notify";
	public static final String ID = "id";
	public static final String NOTIFICATION_MNEMONIC = "notification_mnemonic";
	public static final String PASSWORD_RECOVERY_NOTIFICATION = "RECPWD";
	public static final String NOTIFICATOR_KEY = "notificator";
	public static final String USER_PASSWORD_RECOVERY = "com.devtence.will.dev.notificators.UserPasswordRecovery";
	public static final String NO_PASSWORD_RESET_PROCESS_PENDING = "There is no password reset process pending";
	public static final String TOKEN = "token";
	public static final String INVALID_PARAMS = "invalid params";

	public static final String GENERIC_USER = "user@devtence.com";
	public static final String GENERIC_KEY = "OK";

	//Test constants
	public static final String RESULT_MUST_BE_NULL = "Result must be null";
	public static final String RESULT_MUST_NOT_BE_NULL = "Result must not be null";
	public static final String ID_MUST_NOT_BE_NULL = "Id must not be null";
	public static final String CONFIG_KEY_MUST_NOT_BE_NULL = "Config Key must not be null";
	public static final String DESCRIPTION_MUST_NOT_BE_NULL = "Description must not be null";
	public static final String VALUE_MUST_NOT_BE_NULL = "Value must not be null";
	public static final String CONFIG_KEY_MUST_NOT_BE_EMPTY = "Config Key must not be empty";
	public static final String DESCRIPTION_MUST_NOT_BE_EMPTY = "Description must not be empty";
	public static final String VALUE_MUST_NOT_BE_EMPTY = "Value must not be empty";
	public static final String CONFIG_KEY_MUST_BE_VALUE = "Config Key must be %s";
	public static final String DESCRIPTION_MUST_BE_VALUE = "Description must be %s";
	public static final String VALUE_MUST_BE_VALUE = "Value must be %s";
	public static final String ARRAYS_MUST_HAVE_SAME_SIZE = "Arrays must have same size";
	public static final String SEPARATOR = ";";
	public static final String LIST_MUST_BE_NULL = "List must be null";
	public static final String LIST_MUST_NOT_BE_NULL = "List must not be null";
	public static final String LIST_MUST_NOT_BE_EMPTY = "List must not be empty";
	public static final String CONFIG_KEY = "configKey";
	public static final String DESCRIPTION = "description";
	public static final String VALUE = "value";
	public static final String ASC = "ASC";
	public static final int INDEX = 0;
	public static final int OFFSET = 100;
	public static final String LIST_SIZE_MUST_BE_SEVEN = "List size must be seven";


	public static final String SENDER_MUST_NOT_BE_NULL = "Sender must not be null";
	public static final String RECIPIENTS_MUST_NOT_BE_NULL = "Recipients must not be null";
	public static final String SUBJECT_MUST_NOT_BE_NULL = "Subject must not be null";
	public static final String MESSAGE_MUST_NOT_BE_NULL = "Message must not be null";
	public static final String MNEMONIC_MUST_NOT_BE_NULL = "Mnemonic must not be null";
	public static final String SENDER_MUST_NOT_BE_EMPTY = "Sender must not be empty";
	public static final String RECIPIENTS_MUST_NOT_BE_EMPTY = "Recipients must not be empty";
	public static final String SUBJECT_MUST_NOT_BE_EMPTY = "Value must not be empty";
	public static final String MESSAGE_MUST_NOT_BE_EMPTY = "Message must not be empty";
	public static final String MNEMONIC_MUST_NOT_BE_EMPTY = "Mnemonic must not be empty";
	public static final String SENDER_MUST_BE_VALUE = "Sender must be %s";
	public static final String RECIPIENTS_MUST_BE_VALUE = "Recipients must be %s";
	public static final String SUBJECT_MUST_BE_VALUE = "Subject must be %s";
	public static final String MESSAGE_MUST_BE_VALUE = "Message must be %s";
	public static final String MNEMONIC_MUST_BE_VALUE = "Mnemonic must be %s";
	public static final String MNEMONIC = "mnemonic";
	public static final String NAME = "name";
	public static final String USERNAME = "user";

	public static final String NAME_MUST_NOT_BE_NULL = "Name must not be null";
	public static final String PERMISSION_MUST_NOT_BE_NULL = "Permission must not be null";
	public static final String NAME_MUST_NOT_BE_EMPTY = "Name must not be empty";
	public static final String PERMISSION_MUST_NOT_BE_EMPTY = "Permission must not be empty";
	public static final String NAME_MUST_BE_VALUE = "Name must be %s";
	public static final String PERMISSION_MUST_BE_VALUE = "Permission must be %s";
	public static final String USER_MUST_NOT_BE_NULL = "User must not be null";
	public static final String EMAIL_MUST_NOT_BE_NULL = "Email must not be null";
	public static final String PASSWORD_MUST_NOT_BE_NULL = "Password must not be null";
	public static final String ROLES_MUST_NOT_BE_NULL = "Roles must not be null";
	public static final String USER_MUST_NOT_BE_EMPTY = "User must not be empty";
	public static final String EMAIL_MUST_NOT_BE_EMPTY = "Email must not be empty";
	public static final String PASSWORD_MUST_NOT_BE_EMPTY = "Password must not be empty";
	public static final String ROLES_MUST_NOT_BE_EMPTY = "Roles must not be empty";
	public static final String USER_MUST_BE_VALUE = "User must be %s";
	public static final String EMAIL_MUST_BE_VALUE = "Email must be %s";
	public static final String PASSWORD_MUST_BE_VALUE = "Password must be %s";
	public static final String ROLE_MUST_BE_VALUE = "Role must be %s";
	public static final String AUTHORIZATION_MUST_NOT_BE_NULL = "Authotization must not be null";
	public static final String AUTHORIZATION_KEY_MUST_NOT_BE_NULL = "Authotization key must not be null";
	public static final String AUTHORIZATION_MUST_NOT_BE_EMPTY = "Authotization must not be empty";
	public static final String AUTHORIZATION_KEY_MUST_NOT_BE_EMPTY = "Authotization key must not be empty";
	public static final String RESULT_MUST_BE_TRUE = "Result must be true";
	public static final String RESULT_MUST_BE_FALSE = "Result must be false";

	public static final String WEB_TOKEN_MUST_NOT_BE_NULL = "Web token must not be null";
	public static final String SECRET_MUST_NOT_BE_NULL = "Secret must not be null";
	public static final String WEB_TOKEN_MUST_NOT_BE_EMPTY = "Web token must not be empty";
	public static final String SECRET_MUST_NOT_BE_EMPTY = "Secret must not be empty";
	public static final String DESC_SORTER = "-";

	public static final String VALUE_MUST_BE_MAJOR_OR_EQUAL = "Value must be major or equal";
	public static final String VALUE_MUST_BE_MINOR_OR_EQUAL = "Value must be minor or equal";


	// If set to true, the app will use Memcached. If set to false it'll consult the DB all the time
	public static final boolean USE_CACHE = true;
}
