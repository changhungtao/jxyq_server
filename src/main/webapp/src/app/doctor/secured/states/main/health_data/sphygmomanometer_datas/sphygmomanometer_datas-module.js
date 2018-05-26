'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_data.sphygmomanometer_datas.module');

/**
 * Require controller.
 */
goog.require('jxdctsec.main.health_data.sphygmomanometer_datas.Ctrl');
goog.require('jxdctsec.evaluation_sphygmomanometer_modal.Ctrl');


/**
 * Module for main.health_data.sphygmomanometer_datas state.
 *
 * @return {angular.Module}
 */
jxdctsec.main.health_data.sphygmomanometer_datas.module = angular.module('main.health_data.sphygmomanometer_datas', [
  'ui.router',
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
jxdctsec.main.health_data.sphygmomanometer_datas.module.configuration = function($stateProvider) {

  $stateProvider.state('main.health_data.sphygmomanometer_datas', {
    url: '/sphygmomanometer_datas/:permission_id/:permission_name',
    templateUrl: 'states/main/health_data/sphygmomanometer_datas/sphygmomanometer_datas.html',
    controller: 'SphygmomanometerDatasCtrl as sphygmomanometerDatasCtrl'
  });

};



/**
 * Init main.health_data.sphygmomanometer_datas module.
 */
jxdctsec.main.health_data.sphygmomanometer_datas.module
.config(jxdctsec.main.health_data.sphygmomanometer_datas.module.configuration)
.controller('SphygmomanometerDatasCtrl', jxdctsec.main.health_data.sphygmomanometer_datas.Ctrl)
.controller('sphygmomanometerEvaluationCtrl', jxdctsec.evaluation_sphygmomanometer_modal.Ctrl);
