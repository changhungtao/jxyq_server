'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmnfopn.panel.sign_in.module');

/**
 * Require controller.
 */
goog.require('jxmnfopn.panel.sign_in.Ctrl');



/**
 * Sign in module.
 *
 * @return {angular.Module}
 */
jxmnfopn.panel.sign_in.module = angular.module('panel.sign_in', [
  'ui.router',
  'angular-md5'
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmnfopn.panel.sign_in.module.configuration = function($stateProvider) {

  $stateProvider.state('panel.sign_in', {
    url: '/sign_in',
    templateUrl: 'states/panel/sign_in/sign_in.html',
    controller: 'SignInCtrl as signIn'
  });

};



/**
 * Init sign in module.
 */
jxmnfopn.panel.sign_in.module
.config(jxmnfopn.panel.sign_in.module.configuration)
.controller('SignInCtrl', jxmnfopn.panel.sign_in.Ctrl);
