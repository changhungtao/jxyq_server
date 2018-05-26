'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.manufactory.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.db_operation.manufactory.Ctrl');
goog.require('jxmgrsec.manufacturer_detail_modal.Ctrl');
goog.require('jxmgrsec.add_manufactory_modal.Ctrl');
goog.require('jxmgrsec.modify_manufactory_modal.Ctrl');
goog.require('jxmgrsec.delete_manufactory_modal.Ctrl');
/**
 * Module for main.db_operation.manufactory state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.db_operation.manufactory.module = angular.module('main.db_operation.manufactory', [
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
jxmgrsec.main.db_operation.manufactory.module.configuration = function($stateProvider) {

    $stateProvider.state('main.db_operation.manufactory', {
        url: '/manufactory',
        templateUrl: 'states/main/db_operation/manufactory/manufactory.html',
        controller: 'ManufactoryCtrl as manufactory',
        resolve: {
            //使用前保证数据表加载完毕
            'DEVICETYPESPromise': function(constants) {
                return constants.DEVICETYPESPromise;
            },
            'GENDERSPromise': function(constants) {
                return constants.GENDERSPromise;
            },
            'DISTRICTSPromise': function(constants) {
                return constants.DISTRICTSPromise;
            },
            'DEPARTMENTSPromise': function(constants) {
                return constants.DEPARTMENTSPromise;
            },
            'MANUFACTORYSTATUSPromise': function(constants) {
                return constants.MANUFACTORYSTATUSPromise;
            },
            'MANUFACTORIESPromise': function(constants) {
                return constants.MANUFACTORIESPromise;
            },
            'COMPANYDEPARTMENTSPromise': function(constants) {
                return constants.COMPANYDEPARTMENTSPromise;
            },
            'COMPANYMEMBERSPromise': function(constants) {
                return constants.COMPANYMEMBERSPromise;
            },
            'COMPANYNATURESPromise': function(constants) {
                return constants.COMPANYNATURESPromise;
            },
            'COMPANYINDUSTRIESPromise': function(constants) {
                return constants.COMPANYINDUSTRIESPromise;
            },
            'PROVINCESPromise': function(constants) {
                return constants.PROVINCESPromise;
            },
            'CITIESPromise': function(constants) {
                return constants.CITIESPromise;
            },
            'ZONESPromise': function(constants) {
                return constants.ZONESPromise;
            }
        }
    });

};



/**
 * Init main.db_operation.manufactory module.
 */
jxmgrsec.main.db_operation.manufactory.module
    .config(jxmgrsec.main.db_operation.manufactory.module.configuration)
    .controller('ManufactoryCtrl', jxmgrsec.main.db_operation.manufactory.Ctrl)
    .controller('ManugfacturerDetailModal', jxmgrsec.manufacturer_detail_modal.Ctrl)
    .controller('AddManufactoryModal', jxmgrsec.add_manufactory_modal.Ctrl)
    .controller('ModifyManufactoryModal', jxmgrsec.modify_manufactory_modal.Ctrl)
    .controller('DeleteManufactoryModal', jxmgrsec.delete_manufactory_modal.Ctrl);
