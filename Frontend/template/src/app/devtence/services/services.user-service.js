(function() {
  'use strict';

  angular.module('template.devtence.services')
    .factory('UserService', UserService);

  UserService.$injector = ['$timeout', '$q', '$filter','User', 'localStorageService', 'ConnectionManager', 'UrlService', '$log'];
  function UserService($timeout, $q, $filter, User, localStorageService, ConnectionManager, UrlService, $log) {
    var service = {
      doLogin: DoLogin,
      create: Create,
      activate: Activate,
      reset: Reset,
      changePassword: ChangePassword,
      createProfile: CreateProfile,
      getProfile: GetProfile
    };

    function DoLogin(username, password){
      var deferred = $q.defer();
      ConnectionManager.post(UrlService.login(username, password), {}, {}, true).then(success, error);

      function success(response){
        $log.log("UserService: Verified credentials!!!, data:", response);
        User.updateUserData(response.data);
        deferred.resolve('Success');
      }

      function error(response){
        deferred.reject('Fail');
      }

      return deferred.promise;
    }

    function Create(user) {
      User.logOut();
      var deferred = $q.defer();

      var postData = {};
      postData.loginEmail = user.username;
      postData.password  = user.password;

      var rightNow = new Date();
      postData.subscriptionDate = rightNow.toISOString().slice(0,10).replace(/-/g,"");
      postData.userType = "REGULAR";
      $log.log("postData:", postData);

      ConnectionManager.post(UrlService.addUser(), {}, postData, true).then(success, error);

      function success(response){
        $log.log("Create user succes:", response);
        deferred.resolve("Success");
      }

      function error(response){
        $log.log("Create user error:", response.data);
        deferred.reject("Fail:" + response.data.error.message);
      }

      return deferred.promise;
    }

    function Activate(username, activationToken){
      var deferred = $q.defer();

      ConnectionManager.post(UrlService.activateUser(username, activationToken), {}, {}, true).then(success, error);

      function success(response){
        $log.log("user activated:", response);
        deferred.resolve("Success");
      }

      function error(response){
        $log.log("Activation failed:", response.data);
        deferred.reject("Fail:" + response.data.error.message);
      }

      return deferred.promise;
    }

    function Reset(username){
      var deferred = $q.defer();
      ConnectionManager.post(UrlService.resetUser(username), {}, {}, true).then(success, error);

      function success(response){
        $log.log("user reset:", response);
        deferred.resolve("Success");
      }

      function error(response){
        $log.log("reset failed:", response.data);
        deferred.reject("Fail:" + response.data.error.message);
      }

      return deferred.promise;
    }

    function ChangePassword(userId, password){
      var deferred = $q.defer();

      var postData = {};
      postData.id = userId;
      postData.password  = password;

      $log.log("postData:", postData);

      ConnectionManager.post(UrlService.modUser(userId), {}, postData, true).then(success, error);

      function success(response){
        $log.log("user reset:", response);
        deferred.resolve("Success");
      }

      function error(response){
        $log.log("reset failed:", response.data);
        deferred.reject("Fail:" + response.data.error.message);
      }

      return deferred.promise;

    }

    function CreateProfile(user){
      var deferred = $q.defer();

      var postData = {};
      postData.firstName = user.name;
      postData.lastName = user.lastName;
      postData.companyName = user.company;
      postData.profilePictureLocation = user.pic;
      postData.bio = user.bio;
      postData.birthDate = user.bday;

      //calling ws
      ConnectionManager.post(UrlService.modProfile(User.getUserId()), {}, postData, true).then(success, error);

      function success(response){
        $log.log("mod profile succes:", response);
        deferred.resolve("Success");
      }

      function error(response){
        $log.log("mod profile error:", response.data);
        deferred.reject("Fail:" + response.data.error.message);
      }
      return deferred.promise;
    }

    function GetProfile(){
      var deferred = $q.defer();

      //call ws
      ConnectionManager.get(UrlService.getProfile(User.getUserId()), {}, true).then(success, error);

      function success(response){
        $log.log("get profile succes:", response);
        var data = response.data;

        deferred.resolve("Success");
      }

      function error(response){
        $log.log("get profile error:", response.data);
        deferred.reject("Fail:" + response.data.error.message);
      }


      return deferred.promise;
    }

    return service;
  }
})();
