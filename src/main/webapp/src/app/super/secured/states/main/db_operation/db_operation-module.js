'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxsprsec.main.db_operation.module');


/**
 * Require controller.
 */
goog.require('jxsprsec.main.db_operation.Ctrl');
goog.require('jxsprsec.add_manager_modal.add_manager_modal.Ctrl');
goog.require('jxsprsec.modify_manager_modal.modify_manager_modal.Ctrl');
goog.require('jxsprsec.setRight_manager_modal.setRight_manager_modal.Ctrl');


/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxsprsec.main.db_operation.module = angular.module('main.db_operation', [
  'ui.router',
  'ui.grid', 
  'ui.grid.pinning', 
  'ui.grid.resizeColumns', 
  'ui.grid.saveState',
  'ui.grid.edit',
  'ui.grid.pagination',
  'ui.select',
  'ui.bootstrap',
  'ngSanitize',
  'angularModalService',
  'angular-md5'
]);



/** \
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxsprsec.main.db_operation.module.configuration = function($stateProvider) {

  $stateProvider.state('main.db_operation', {
    url: '/db_operation',
    templateUrl: 'states/main/db_operation/db_operation.html',
    controller: 'DbOperationCtrl as dbOperation',
    resolve:{
         'GENDERSPromise' : function(constants){
              return constants.GENDERSPromise;
         },
         'DISTRICTSPromise' : function(constants){
             return constants.DISTRICTSPromise;
         },
        'PROVINCESPromise' : function(constants){
            return constants.PROVINCESPromise;
        },
        'CITIESPromise' : function(constants){
            return constants.CITIESPromise;
        },
        'ZONESPromise' : function(constants){
            return constants.ZONESPromise;
        },
        'DEVICETYPESPromise' : function(constants){
            return constants.DEVICETYPESPromise;
        }
    }
  });

};



/**
 * Init user center module.
 */
jxsprsec.main.db_operation.module
.config(jxsprsec.main.db_operation.module.configuration)
.controller('DbOperationCtrl', jxsprsec.main.db_operation.Ctrl)
.controller('AddManagerModalCtrl', jxsprsec.add_manager_modal.add_manager_modal.Ctrl)
.controller('ModifyManagerModalCtrl', jxsprsec.modify_manager_modal.modify_manager_modal.Ctrl)
.controller('SetRightManagerModalCtrl', jxsprsec.setRight_manager_modal.setRight_manager_modal.Ctrl);
