'use strict';

angular
.module('template.devtence.endpoints')
.factory('ConnectionManager', ConnectionManager);
ConnectionManager.$injector = ['$timeout', '$q', '$http', '$log'];

function ConnectionManager($timeout, $q, $http, $log) {
	var service = {};
	service.get = get;
	service.post = post;
  service.put = put;
  service.remove = remove;

	function get(url, config){
		if(typeof config === 'undefined'){
			config = {};
		}

		var request = config;
		request.method = 'GET';
		request.url = url;
		return doRequest(request);
	}

	function post(url, config, data){
		if(typeof config === 'undefined'){
			config = {};
		}

		if(typeof data === 'undefined'){
			data = {};
		}

		var request = config;
		request.method = 'POST';
		request.url = url;
		request.data = data;
		return doRequest(request)
	}

  function put(url, config, data){
    if(typeof config === 'undefined'){
      config = {};
    }

    if(typeof data === 'undefined'){
      data = {};
    }

    var request = config;
    request.method = 'PUT';
    request.url = url;
    request.data = data;
    return doRequest(request)
  }

  function remove(url, config, data){
		if(typeof config === 'undefined'){
			config = {};
		}

		if(typeof data === 'undefined'){
			data = {};
		}

		var request = config;
		request.method = 'DELETE';
		request.url = url;
		request.data = data;
		return doRequest(request)
	}


	function doRequest(request){
		var deferred = $q.defer();

		$log.log("ConnectionManager request:", request);

		$http(request).then(success,error).finally(end);

		function success(response) {
			try{
				$log.log("ConnectionManager Success:", response)
				deferred.resolve(response);
			} catch(err) {
				deferred.reject(response);
			}
		}

		function error(response) {
			$log.log("ConnectionManager error:", response);
			deferred.reject(response);
		}

		function end(){
			$log.log("Finaliza el request");
		}

		return deferred.promise;
	}

	return service;
}
