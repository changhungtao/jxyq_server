'use strict';

goog.provide('jxsprsec.basic.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxsprsec.basic.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];
jxsprsec.basic.Service = function($http, $q, $rootScope) {
  var basic = this;

  // data = {
  //   username: 'username',
  //   password: 'password',
  //   captcha: 'captcha'
  // }

  basic.signIn = function(rbody) {
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

  //3.3.2.2 POST /admins/sign_out
  basic.signOut = function() {
    var defer = $q.defer();
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/sign_out'
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

  //3.3.9.1 GET /admins/db/normal_managers
  basic.getNormalManagers = function(manager) {
    var defer = $q.defer();
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/normal_managers',
      params: manager
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

  //3.3.9.2 PUT /admins/db/normal_managers/{mid}
  basic.updateNormalManagers = function(mid, manager) {
    var defer = $q.defer();
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/normal_managers/' + mid,
      data: manager
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


  //3.3.9.3 POST /admins/db/normal_managers
  basic.addManager = function(manager) {
    var defer = $q.defer();
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/normal_managers',
      data: manager
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

  //3.3.9.4 PUT /admins/db/normal_managers/status/{mid}
  basic.updateStatus = function(mid, status) {
    var defer = $q.defer();
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/normal_managers/status/' + mid,
      data: status
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

  //3.3.9.6 GET /admins/permissions/normal_managers/{mid}
  basic.getManagerPermissions = function(mid) {
    var defer = $q.defer();
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/permissions/normal_managers/' + mid,
      data: status
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

  //3.3.9.7 PUT /admins/permissions/normal_managers/{mid}
  basic.editManagerPermissions = function(mid, permission) {
    var defer = $q.defer();
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/permissions/normal_managers/' + mid,
      data: permission
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

  //3.3.9.5 DELETE /admins/db/normal_managers/{mid}
  basic.deleteManager = function(mid) {
    var defer = $q.defer();
    var req = {
      method: 'DELETE',
      url: $rootScope.endPoint + '/admins/db/normal_managers/' + mid
        //            data: permission
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
