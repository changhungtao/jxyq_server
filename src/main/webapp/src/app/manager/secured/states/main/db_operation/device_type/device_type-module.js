'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.device_type.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.db_operation.device_type.Ctrl');
goog.require('jxmgrsec.check_manufacturers_modal.Ctrl');
goog.require('jxmgrsec.add_device_type_modal.Ctrl');
goog.require('jxmgrsec.modify_device_type_modal.Ctrl');
/**
 * Module for main.db_operation.device_type state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.db_operation.device_type.module = angular.module('main.db_operation.device_type', [
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
jxmgrsec.main.db_operation.device_type.module.configuration = function($stateProvider) {

  $stateProvider.state('main.db_operation.device_type', {
    url: '/device_type',
    templateUrl: 'states/main/db_operation/device_type/device_type.html',
    controller: 'DeviceTypeCtrl as deviceType',
    resolve: {      
      'DEVICETYPESPromise': function(constants){
        return constants.DEVICETYPESPromise;
      }
    }
  });

};



/**
 * Init main.db_operation.device_type module.
 */
jxmgrsec.main.db_operation.device_type.module
  .config(jxmgrsec.main.db_operation.device_type.module.configuration)
  .controller('DeviceTypeCtrl', jxmgrsec.main.db_operation.device_type.Ctrl)
  .controller('CheckManufacturersModalCtrl', jxmgrsec.check_manufacturers_modal.Ctrl)
  .controller('AddDeviceTypeModalCtrl', jxmgrsec.add_device_type_modal.Ctrl)
  .controller('ModifyDeviceTypeModalCtrl', jxmgrsec.modify_device_type_modal.Ctrl)
  ;
