'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.permission.doctor_permission.module');


/**
 * Require controller.
 */
goog.require('jxmgrsec.main.permission.doctor_permission.Ctrl');



/**
 * Doctor permission module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.permission.doctor_permission.module = angular.module('main.permission.doctor_permission', [
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
jxmgrsec.main.permission.doctor_permission.module.configuration = function($stateProvider) {

  $stateProvider.state('main.permission.doctor_permission', {
    url: '/doctor_permission',
    templateUrl: 'states/main/permission/doctor_permission/doctor_permission.html',
    controller: 'DoctorPermissionCtrl as doctorPermission'
  });

};



/**
 * Init doctor permission module.
 */
jxmgrsec.main.permission.doctor_permission.module
.config(jxmgrsec.main.permission.doctor_permission.module.configuration)
.controller('DoctorPermissionCtrl', jxmgrsec.main.permission.doctor_permission.Ctrl);
