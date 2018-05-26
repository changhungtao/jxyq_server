'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.home_security.button.module');


/**
 * Require controller.
 */
goog.require('jxmgrsec.main.home_security.button.Ctrl');
goog.require('jxmgrsec.show_touch_button_modal.show_touch_button_modal.Ctrl');



/**
 * Button module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.home_security.button.module = angular.module('main.home_security.button', [
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
  'angularModalService',
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.home_security.button.module.configuration = function($stateProvider) {

  $stateProvider.state('main.home_security.button', {
    url: '/button',
    templateUrl: 'states/main/home_security/button/button.html',
    controller: 'ButtonCtrl as button'
  });

};



/**
 * Init button module.
 */
jxmgrsec.main.home_security.button.module
.config(jxmgrsec.main.home_security.button.module.configuration)
.controller('ButtonCtrl', jxmgrsec.main.home_security.button.Ctrl)
.controller('ShowTouchButtonModalCtrl', jxmgrsec.show_touch_button_modal.show_touch_button_modal.Ctrl);

