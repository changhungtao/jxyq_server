'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_data.oximeter_datas.module');

/**
 * Require controller.
 */
goog.require('jxdctsec.main.health_data.oximeter_datas.Ctrl');
goog.require('jxdctsec.evaluation_oximeter_modal.Ctrl');


/**
 * Module for main.health_data.oximeter_datas state.
 *
 * @return {angular.Module}
 */
jxdctsec.main.health_data.oximeter_datas.module = angular.module('main.health_data.oximeter_datas', [
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
jxdctsec.main.health_data.oximeter_datas.module.configuration = function($stateProvider) {

  $stateProvider.state('main.health_data.oximeter_datas', {
    url: '/oximeter_datas/:permission_id/:permission_name',
    templateUrl: 'states/main/health_data/oximeter_datas/oximeter_datas.html',
    controller: 'OximeterDatasCtrl as oximeterDatas'
  });

};



/**
 * Init main.health_data.oximeter_datas module.
 */
jxdctsec.main.health_data.oximeter_datas.module
.config(jxdctsec.main.health_data.oximeter_datas.module.configuration)
.controller('OximeterDatasCtrl', jxdctsec.main.health_data.oximeter_datas.Ctrl)
.controller('OximeterEvaluationCtrl',jxdctsec.evaluation_oximeter_modal.Ctrl);
