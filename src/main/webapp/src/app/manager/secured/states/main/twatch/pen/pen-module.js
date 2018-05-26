'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.pen.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.twatch.pen.Ctrl');
goog.require('jxmgrsec.pen_point_modal.Ctrl');


/**
 * Module for main.twatch.pen state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.twatch.pen.module = angular.module('main.twatch.pen', [
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
jxmgrsec.main.twatch.pen.module.configuration = function($stateProvider) {

  $stateProvider.state('main.twatch.pen', {
    url: '/pen',
    templateUrl: 'states/main/twatch/pen/pen.html',
    controller: 'PenCtrl as pen'
  });

};



/**
 * Init main.twatch.pen module.
 */
jxmgrsec.main.twatch.pen.module
.config(jxmgrsec.main.twatch.pen.module.configuration)
.controller('PenCtrl', jxmgrsec.main.twatch.pen.Ctrl)
.controller('PenPointModalCtrl', jxmgrsec.pen_point_modal.Ctrl)
