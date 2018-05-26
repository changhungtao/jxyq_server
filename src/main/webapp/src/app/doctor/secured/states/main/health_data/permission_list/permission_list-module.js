'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_data.permission_list.module');


/**
 * Require controller.
 */
goog.require('jxdctsec.main.health_data.permission_list.Ctrl');
goog.require('jxdctsec.health_report.permission_list_modal.Ctrl');

/**
 * Module for main.health_data.permission_list state.
 *
 * @return {angular.Module}
 */
jxdctsec.main.health_data.permission_list.module = angular.module('main.health_data.permission_list', [
  'ui.router',
  'ui.grid',
  'ui.grid.pinning',
  'ui.grid.resizeColumns',
  'ui.grid.saveState',
  'ui.grid.edit',
  'ui.grid.pagination'
]);



/**
 * Configuration function.
 *
 * Important! Do not call this function `config()`. It would collide
 * with the AngularJS `config()` function. The init part at the end of this file
 * would look like `my.blabla.config(my.blabla.config())` which obviously would
 * not work.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxdctsec.main.health_data.permission_list.module.configuration = function($stateProvider) {

  $stateProvider.state('main.health_data.permission_list', {
    url: '/permission_list',
    templateUrl: 'states/main/health_data/permission_list/permission_list.html',
    controller: 'PermissionListCtrl as permissionList',
    resolve: {
      'DATATYPESPromise': function(constants){
        return constants.DATATYPESPromise;
      },
      'DISTRICTSPromise': function(constants){
        return constants.DISTRICTSPromise;
      },
      'PROVINCESPromise': function(constants){
        return constants.PROVINCESPromise;
      },
      'CITIESPromise': function(constants){
        return constants.CITIESPromise;
      },
      'ZONESPromise': function(constants){
        return constants.ZONESPromise;
      }
    }
  });

};



/**
 * Init main.health_data.permission_list module.
 */
jxdctsec.main.health_data.permission_list.module
  .config(jxdctsec.main.health_data.permission_list.module.configuration)
  .controller('PermissionListCtrl', jxdctsec.main.health_data.permission_list.Ctrl)
  .controller('PermissionListModalCtrl', jxdctsec.health_report.permission_list_modal.Ctrl);

