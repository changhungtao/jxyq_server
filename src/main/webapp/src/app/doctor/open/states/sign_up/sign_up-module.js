'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctopn.sign_up.module');

/**
 * Require controller.
 */
goog.require('jxdctopn.sign_up.Ctrl');



/**
 * Sign up module.
 *
 * @return {angular.Module}
 */
jxdctopn.sign_up.module = angular.module('sign_up', [
  'ui.router',
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
jxdctopn.sign_up.module.configuration = function($stateProvider) {

  $stateProvider.state('sign_up', {
    url: '/sign_up',
    templateUrl: 'states/sign_up/sign_up.html',
    controller: 'SignUpCtrl as signUp',
    resolve: {
      'DISTRICTSPromise': function(constants){
        return constants.DISTRICTSPromise;
      },
      'DEPARTMENTSPromise': function(constants){
        return constants.DEPARTMENTSPromise;
      },
      'GENDERSPromise': function(constants){
        return constants.GENDERSPromise;
      }
    }
  });

};



/**
 * Init sign up module.
 */
jxdctopn.sign_up.module
.config(jxdctopn.sign_up.module.configuration)
.controller('SignUpCtrl', jxdctopn.sign_up.Ctrl);
