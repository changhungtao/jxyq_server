'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_data.wristband_datas.module');


/**
 * Require controller.
 */
goog.require('jxdctsec.main.health_data.wristband_datas.Ctrl');
//goog.require('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.require('jxdctsec.evaluation_wristband_modal.Ctrl');


/**
 * Module for main.health_data.wristband_datas state.
 *
 * @return {angular.Module}
 */
jxdctsec.main.health_data.wristband_datas.module = angular.module('main.health_data.wristband_datas', [
  'ui.router',
  "ui.grid.pinning",
  "ui.grid",
  "ui.grid.pagination",
  "ui.grid.grouping"
//  "ngTouch"
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
jxdctsec.main.health_data.wristband_datas.module.configuration = function($stateProvider) {

  $stateProvider.state('main.health_data.wristband_datas', {
    url: '/wristband_datas/:permission_id/:permission_name',
    templateUrl: 'states/main/health_data/wristband_datas/wristband_datas.html',
    controller: 'WristbandDatasCtrl as wristbandDatasCtrl'
  });

};



/**
 * Init main.health_data.wristband_datas module.
 */
jxdctsec.main.health_data.wristband_datas.module
.config(jxdctsec.main.health_data.wristband_datas.module.configuration)
.controller('WristbandEvaluationCtrl', jxdctsec.evaluation_wristband_modal.Ctrl)
.controller('WristbandDatasCtrl', jxdctsec.main.health_data.wristband_datas.Ctrl);
