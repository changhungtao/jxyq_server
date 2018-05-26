'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmnfopn.panel.module');

/**
 * Require child states.
 */
goog.require('jxmnfopn.panel.sign_in.module');
goog.require('jxmnfopn.panel.forgot_password.module');

/**
 * Require controller.
 */
goog.require('jxmnfopn.panel.Ctrl');



/**
 * Third module.
 *
 * Require child states `jxmnfopn.panel.sign_in` and 
 * `jxmnfopn.panel.forgot_password` here
 * to reduce noise in `app.js`.
 *
 * @return {angular.Module}
 */
jxmnfopn.panel.module = angular.module('panel', [
  'ui.router',
  jxmnfopn.panel.sign_in.module.name,
  jxmnfopn.panel.forgot_password.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmnfopn.panel.module.configuration = function($stateProvider) {

  $stateProvider.state('panel', {
    url: '/panel',
    templateUrl: 'states/panel/panel.html',
    controller: 'PanelCtrl as panel'
  });

};



/**
 * Init panel module.
 */
jxmnfopn.panel.module
.config(jxmnfopn.panel.module.configuration)
.controller('PanelCtrl', jxmnfopn.panel.Ctrl);
