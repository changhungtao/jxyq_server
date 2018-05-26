'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgropn.panel.module');

/**
 * Require child states.
 */
goog.require('jxmgropn.panel.sign_in.module');

/**
 * Require controller.
 */
goog.require('jxmgropn.panel.Ctrl');



/**
 * Third module.
 *
 * Require child states `jxmgropn.panel.sign_in` and 
 * `jxmgropn.panel.forgot_password` here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxmgropn.panel.module = angular.module('panel', [
  'ui.router',
  jxmgropn.panel.sign_in.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgropn.panel.module.configuration = function($stateProvider) {

  $stateProvider.state('panel', {
    url: '/panel',
    templateUrl: 'states/panel/panel.html',
    controller: 'PanelCtrl as panel'
  });

};



/**
 * Init user center module.
 */
jxmgropn.panel.module
.config(jxmgropn.panel.module.configuration)
.controller('PanelCtrl', jxmgropn.panel.Ctrl);
