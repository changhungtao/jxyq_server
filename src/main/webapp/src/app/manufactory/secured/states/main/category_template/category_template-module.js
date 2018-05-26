'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmnfsec.main.category_template.module');


/**
 * Require controller.
 */
goog.require('jxmnfsec.main.category_template.Ctrl');
goog.require('jxmnfsec.show_cs_template_modal.show_cs_template_modal.Ctrl');


/**
 * Third module.
 *
 *
 * @return {angular.Module}
 */
jxmnfsec.main.category_template.module = angular.module('main.category_template', [
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
jxmnfsec.main.category_template.module.configuration = function($stateProvider) {

  $stateProvider.state('main.category_template', {
    url: '/category_template',
    templateUrl: 'states/main/category_template/category_template.html',
    controller: 'CategoryTemplateCtrl as categoryTemplate',
    resolve: {
      'TEMPLATETYPESPromise': function(constants) {
        return constants.TEMPLATETYPESPromise;
      },
      'DEVICETYPESPromise': function(constants) {
        return constants.DEVICETYPESPromise;
      }
    }
  });

};



/**
 * Init category template module.
 */
jxmnfsec.main.category_template.module
.config(jxmnfsec.main.category_template.module.configuration)
.controller('CategoryTemplateCtrl', jxmnfsec.main.category_template.Ctrl)
.controller('ShowCSTemplateModalCtrl', jxmnfsec.show_cs_template_modal.show_cs_template_modal.Ctrl)
