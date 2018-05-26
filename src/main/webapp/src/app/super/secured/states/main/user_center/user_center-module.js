'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxsprsec.main.user_center.module');

/**
 * Require child states.
 */
goog.require('jxsprsec.main.user_center.basics.module');
goog.require('jxsprsec.main.user_center.avatar.module');
goog.require('jxsprsec.main.user_center.password.module');

/**
 * Require controller.
 */
goog.require('jxsprsec.main.user_center.Ctrl');



/**
 * Third module.
 *
 * Require child states `jxsprsec.main.user_center.basics` and 
 * `jxsprsec.main.user_center.password` here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxsprsec.main.user_center.module = angular.module('main.user_center', [
  'ui.router',
  jxsprsec.main.user_center.basics.module.name,
  jxsprsec.main.user_center.avatar.module.name,
  jxsprsec.main.user_center.password.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxsprsec.main.user_center.module.configuration = function($stateProvider) {

  $stateProvider.state('main.user_center', {
    url: '/user_center',
    templateUrl: 'states/main/user_center/user_center.html',
    controller: 'UserCenterCtrl as userCenter'
  });

};



/**
 * Init user center module.
 */
jxsprsec.main.user_center.module
.config(jxsprsec.main.user_center.module.configuration)
.controller('UserCenterCtrl', jxsprsec.main.user_center.Ctrl);
