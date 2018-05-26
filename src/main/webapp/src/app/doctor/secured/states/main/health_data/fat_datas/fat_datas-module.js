'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_data.fat_datas.module');

/**
 * Require controller.
 */
goog.require('jxdctsec.main.health_data.fat_datas.Ctrl');
goog.require('jxdctsec.evaluation_fat_modal.Ctrl');


/**
 * Module for main.health_data.fat_datas state.
 *
 * @return {angular.Module}
 */
jxdctsec.main.health_data.fat_datas.module = angular.module('main.health_data.fat_datas', [
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
jxdctsec.main.health_data.fat_datas.module.configuration = function($stateProvider) {

  $stateProvider.state('main.health_data.fat_datas', {
    url: '/fat_datas/:permission_id/:permission_name',
    templateUrl: 'states/main/health_data/fat_datas/fat_datas.html',
    controller: 'FatDatasCtrl as fatDatas'
  });

};



/**
 * Init main.health_data.fat_datas module.
 */
jxdctsec.main.health_data.fat_datas.module
.config(jxdctsec.main.health_data.fat_datas.module.configuration)
.controller('FatDatasCtrl', jxdctsec.main.health_data.fat_datas.Ctrl)
.controller('FatEvaluationCtrl', jxdctsec.evaluation_fat_modal.Ctrl);
