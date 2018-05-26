'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.db_operation.wristband_files.module');

/**
 * Require controller.
 */
goog.require('jxdctsec.main.db_operation.wristband_files.Ctrl');



/**
 * Module for main.db_operation.wristband_files state.
 *
 * @return {angular.Module}
 */
jxdctsec.main.db_operation.wristband_files.module = angular.module('main.db_operation.wristband_files', [
  'ui.router',
  "ui.grid.pinning",
  "ui.grid",
  "ui.grid.pagination"
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
jxdctsec.main.db_operation.wristband_files.module.configuration = function($stateProvider) {

  $stateProvider.state('main.db_operation.wristband_files', {
    url: '/wristband_files/:patient_id/:file_type',
    templateUrl: 'states/main/db_operation/wristband_files/wristband_files.html',
    controller: 'WristbandFilesCtrl as wristbandFiles'
  });

};



/**
 * Init main.db_operation.wristband_files module.
 */
jxdctsec.main.db_operation.wristband_files.module
  .config(jxdctsec.main.db_operation.wristband_files.module.configuration)
  .controller('WristbandFilesCtrl', jxdctsec.main.db_operation.wristband_files.Ctrl);
