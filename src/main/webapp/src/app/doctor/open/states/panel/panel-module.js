'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxdctopn.panel.module');

/**
 * Require child states.
 */
goog.require('jxdctopn.panel.sign_in.module');
goog.require('jxdctopn.panel.forgot_password.module');

/**
 * Require controller.
 */
goog.require('jxdctopn.panel.Ctrl');



/**
 * Third module.
 *
 * Require child states `jxdctopn.panel.sign_in` and 
 * `jxdctopn.panel.forgot_password` here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxdctopn.panel.module = angular.module('panel', [
  'ui.router',
  jxdctopn.panel.sign_in.module.name,
  jxdctopn.panel.forgot_password.module.name,'ui.bootstrap'
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxdctopn.panel.module.configuration = function($stateProvider) {

  $stateProvider.state('panel', {
    url: '/panel',
    templateUrl: 'states/panel/panel.html',
    controller: 'PanelCtrl as panel'
  });

};



/**
 * Init user center module.
 */
jxdctopn.panel.module
.config(jxdctopn.panel.module.configuration)
.controller('PanelCtrl', jxdctopn.panel.Ctrl);
