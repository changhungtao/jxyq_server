'use strict';

goog.provide('jxmgrsec.home_security.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmgrsec.home_security.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];
jxmgrsec.home_security.Service = function($rootScope, $http, $q) {
  var home_security = this;

  var validParam = function(param) {
    if (param != undefined && param != null && param != "") {
      return true;
    }
    return false;
  }

  function sendRequest(req) {
    return $http(req)
      .then(success)
      .catch(fail);

    function success(response) {
      return response.data.success_message;
    }

    function fail(error) {
      return $q.reject("rejected");
    }
  };

  home_security.getFiles = function(file) {
      if (file == undefined || file == '') {
        return null;
      }
      var req = {
        method: 'POST',
        url: $rootScope.endPoint + '/doctors/patients/files',
        data: file
      }
      return sendRequest(req);
    }
    //3.3.5.1   GET /admins/cameras
  home_security.getCameras = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/cameras',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.5.4  GET /admins/touch_buttons
  home_security.getTouchButtons = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/touch_buttons',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.5.5  GET /admins/touch_buttons/{bid}
  home_security.getButtonEvent = function(bid, params) {
    if (!validParam(bid) || !validParam(params)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/touch_buttons/' + bid,
      params: params
    }
    return sendRequest(req);
  }

  return home_security;
};
