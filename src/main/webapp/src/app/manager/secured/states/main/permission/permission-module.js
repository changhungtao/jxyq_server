'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.permission.module');


goog.require('jxmgrsec.main.permission.manufactory_permission.module');
goog.require('jxmgrsec.main.permission.doctor_permission.module');
goog.require('jxmgrsec.main.permission.doctor_permission_list.module');
/**
 * Require controller.
 */
goog.require('jxmgrsec.main.permission.Ctrl');


/**
 * Permission module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.permission.module = angular.module('main.permission', [
  'ui.router',
  'ui.grid', 
  'ui.grid.pinning', 
  'ui.grid.resizeColumns', 
  'ui.grid.saveState',
  'ui.grid.edit',
  'ui.grid.pagination',
  jxmgrsec.main.permission.manufactory_permission.module.name,
  jxmgrsec.main.permission.doctor_permission.module.name,
  jxmgrsec.main.permission.doctor_permission_list.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.permission.module.configuration = function($stateProvider) {

  $stateProvider.state('main.permission', {
    url: '/permission',
    templateUrl: 'states/main/permission/permission.html',
    controller: 'Permissiontrl as permission'
  });

};



/**
 * Init permission module.
 */
jxmgrsec.main.permission.module
.config(jxmgrsec.main.permission.module.configuration)
.controller('Permissiontrl', jxmgrsec.main.permission.Ctrl);
