'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.alarm.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.twatch.alarm.Ctrl');
goog.require('jxmgrsec.modify_voice_pieroid_modal.Ctrl');
goog.require('jxmgrsec.add_voice_pieroid_modal.Ctrl');


/**
 * Module for main.twatch.alarm state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.twatch.alarm.module = angular.module('main.twatch.alarm', [
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
jxmgrsec.main.twatch.alarm.module.configuration = function($stateProvider) {

  $stateProvider.state('main.twatch.alarm', {
    url: '/alarm',
    templateUrl: 'states/main/twatch/alarm/alarm.html',
    controller: 'AlarmCtrl as alarm'
  });

};



/**
 * Init main.twatch.alarm module.
 */
jxmgrsec.main.twatch.alarm.module
.config(jxmgrsec.main.twatch.alarm.module.configuration)
.controller('ModifyVoicePieroidModalCtrl', jxmgrsec.modify_voice_pieroid_modal.Ctrl)
.controller('AddVoicePieroidModalCtrl', jxmgrsec.add_voice_pieroid_modal.Ctrl)
.controller('AlarmCtrl', jxmgrsec.main.twatch.alarm.Ctrl);
