'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.loc.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.twatch.loc.Ctrl');


/**
 * Module for main.twatch.loc state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.twatch.loc.module = angular.module('main.twatch.loc', [
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
jxmgrsec.main.twatch.loc.module.configuration = function($stateProvider) {

  $stateProvider.state('main.twatch.loc', {
    url: '/loc',
    templateUrl: 'states/main/twatch/loc/loc.html',
    controller: 'LocCtrl as loc'
  });

};



/**
 * Init main.twatch.loc module.
 */
jxmgrsec.main.twatch.loc.module
.config(jxmgrsec.main.twatch.loc.module.configuration)
.controller('LocCtrl', jxmgrsec.main.twatch.loc.Ctrl);
