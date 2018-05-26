'use strict';

goog.provide('jxmnfopn.basic.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmnfopn.basic.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];

jxmnfopn.basic.Service = function($rootScope, $http, $q) {
  var basic = this;

  basic.signIn = function(rbody){
    var defer = $q.defer();
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/manufactories/sign_in',
      // dataType: 'json',
      data: rbody
    }
    $http(req)
    .success(function(res){
      defer.resolve(res);
    })
    .error(function(err, status){
      defer.reject(err);
    })

    return defer.promise;
  };

  basic.verificationCode = function(rbody){
    var defer = $q.defer();
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/common/verification_code',
      params: rbody
    }
    $http(req)
    .success(function(res){
      defer.resolve(res);
    })
    .error(function(err, status){
      defer.reject(err);
    })

    return defer.promise;
  };

  basic.resetPassword = function(rbody){
    var defer = $q.defer();
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/manufactories/reset_password',
      data: rbody
    }
    $http(req)
    .success(function(res){
      defer.resolve(res);
    })
    .error(function(err, status){
      defer.reject(err);
    })

    return defer.promise;
  };

basic.signUp = function(rbody){
    var defer = $q.defer();
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/manufactories/sign_up',
      data: rbody
    }
    $http(req).
    success(function(res){
      defer.resolve(res);
    })
    .error(function(err, status){
      defer.reject(err);
    })

    return defer.promise;
  };

    //文件上传
    //3.2.1.3	POST /common/uploads
    basic.fileUpload = function(file){
        var defer = $q.defer();
        if(file == undefined || file == null || file == ""){
            return null;
        }
        var uploadUrl = $rootScope.endPoint + '/common/uploads';
        var fd = new FormData();
        fd.append('files', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function(succ){
                defer.resolve(succ);
            })
            .error(function(error){
                defer.resolve(error);
            });
        return defer.promise;
    }

  return basic;
};
