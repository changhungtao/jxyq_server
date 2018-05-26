'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.product_type.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.db_operation.product_type.Ctrl');
goog.require('jxmgrsec.check_devices_modal.Ctrl');
goog.require('jxmgrsec.add_product_type_modal.Ctrl');
goog.require('jxmgrsec.modify_product_type_modal.Ctrl');
/**
 * Module for main.db_operation.product_type state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.db_operation.product_type.module = angular.module('main.db_operation.product_type', [
  'ui.router', 
  'ngResource', 
  'ui.select',
  'ngSanitize', 
  'ui.bootstrap', 
  'angularModalService'
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
jxmgrsec.main.db_operation.product_type.module.configuration = function($stateProvider) {

  $stateProvider.state('main.db_operation.product_type', {
    url: '/product_type',
    templateUrl: 'states/main/db_operation/product_type/product_type.html',
    controller: 'ProductTypeCtrl as productType',
    resolve: {      
      'PRODUCTTYPESPromise': function(constants) {
            return constants.PRODUCTTYPESPromise;
        }
    }
  });

};



/**
 * Init main.db_operation.product_type module.
 */
jxmgrsec.main.db_operation.product_type.module
  .config(jxmgrsec.main.db_operation.product_type.module.configuration)
  .controller('ProductTypeCtrl', jxmgrsec.main.db_operation.product_type.Ctrl)
  .controller('CheckDevicesModalCtrl', jxmgrsec.check_devices_modal.Ctrl)
  .controller('AddProductTypeModalCtrl', jxmgrsec.add_product_type_modal.Ctrl)
  .controller('ModifyProductTypeModalCtrl', jxmgrsec.modify_product_type_modal.Ctrl)
  ;
