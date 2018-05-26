'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxdctsec.main.db_operation.module');


goog.require('jxdctsec.main.db_operation.file_list.module');
goog.require('jxdctsec.main.db_operation.glucosemeter_files.module');
goog.require('jxdctsec.main.db_operation.fat_files.module');
goog.require('jxdctsec.main.db_operation.oximeter_files.module');
goog.require('jxdctsec.main.db_operation.sphygmomanometer_files.module');
goog.require('jxdctsec.main.db_operation.thermometer_files.module');
goog.require('jxdctsec.main.db_operation.wristband_files.module');
goog.require('jxdctsec.main.db_operation.other_files.module');
/**
 * Require controller.
 */
goog.require('jxdctsec.main.db_operation.Ctrl');
goog.require('jxdctsec.confirm_modal.Ctrl');
goog.require('jxdctsec.custom_modal.Ctrl');


/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxdctsec.main.db_operation.module = angular.module('main.db_operation', [
  'ui.router','ui.grid', 'ui.grid.pinning', 'ui.grid.resizeColumns', 'ui.grid.saveState','ui.grid.edit','ui.grid.pagination',
  jxdctsec.main.db_operation.file_list.module.name,
  jxdctsec.main.db_operation.fat_files.module.name,
  jxdctsec.main.db_operation.glucosemeter_files.module.name,
  jxdctsec.main.db_operation.oximeter_files.module.name,
  jxdctsec.main.db_operation.sphygmomanometer_files.module.name,
  jxdctsec.main.db_operation.thermometer_files.module.name,
  jxdctsec.main.db_operation.wristband_files.module.name,
  jxdctsec.main.db_operation.other_files.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxdctsec.main.db_operation.module.configuration = function($stateProvider) {

  $stateProvider.state('main.db_operation', {
    url: '/db_operation',
    templateUrl: 'states/main/db_operation/db_operation.html',
    controller: 'DbOperationCtrl as dbOperation'
  });

};



/**
 * Init db operation module.
 */
jxdctsec.main.db_operation.module
.config(jxdctsec.main.db_operation.module.configuration)
.controller('DbOperationCtrl', jxdctsec.main.db_operation.Ctrl);
