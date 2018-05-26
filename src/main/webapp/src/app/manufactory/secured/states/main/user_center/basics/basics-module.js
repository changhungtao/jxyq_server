'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmnfsec.main.user_center.basics.module');

/**
 * Require controller.
 */
goog.require('jxmnfsec.main.user_center.basics.Ctrl');



/**
 * Module for user_center.basics state.
 *
 * @return {angular.Module}
 */
jxmnfsec.main.user_center.basics.module = angular.module('main.user_center.basics', [
  'ui.router'
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
jxmnfsec.main.user_center.basics.module.configuration = function($stateProvider) {

  $stateProvider.state('main.user_center.basics', {
    url: '/basics',
    templateUrl: 'states/main/user_center/basics/basics.html',
    controller: 'BasicsCtrl as basics',
    resolve: {
      'COMPANYDEPARTMENTSPromise': function(constants){
        return constants.COMPANYDEPARTMENTSPromise;
      },
      'DATATYPESPromise': function(constants){
        return constants.DATATYPESPromise;
      },
      'COMPANYMEMBERSPromise': function(constants){
        return constants.COMPANYMEMBERSPromise;
      },
      'COMPANYNATURESPromise': function(constants){
        return constants.COMPANYNATURESPromise;
      },
      'COMPANYINDUSTRIESPromise': function(constants){
        return constants.COMPANYINDUSTRIESPromise;
      },
      'PROVINCESPromise': function(constants){
        return constants.PROVINCESPromise;
      },
      'CITIESPromise': function(constants){
        return constants.CITIESPromise;
      },
      'ZONESPromise': function(constants){
        return constants.ZONESPromise;
      }
    }
  });

};



/**
 * Init user_center.basics module.
 */
jxmnfsec.main.user_center.basics.module
.config(jxmnfsec.main.user_center.basics.module.configuration)
.controller('BasicsCtrl', jxmnfsec.main.user_center.basics.Ctrl);
