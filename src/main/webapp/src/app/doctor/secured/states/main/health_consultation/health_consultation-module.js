'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxdctsec.main.health_consultation.module');


/**
 * Require controller.
 */
goog.require('jxdctsec.main.health_consultation.Ctrl');
goog.require('jxdctsec.health_report.health_con_modal.Ctrl');



/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxdctsec.main.health_consultation.module = angular.module('main.health_consultation', [
  'ui.router',
  'ui.select',
  'ui.bootstrap',
  'ngSanitize',
  'irontec.simpleChat',
  'com.2fdevs.videogular',
  'com.2fdevs.videogular.plugins.controls',
  'ngAudio'
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxdctsec.main.health_consultation.module.configuration = function($stateProvider) {

  $stateProvider.state('main.health_consultation', {
    url: '/health_consultation',
    templateUrl: 'states/main/health_consultation/health_consultation.html',
    controller: 'HealthConsultationCtrl as healthconsultation',
    resolve: {
      'DISTRICTSPromise': function(constants) {
        return constants.DISTRICTSPromise;
      },
      'HEALTHCONSULTATIONSTATUSPromise': function(constants) {
        return constants.HEALTHCONSULTATIONSTATUSPromise;
      },
      'DEPARTMENTSPromise': function(constants){
        return constants.DEPARTMENTSPromise;
      }
    }
  });

};


/**
 * Init health consultation module.
 */
jxdctsec.main.health_consultation.module
  .config(jxdctsec.main.health_consultation.module.configuration)
  .controller('HealthConsultationCtrl', jxdctsec.main.health_consultation.Ctrl)
  .controller('HealthConModalCtrl', jxdctsec.health_report.health_con_modal.Ctrl);
