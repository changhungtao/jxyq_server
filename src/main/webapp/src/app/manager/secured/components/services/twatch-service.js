'use strict';

goog.provide('jxmgrsec.twatch.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmgrsec.twatch.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];
jxmgrsec.twatch.Service = function($rootScope, $http, $q) {
  var twatch = this;

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

  //3.3.10.1   GET /admins/imei/modeset
  twatch.getModeset = function(params) {
    if (!validParam(params)) {
      return null;
    }
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/imei/modeset',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.10.2   PUT /admins/imei/modeset
  twatch.setModeset = function(params) {
    if (!validParam(params)) {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/imei/modeset',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.10.3   POST /admins/imei/mode_periods
  twatch.addModePeriods = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/imei/mode_periods',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.10.4   DELETE /admins/imei/mode_periods
  twatch.deleteModePeriods = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'DELETE',
      url: $rootScope.endPoint + '/admins/imei/mode_periods',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.10.5   PUT /admins/imei/mode_periods
  twatch.modifyModePeriods = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/imei/mode_periods',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.10.8   POST /admins/imei/whitelist
  twatch.addWhiteList = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/imei/whitelist',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.10.9   GET /admins/imei/whitelist
  twatch.getWhiteList = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/imei/whitelist',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.10.10   POST /admins/imei/blacklist
  twatch.addBlackList = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/imei/blacklist',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.10.11   GET /admins/imei/blacklist
  twatch.getBlackList = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/imei/blacklist',
      params: params
    }
    return sendRequest(req);
  }

    //3.3.10.22 GET /admins/imei/cur_location
  twatch.getCurLocation = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/imei/cur_location',
      params: params
    }
    return sendRequest(req);
  }

    //3.3.10.23 GET /admins/imei/monitor
  twatch.getMonitor = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/imei/monitor',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.10.24 GET /admins/imei/locations
  twatch.getLocations = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/imei/locations',
      params: params
    }
    return sendRequest(req);
  }


  //3.3.10.25   GET /admins/imei/commands
  twatch.getCommands = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/imei/commands',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.10.26   GET /admins/imei/playvoice
  twatch.getPlayVoices = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/imei/playvoice',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.10.27   POST /admins/imei/playvoice
  twatch.addPlayVoices = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/imei/playvoice',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.10.27   PUT /admins/imei/playvoice
  twatch.putPlayVoices = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/imei/playvoice',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.10.28   DELETE /admins/imei/playvoice
  twatch.deletePlayVoices = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'DELETE',
      url: $rootScope.endPoint + '/admins/imei/playvoice',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.10.12 GET /admins/imei/routeset
  twatch.getRouteset = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/imei/routeset',
      params: params
    }
    return sendRequest(req);
  }
  //3.3.10.13 POST /admins/imei/routeset
  twatch.postRouteset = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    console.log(rbody);
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/imei/routeset',
      data: rbody
    }
    return sendRequest(req);
  }
  // 3.3.10.14 POST /admins/imei/routeset/period
  twatch.postRoutePeriod = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    console.log(rbody);
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/imei/routeset/period',
      data: rbody
    }
    return sendRequest(req);
  }

  // 3.3.10.15 PUT /admins/imei/routeset/period
  twatch.putRoutePeriod = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    console.log(rbody);
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/imei/routeset/period',
      data: rbody
    }
    return sendRequest(req);
  }

  // 3.3.10.16 DELETE /admins/imei/routeset/period
  twatch.deleteRoutePeriod = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    console.log(rbody);
    var req = {
      method: 'DELETE',
      url: $rootScope.endPoint + '/admins/imei/routeset/period',
      params: rbody
    }
    return sendRequest(req);
  }

  //3.3.10.6  POST /admins/imei/numbers
  twatch.postNumbers = function(rbody) {
    if (!validParam(rbody)) {
      return null;
    }
    console.log(rbody);
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/imei/numbers',
      data: rbody
    }
    return sendRequest(req);
  }
  //3.3.10.7  GET /admins/imei/numbers
  twatch.getNumbers = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/imei/numbers',
      params: params
    }
    return sendRequest(req);
  }

    //3.3.10.20	GET /admins/imei/penset
    twatch.getPensets = function(params) {
        if (!validParam(params)) {
            return null;
        }
        console.log(params);
        var req = {
            method: 'GET',
            url: $rootScope.endPoint + '/admins/imei/penset',
            params: params
        }
        return sendRequest(req);
    }

    //3.3.10.17	POST /admins/imei/penset
    twatch.addPensets = function(params) {
        if (!validParam(params)) {
            return null;
        }
        console.log(params);
        var req = {
            method: 'POST',
            url: $rootScope.endPoint + '/admins/imei/penset',
            data: params
        }
        return sendRequest(req);
    }

    //3.3.10.18	PUT /admins/imei/penset
    twatch.savePensets = function(params) {
        if (!validParam(params)) {
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/admins/imei/penset',
            data: params
        }
        return sendRequest(req);
    }

    //3.3.10.19	DELETE /admins/imei/penset
    twatch.delPenset = function(params){
        if (!validParam(params)) {
            return null;
        }
        var req = {
            method: 'DELETE',
            url: $rootScope.endPoint + '/admins/imei/penset',
            params: params
        }
        return sendRequest(req);
    }
    //3.3.10.30	PUT /admins/imei/heartbeat
    twatch.heartbeat = function(params){
        if (!validParam(params)) {
            return null;
        }
        var req = {
            method: 'PUT',
            url: $rootScope.endPoint + '/admins/imei/heartbeat',
            data: params
        }
        return sendRequest(req);
    }

    return twatch;
};
