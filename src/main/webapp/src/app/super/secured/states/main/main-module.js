 'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxsprsec.main.module');

/**
 * Require child states.
 */
goog.require('jxsprsec.main.db_operation.module');
goog.require('jxsprsec.main.user_center.module');
goog.require('jxsprsec.main.log_manage.module');

/**
 * Require controller.
 */
goog.require('jxsprsec.main.Ctrl');


/**
 * Main module.
 *
 * Require child states here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxsprsec.main.module = angular.module('main', [
  'ui.router',
  'ui.navbar',
  jxsprsec.main.db_operation.module.name,
  jxsprsec.main.user_center.module.name,
  jxsprsec.main.log_manage.module.name,
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxsprsec.main.module.configuration = function($stateProvider) {

  $stateProvider.state('main', {
    url: '/main',
    templateUrl: 'states/main/main.html',
    controller: 'MainCtrl as main'
  });

};



/**
 * Init main module.
 */
jxsprsec.main.module
.config(jxsprsec.main.module.configuration)
.controller('MainCtrl', jxsprsec.main.Ctrl);
