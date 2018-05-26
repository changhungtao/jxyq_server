'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.doctor.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.db_operation.doctor.Ctrl');
goog.require('jxmgrsec.add_doctor_modal.Ctrl');
goog.require('jxmgrsec.modify_doctor_modal.Ctrl');
goog.require('jxmgrsec.show_doctor_modal.Ctrl');
goog.require('jxmgrsec.delete_doctor_modal.Ctrl');
/**
/**
 * Module for main.db_operation.doctor state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.db_operation.doctor.module = angular.module('main.db_operation.doctor', [
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
jxmgrsec.main.db_operation.doctor.module.configuration = function($stateProvider) {

    $stateProvider.state('main.db_operation.doctor', {
        url: '/doctor',
        templateUrl: 'states/main/db_operation/doctor/doctor.html',
        controller: 'DoctorCtrl as doctor',
        resolve: {
            //使用前保证数据表加载完毕
            'DEVICETYPESPromise': function(constants) {
                return constants.DEVICETYPESPromise;
            },
            'USERSTATUSPromise': function(constants) {
                return constants.USERSTATUSPromise;
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
            'DOCTORSTATUSPromise': function(constants) {
                return constants.DOCTORSTATUSPromise;
            },
        }
    });

};



/**
 * Init main.db_operation.doctor module.
 */
jxmgrsec.main.db_operation.doctor.module
    .config(jxmgrsec.main.db_operation.doctor.module.configuration)
    .controller('DoctorCtrl', jxmgrsec.main.db_operation.doctor.Ctrl)
    .controller('AddDoctorModalCtrl', jxmgrsec.add_doctor_modal.Ctrl)
    .controller('ModifyDoctorModalCtrl', jxmgrsec.modify_doctor_modal.Ctrl)
    .controller('ShowDoctorModalCtrl', jxmgrsec.show_doctor_modal.Ctrl)
    .controller('DeleteDoctorModalCtrl', jxmgrsec.delete_doctor_modal.Ctrl);
