'use strict';

goog.require('jxmnfsec.main.module');

goog.require('jxmnfsec.version.Directive.factory');
goog.require('jxmnfsec.fileModel.Directive.factory');
goog.require('jxmnfsec.version.Service');

goog.require('jxmnfsec.district.Filter.factory');
goog.require('jxmnfsec.province.Filter.factory');
goog.require('jxmnfsec.city.Filter.factory');
goog.require('jxmnfsec.zone.Filter.factory');
goog.require('jxmnfsec.product_type.Filter.factory');
goog.require('jxmnfsec.device_type.Filter.factory');
goog.require('jxmnfsec.data_type.Filter.factory');
goog.require('jxmnfsec.template_type.Filter.factory');
goog.require('jxmnfsec.user_status.Filter.factory');
goog.require('jxmnfsec.manufactory_status.Filter.factory');
goog.require('jxmnfsec.terminal_status.Filter.factory');
goog.require('jxmnfsec.doctor_status.Filter.factory');
goog.require('jxmnfsec.health_data_status.Filter.factory');
goog.require('jxmnfsec.periods.Filter.factory');
goog.require('jxmnfsec.company_department.Filter.factory');
goog.require('jxmnfsec.company_member.Filter.factory');
goog.require('jxmnfsec.company_nature.Filter.factory');
goog.require('jxmnfsec.company_industry.Filter.factory');
goog.require('jxmnfsec.gender.Filter.factory');

goog.require('jxmnfsec.unixTodate.Filter.factory');
goog.require('jxmnfsec.dateTounix.Filter.factory');

goog.require('jxmnfsec.basic.Service');
goog.require('jxmnfsec.constants.Factory');
goog.require('jxmnfsec.db_operation.Service');

/**
 * Configuration function.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @param {ui.router.$urlRouterProvider} $urlRouterProvider
 * @ngInject
 */
function config($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {

  $urlRouterProvider.otherwise('/main/terminal_category');
  $httpProvider.defaults.withCredentials = true;

  //initialize get if not there
  if (!$httpProvider.defaults.headers.get) {
    $httpProvider.defaults.headers.get = {};    
  }    
  //disable IE ajax request caching
  $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
  // extra
  $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
  $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';

  //================================================
  // Add an interceptor for AJAX errors
  //================================================
  $httpProvider.interceptors.push(function($q, $window) {
    return {
      response: function(response) {
        // do something on success
        return response;
      },
      responseError: function(response) {
        if (response.status === 401)
          $window.location.href = '/manufactory/open';
        if (response.status === 403){
          if(response.data.role === "manufactory"){
            alert("您无操作权限！");
          }else{
            $window.location.href = '/manufactory/open';
          }
        }
        return $q.reject(response);
      }
    };
  });

}

var onChangeConfig = ['$rootScope', '$state',
 function ($rootScope, $state) {

  $rootScope.$on('$stateChangeStart', function (event, toState) {    
    if (toState.name === "main") { 
      event.preventDefault();
      $state.go('main.terminal_category', {});
    } else if (toState.name === "main.user_center") { 
      event.preventDefault();
      $state.go('main.user_center.basics', {});
    }
  });
  
}];

var endPointConfig = ['$rootScope',
 function ($rootScope, $state) {

     $rootScope.endPoint = '';

}];

/**
 * Main app.
 */
angular.module('app', [
  'ui.router',
  'ngResource',
  jxmnfsec.main.module.name
])
.config(config)
.run(onChangeConfig)
.run(endPointConfig)
.directive('version', jxmnfsec.version.Directive.factory)
.directive('fileModel', jxmnfsec.fileModel.Directive.factory)
.service('version', jxmnfsec.version.Service)
.filter('mapDistrict', jxmnfsec.district.Filter.factory)
.filter('mapProvince', jxmnfsec.province.Filter.factory)
.filter('mapCity', jxmnfsec.city.Filter.factory)
.filter('mapZone', jxmnfsec.zone.Filter.factory)
.filter('mapProductType', jxmnfsec.product_type.Filter.factory)
.filter('mapDeviceType', jxmnfsec.device_type.Filter.factory)
.filter('mapDataType', jxmnfsec.data_type.Filter.factory)
.filter('mapTemplateType', jxmnfsec.template_type.Filter.factory)
.filter('mapUserStatus', jxmnfsec.user_status.Filter.factory)
.filter('mapManufactoryStatus', jxmnfsec.manufactory_status.Filter.factory)
.filter('mapTerminalStatus', jxmnfsec.terminal_status.Filter.factory)
.filter('mapDoctorStatus', jxmnfsec.doctor_status.Filter.factory)
.filter('mapHalthDataStatus', jxmnfsec.health_data_status.Filter.factory)
.filter('mapPeriods', jxmnfsec.periods.Filter.factory)
.filter('mapCompanyDepartment', jxmnfsec.company_department.Filter.factory)
.filter('mapCompanyMember', jxmnfsec.company_member.Filter.factory)
.filter('mapCompanyNature', jxmnfsec.company_nature.Filter.factory)
.filter('mapCompanyIndustry', jxmnfsec.company_industry.Filter.factory)
.filter('mapGender', jxmnfsec.gender.Filter.factory)
.filter('unixTodate', jxmnfsec.unixTodate.Filter.factory)
.filter('dateTounix', jxmnfsec.dateTounix.Filter.factory)
.service('basic', jxmnfsec.basic.Service)
.factory('constants', jxmnfsec.constants.Factory)
.service('db_operation', jxmnfsec.db_operation.Service);
