'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.terminal.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.db_operation.terminal.Ctrl');
goog.require('jxmgrsec.check_terminal_modal.Ctrl');
goog.require('jxmgrsec.check_users_modal.Ctrl');
/**
 * Module for main.db_operation.terminal state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.db_operation.terminal.module = angular.module('main.db_operation.terminal', [
  'ui.router', 
  'ngResource', 
  'ui.select',
  'ngSanitize', 
  'ui.bootstrap', 
  'angularModalService'
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
jxmgrsec.main.db_operation.terminal.module.configuration = function($stateProvider) {

  $stateProvider.state('main.db_operation.terminal', {
    url: '/terminal',
    templateUrl: 'states/main/db_operation/terminal/terminal.html',
    controller: 'TerminalCtrl as terminal'
  });

};



/**
 * Init main.db_operation.terminal module.
 */
jxmgrsec.main.db_operation.terminal.module
  .config(jxmgrsec.main.db_operation.terminal.module.configuration)
  .controller('TerminalCtrl', jxmgrsec.main.db_operation.terminal.Ctrl)
  .controller('CheckTerminalModalCtrl', jxmgrsec.check_terminal_modal.Ctrl)
  .controller('CheckUsersModalCtrl', jxmgrsec.check_users_modal.Ctrl)
  ;
