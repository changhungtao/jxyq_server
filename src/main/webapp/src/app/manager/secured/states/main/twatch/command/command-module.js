'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.command.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.twatch.command.Ctrl');


/**
 * Module for main.twatch.command state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.twatch.command.module = angular.module('main.twatch.command', [
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
jxmgrsec.main.twatch.command.module.configuration = function($stateProvider) {

  $stateProvider.state('main.twatch.command', {
    url: '/command',
    templateUrl: 'states/main/twatch/command/command.html',
    controller: 'CommandCtrl as command'
  });

};



/**
 * Init main.twatch.command module.
 */
jxmgrsec.main.twatch.command.module
.config(jxmgrsec.main.twatch.command.module.configuration)
.controller('CommandCtrl', jxmgrsec.main.twatch.command.Ctrl);
