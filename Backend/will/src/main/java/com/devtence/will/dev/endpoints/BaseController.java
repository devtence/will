package com.devtence.will.dev.endpoints;

import com.devtence.will.Constants;
import com.devtence.will.dev.commons.authenticators.UserAuthenticator;
import com.devtence.will.dev.models.BaseModel;
import com.devtence.will.dev.models.ListItem;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.*;

import java.util.List;


/**
 * Abstract Class created to serve as the base for the controllers that use Google App engine to define endpoints
 * Defines basic methods every model must have, and implements the basic functions to validate an user
 *
 * every class that extends from this will have the UserAuthenticator class as default authenticator

 * @author plessmann
 * @since 2016-06-02
 *
 * @param <T>   type that extends BaseModel
 */
@ApiClass(
    scopes = {Constants.EMAIL_SCOPE},
    clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
    audiences = {Constants.ANDROID_AUDIENCE},
    authenticators = {UserAuthenticator.class}
)
public abstract class BaseController<T extends BaseModel> {

    /**
     * Base method to create elements of type T which must be extensions of BaseModel
     *
     * @param data  BaseModel child containing the data to insert
     * @param user  user provided by authentication to restrict access to this operation
     * @return      the instance that was created in the persistence unit
     * @throws BadRequestException  the data parameter is missing data needed to create it
     * @throws ConflictException    the instance to be created already exists
     * @throws InternalServerErrorException a generic error ocurred that is not related to the input
     * @throws UnauthorizedException    the user parameter did not pass the authentication test
     */
    @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST)
    public abstract T create(T data, User user) throws BadRequestException, ConflictException,InternalServerErrorException, UnauthorizedException;

    /**
     * Base method to obtain an entity that exists in the persistence unit
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict acces to this operation
     * @return      insntace of type T that holds the id
     * @throws NotFoundException    the instance related to the id doesnt exists in the persistance unit
     * @throws InternalServerErrorException a generic error ocurred that is not related to the input
     * @throws UnauthorizedException    the user parameter did not pass the authentication test
     */
    @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET)
    public abstract T read(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException;

    /**
     * Base method to update an entity of type T that exists in the persistance unit
     * @param id    id of the instance of type T to e updated
     * @param data  instance of the same type that holds the new values
     * @param user  user provided by authentication to restrict access to this operation
     * @return      insntace of type T updated
     * @throws BadRequestException  the data parameter is missing data needed to update it
     * @throws NotFoundException    the instance related to the id doesnt exists in the persistance unit
     * @throws InternalServerErrorException a generic error ocurred that is not related to the input
     * @throws UnauthorizedException    the user parameter did not pass the authentication test
     */
    @ApiMethod(httpMethod = ApiMethod.HttpMethod.PUT)
    public abstract T update(@Named("id") Long id, T data, User user) throws BadRequestException, NotFoundException, InternalServerErrorException, UnauthorizedException;

    /**
     * Base method to delete an entity that exists in the persistance unit
     * @param id    id of the required instance of type T
     * @param user  user provided by authentication to restrict acces to this operation
     * @return      insntace of type T that holds the id and was deleted
     * @throws NotFoundException    the instance related to the id doesnt exists in the persistance unit
     * @throws InternalServerErrorException a generic error occurred that is not related to the input
     * @throws UnauthorizedException    the user parameter did not pass the authentication test
     */
    @ApiMethod(httpMethod = ApiMethod.HttpMethod.DELETE)
    public abstract T delete(@Named("id") Long id, User user) throws NotFoundException, InternalServerErrorException, UnauthorizedException;

    /**
     * Base method to get a segmented and sorted list of elements of type T
     * @param index initial point of the segment
     * @param limit max elements for the segment
     * @param sortFields    array of strings with the names of the fields to be used to sort the data
     * @param sortDirections    array of booleans that define wether the sortings is DEC or not
     * @param cursor        index of the previous segmente required using this method
     * @param user  user provided by authentication to restrict acces to this operation
     * @return  object containign the total elemnts of type T in the persistence unit, the cursor for the current segment and the list of the elements in the segemnt
     * @throws InternalServerErrorException a generic error occurred that is not related to the input
     * @throws UnauthorizedException    the user parameter did not pass the authentication test
     */
    @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET)
    public abstract ListItem list(@Named("index") @Nullable @DefaultValue("0") Integer index, @Named("limit") @Nullable @DefaultValue("100") Integer limit, @Named("sortFields") @Nullable List<String> sortFields, @Named("sortDirection") @Nullable List<Boolean> sortDirections, @Named("cursor") @Nullable String cursor, User user) throws InternalServerErrorException, UnauthorizedException;

    /**
     * method to check if the call has valid authorization
     * @param user  user isntatiated by the authenticator
     * @throws UnauthorizedException    if the user is null the user is not authorized
     */
    public static void validateUser(User user) throws UnauthorizedException {
        if(user == null){
            throw new UnauthorizedException(Constants.INVALID_USER);
        }
    }

}
