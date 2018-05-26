'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.home_security.module');


goog.require('jxmgrsec.main.home_security.camera.module');
goog.require('jxmgrsec.main.home_security.button.module');
/**
 * Require controller.
 */
goog.require('jxmgrsec.main.home_security.Ctrl');


/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.home_security.module = angular.module('main.home_security', [
  'ui.router',
  // 'ui.grid', 
  // 'ui.grid.pinning', 
  // 'ui.grid.resizeColumns', 
  // 'ui.grid.saveState',
  // 'ui.grid.edit',
  // 'ui.grid.pagination',
  jxmgrsec.main.home_security.camera.module.name,
  jxmgrsec.main.home_security.button.module.name
  
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.home_security.module.configuration = function($stateProvider) {

  $stateProvider.state('main.home_security', {
    url: '/home_security',
    templateUrl: 'states/main/home_security/home_security.html',
    controller: 'HomeSecurityCtrl as homeSecurity'
  });

};



/**
 * Init db operation module.
 */
jxmgrsec.main.home_security.module
.config(jxmgrsec.main.home_security.module.configuration)
.controller('HomeSecurityCtrl', jxmgrsec.main.home_security.Ctrl);
