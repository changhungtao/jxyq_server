'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxspropn.panel.module');

/**
 * Require child states.
 */
goog.require('jxspropn.panel.sign_in.module');

/**
 * Require controller.
 */
goog.require('jxspropn.panel.Ctrl');



/**
 * Third module.
 *
 * Require child states `jxspropn.panel.sign_in` and 
 * `jxspropn.panel.forgot_password` here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxspropn.panel.module = angular.module('panel', [
  'ui.router',
  jxspropn.panel.sign_in.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxspropn.panel.module.configuration = function($stateProvider) {

  $stateProvider.state('panel', {
    url: '/panel',
    templateUrl: 'states/panel/panel.html',
    controller: 'PanelCtrl as panel'
  });

};



/**
 * Init user center module.
 */
jxspropn.panel.module
.config(jxspropn.panel.module.configuration)
.controller('PanelCtrl', jxspropn.panel.Ctrl);
