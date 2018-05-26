'use strict';

goog.require('jxdctsec.main.module');

goog.require('jxdctsec.version.Directive.factory');
goog.require('jxdctsec.fileModel.Directive.factory');
goog.require('jxdctsec.version.Service');

goog.require('jxdctsec.district.Filter.factory');
goog.require('jxdctsec.province.Filter.factory');
goog.require('jxdctsec.city.Filter.factory');
goog.require('jxdctsec.zone.Filter.factory');
goog.require('jxdctsec.product_type.Filter.factory');
goog.require('jxdctsec.device_type.Filter.factory');
goog.require('jxdctsec.data_type.Filter.factory');
goog.require('jxdctsec.file_type.Filter.factory');
goog.require('jxdctsec.wristband_column.Filter.factory');
goog.require('jxdctsec.sphygmomanometer_column.Filter.factory');
goog.require('jxdctsec.glucosemeter_column.Filter.factory');
goog.require('jxdctsec.thermometer_column.Filter.factory');
goog.require('jxdctsec.oximeter_column.Filter.factory');
goog.require('jxdctsec.fat_column.Filter.factory');
goog.require('jxdctsec.comparison_ops.Filter.factory');
goog.require('jxdctsec.logical_ops.Filter.factory');
goog.require('jxdctsec.periods.Filter.factory');
goog.require('jxdctsec.user_status.Filter.factory');
goog.require('jxdctsec.manufactory_status.Filter.factory');
goog.require('jxdctsec.terminal_status.Filter.factory');
goog.require('jxdctsec.doctor_status.Filter.factory');
goog.require('jxdctsec.helath_consultation_status.Filter.factory');
goog.require('jxdctsec.health_data_status.Filter.factory');
goog.require('jxdctsec.health_file_status.Filter.factory');
goog.require('jxdctsec.company_department.Filter.factory');
goog.require('jxdctsec.company_member.Filter.factory');
goog.require('jxdctsec.company_nature.Filter.factory');
goog.require('jxdctsec.company_industry.Filter.factory');
goog.require('jxdctsec.department.Filter.factory');
goog.require('jxdctsec.gender.Filter.factory');

goog.require('jxdctsec.unixTodate.Filter.factory');
goog.require('jxdctsec.dateTounix.Filter.factory');

goog.require('jxdctsec.basic.Service');
goog.require('jxdctsec.constants.Factory');
goog.require('jxdctsec.health_con.Service');
goog.require('jxdctsec.health_data.Service');

goog.require('jxdctsec.db_operation.Service');
goog.require('jxdctsec.health_file.Service');
/**
 * Configuration function.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @param {ui.router.$urlRouterProvider} $urlRouterProvider
 * @ngInject
 */
function config($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {

  $urlRouterProvider.otherwise('/main/db_operation/file_list');
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
          $window.location.href = '/doctor/open';
        if (response.status === 403){
          if(response.data.role === "doctor"){
            alert("您无操作权限！");
          }else{
            $window.location.href = '/doctor/open';
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
    if (toState.name === "main.user_center") { 
      event.preventDefault();
      $state.go('main.user_center.basics', {});
    } else if (toState.name === "main.db_operation") { 
      event.preventDefault();
      $state.go('main.db_operation.file_list', {});
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
  jxdctsec.main.module.name
])
.config(config)
.run(onChangeConfig)
.run(endPointConfig)
.directive('version', jxdctsec.version.Directive.factory)
.directive('fileModel', jxdctsec.fileModel.Directive.factory)
.service('version', jxdctsec.version.Service)
.filter('mapDistrict', jxdctsec.district.Filter.factory)
.filter('mapProvince', jxdctsec.province.Filter.factory)
.filter('mapCity', jxdctsec.city.Filter.factory)
.filter('mapZone', jxdctsec.zone.Filter.factory)
.filter('mapProductType', jxdctsec.product_type.Filter.factory)
.filter('mapDeviceType', jxdctsec.device_type.Filter.factory)
.filter('mapDataType', jxdctsec.data_type.Filter.factory)
.filter('mapFileType', jxdctsec.file_type.Filter.factory)
.filter('mapWristbandColumn', jxdctsec.wristband_column.Filter.factory)
.filter('mapSphygmomanometerColumn', jxdctsec.sphygmomanometer_column.Filter.factory)
.filter('mapGlucosemeterColumn', jxdctsec.glucosemeter_column.Filter.factory)
.filter('mapThermometerColumn', jxdctsec.thermometer_column.Filter.factory)
.filter('mapOximeterColumn', jxdctsec.oximeter_column.Filter.factory)
.filter('mapFatColumn', jxdctsec.fat_column.Filter.factory)
.filter('mapComparisonOps', jxdctsec.comparison_ops.Filter.factory)
.filter('mapLogicalOps', jxdctsec.logical_ops.Filter.factory)
.filter('mapPeriods', jxdctsec.periods.Filter.factory)
.filter('mapUserStatus', jxdctsec.user_status.Filter.factory)
.filter('mapManufactoryStatus', jxdctsec.manufactory_status.Filter.factory)
.filter('mapTerminalStatus', jxdctsec.terminal_status.Filter.factory)
.filter('mapDoctorStatus', jxdctsec.doctor_status.Filter.factory)
.filter('mapHelathConsultationStatus', jxdctsec.helath_consultation_status.Filter.factory)
.filter('mapHalthDataStatus', jxdctsec.health_data_status.Filter.factory)
.filter('mapHealthFileStatus', jxdctsec.health_file_status.Filter.factory)
.filter('mapCompanyDepartment', jxdctsec.company_department.Filter.factory)
.filter('mapCompanyMember', jxdctsec.company_member.Filter.factory)
.filter('mapCompanyNature', jxdctsec.company_nature.Filter.factory)
.filter('mapCompanyIndustry', jxdctsec.company_industry.Filter.factory)
.filter('mapDepartment', jxdctsec.department.Filter.factory)
.filter('mapGender', jxdctsec.gender.Filter.factory)
.filter('unixTodate', jxdctsec.unixTodate.Filter.factory)
.filter('dateTounix', jxdctsec.dateTounix.Filter.factory)
.service('basic', jxdctsec.basic.Service)
.service('health_con', jxdctsec.health_con.Service)
.factory('constants', jxdctsec.constants.Factory)
.service('health_file', jxdctsec.health_file.Service)
.service('health_data', jxdctsec.health_data.Service)
.service('db_operation', jxdctsec.db_operation.Service);
