'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.route.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.twatch.route.Ctrl');


/**
 * Module for main.twatch.route state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.twatch.route.module = angular.module('main.twatch.route', [
  'ui.router',
  'oc.lazyLoad'
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
jxmgrsec.main.twatch.route.module.configuration = function($stateProvider) {

  $stateProvider.state('main.twatch.route', {
    url: '/route',
    templateUrl: 'states/main/twatch/route/route.html',
    controller: 'RouteCtrl as route'
  });

};



/**
 * Init main.twatch.route module.
 */
jxmgrsec.main.twatch.route.module
.config(jxmgrsec.main.twatch.route.module.configuration)
.controller('RouteCtrl', jxmgrsec.main.twatch.route.Ctrl);
