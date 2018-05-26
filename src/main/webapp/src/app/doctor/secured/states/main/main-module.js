'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxdctsec.main.module');

/**
 * Require child states.
 */
goog.require('jxdctsec.main.db_operation.module');
goog.require('jxdctsec.main.health_file.module');
goog.require('jxdctsec.main.health_data.module');
goog.require('jxdctsec.main.health_consultation.module');
goog.require('jxdctsec.main.user_center.module');

/**
 * Require controller.
 */
goog.require('jxdctsec.main.Ctrl');
goog.require('jxdctsec.confirm_modal.Ctrl');
goog.require('jxdctsec.custom_modal.Ctrl');


/**
 * Main module.
 *
 * Require child states here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxdctsec.main.module = angular.module('main', [
  'ui.router',
  'ui.bootstrap',
  'ui.navbar',
  'angularModalService',
  jxdctsec.main.db_operation.module.name,
  jxdctsec.main.health_file.module.name,
  jxdctsec.main.health_data.module.name,
  jxdctsec.main.health_consultation.module.name,
  jxdctsec.main.user_center.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxdctsec.main.module.configuration = function($stateProvider) {

  $stateProvider.state('main', {
    url: '/main',
    templateUrl: 'states/main/main.html',
    controller: 'MainCtrl as main'
  });

};



/**
 * Init main module.
 */
jxdctsec.main.module
.config(jxdctsec.main.module.configuration)
.controller('MainCtrl', jxdctsec.main.Ctrl)
.controller('ConfirmModalCtrl', jxdctsec.confirm_modal.Ctrl)
.controller('CustomModalCtrl', jxdctsec.custom_modal.Ctrl);
