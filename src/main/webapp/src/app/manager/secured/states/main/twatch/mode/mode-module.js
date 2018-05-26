'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.mode.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.twatch.mode.Ctrl');
goog.require('jxmgrsec.modify_mode_whitename_modal.Ctrl');
goog.require('jxmgrsec.add_mode_whitename_modal.Ctrl');
goog.require('jxmgrsec.delete_mode_whitename_modal.Ctrl');
goog.require('jxmgrsec.modify_mode_blackname_modal.Ctrl');
goog.require('jxmgrsec.add_mode_blackname_modal.Ctrl');
goog.require('jxmgrsec.delete_mode_blackname_modal.Ctrl');
goog.require('jxmgrsec.modify_mode_pieroid_modal.Ctrl');
goog.require('jxmgrsec.add_mode_pieroid_modal.Ctrl');
goog.require('jxmgrsec.delete_mode_pieroid_modal.Ctrl');


/**
 * Module for main.twatch.mode state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.twatch.mode.module = angular.module('main.twatch.mode', [
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
jxmgrsec.main.twatch.mode.module.configuration = function($stateProvider) {

  $stateProvider.state('main.twatch.mode', {
    url: '/mode',
    templateUrl: 'states/main/twatch/mode/mode.html',
    controller: 'ModeCtrl as mode',
    resolve: {
      'WORKMODESPromise': function(constants) {
        return constants.WORKMODESPromise;
      },
      'WORKTYPESPromise': function(constants) {
        return constants.WORKTYPESPromise;
      }
    }
  });

};



/**
 * Init main.twatch.mode module.
 */
jxmgrsec.main.twatch.mode.module
.config(jxmgrsec.main.twatch.mode.module.configuration)
.controller('ModeCtrl', jxmgrsec.main.twatch.mode.Ctrl)
.controller('ModifyModeWhitenameModalCtrl', jxmgrsec.modify_mode_whitename_modal.Ctrl)
.controller('AddModeWhitenameModalCtrl', jxmgrsec.add_mode_whitename_modal.Ctrl)
.controller('DeleteModeWhitenameModalCtrl', jxmgrsec.delete_mode_whitename_modal.Ctrl)
.controller('ModifyModeBlacknameModalCtrl', jxmgrsec.modify_mode_blackname_modal.Ctrl)
.controller('AddModeBlacknameModalCtrl', jxmgrsec.add_mode_blackname_modal.Ctrl)
.controller('DeleteModeBlacknameModalCtrl', jxmgrsec.delete_mode_blackname_modal.Ctrl)
.controller('ModifyModePieroidModalCtrl', jxmgrsec.modify_mode_pieroid_modal.Ctrl)
.controller('AddModePieroidModalCtrl', jxmgrsec.add_mode_pieroid_modal.Ctrl)
.controller('DeleteModePieroidModalCtrl', jxmgrsec.delete_mode_pieroid_modal.Ctrl);

