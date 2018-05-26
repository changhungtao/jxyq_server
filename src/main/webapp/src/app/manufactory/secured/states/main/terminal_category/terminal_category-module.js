'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmnfsec.main.terminal_category.module');


/**
 * Require controller.
 */
goog.require('jxmnfsec.main.terminal_category.Ctrl');
goog.require('jxmnfsec.add_terminal_catagory_modal.Ctrl');
goog.require('jxmnfsec.edit_terminal_catagory_modal.Ctrl');



/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxmnfsec.main.terminal_category.module = angular.module('main.terminal_category', [
  'ui.router'
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmnfsec.main.terminal_category.module.configuration = function($stateProvider) {

  $stateProvider.state('main.terminal_category', {
    url: '/terminal_category',
    templateUrl: 'states/main/terminal_category/terminal_category.html',
    controller: 'TerminalCategoryCtrl as terminalCategory',
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
 * Init terminal category module.
 */
jxmnfsec.main.terminal_category.module
.config(jxmnfsec.main.terminal_category.module.configuration)
.controller('TerminalCategoryCtrl', jxmnfsec.main.terminal_category.Ctrl)
.controller('AddTerminalCategoryCtrl', jxmnfsec.add_terminal_catagory_modal.Ctrl)
.controller('EditTerminalCategoryCtrl', jxmnfsec.edit_terminal_catagory_modal.Ctrl);
