'use strict';

goog.require('jxmgrsec.main.module');

goog.require('jxmgrsec.version.Directive.factory');
goog.require('jxmgrsec.fileModel.Directive.factory');
goog.require('jxmgrsec.version.Service');

goog.require('jxmgrsec.district.Filter.factory');
goog.require('jxmgrsec.province.Filter.factory');
goog.require('jxmgrsec.city.Filter.factory');
goog.require('jxmgrsec.zone.Filter.factory');
goog.require('jxmgrsec.product_type.Filter.factory');
goog.require('jxmgrsec.device_type.Filter.factory');
goog.require('jxmgrsec.device_type_array.Filter.factory');
goog.require('jxmgrsec.data_type.Filter.factory');
goog.require('jxmgrsec.file_type.Filter.factory');
goog.require('jxmgrsec.wristband_column.Filter.factory');
goog.require('jxmgrsec.sphygmomanometer_column.Filter.factory');
goog.require('jxmgrsec.glucosemeter_column.Filter.factory');
goog.require('jxmgrsec.thermometer_column.Filter.factory');
goog.require('jxmgrsec.oximeter_column.Filter.factory');
goog.require('jxmgrsec.fat_column.Filter.factory');
goog.require('jxmgrsec.comparison_ops.Filter.factory');
goog.require('jxmgrsec.logical_ops.Filter.factory');
goog.require('jxmgrsec.periods.Filter.factory');
goog.require('jxmgrsec.user_status.Filter.factory');
goog.require('jxmgrsec.manufactory_status.Filter.factory');
goog.require('jxmgrsec.terminal_status.Filter.factory');
goog.require('jxmgrsec.doctor_status.Filter.factory');
goog.require('jxmgrsec.helath_consultation_status.Filter.factory');
goog.require('jxmgrsec.health_data_status.Filter.factory');
goog.require('jxmgrsec.health_file_status.Filter.factory');
goog.require('jxmgrsec.company_department.Filter.factory');
goog.require('jxmgrsec.company_member.Filter.factory');
goog.require('jxmgrsec.company_nature.Filter.factory');
goog.require('jxmgrsec.company_industry.Filter.factory');
goog.require('jxmgrsec.department.Filter.factory');
goog.require('jxmgrsec.gender.Filter.factory');

goog.require('jxmgrsec.twatch_qing_name.Filter.factory');
goog.require('jxmgrsec.twatch_sos_name.Filter.factory');
goog.require('jxmgrsec.twatch_work_mode.Filter.factory');
goog.require('jxmgrsec.twatch_work_type.Filter.factory');
goog.require('jxmgrsec.twatch_alarm_event.Filter.factory');
goog.require('jxmgrsec.twatch_period_state.Filter.factory');
goog.require('jxmgrsec.twatch_route_state.Filter.factory');
goog.require('jxmgrsec.twatch_gps_mode.Filter.factory');
goog.require('jxmgrsec.twatch_pen_shape.Filter.factory');
goog.require('jxmgrsec.twatch_week_day.Filter.factory');
goog.require('jxmgrsec.twatch_week_days.Filter.factory');

goog.require('jxmgrsec.unixTodate.Filter.factory');
goog.require('jxmgrsec.dateTounix.Filter.factory');

goog.require('jxmgrsec.basic.Service');
goog.require('jxmgrsec.constants.Factory');
goog.require('jxmgrsec.health_con.Service');
goog.require('jxmgrsec.health_data.Service');
goog.require('jxmgrsec.home_security.Service');
goog.require('jxmgrsec.push_message.Service');
goog.require('jxmgrsec.page.Service');
goog.require('jxmgrsec.twatch.Service');
goog.require('jxmgrsec.db_operation.Service');
goog.require('jxmgrsec.doctor.Service');
goog.require('jxmgrsec.manufactory.Service');

/**
 * Configuration function.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @param {ui.router.$urlRouterProvider} $urlRouterProvider
 * @ngInject
 */
function config($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {

  $urlRouterProvider.otherwise('/main/db_operation/product_type');
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
          $window.location.href = '/manager/open';
        if (response.status === 403){
          if(response.data.role === "manager"){
            alert("您无操作权限！");
          }else{
            $window.location.href = '/manager/open';
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
    } else if (toState.name === "main.home_security") { 
      event.preventDefault();
      $state.go('main.home_security.camera', {});
    } else if (toState.name === "main.permission") { 
      event.preventDefault();
      $state.go('main.permission.manager_permission', {});
    } else if (toState.name === "main.page") { 
      event.preventDefault();
      $state.go('main.page.category_system_template', {});
    } else if (toState.name === "main.db_operation") { 
      event.preventDefault();
      $state.go('main.db_operation.product_type', {});
    }
  });
  
}];

var endPointConfig = ['$rootScope',
 function ($rootScope, $state) {

  $rootScope.endPoint = '';
  $rootScope.imei = undefined;


}];

/**
 * Main app.
 */
angular.module('app', [
  'ui.router',
  'ngResource',
  jxmgrsec.main.module.name
])
.config(config)
.run(onChangeConfig)
.run(endPointConfig)
.directive('version', jxmgrsec.version.Directive.factory)
.directive('fileModel', jxmgrsec.fileModel.Directive.factory)
.service('version', jxmgrsec.version.Service)
.filter('mapDistrict', jxmgrsec.district.Filter.factory)
.filter('mapProvince', jxmgrsec.province.Filter.factory)
.filter('mapCity', jxmgrsec.city.Filter.factory)
.filter('mapZone', jxmgrsec.zone.Filter.factory)
.filter('mapProductType', jxmgrsec.product_type.Filter.factory)
.filter('mapDeviceType', jxmgrsec.device_type.Filter.factory)
.filter('mapDeviceTypeArray', jxmgrsec.device_type_array.Filter.factory)
.filter('mapDataType', jxmgrsec.data_type.Filter.factory)
.filter('mapFileType', jxmgrsec.file_type.Filter.factory)
.filter('mapWristbandColumn', jxmgrsec.wristband_column.Filter.factory)
.filter('mapSphygmomanometerColumn', jxmgrsec.sphygmomanometer_column.Filter.factory)
.filter('mapGlucosemeterColumn', jxmgrsec.glucosemeter_column.Filter.factory)
.filter('mapThermometerColumn', jxmgrsec.thermometer_column.Filter.factory)
.filter('mapOximeterColumn', jxmgrsec.oximeter_column.Filter.factory)
.filter('mapFatColumn', jxmgrsec.fat_column.Filter.factory)
.filter('mapComparisonOps', jxmgrsec.comparison_ops.Filter.factory)
.filter('mapLogicalOps', jxmgrsec.logical_ops.Filter.factory)
.filter('mapPeriods', jxmgrsec.periods.Filter.factory)
.filter('mapUserStatus', jxmgrsec.user_status.Filter.factory)
.filter('mapManufactoryStatus', jxmgrsec.manufactory_status.Filter.factory)
.filter('mapTerminalStatus', jxmgrsec.terminal_status.Filter.factory)
.filter('mapDoctorStatus', jxmgrsec.doctor_status.Filter.factory)
.filter('mapHelathConsultationStatus', jxmgrsec.helath_consultation_status.Filter.factory)
.filter('mapHalthDataStatus', jxmgrsec.health_data_status.Filter.factory)
.filter('mapHealthFileStatus', jxmgrsec.health_file_status.Filter.factory)
.filter('mapCompanyDepartment', jxmgrsec.company_department.Filter.factory)
.filter('mapCompanyMember', jxmgrsec.company_member.Filter.factory)
.filter('mapCompanyNature', jxmgrsec.company_nature.Filter.factory)
.filter('mapCompanyIndustry', jxmgrsec.company_industry.Filter.factory)
.filter('mapDepartment', jxmgrsec.department.Filter.factory)
.filter('mapGender', jxmgrsec.gender.Filter.factory)
.filter('mapTwatchQingName', jxmgrsec.twatch_qing_name.Filter.factory)
.filter('mapTwatchSosName', jxmgrsec.twatch_sos_name.Filter.factory)
.filter('mapTwatchWorkMode', jxmgrsec.twatch_work_mode.Filter.factory)
.filter('mapTwatchWorkType', jxmgrsec.twatch_work_type.Filter.factory)
.filter('mapTwatchAlarmEvent', jxmgrsec.twatch_alarm_event.Filter.factory)
.filter('mapTwatchPeriodState', jxmgrsec.twatch_period_state.Filter.factory)
.filter('mapTwatchRouteState', jxmgrsec.twatch_route_state.Filter.factory)
.filter('mapTwatchGpsMode', jxmgrsec.twatch_gps_mode.Filter.factory)
.filter('mapPenShape', jxmgrsec.twatch_pen_shape.Filter.factory)
.filter('mapWeekDay', jxmgrsec.twatch_week_day.Filter.factory)
.filter('mapWeekDays', jxmgrsec.twatch_week_days.Filter.factory)
.filter('unixTodate', jxmgrsec.unixTodate.Filter.factory)
.filter('dateTounix', jxmgrsec.dateTounix.Filter.factory)
.service('basic', jxmgrsec.basic.Service)
.service('health_con', jxmgrsec.health_con.Service)
.factory('constants', jxmgrsec.constants.Factory)
.service('health_data', jxmgrsec.health_data.Service)
.service('db_operation', jxmgrsec.db_operation.Service)
.service('doctor', jxmgrsec.doctor.Service)
.service('push_message', jxmgrsec.push_message.Service)
.service('home_security', jxmgrsec.home_security.Service)
.service('page', jxmgrsec.page.Service)
.service('twatch', jxmgrsec.twatch.Service)
.service('manufactory', jxmgrsec.manufactory.Service);
