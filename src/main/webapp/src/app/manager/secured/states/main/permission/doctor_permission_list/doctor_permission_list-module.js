'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.permission.doctor_permission_list.module');


/**
 * Require controller.
 */
goog.require('jxmgrsec.main.permission.doctor_permission_list.Ctrl');
goog.require('jxmgrsec.edit_doctor_permission_modal.Ctrl');
goog.require('jxmgrsec.add_doctor_permission_modal.Ctrl');



/**
 * Doctor permission module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.permission.doctor_permission_list.module = angular.module('main.permission.doctor_permission_list', [
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
jxmgrsec.main.permission.doctor_permission_list.module.configuration = function($stateProvider) {

  $stateProvider.state('main.permission.doctor_permission_list', {
    url: '/doctor_permission_list/:doctor_id/:full_name',
    templateUrl: 'states/main/permission/doctor_permission_list/doctor_permission_list.html',
    controller: 'DoctorPermissionListCtrl as doctorPermissionList',
    resolve: {
        //使用前保证数据表加载完毕
        'DISTRICTSPromise': function(constants) {
            return constants.DISTRICTSPromise;
        },
        'PROVINCESPromise': function(constants) {
            return constants.PROVINCESPromise;
        },
        'CITIESPromise': function(constants) {
            return constants.CITIESPromise;
        },
        'ZONESPromise': function(constants) {
            return constants.ZONESPromise;
        },
        'DATATYPESPromise': function(constants) {
            return constants.DATATYPESPromise;
        }
    }
  });

};



/**
 * Init doctor permission module.
 */
jxmgrsec.main.permission.doctor_permission_list.module
.config(jxmgrsec.main.permission.doctor_permission_list.module.configuration)
.controller('DoctorPermissionListCtrl', jxmgrsec.main.permission.doctor_permission_list.Ctrl)
.controller('EditDoctorPermissionModalCtrl', jxmgrsec.edit_doctor_permission_modal.Ctrl)
.controller('AddDoctorPermissionModalCtrl', jxmgrsec.add_doctor_permission_modal.Ctrl);
