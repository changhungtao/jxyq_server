'use strict';

goog.provide('jxdctsec.basic.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxdctsec.basic.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];
jxdctsec.basic.Service = function($rootScope,$http, $q) {
  var basic = this;

  // data = {
  //   username: 'username',
  //   password: 'password',
  //   captcha: 'captcha'
  // }

  basic.signIn = function(rbody){
    var defer = $q.defer();
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/doctors/sign_in',
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
  }

    //3.4.1.10	POST /doctors/settings/password
    basic.change_password = function(password){
        var defer = $q.defer();
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/settings/password',
            data: password
        }
        $http(req)
            .success(function(res){
                defer.resolve(res);
            })
            .error(function(err, status){
                defer.reject(err);
            })
        return defer.promise;
    }

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

    //3.4.1.5	POST /doctors/sign_out
    basic.signOut = function(){
        var defer = $q.defer();
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/doctors/sign_out'
        }
        $http(req)
            .success(function(res){
                defer.resolve(res);
            })
            .error(function(err, status){
                defer.reject(err);
            })
        return defer.promise;
    }

    //3.4.1.6	GET /doctors/settings/basic
    basic.getDoctorBasics = function(){
        var defer = $q.defer();
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/doctors/settings/basic'
            //data: password
        }
        $http(req)
            .success(function(res){
                defer.resolve(res);
            })
            .error(function(err, status){
                defer.reject(err);
            })
        return defer.promise;
    }

    //3.4.1.7	PUT /doctors/settings/basic
    basic.putDoctorBasic = function(doctor){
        var defer = $q.defer();
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/doctors/settings/basic',
            data: doctor
        }
        $http(req)
            .success(function(res){
                defer.resolve(res);
            })
            .error(function(err, status){
                defer.reject(err);
            })
        return defer.promise;
    }

    //3.4.1.8	PUT /doctors/settings/avatar
    basic.updateDocAvatar = function(avatar_url){
        var defer = $q.defer();
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/doctors/settings/avatar',
            data: avatar_url
        }
        $http(req)
            .success(function(res){
                defer.resolve(res);
            })
            .error(function(err, status){
                defer.reject(err);
            })
        return defer.promise;
    }

  return basic;
};
