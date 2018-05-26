'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxdctsec.main.health_file.module');

/**
 * Require controller.
 */
goog.require('jxdctsec.main.health_file.Ctrl');
goog.require('jxdctsec.add_patient_modal.add_patient_modal.Ctrl');
goog.require('jxdctsec.edit_patient_modal.edit_patient_modal.Ctrl');
goog.require('jxdctsec.add_file_modal.add_file_modal.Ctrl');

/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxdctsec.main.health_file.module = angular.module('main.health_file', [
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
  'angularModalService'
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxdctsec.main.health_file.module.configuration = function($stateProvider) {

  $stateProvider.state('main.health_file', {
    url: '/health_file',
    templateUrl: 'states/main/health_file/health_file.html',
    controller: 'HealthFileCtrl as healthFile',
    resolve: {
      'GENDERSPromise': function(constants){
        return constants.GENDERSPromise;
      }
    }
  });

};



/**
 * Init health file module.
 */
jxdctsec.main.health_file.module
.config(jxdctsec.main.health_file.module.configuration)
.controller('AddPatientModalCtrl', jxdctsec.add_patient_modal.add_patient_modal.Ctrl)
.controller('EditPatientModalCtrl', jxdctsec.edit_patient_modal.edit_patient_modal.Ctrl)
.controller('AddFileModalCtrl', jxdctsec.add_file_modal.add_file_modal.Ctrl)
.controller('HealthFileCtrl', jxdctsec.main.health_file.Ctrl);
