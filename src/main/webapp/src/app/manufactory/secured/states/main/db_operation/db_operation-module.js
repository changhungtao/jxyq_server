'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmnfsec.main.db_operation.module');


/**
 * Require controller.
 */
goog.require('jxmnfsec.main.db_operation.Ctrl');



/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxmnfsec.main.db_operation.module = angular.module('main.db_operation', [
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
jxmnfsec.main.db_operation.module.configuration = function($stateProvider) {

    $stateProvider.state('main.db_operation', {
        url: '/db_operation',
        templateUrl: 'states/main/db_operation/db_operation.html',
        controller: 'DbOperationCtrl as dbOperation',
        resolve: {
            //使用前保证数据表加载完毕
            'DEVICETYPESPromise': function(constants) {
                return constants.DEVICETYPESPromise;
            },
            'TERMINALSTATUSPromise': function(constants) {
                return constants.TERMINALSTATUSPromise;
            }
        }
    });

};



/**
 * Init user center module.
 */
jxmnfsec.main.db_operation.module
    .config(jxmnfsec.main.db_operation.module.configuration)
    .controller('DbOperationCtrl', jxmnfsec.main.db_operation.Ctrl);
