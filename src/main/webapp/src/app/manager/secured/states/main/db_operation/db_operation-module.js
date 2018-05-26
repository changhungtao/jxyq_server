'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.db_operation.module');


goog.require('jxmgrsec.main.db_operation.product_type.module');
goog.require('jxmgrsec.main.db_operation.device_type.module');
goog.require('jxmgrsec.main.db_operation.terminal_category.module');
goog.require('jxmgrsec.main.db_operation.terminal.module');
goog.require('jxmgrsec.main.db_operation.manufactory.module');
goog.require('jxmgrsec.main.db_operation.doctor.module');
goog.require('jxmgrsec.main.db_operation.user.module');
/**
 * Require controller.
 */
goog.require('jxmgrsec.main.db_operation.Ctrl');


/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.db_operation.module = angular.module('main.db_operation', [
  'ui.router',
  // 'ui.grid', 
  // 'ui.grid.pinning', 
  // 'ui.grid.resizeColumns', 
  // 'ui.grid.saveState',
  // 'ui.grid.edit',
  // 'ui.grid.pagination',
  jxmgrsec.main.db_operation.product_type.module.name,
  jxmgrsec.main.db_operation.device_type.module.name,
  jxmgrsec.main.db_operation.terminal_category.module.name,
  jxmgrsec.main.db_operation.terminal.module.name,
  jxmgrsec.main.db_operation.manufactory.module.name,
  jxmgrsec.main.db_operation.doctor.module.name,
  jxmgrsec.main.db_operation.user.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.db_operation.module.configuration = function($stateProvider) {

  $stateProvider.state('main.db_operation', {
    url: '/db_operation',
    templateUrl: 'states/main/db_operation/db_operation.html',
    controller: 'DbOperationCtrl as dbOperation'
  });

};



/**
 * Init db operation module.
 */
jxmgrsec.main.db_operation.module
.config(jxmgrsec.main.db_operation.module.configuration)
.controller('DbOperationCtrl', jxmgrsec.main.db_operation.Ctrl);
