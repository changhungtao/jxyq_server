'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmnfopn.sign_up.module');

/**
 * Require controller.
 */
goog.require('jxmnfopn.sign_up.Ctrl');


/**
 * Sign up module.
 *
 * @return {angular.Module}
 */
jxmnfopn.sign_up.module = angular.module('sign_up', [
  'ui.router',
  'ui.select',
  'ui.bootstrap',
  'ngSanitize',
  'angular-md5'
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmnfopn.sign_up.module.configuration = function($stateProvider) {

  $stateProvider.state('sign_up', {
    url: '/sign_up',
    templateUrl: 'states/sign_up/sign_up.html',
    controller: 'SignUpCtrl as signUp',
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
 * Init sign up module.
 */
jxmnfopn.sign_up.module
.config(jxmnfopn.sign_up.module.configuration)
.controller('SignUpCtrl', jxmnfopn.sign_up.Ctrl);
