'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.user_center.module');

/**
 * Require child states.
 */
goog.require('jxmgrsec.main.user_center.basics.module');
goog.require('jxmgrsec.main.user_center.password.module');
goog.require('jxmgrsec.main.user_center.avatar.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.user_center.Ctrl');



/**
 * User center module.
 *
 * Require child states `jxmgrsec.main.user_center.basics` and 
 * `jxmgrsec.main.user_center.password` here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.user_center.module = angular.module('main.user_center', [
  'ui.router',
  jxmgrsec.main.user_center.basics.module.name,
  jxmgrsec.main.user_center.password.module.name,
  jxmgrsec.main.user_center.avatar.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.user_center.module.configuration = function($stateProvider) {

  $stateProvider.state('main.user_center', {
    url: '/user_center',
    templateUrl: 'states/main/user_center/user_center.html',
    controller: 'UserCenterCtrl as userCenter'
  });

};



/**
 * Init user center module.
 */
jxmgrsec.main.user_center.module
.config(jxmgrsec.main.user_center.module.configuration)
.controller('UserCenterCtrl', jxmgrsec.main.user_center.Ctrl);
