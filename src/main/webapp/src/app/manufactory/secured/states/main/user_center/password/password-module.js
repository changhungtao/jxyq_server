'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmnfsec.main.user_center.password.module');

/**
 * Require controller.
 */
goog.require('jxmnfsec.main.user_center.password.Ctrl');



/**
 * Module for user_center.password state.
 *
 * @return {angular.Module}
 */
jxmnfsec.main.user_center.password.module = angular.module('main.user_center.password', [
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
jxmnfsec.main.user_center.password.module.configuration = function($stateProvider) {

  $stateProvider.state('main.user_center.password', {
    url: '/password',
    templateUrl: 'states/main/user_center/password/password.html',
    controller: 'PasswordCtrl as password'
  });

};



/**
 * Init user_center.password module.
 */
jxmnfsec.main.user_center.password.module
.config(jxmnfsec.main.user_center.password.module.configuration)
.controller('PasswordCtrl', jxmnfsec.main.user_center.password.Ctrl);
