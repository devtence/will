(function() {
  'use strict';

  angular.module('template.devtence.services').factory('User', User);

  User.$injector = ['$timeout', 'localStorageService', '$log'];
  function User($timeout, localStorageService, $log) {
    var USER_DATA_STORAGE_KEY = 'UserData';
    var UserData = {};
    var loggedIn = false;
    var service = {
      isLoggedIn: IsLoggedIn,
      isUserLogged: IsUserLogged,
      logout: LogOut,
      updateUserData: UpdateUserData,
      getUserData: GetUserData,
      getUserId: GetUserId
    };
    Init();
    return service;

    //Definition of this service
    function IsLoggedIn(){
      return loggedIn;
    }

    function IsUserLogged(_username){
      return ((angular.isDefined(typeof UserData.loginEmail)) && (UserData.loginEmail === _username))
    }

    function LogOut(){
      UserData = {};
      localStorageService.remove(USER_DATA_STORAGE_KEY);
      loggedIn = false;
    }

    function UpdateUserData(data){
      UserData.id = data.id;
      UserData.loginEmail = data.loginEmail;
      if(angular.isDefined(typeof data.subscriptionDate)){
      UserData.subscriptionDate = data.subscriptionDate;
    }

    localStorageService.set(USER_DATA_STORAGE_KEY, UserData);
      loggedIn = true;
    }

    function GetUserData(){
      return UserData;
    }

    function GetUserId(){
      if(angular.isDefined(UserData.id)){
        return UserData.id;
      }
      return undefined;
    }

    function Init(){
      UserData = localStorageService.get(USER_DATA_STORAGE_KEY);
      $log.log('Init User, data:',UserData);

      if(UserData !== null && angular.isDefined(typeof UserData.loginEmail)){
        loggedIn = true;
      } else {
        UserData = {};
        loggedIn = false;
      }
    }
  }

})();
