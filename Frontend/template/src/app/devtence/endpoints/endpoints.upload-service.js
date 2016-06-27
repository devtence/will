'use strict';

angular
.module('template.devtence.endpoints')
.factory('UploadService', UploadService);

UploadService.$injector = [];
function UploadService(Upload) {
	var service = {};
  service.upload = upload;

  function upload(GCSUrl, authKey, contentType, file, success, error, progress){
    return Upload.http({
      url : GCSUrl,
      data : file,
      headers : {
        'Content-Type': contentType,
        'Authorization': authKey
      },
      method : 'PUT'
    })
    .progress(progress)
    .success(success)
    .error(error);
  }

	return service;
}
