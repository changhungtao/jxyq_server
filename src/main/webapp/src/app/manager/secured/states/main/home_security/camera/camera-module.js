'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.home_security.camera.module');


/**
 * Require controller.
 */
goog.require('jxmgrsec.main.home_security.camera.Ctrl');



/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.home_security.camera.module = angular.module('main.home_security.camera', [
  'ui.router',
  'ui.grid', 
  'ui.grid.pinning', 
  'ui.grid.resizeColumns', 
  'ui.grid.saveState',
  'ui.grid.edit',
  'ui.grid.pagination',
  'ui.select',
  'ui.bootstrap',
  'ngSanitize'
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.home_security.camera.module.configuration = function($stateProvider) {

  $stateProvider.state('main.home_security.camera', {
    url: '/camera',
    templateUrl: 'states/main/home_security/camera/camera.html',
    controller: 'CameraCtrl as camera'
  });

};



/**
 * Init user center module.
 */
jxmgrsec.main.home_security.camera.module
.config(jxmgrsec.main.home_security.camera.module.configuration)
.controller('CameraCtrl', jxmgrsec.main.home_security.camera.Ctrl);
