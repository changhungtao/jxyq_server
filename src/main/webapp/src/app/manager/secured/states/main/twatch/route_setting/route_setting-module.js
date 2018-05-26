'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.route_setting.module');

/**
 * Require controller.
 */
goog.require('jxmgrsec.main.twatch.route_setting.Ctrl');
goog.require('jxmgrsec.add_route_period_modal.Ctrl');
goog.require('jxmgrsec.modify_route_period_modal.Ctrl');


/**
 * Module for main.twatch.route_setting state.
 *
 * @return {angular.Module}
 */
jxmgrsec.main.twatch.route_setting.module = angular.module('main.twatch.route_setting', [
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
jxmgrsec.main.twatch.route_setting.module.configuration = function($stateProvider) {

  $stateProvider.state('main.twatch.route_setting', {
    url: '/route_setting',
    templateUrl: 'states/main/twatch/route_setting/route_setting.html',
    controller: 'RouteSettingCtrl as routeSetting',
    resolve: {
      'GPSMODESPromise': function(constants){
        return constants.GPSMODESPromise;
      },
      'ROUTESTATESPromise': function(constants){
        return constants.ROUTESTATESPromise;
      }
    }
  });

};



/**
 * Init main.twatch.route_setting module.
 */
jxmgrsec.main.twatch.route_setting.module
.config(jxmgrsec.main.twatch.route_setting.module.configuration)
.controller('RouteSettingCtrl', jxmgrsec.main.twatch.route_setting.Ctrl)
.controller('AddRoutePeriodModal', jxmgrsec.add_route_period_modal.Ctrl)
.controller('ModifyRoutePeriodModal', jxmgrsec.modify_route_period_modal.Ctrl);
