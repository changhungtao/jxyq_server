'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.permission.manufactory_permission.module');


/**
 * Require controller.
 */
goog.require('jxmgrsec.main.permission.manufactory_permission.Ctrl');
goog.require('jxmgrsec.edit_manufactory_permission_modal.Ctrl');



/**
 * Manufactory permission module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.permission.manufactory_permission.module = angular.module('main.permission.manufactory_permission', [
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
jxmgrsec.main.permission.manufactory_permission.module.configuration = function($stateProvider) {

  $stateProvider.state('main.permission.manufactory_permission', {
    url: '/manufactory_permission',
    templateUrl: 'states/main/permission/manufactory_permission/manufactory_permission.html',
    controller: 'ManufactoryPermissionCtrl as manufactoryPermission',
      resolve: {
          'DEVICETYPESPromise': function(constants) {
              return constants.DEVICETYPESPromise;
          },
          'MANUFACTORYSTATUSPromise':function(constants){
              return constants.MANUFACTORYSTATUSPromise;
          }
      }
  });

};



/**
 * Init manufactory permission module.
 */
jxmgrsec.main.permission.manufactory_permission.module
.config(jxmgrsec.main.permission.manufactory_permission.module.configuration)
.controller('ManufactoryPermissionCtrl', jxmgrsec.main.permission.manufactory_permission.Ctrl)
.controller('EditManufactoryPermissionModalCtrl', jxmgrsec.edit_manufactory_permission_modal.Ctrl)
