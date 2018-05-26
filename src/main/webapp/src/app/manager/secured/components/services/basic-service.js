'use strict';

goog.provide('jxmgrsec.basic.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmgrsec.basic.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];
jxmgrsec.basic.Service = function($rootScope, $http, $q) {
  var basic = this;

  // data = {
  //   username: 'username',
  //   password: 'password',
  //   captcha: 'captcha'
  // }

  basic.signOut = function(rbody) {
    var defer = $q.defer();

    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/sign_out',
      data: rbody
    }
    $http(req)
      .success(function(res) {
        defer.resolve(res);
      })
      .error(function(err, status) {
        defer.reject(err);
      })

    return defer.promise;
  }

  //3.3.2.3 GET /admins/settings/basic
  basic.getManagerBasics = function() {
    var defer = $q.defer();
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/settings/basic'
    }
    $http(req)
      .success(function(res) {
        defer.resolve(res);
      })
      .error(function(err, status) {
        defer.reject(err);
      })
    return defer.promise;
  }

  //PUT /admins/settings/password
  basic.change_password = function(password) {
    var defer = $q.defer();
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/settings/password',
      data: password
    }
    $http(req)
      .success(function(res) {
        defer.resolve(res);
      })
      .error(function(err, status) {
        defer.reject(err);
      })
    return defer.promise;
  }

  //文件上传
  //3.2.1.3 POST /common/uploads
  basic.fileUpload = function(file) {
    var defer = $q.defer();
    if (file == undefined || file == null || file == "") {
      return null;
    }
    var uploadUrl = $rootScope.endPoint + '/common/uploads';
    var fd = new FormData();
    fd.append('files', file);
    $http.post(uploadUrl, fd, {
        transformRequest: angular.identity,
        headers: {
          'Content-Type': undefined
        }
      })
      .success(function(succ) {
        defer.resolve(succ);
      })
      .error(function(error) {
        defer.resolve(error);
      });
    return defer.promise;
  }

  //3.4.1.6 GET /admins/sign_in
  // basic.getManagerBasics = function(){
  //     var defer = $q.defer();
  //     var req = {
  //         method: 'GET',
  //         url: $rootScope.endPoint + '/admins/settings/basic'
  //         //data: password
  //     }
  //     $http(req)
  //         .success(function(res){
  //             defer.resolve(res);
  //         })
  //         .error(function(err, status){
  //             defer.reject(err);
  //         })
  //     return defer.promise;
  // }

  // PUT /admins/settings/avatar
  basic.updateManAvatar = function(avatar_url) {
    var defer = $q.defer();
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/settings/avatar',
      data: avatar_url
    }
    $http(req)
      .success(function(res) {
        defer.resolve(res);
      })
      .error(function(err, status) {
        defer.reject(err);
      })
    return defer.promise;
  }

  //  GET /admins/settings/basic
  basic.getAdminBasics = function() {
    var defer = $q.defer();
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/settings/basic'
        //data: password
    }
    $http(req)
      .success(function(res) {
        defer.resolve(res);
      })
      .error(function(err, status) {
        defer.reject(err);
      })
    return defer.promise;
  }

  // PUT /admins/settings/basic
  basic.putAdminBasic = function(admin) {
    var defer = $q.defer();
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/settings/basic',
      data: admin
    }
    $http(req)
      .success(function(res) {
        defer.resolve(res);
      })
      .error(function(err, status) {
        defer.reject(err);
      })
    return defer.promise;
  }
  return basic;
};
