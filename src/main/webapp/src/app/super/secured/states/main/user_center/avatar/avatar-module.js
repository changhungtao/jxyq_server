'use strict';

/**
 * Create namespace.
 */
goog.provide('jxsprsec.main.user_center.avatar.module');

/**
 * Require controller.
 */
goog.require('jxsprsec.main.user_center.avatar.Ctrl');



/**
 * Module for main.user_center.avatar state.
 *
 * @return {angular.Module}
 */
jxsprsec.main.user_center.avatar.module = angular.module('main.user_center.avatar', [
  'ui.router'
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
jxsprsec.main.user_center.avatar.module.configuration = function($stateProvider) {

  $stateProvider.state('main.user_center.avatar', {
    url: '/avatar',
    templateUrl: 'states/main/user_center/avatar/avatar.html',
    controller: 'AvatarCtrl as avatar'
  });

};



/**
 * Init main.user_center.avatar module.
 */
jxsprsec.main.user_center.avatar.module
.config(jxsprsec.main.user_center.avatar.module.configuration)
.controller('AvatarCtrl', jxsprsec.main.user_center.avatar.Ctrl);
