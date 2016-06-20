	(function() {
		'use strict';

		angular
			.module('template.devtence.endpoints')
			.factory('UrlService', UrlService);

		UrlService.$injector = [];
		function UrlService() {
			var apiUrl = 'https://lpapp-1001.appspot.com/_ah/api';
			var apiVersion = 'v1';

			var webservices = {
				login: Login,
				addUser: AddUser,
				modUser: ModUser,
				resetUser: ResetUser,
				getGCSAuth : getGCSAuth
			};

			return webservices;

			//Definition of functions

			function Login(username, password){
				var queryString = "?p=" + encodeURIComponent(password) + "&u=" + encodeURIComponent(username);
				return buildUrl("accounts", "verifyCredentialsSecure", [queryString]);
			}

			function AddUser(){
				return buildUrl("accounts", "addUser");
			}

			function ModUser(idUser){
				return buildUrl("accounts", "modifyUser",[idUser]);
			}

			function ResetUser(username){
				var queryString = "?u=" + encodeURIComponent(username);
				return buildUrl("accounts", "resetUser", [queryString])
			}

			function getGCSAuth(contentType){
				var params = "?type=" + encodeURIComponent(contentType);
				return buildUrl('upload', 'auth', [params]);
			}

			function buildUrl(api, method, params){
				var base = [apiUrl , api , apiVersion, method];
				var url = base.join('/');

				if(angular.isDefined(typeof params) && params != null){
					url = url.concat(params);
				}

				return url;
			}
		}

	})();
