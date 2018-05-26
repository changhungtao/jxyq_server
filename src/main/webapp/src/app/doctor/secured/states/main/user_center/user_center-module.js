'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxdctsec.main.user_center.module');

/**
 * Require child states.
 */
goog.require('jxdctsec.main.user_center.basics.module');
goog.require('jxdctsec.main.user_center.password.module');
goog.require('jxdctsec.main.user_center.avatar.module');

/**
 * Require controller.
 */
goog.require('jxdctsec.main.user_center.Ctrl');



/**
 * User center module.
 *
 * Require child states `jxdctsec.main.user_center.basics` and 
 * `jxdctsec.main.user_center.password` here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxdctsec.main.user_center.module = angular.module('main.user_center', [
  'ui.router',
  jxdctsec.main.user_center.basics.module.name,
  jxdctsec.main.user_center.password.module.name,
  jxdctsec.main.user_center.avatar.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxdctsec.main.user_center.module.configuration = function($stateProvider) {

  $stateProvider.state('main.user_center', {
    url: '/user_center',
    templateUrl: 'states/main/user_center/user_center.html',
    controller: 'UserCenterCtrl as userCenter'
  });

};



/**
 * Init user center module.
 */
jxdctsec.main.user_center.module
.config(jxdctsec.main.user_center.module.configuration)
.controller('UserCenterCtrl', jxdctsec.main.user_center.Ctrl);
