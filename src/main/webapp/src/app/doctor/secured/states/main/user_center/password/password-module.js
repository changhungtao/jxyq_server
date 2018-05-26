'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.user_center.password.module');

/**
 * Require controller.
 */
goog.require('jxdctsec.main.user_center.password.Ctrl');



/**
 * Module for main.user_center.password state.
 *
 * @return {angular.Module}
 */
jxdctsec.main.user_center.password.module = angular.module('main.user_center.password', [
  'ui.router',
  'angular-md5'
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
jxdctsec.main.user_center.password.module.configuration = function($stateProvider) {

  $stateProvider.state('main.user_center.password', {
    url: '/password',
    templateUrl: 'states/main/user_center/password/password.html',
    controller: 'PasswordCtrl as password'
  });

};



/**
 * Init main.user_center.password module.
 */
jxdctsec.main.user_center.password.module
  .config(jxdctsec.main.user_center.password.module.configuration)
  .controller('PasswordCtrl', jxdctsec.main.user_center.password.Ctrl);
