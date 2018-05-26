'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.user_center.basics.module');

/**
 * Require controller.
 */
goog.require('jxdctsec.main.user_center.basics.Ctrl');



/**
 * Module for main.user_center.basics state.
 *
 * @return {angular.Module}
 */
jxdctsec.main.user_center.basics.module = angular.module('main.user_center.basics', [
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
jxdctsec.main.user_center.basics.module.configuration = function($stateProvider) {

  $stateProvider.state('main.user_center.basics', {
    url: '/basics',
    templateUrl: 'states/main/user_center/basics/basics.html',
    controller: 'BasicsCtrl as basics',
    resolve:{
     'GENDERSPromise' : function(constants){
          return constants.GENDERSPromise;
     }
    }
  });

};



/**
 * Init main.user_center.basics module.
 */
jxdctsec.main.user_center.basics.module
.config(jxdctsec.main.user_center.basics.module.configuration)
.controller('BasicsCtrl', jxdctsec.main.user_center.basics.Ctrl);
