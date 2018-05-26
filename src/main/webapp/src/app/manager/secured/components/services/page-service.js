'use strict';

goog.provide('jxmgrsec.page.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmgrsec.page.Service.$inject = [
  "$rootScope",
  "$http",
  "$q"
];
jxmgrsec.page.Service = function($rootScope, $http, $q) {
  var page = this;

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

  //3.3.8.1   GET /admins/db/web_templates
  page.getWebTemplates = function() {
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/web_templates'
    }
    return sendRequest(req);
  }

  //3.3.8.2   PUT /admins/db/web_templates
  page.modifyWebTemplates = function(params) {
    if (!validParam(params)) {
      return null;
    }
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/web_templates',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.8.3   GET /admins/db/exhibition_resources
  page.getExhibitionResources = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/exhibition_resources',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.8.4   POST /admins/db/exhibition_resources
  page.addExhibitionResources = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/exhibition_resources',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.8.5   PUT /admins/db/exhibition_resources
  page.modifyExhibitionResources = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/exhibition_resources',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.8.6   DELETE /admins/db/exhibition_resources
  page.deleteExhibitionResources = function(eid) {
    var req = {
      method: 'DELETE',
      url: $rootScope.endPoint + '/admins/db/exhibition_resources/' + eid
    }
    return sendRequest(req);
  }

  //3.3.8.7   GET /admins/db/news_resources
  page.getNewResources = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'GET',
      url: $rootScope.endPoint + '/admins/db/news_resources',
      params: params
    }
    return sendRequest(req);
  }

  //3.3.8.8   POST /admins/db/news_resources
  page.addNewResources = function(params) {
    if (!validParam(params)) {
      return null;
    }
    var req = {
      method: 'POST',
      url: $rootScope.endPoint + '/admins/db/news_resources',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.8.9   PUT /admins/db/news_resources
  page.modifyNewResources = function(params) {
    if (!validParam(params)) {
      return null;
    }
    console.log(params);
    var req = {
      method: 'PUT',
      url: $rootScope.endPoint + '/admins/db/news_resources',
      data: params
    }
    return sendRequest(req);
  }

  //3.3.8.10   DELETE /admins/db/news_resources
  page.deleteNewResources = function(nid) {
    var req = {
      method: 'DELETE',
      url: $rootScope.endPoint + '/admins/db/news_resources/' + nid
    }
    return sendRequest(req);
  }


  return page;
};
