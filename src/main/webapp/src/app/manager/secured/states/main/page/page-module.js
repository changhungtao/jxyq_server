'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.page.module');


goog.require('jxmgrsec.main.page.category_system_template.module');
goog.require('jxmgrsec.main.page.exhibition.module');
goog.require('jxmgrsec.main.page.news.module');
/**
 * Require controller.
 */
goog.require('jxmgrsec.main.page.Ctrl');

/**
 * Page module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.page.module = angular.module('main.page', [
  'ui.router',
  'ui.grid', 
  'ui.grid.pinning', 
  'ui.grid.resizeColumns', 
  'ui.grid.saveState',
  'ui.grid.edit',
  'ui.grid.pagination',
  jxmgrsec.main.page.category_system_template.module.name,
  jxmgrsec.main.page.exhibition.module.name,
  jxmgrsec.main.page.news.module.name
]);



/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.page.module.configuration = function($stateProvider) {

  $stateProvider.state('main.page', {
    url: '/page',
    templateUrl: 'states/main/page/page.html',
    controller: 'PageCtrl as page'
  });

};



/**
 * Init page module.
 */
jxmgrsec.main.page.module
.config(jxmgrsec.main.page.module.configuration)
.controller('PageCtrl', jxmgrsec.main.page.Ctrl);
