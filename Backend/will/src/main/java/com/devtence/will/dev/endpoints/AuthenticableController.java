package com.devtence.will.dev.endpoints;

import com.devtence.will.dev.commons.wrappers.AuthorizationWrapper;
import com.devtence.will.dev.commons.wrappers.BooleanWrapper;
import com.devtence.will.dev.models.BaseModel;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

/**
 * Interface for a BaseController Class that handles authentication and must implement the following methods

 * @author plessmann
 * @since 2016-06-03
 * @see BaseController
 * @see BaseModel
 */
public interface AuthenticableController<T extends BaseModel> {

    /**
     * This method should validate the user and generate a JWT to be used for authenticated operations
     * @param data  user and password of the AuthenticableEntity
     * @param user  user that accepts the client being used to for the authentication operation
     * @return  an object containing the JWT to be uesed in subsequent operations
     * @throws BadRequestException  the data parameter is missing data needed to authenticate
     * @throws InternalServerErrorException a generic error ocurred that is not related to the input
     * @throws NotFoundException    the username trying to authenticate doesnt exists
     * @throws UnauthorizedException    the user parameter did not pass the authentication test
     */
    @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST)
    AuthorizationWrapper authenticate(T data, User user) throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException;

    /**
     * Method to start the password recovery process, this mechanic should be defined in a porpper way
     * @param data  data to identify the user starting the process
     * @param user  user that accpets the client being used to for the authentication operation
     * @return  a simple flag defining the succes of the start of the process
     * @throws BadRequestException  the data parameter is missing data needed to init the process
     * @throws InternalServerErrorException a generic error ocurred that is not related to the input
     * @throws NotFoundException    the username trying to authenticate doesnt exists
     * @throws UnauthorizedException    the user parameter did not pass the authentication test
     */
    @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST)
    BooleanWrapper recoverPassword (T data, User user) throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException;

    /**
     * Method to finsih the password recovery process, this mechanic should be defined in a porpper way
     * @param data  data to identify the user starting the process
     * @param user  user that accpets the client being used to for the authentication operation
     * @return  a simple flag defining the succes of the finish of the process
     * @throws BadRequestException  the data parameter is missing data needed to complet e the process
     * @throws InternalServerErrorException a generic error ocurred that is not related to the input
     * @throws NotFoundException    the username trying to authenticate doesnt exists
     * @throws UnauthorizedException    the user parameter did not pass the authentication test
     */
    @ApiMethod(httpMethod = ApiMethod.HttpMethod.PUT)
    BooleanWrapper updatePassword(T data, User user) throws BadRequestException, InternalServerErrorException, NotFoundException, UnauthorizedException;

    /**
     * Mehod to check for the existance of a field to be used as a unique identifier for the entitty. for example the username
     * @param data  element of T containing the field to check
     * @param user  user that accpets the client being used to for the authentication operation
     * @return  flag defining the existance of the filed in the persistacne unit
     * @throws BadRequestException  the data parameter is missing data to check the uniqueness
     * @throws InternalServerErrorException a generic error ocurred that is not related to the input
     * @throws UnauthorizedException    the user parameter did not pass the authentication test
     */
    @ApiMethod(httpMethod = ApiMethod.HttpMethod.POST)
    BooleanWrapper checkUser(T data, User user) throws BadRequestException, InternalServerErrorException, UnauthorizedException;

}
