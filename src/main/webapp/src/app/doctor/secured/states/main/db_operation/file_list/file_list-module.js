'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.db_operation.file_list.module');

/**
 * Require controller.
 */
goog.require('jxdctsec.main.db_operation.file_list.Ctrl');
//goog.require('jxdctsec.ages.Factory');
//goog.require('jxdctsec.time_section.Factory');
//goog.require('jxdctsec.file_type.Factory');
goog.require('jxdctsec.health_report.fat_modal.Ctrl');
goog.require('jxdctsec.health_report.glucosemeter_modal.Ctrl');
goog.require('jxdctsec.health_report.oximeter_modal.Ctrl');
goog.require('jxdctsec.health_report.sphygmomanometer_modal.Ctrl');
goog.require('jxdctsec.health_report.thermometer_modal.Ctrl');
goog.require('jxdctsec.health_report.wristband_modal.Ctrl');
goog.require('jxdctsec.health_report.other_modal.Ctrl');

/**
 * Module for main.db_operation.file_list state.
 *
 * @return {angular.Module}
 */
jxdctsec.main.db_operation.file_list.module = angular.module('main.db_operation.file_list', [
  'ui.router',
  'ngResource',
  'ui.select',
  'ngSanitize',
  'ui.bootstrap',
  'angularModalService',
  'ui.grid.grouping'
]);



/**
 * Configuration function.
 *
 * Important! Do not call this function `config()`. It would collide
 * with the AngularJS `config()` function. The init part at the end of this file
 * would look like `my.blabla.config(my.blabla.config())` which obviously would
 * not work.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxdctsec.main.db_operation.file_list.module.configuration = function($stateProvider) {

  $stateProvider.state('main.db_operation.file_list', {
    url: '/file_list',
    templateUrl: 'states/main/db_operation/file_list/file_list.html',
    controller: 'FileListCtrl as fileList',
    resolve: {
      'DATATYPESPromise': function(constants) {
        return constants.DATATYPESPromise;
      },
      'GENDERSPromise': function(constants) {
        return constants.GENDERSPromise;
      },
      'PERIODSPromise':function(constants){
          return constants.PERIODSPromise;
      }
    }
  });

};



/**
 * Init main.db_operation.file_list module.
 */
jxdctsec.main.db_operation.file_list.module
  .config(jxdctsec.main.db_operation.file_list.module.configuration)
  .controller('FileListCtrl', jxdctsec.main.db_operation.file_list.Ctrl)
  //  .factory('Ages', jxdctsec.ages.Factory)
  //  .factory('TimeSection', jxdctsec.time_section.Factory)
  //  .factory('FileTypes', jxdctsec.file_type.Factory)
  //  .controller('FileListCtrl', jxdctsec.main.db_operation.file_list.Ctrl)
  .controller('FatModalCtrl', jxdctsec.health_report.fat_modal.Ctrl)
  .controller('GlucosemeterModalCtrl', jxdctsec.health_report.glucosemeter_modal.Ctrl)
  .controller('OximeterModalCtrl', jxdctsec.health_report.oximeter_modal.Ctrl)
  .controller('SphygmomanometerModalCtrl', jxdctsec.health_report.sphygmomanometer_modal.Ctrl)
  .controller('ThermometerModalCtrl', jxdctsec.health_report.thermometer_modal.Ctrl)
  .controller('WristbandModalCtrl', jxdctsec.health_report.wristband_modal.Ctrl)
  .controller('OtherModalCtrl', jxdctsec.health_report.other_modal.Ctrl);
