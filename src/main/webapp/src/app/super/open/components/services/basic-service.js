'use strict';

goog.provide('jxspropn.basic.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxspropn.basic.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];

jxspropn.basic.Service = function($rootScope, $http, $q) {
  var basic = this;

  basic.signIn = function(rbody){
    var defer = $q.defer();
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/sign_in',
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

  return basic;
};
