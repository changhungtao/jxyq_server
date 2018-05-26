'use strict';

goog.provide('jxmgrsec.push_message.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmgrsec.push_message.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];
jxmgrsec.push_message.Service = function($rootScope, $http, $q) {
  var push_message = this;

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

  //3.3.7.1   POST /admins/db/push_messages
  push_message.pushMessage = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/push_messages',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.7.3   POST /admins/db/terminal_catagories
  push_message.getCatagoryList = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/terminal_catagories',
      data: params
    }
    return sendRequest(req);
  }

  return push_message;
};
