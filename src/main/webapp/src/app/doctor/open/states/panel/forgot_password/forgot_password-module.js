'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctopn.panel.forgot_password.module');

/**
 * Require controller.
 */
goog.require('jxdctopn.panel.forgot_password.Ctrl');



/**
 * Second module.
 *
 * @return {angular.Module}
 */
jxdctopn.panel.forgot_password.module = angular.module('panel.forgot_password', [
  'ui.router',
  'angular-md5'
]);



/**
 * Configuration function.
 *
 * Important! Do not call this function `jxdctopn.panel.forgot_password.config`. It would collide
 * with the AngularJS `config()` function. The init part at the end of this file
 * would look like `my.blabla.config(my.blabla.config())` which obviously would
 * not work.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxdctopn.panel.forgot_password.module.configuration = function($stateProvider) {

  $stateProvider.state('panel.forgot_password', {
    url: '/forgot_password',
    templateUrl: 'states/panel/forgot_password/forgot_password.html',
    controller: 'ForgotPasswordCtrl as forgotPassword'
  });

};



/**
 * Init second module.
 */
jxdctopn.panel.forgot_password.module
.config(jxdctopn.panel.forgot_password.module.configuration)
.controller('ForgotPasswordCtrl', jxdctopn.panel.forgot_password.Ctrl);
