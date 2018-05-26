'use strict';

goog.provide('jxdctopn.constants.Factory');

/**
 * constants Factory. 
 *
 * @constructor
 */
jxdctopn.constants.Factory.$inject = [
  '$rootScope',
  '$http', 
  '$q'
];
jxdctopn.constants.Factory = function($rootScope, $http, $q) {
  /**
   * 通用函数
   */
  function getConstants(req) {
    req.url = $rootScope.endPoint + req.url;
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

  /**
   * 区域常量
   */
  //中国传统地理大区
  var DISTRICTS = [];
  var DISTRICTSPromise = getDistricts().then(
    function(data){
      DISTRICTS = data.districts;
    }, 
    function(data){
      DISTRICTS = [];
  });
  function getDistricts(){
    var req = {
      method: 'GET',
      url: '/common/constants/districts'
    }
    return getConstants(req);
  };
  function gotDISTRICTS(){
    return DISTRICTS;
  };


  /**
   * 其他常量
   */
  //医院科室
  var DEPARTMENTS = [];
  var DEPARTMENTSPromise = getDepartments().then(
    function(data){
      DEPARTMENTS = data.departments;
    }, 
    function(data){
      DEPARTMENTS = [];
  });
  function getDepartments(){
    var req = {
      method: 'GET',
      url: '/common/constants/departments'
    }
    return getConstants(req);
  };
  function gotDEPARTMENTS(){
    return DEPARTMENTS;
  };

  //性别
  var GENDERS = [];
  var GENDERSPromise = getGenders().then(
    function(data){
      GENDERS = data.genders;
    }, 
    function(data){
      GENDERS = [];
  });
  function getGenders(){
    var req = {
      method: 'GET',
      url: '/common/constants/genders'
    }
    return getConstants(req);
  };
  function gotGENDERS(){
    return GENDERS;
  };

  var service = {
    getConstants: getConstants,

    getDistricts: getDistricts,
    gotDISTRICTS: gotDISTRICTS,
    DISTRICTSPromise: DISTRICTSPromise,

    getDepartments: getDepartments,
    gotDEPARTMENTS: gotDEPARTMENTS,
    DEPARTMENTSPromise: DEPARTMENTSPromise,

    getGenders: getGenders,
    gotGENDERS: gotGENDERS,
    GENDERSPromise: GENDERSPromise
  };

  return service;
};
