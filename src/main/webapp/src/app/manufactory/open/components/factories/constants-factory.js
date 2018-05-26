'use strict';

goog.provide('jxmnfopn.constants.Factory');

/**
 * constants Factory. 
 *
 * @constructor
 */
jxmnfopn.constants.Factory.$inject = [
  'rootScope',
  '$http', 
  '$q'
];
jxmnfopn.constants.Factory = function($rootScope, $http, $q) {

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
  //省级行政区
  var PROVINCES = [];
  var PROVINCESPromise = getProvinces().then(
    function(data){
      PROVINCES = data.provinces;
    }, 
    function(data){
      PROVINCES = [];
  });
  function getProvinces(){
    var req = {
      method: 'GET',
      url: '/common/constants/provinces'
    }
    return getConstants(req);
  };
  function gotPROVINCES(){
    return PROVINCES;
  };

  //市级行政区
  var CITIES = [];
  var CITIESPromise = getCities().then(
    function(data){
      CITIES = data.cities;
    }, 
    function(data){
      CITIES = [];
  });
  function getCities(){
    var req = {
      method: 'GET',
      url: '/common/constants/cities'
    }
    return getConstants(req);
  };
  function gotCITIES(){
    return CITIES;
  };

  //区县行政区
  var ZONES = [];
  var ZONESPromise = getZones().then(
    function(data){
      ZONES = data.zones;
    }, 
    function(data){
      ZONES = [];
  });
  function getZones(){
    var req = {
      method: 'GET',
      url: '/common/constants/zones'
    }
    return getConstants(req);
  };
  function gotZONES(){
    return ZONES;
  };

  /**
   * 产品常量
   */
  //设备类型
  var DEVICETYPES = [];
  var DEVICETYPESPromise = getDeviceTypes().then(
    function(data){
      DEVICETYPES = data.device_types;
    }, 
    function(data){
      DEVICETYPES = [];
  });
  function getDeviceTypes(){
    var req = {
      method: 'GET',
      url: '/common/constants/device_types'
      // headers: {
      //   'Content-Type': undefined
      // },
      // data: { 
      //   test: 'test'
      // }
    }
    return getConstants(req);
  };
  function gotDEVICETYPES(){
    return DEVICETYPES;
  };

  /**
   * 公司常量
   */
  //公司部门
  var COMPANYDEPARTMENTS = [];
  var COMPANYDEPARTMENTSPromise = getCompanyDepartments().then(
    function(data){
      COMPANYDEPARTMENTS = data.departments;
    }, 
    function(data){
      COMPANYDEPARTMENTS = [];
  });
  function getCompanyDepartments(){
    var req = {
      method: 'GET',
      url: '/common/constants/company_departments'
      // headers: {
      //   'Content-Type': undefined
      // },
      // data: { 
      //   test: 'test'
      // }
    }
    return getConstants(req);
  };
  function gotCOMPANYDEPARTMENTS(){
    return COMPANYDEPARTMENTS;
  };

  //公司规模
  var COMPANYMEMBERS = [];
  var COMPANYMEMBERSPromise = getCompanyMembers().then(
    function(data){
      COMPANYMEMBERS = data.members;
    }, 
    function(data){
      COMPANYMEMBERS = [];
  });
  function getCompanyMembers(){
    var req = {
      method: 'GET',
      url: '/common/constants/company_members'
    }
    return getConstants(req);
  };
  function gotCOMPANYMEMBERS(){
    return COMPANYMEMBERS;
  };

  //公司性质
  var COMPANYNATURES = [];
  var COMPANYNATURESPromise = getCompanyNatures().then(
    function(data){
      COMPANYNATURES = data.natures;
    }, 
    function(data){
      COMPANYNATURES = [];
  });
  function getCompanyNatures(){
    var req = {
      method: 'GET',
      url: '/common/constants/company_natures'
    }
    return getConstants(req);
  };
  function gotCOMPANYNATURES(){
    return COMPANYNATURES;
  };

  //公司行业
  var COMPANYINDUSTRIES = [];
  var COMPANYINDUSTRIESPromise = getCompanyIndustries().then(
    function(data){
      COMPANYINDUSTRIES = data.industries;
    }, 
    function(data){
      COMPANYINDUSTRIES = [];
  });
  function getCompanyIndustries(){
    var req = {
      method: 'GET',
      url: '/common/constants/company_industries'
    }
    return getConstants(req);
  };
  function gotCOMPANYINDUSTRIES(){
    return COMPANYINDUSTRIES;
  };

  var service = {
    getConstants: getConstants,

    getProvinces: getProvinces,
    gotPROVINCES: gotPROVINCES,
    PROVINCESPromise: PROVINCESPromise,

    getCities: getCities,
    gotCITIES: gotCITIES,
    CITIESPromise: CITIESPromise,

    getZones: getZones,
    gotZONES: gotZONES,
    ZONESPromise: ZONESPromise,

    getDeviceTypes: getDeviceTypes,
    gotDEVICETYPES: gotDEVICETYPES,
    DEVICETYPESPromise: DEVICETYPESPromise,

    getCompanyDepartments: getCompanyDepartments,
    gotCOMPANYDEPARTMENTS: gotCOMPANYDEPARTMENTS,
    COMPANYDEPARTMENTSPromise: COMPANYDEPARTMENTSPromise,

    getCompanyMembers: getCompanyMembers,
    gotCOMPANYMEMBERS: gotCOMPANYMEMBERS,
    COMPANYMEMBERSPromise: COMPANYMEMBERSPromise,

    getCompanyNatures: getCompanyNatures,
    gotCOMPANYNATURES: gotCOMPANYNATURES,
    COMPANYNATURESPromise: COMPANYNATURESPromise,

    getCompanyIndustries: getCompanyIndustries,
    gotCOMPANYINDUSTRIES: gotCOMPANYINDUSTRIES,
    COMPANYINDUSTRIESPromise: COMPANYINDUSTRIESPromise

  };

  return service;

};
