'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.page.category_system_template.module');


/**
 * Require controller.
 */
goog.require('jxmgrsec.main.page.category_system_template.Ctrl');
goog.require('jxmgrsec.show_cs_template_modal.show_cs_template_modal.Ctrl');
goog.require('jxmgrsec.add_template_modal.Ctrl');



/**
 * Category system template module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.page.category_system_template.module = angular.module('main.page.category_system_template', [
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
jxmgrsec.main.page.category_system_template.module.configuration = function($stateProvider) {

  $stateProvider.state('main.page.category_system_template', {
    url: '/category_system_template',
    templateUrl: 'states/main/page/category_system_template/category_system_template.html',
    controller: 'CategorySystemTemplateCtrl as categorySystemTemplate'
  });

};



/**
 * Init category system template module.
 */
jxmgrsec.main.page.category_system_template.module
.config(jxmgrsec.main.page.category_system_template.module.configuration)
.controller('CategorySystemTemplateCtrl', jxmgrsec.main.page.category_system_template.Ctrl)
.controller('ShowCSTemplateModalCtrl', jxmgrsec.show_cs_template_modal.show_cs_template_modal.Ctrl)
.controller('AddTemplateModalCtrl', jxmgrsec.add_template_modal.Ctrl)
;
