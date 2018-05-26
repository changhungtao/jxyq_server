'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmnfsec.main.health_data.module');


/**
 * Require controller.
 */
goog.require('jxmnfsec.main.health_data.Ctrl');


/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxmnfsec.main.health_data.module = angular.module('main.health_data', [
  'ui.router',
  'ui.grid', 
  'ui.grid.pinning', 
  'ui.grid.resizeColumns', 
  'ui.grid.saveState',
  'ui.grid.edit',
  'ui.grid.pagination'
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmnfsec.main.health_data.module.configuration = function($stateProvider) {

  $stateProvider.state('main.health_data', {
    url: '/health_data',
    templateUrl: 'states/main/health_data/health_data.html',
    controller: 'HealthDataCtrl as healthData',
    resolve:{
        'DATATYPESPromise':function(constants){
            return constants.DATATYPESPromise;
        },
        'PERIODSPromise':function(constants){
            return constants.PERIODSPromise;
        },
        'HEALTHDATASTATUSPromise':function(constants){
            return constants.HEALTHDATASTATUSPromise
        }
    }
  });

};



/**
 * Init health data module.
 */
jxmnfsec.main.health_data.module
.config(jxmnfsec.main.health_data.module.configuration)
.controller('HealthDataCtrl', jxmnfsec.main.health_data.Ctrl);
