'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.number.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.twatch.number.Ctrl');
goog.require('jxmgrsec.modify_number_modal.Ctrl');


/**
 * Module for main.twatch.number state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.twatch.number.module = angular.module('main.twatch.number', [
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
jxmgrsec.main.twatch.number.module.configuration = function($stateProvider) {

  $stateProvider.state('main.twatch.number', {
    url: '/number',
    templateUrl: 'states/main/twatch/number/number.html',
    controller: 'NumberCtrl as number',
    resolve: {
      'SOSNAMESPromise': function(constants){
        return constants.SOSNAMESPromise;
      },
      'QINGNAMESPromise': function(constants){
        return constants.QINGNAMESPromise;
      }
    }
  });

};



/**
 * Init main.twatch.number module.
 */
jxmgrsec.main.twatch.number.module
.config(jxmgrsec.main.twatch.number.module.configuration)
.controller('NumberCtrl', jxmgrsec.main.twatch.number.Ctrl)
.controller('ModifyNumberModalCtrl', jxmgrsec.modify_number_modal.Ctrl);
