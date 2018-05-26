'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmnfsec.main.user_center.module');

/**
 * Require child states.
 */
goog.require('jxmnfsec.main.user_center.basics.module');
goog.require('jxmnfsec.main.user_center.avatar.module');
goog.require('jxmnfsec.main.user_center.password.module');

/**
 * Require controller.
 */
goog.require('jxmnfsec.main.user_center.Ctrl');



/**
 * Third module.
 *
 * Require child states `jxmnfsec.main.user_center.basics` and 
 * `jxmnfsec.main.user_center.password` here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxmnfsec.main.user_center.module = angular.module('main.user_center', [
  'ui.router',
  jxmnfsec.main.user_center.basics.module.name,
  jxmnfsec.main.user_center.avatar.module.name,
  jxmnfsec.main.user_center.password.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmnfsec.main.user_center.module.configuration = function($stateProvider) {

  $stateProvider.state('main.user_center', {
    url: '/user_center',
    templateUrl: 'states/main/user_center/user_center.html',
    controller: 'UserCenterCtrl as userCenter'
  });

};



/**
 * Init user center module.
 */
jxmnfsec.main.user_center.module
.config(jxmnfsec.main.user_center.module.configuration)
.controller('UserCenterCtrl', jxmnfsec.main.user_center.Ctrl);
