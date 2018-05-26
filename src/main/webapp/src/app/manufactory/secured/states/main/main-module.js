'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmnfsec.main.module');

/**
 * Require child states.
 */
goog.require('jxmnfsec.main.category_template.module');
goog.require('jxmnfsec.main.db_operation.module');
goog.require('jxmnfsec.main.terminal_category.module');
goog.require('jxmnfsec.main.health_data.module');
goog.require('jxmnfsec.main.user_center.module');

/**
 * Require controller.
 */
goog.require('jxmnfsec.main.Ctrl');


/**
 * Main module.
 *
 * Require child states here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxmnfsec.main.module = angular.module('main', [
  'ui.router',
  'ui.navbar',
  jxmnfsec.main.category_template.module.name,
  jxmnfsec.main.db_operation.module.name,
  jxmnfsec.main.terminal_category.module.name,
  jxmnfsec.main.health_data.module.name,
  jxmnfsec.main.user_center.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmnfsec.main.module.configuration = function($stateProvider) {

  $stateProvider.state('main', {
    url: '/main',
    templateUrl: 'states/main/main.html',
    controller: 'MainCtrl as main'
  });

};



/**
 * Init main module.
 */
jxmnfsec.main.module
.config(jxmnfsec.main.module.configuration)
.controller('MainCtrl', jxmnfsec.main.Ctrl);
