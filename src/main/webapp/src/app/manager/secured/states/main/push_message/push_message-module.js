'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.push_message.module');


/**
 * Require controller.
 */
goog.require('jxmgrsec.main.push_message.Ctrl');
goog.require('jxmgrsec.show_push_message_modal.show_push_message_modal.Ctrl');


/**
 * Push message module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.push_message.module = angular.module('main.push_message', [
  'ui.router',
  'ui.grid', 
  'ui.grid.pinning', 
  'ui.grid.resizeColumns', 
  'ui.grid.saveState',
  'ui.grid.edit',
  'ui.grid.pagination',
  'ui.select',
  'ui.bootstrap',
  'ngSanitize'
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.push_message.module.configuration = function($stateProvider) {

  $stateProvider.state('main.push_message', {
    url: '/push_message',
    templateUrl: 'states/main/push_message/push_message.html',
    controller: 'PushMessageCtrl as pushMessage',
    resolve: {
          'DEVICETYPESPromise': function(constants) {
              return constants.DEVICETYPESPromise;
          }
      }
  });

};



/**
 * Init push message module.
 */
jxmgrsec.main.push_message.module
.config(jxmgrsec.main.push_message.module.configuration)
.controller('PushMessageCtrl', jxmgrsec.main.push_message.Ctrl)
.controller('ShowPushMessageModalCtrl', jxmgrsec.show_push_message_modal.show_push_message_modal.Ctrl);
