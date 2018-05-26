'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.health_data.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.health_data.Ctrl');
goog.require('jxmgrsec.health_data_modals.fat_modal.Ctrl');
goog.require('jxmgrsec.health_data_modals.glucosemeter_modal.Ctrl');
goog.require('jxmgrsec.health_data_modals.oximeter_modal.Ctrl');
goog.require('jxmgrsec.health_data_modals.sphygmomanometer_modal.Ctrl');
goog.require('jxmgrsec.health_data_modals.thermometer_modal.Ctrl');
goog.require('jxmgrsec.health_data_modals.wristband_modal.Ctrl');


/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.health_data.module = angular.module('main.health_data', [
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
jxmgrsec.main.health_data.module.configuration = function($stateProvider) {

  $stateProvider.state('main.health_data', {
    url: '/health_data',
    templateUrl: 'states/main/health_data/health_data.html',
    controller: 'HealthDataCtrl as healthData',
    resolve:{
//          'DEVICETYPESPromise':function(constants){
//              return constants.gotDEVICETYPES;
//          },
          'HEALTHDATASTATUSPromise':function(constants){
              return constants.HEALTHDATASTATUSPromise;
          },
          'DATATYPESPromise':function(constants){
              return constants.gotDATATYPES;
          },
          'PERIODSPromise':function(constants){
              return constants.PERIODSPromise;
          }
      }
  });

};



/**
 * Init health file module.
 */
jxmgrsec.main.health_data.module
.config(jxmgrsec.main.health_data.module.configuration)
.controller('HealthDataCtrl', jxmgrsec.main.health_data.Ctrl)
.controller('FatModalCtrl', jxmgrsec.health_data_modals.fat_modal.Ctrl)
.controller('GlucosemeterModalCtrl', jxmgrsec.health_data_modals.glucosemeter_modal.Ctrl)
.controller('OximeterModalCtrl', jxmgrsec.health_data_modals.oximeter_modal.Ctrl)
.controller('SphygmomanometerModalCtrl', jxmgrsec.health_data_modals.sphygmomanometer_modal.Ctrl)
.controller('ThermometerModalCtrl', jxmgrsec.health_data_modals.thermometer_modal.Ctrl)
.controller('WristbandModalCtrl', jxmgrsec.health_data_modals.wristband_modal.Ctrl)
;
