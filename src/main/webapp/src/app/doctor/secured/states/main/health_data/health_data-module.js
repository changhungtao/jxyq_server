'use strict';
/**
 * Create namespace for `app.js`.
 */
goog.provide('jxdctsec.main.health_data.module');
/**
 * Require child states.
 */
goog.require('jxdctsec.main.health_data.permission_list.module');
goog.require('jxdctsec.main.health_data.fat_datas.module');
goog.require('jxdctsec.main.health_data.glucosemeter_datas.module');
goog.require('jxdctsec.main.health_data.oximeter_datas.module');
goog.require('jxdctsec.main.health_data.sphygmomanometer_datas.module');
goog.require('jxdctsec.main.health_data.wristband_datas.module');
goog.require('jxdctsec.main.health_data.thermometer_datas.module');
/**
 * Require controller.
 */
goog.require('jxdctsec.main.health_data.Ctrl');

/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxdctsec.main.health_data.module = angular.module('main.health_data', ['ui.router',
  jxdctsec.main.health_data.permission_list.module.name,
  jxdctsec.main.health_data.fat_datas.module.name,
  jxdctsec.main.health_data.glucosemeter_datas.module.name,
  jxdctsec.main.health_data.oximeter_datas.module.name,
  jxdctsec.main.health_data.sphygmomanometer_datas.module.name,
  jxdctsec.main.health_data.wristband_datas.module.name,
  jxdctsec.main.health_data.thermometer_datas.module.name,
]);

/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxdctsec.main.health_data.module.configuration = function($stateProvider) {
  $stateProvider.state('main.health_data', {
    url: '/health_data',
    templateUrl: 'states/main/health_data/health_data.html',
    controller: 'HealthDataCtrl as healthData'
  });
};
/**
 * Init health module.
 */
jxdctsec.main.health_data.module.config(jxdctsec.main.health_data.module.configuration)
  .controller('HealthDataCtrl', jxdctsec.main.health_data.Ctrl);
