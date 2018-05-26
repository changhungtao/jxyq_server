'use strict';

/**
 * Create namespace.
 */
goog.provide('jxsprsec.main.user_center.basics.module');

/**
 * Require controller.
 */
goog.require('jxsprsec.main.user_center.basics.Ctrl');



/**
 * Module for user_center.basics state.
 *
 * @return {angular.Module}
 */
jxsprsec.main.user_center.basics.module = angular.module('main.user_center.basics', [
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
jxsprsec.main.user_center.basics.module.configuration = function($stateProvider) {

  $stateProvider.state('main.user_center.basics', {
    url: '/basics',
    templateUrl: 'states/main/user_center/basics/basics.html',
    controller: 'BasicsCtrl as basics',
    resolve: {
      'GENDERSPromise': function(constants) {
        return constants.GENDERSPromise;
      }
    }
  });
};


/**
 * Init user_center.basics module.
 */
jxsprsec.main.user_center.basics.module
  .config(jxsprsec.main.user_center.basics.module.configuration)
  .controller('BasicsCtrl', jxsprsec.main.user_center.basics.Ctrl);
