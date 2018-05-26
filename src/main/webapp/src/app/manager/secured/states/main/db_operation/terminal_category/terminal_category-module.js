'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.terminal_category.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.db_operation.terminal_category.Ctrl');

/**
 * Module for main.db_operation.terminal_category state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.db_operation.terminal_category.module = angular.module('main.db_operation.terminal_category', [
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
jxmgrsec.main.db_operation.terminal_category.module.configuration = function($stateProvider) {

  $stateProvider.state('main.db_operation.terminal_category', {
    url: '/terminal_category',
    templateUrl: 'states/main/db_operation/terminal_category/terminal_category.html',
    controller: 'TerminalCategoryCtrl as terminalcategory',
    resolve: {
        'DEVICETYPESPromise': function(constants){
            return constants.DEVICETYPESPromise;
        },
        'PRODUCTTYPESPromise': function(constants){
            return constants.PRODUCTTYPESPromise;
        },
        'TERMINALSTATUSPromise': function(constants){
            return constants.TERMINALSTATUSPromise;
        }
    }
  });

};



/**
 * Init main.db_operation.terminal_category module.
 */
jxmgrsec.main.db_operation.terminal_category.module
  .config(jxmgrsec.main.db_operation.terminal_category.module.configuration)
  .controller('TerminalCategoryCtrl', jxmgrsec.main.db_operation.terminal_category.Ctrl);
