'use strict';

goog.provide('jxmnfsec.basic.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmnfsec.basic.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];
jxmnfsec.basic.Service = function($http, $q, $rootScope) {
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
      url: $rootScope.endPoint + '/manufactories/sign_in',
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

     //POST /manufactories/settings/password
    basic.change_password = function(password){
        var defer = $q.defer();
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/manufactories/settings/password',
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

        //3.5.1.5   POST /manufactories/sign_out
    basic.signOut = function(){
        var defer = $q.defer();
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/manufactories/sign_out'
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
    //3.5.2.1 POST /manufactories/terminal_catagory/add
    basic.submitTerminalCatagory = function(terminal){
        var defer = $q.defer();
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/manufactories/terminal_catagory/add',
            data: terminal
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
     // GET /manufactories/settings/basic
    
    basic.getManufactoryBasics = function(){
        var defer = $q.defer();
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/manufactories/settings/basic'
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

    // PUT /manufactories/settings/basic
    basic.putManufactoryBasic = function(manufactories){
        var defer = $q.defer();
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/manufactories/settings/basic',
            data: manufactories
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

    //PUT /manufactories/settings/avatar
    basic.updateManufactoryAvatar = function(logo_url){
        var defer = $q.defer();
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/manufactories/settings/avatar',
            data: logo_url
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
    //3.5.2.1   POST /manufactories/terminal_catagory/add
    basic.addTerminalCatagory = function(rbody){
        var defer = $q.defer();

        var req = {
          method: 'POST',
          url: $rootScope.endPoint + '/manufactories/terminal_catagory/add',
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

  return basic;
};
