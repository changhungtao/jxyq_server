'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.user.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.db_operation.user.Ctrl');
goog.require('jxmgrsec.show_user_modal.show_user_modal.Ctrl');
goog.require('jxmgrsec.modify_user_modal.modify_user_modal.Ctrl');

/**
 * Module for main.db_operation.user state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.db_operation.user.module = angular.module('main.db_operation.user', [
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
jxmgrsec.main.db_operation.user.module.configuration = function($stateProvider) {

    $stateProvider.state('main.db_operation.user', {
        url: '/user',
        templateUrl: 'states/main/db_operation/user/user.html',
        controller: 'UserCtrl as user',
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
        }
    });

};



/**
 * Init main.db_operation.user module.
 */
jxmgrsec.main.db_operation.user.module
    .config(jxmgrsec.main.db_operation.user.module.configuration)
    .controller('UserCtrl', jxmgrsec.main.db_operation.user.Ctrl)
    .controller('ModifyUserModalCtrl', jxmgrsec.modify_user_modal.modify_user_modal.Ctrl)
    .controller('ShowUserModalCtrl', jxmgrsec.show_user_modal.show_user_modal.Ctrl);
