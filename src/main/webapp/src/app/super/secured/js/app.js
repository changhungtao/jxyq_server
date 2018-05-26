'use strict';

goog.require('jxsprsec.main.module');

goog.require('jxsprsec.version.Directive.factory');
goog.require('jxsprsec.fileModel.Directive.factory');
goog.require('jxsprsec.version.Service');

goog.require('jxsprsec.district.Filter.factory');
goog.require('jxsprsec.province.Filter.factory');
goog.require('jxsprsec.city.Filter.factory');
goog.require('jxsprsec.zone.Filter.factory');
goog.require('jxsprsec.product_type.Filter.factory');
goog.require('jxsprsec.device_type.Filter.factory');
goog.require('jxsprsec.data_type.Filter.factory');
goog.require('jxsprsec.file_type.Filter.factory');
goog.require('jxsprsec.wristband_column.Filter.factory');
goog.require('jxsprsec.sphygmomanometer_column.Filter.factory');
goog.require('jxsprsec.glucosemeter_column.Filter.factory');
goog.require('jxsprsec.thermometer_column.Filter.factory');
goog.require('jxsprsec.oximeter_column.Filter.factory');
goog.require('jxsprsec.fat_column.Filter.factory');
goog.require('jxsprsec.comparison_ops.Filter.factory');
goog.require('jxsprsec.logical_ops.Filter.factory');
goog.require('jxsprsec.periods.Filter.factory');
goog.require('jxsprsec.user_status.Filter.factory');
goog.require('jxsprsec.manufactory_status.Filter.factory');
goog.require('jxsprsec.terminal_status.Filter.factory');
goog.require('jxsprsec.doctor_status.Filter.factory');
goog.require('jxsprsec.helath_consultation_status.Filter.factory');
goog.require('jxsprsec.health_data_status.Filter.factory');
goog.require('jxsprsec.health_file_status.Filter.factory');
goog.require('jxsprsec.company_department.Filter.factory');
goog.require('jxsprsec.company_member.Filter.factory');
goog.require('jxsprsec.company_nature.Filter.factory');
goog.require('jxsprsec.company_industry.Filter.factory');
goog.require('jxsprsec.department.Filter.factory');
goog.require('jxsprsec.gender.Filter.factory');
goog.require('jxsprsec.user_role.Filter.factory');
goog.require('jxsprsec.operation.Filter.factory');

goog.require('jxsprsec.unixTodate.Filter.factory');
goog.require('jxsprsec.dateTounix.Filter.factory');

goog.require('jxsprsec.basic.Service');
goog.require('jxsprsec.constants.Factory');
goog.require('jxsprsec.health_con.Service');
goog.require('jxsprsec.health_data.Service');

goog.require('jxsprsec.db_operation.Service');
goog.require('jxsprsec.log_manage.Service');

/**
 * Configuration function.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @param {ui.router.$urlRouterProvider} $urlRouterProvider
 * @ngInject
 */
function config($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {

  $urlRouterProvider.otherwise('/main/db_operation');
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
          $window.location.href = '/super/open';
        if (response.status === 403){
          if(response.data.role === "super"){
            alert("您无操作权限！");
          }else{
            $window.location.href = '/super/open';
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
      $state.go('main.db_operation', {});
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
  jxsprsec.main.module.name
])
.config(config)
.run(onChangeConfig)
.run(endPointConfig)
.directive('version', jxsprsec.version.Directive.factory)
.directive('fileModel', jxsprsec.fileModel.Directive.factory)
.service('version', jxsprsec.version.Service)
.filter('mapDistrict', jxsprsec.district.Filter.factory)
.filter('mapProvince', jxsprsec.province.Filter.factory)
.filter('mapCity', jxsprsec.city.Filter.factory)
.filter('mapZone', jxsprsec.zone.Filter.factory)
.filter('mapProductType', jxsprsec.product_type.Filter.factory)
.filter('mapDeviceType', jxsprsec.device_type.Filter.factory)
.filter('mapDataType', jxsprsec.data_type.Filter.factory)
.filter('mapFileType', jxsprsec.file_type.Filter.factory)
.filter('mapWristbandColumn', jxsprsec.wristband_column.Filter.factory)
.filter('mapSphygmomanometerColumn', jxsprsec.sphygmomanometer_column.Filter.factory)
.filter('mapGlucosemeterColumn', jxsprsec.glucosemeter_column.Filter.factory)
.filter('mapThermometerColumn', jxsprsec.thermometer_column.Filter.factory)
.filter('mapOximeterColumn', jxsprsec.oximeter_column.Filter.factory)
.filter('mapFatColumn', jxsprsec.fat_column.Filter.factory)
.filter('mapComparisonOps', jxsprsec.comparison_ops.Filter.factory)
.filter('mapLogicalOps', jxsprsec.logical_ops.Filter.factory)
.filter('mapPeriods', jxsprsec.periods.Filter.factory)
.filter('mapUserStatus', jxsprsec.user_status.Filter.factory)
.filter('mapManufactoryStatus', jxsprsec.manufactory_status.Filter.factory)
.filter('mapTerminalStatus', jxsprsec.terminal_status.Filter.factory)
.filter('mapDoctorStatus', jxsprsec.doctor_status.Filter.factory)
.filter('mapHelathConsultationStatus', jxsprsec.helath_consultation_status.Filter.factory)
.filter('mapHalthDataStatus', jxsprsec.health_data_status.Filter.factory)
.filter('mapHealthFileStatus', jxsprsec.health_file_status.Filter.factory)
.filter('mapCompanyDepartment', jxsprsec.company_department.Filter.factory)
.filter('mapCompanyMember', jxsprsec.company_member.Filter.factory)
.filter('mapCompanyNature', jxsprsec.company_nature.Filter.factory)
.filter('mapCompanyIndustry', jxsprsec.company_industry.Filter.factory)
.filter('mapDepartment', jxsprsec.department.Filter.factory)
.filter('mapGender', jxsprsec.gender.Filter.factory)
.filter('mapUserRole', jxsprsec.user_role.Filter.factory)
.filter('mapOperation',jxsprsec.operation.Filter.factory)
.filter('unixTodate', jxsprsec.unixTodate.Filter.factory)
.filter('dateTounix', jxsprsec.dateTounix.Filter.factory)
.service('basic', jxsprsec.basic.Service)
.service('health_con', jxsprsec.health_con.Service)
.factory('constants', jxsprsec.constants.Factory)
.service('health_data', jxsprsec.health_data.Service)
.service('db_operation', jxsprsec.db_operation.Service)
.service('log_manage', jxsprsec.log_manage.Service);
