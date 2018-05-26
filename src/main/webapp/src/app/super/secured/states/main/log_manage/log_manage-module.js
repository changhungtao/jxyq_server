/**
 * Created by zhanga.fnst on 2015/7/16.
 */
'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxsprsec.main.log_manage.module');


/**
 * Require controller.
 */
goog.require('jxsprsec.main.log_manage.Ctrl');


/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxsprsec.main.log_manage.module = angular.module('main.log_manage', [
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
//    'angular-md5'
]);



/** \
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxsprsec.main.log_manage.module.configuration = function($stateProvider) {

    $stateProvider.state('main.log_manage', {
        url: '/log_manage',
        templateUrl: 'states/main/log_manage/log_manage.html',
        controller: 'LogManageCtrl as logManageCtrl',
        resolve:{
            'USERROLESPromise' : function(constants){
                return constants.USERROLESPromise;
            },
            'OPERATIONSPromise' : function(constants){
                return constants.OPERATIONSPromise;
            }
        }
    });

};



/**
 * Init user center module.
 */
jxsprsec.main.log_manage.module
    .config(jxsprsec.main.log_manage.module.configuration)
    .controller('LogManageCtrl', jxsprsec.main.log_manage.Ctrl);
