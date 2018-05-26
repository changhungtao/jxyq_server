'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.module');

/**
 * Require child states.
 */
goog.require('jxmgrsec.main.db_operation.module');
goog.require('jxmgrsec.main.health_data.module');
goog.require('jxmgrsec.main.home_security.module');
goog.require('jxmgrsec.main.push_message.module');
goog.require('jxmgrsec.main.permission.module');
goog.require('jxmgrsec.main.page.module');
goog.require('jxmgrsec.main.twatch.module');
goog.require('jxmgrsec.main.user_center.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.Ctrl');

/**
 * Main module.
 *
 * Require child states here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.module = angular.module('main', [
  'ui.router',
  'ui.bootstrap',
  'ui.navbar',
  jxmgrsec.main.db_operation.module.name,
  jxmgrsec.main.health_data.module.name,
  jxmgrsec.main.home_security.module.name,
  jxmgrsec.main.push_message.module.name,
  jxmgrsec.main.permission.module.name,
  jxmgrsec.main.page.module.name,
  jxmgrsec.main.twatch.module.name,
  jxmgrsec.main.user_center.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.module.configuration = function($stateProvider) {

  $stateProvider.state('main', {
    url: '/main',
    templateUrl: 'states/main/main.html',
    controller: 'MainCtrl as main'
  });

};



/**
 * Init main module.
 */
jxmgrsec.main.module
.config(jxmgrsec.main.module.configuration)
.controller('MainCtrl', jxmgrsec.main.Ctrl);
