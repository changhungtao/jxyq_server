'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.page.news.module');


/**
 * Require controller.
 */
goog.require('jxmgrsec.main.page.news.Ctrl');
goog.require('jxmgrsec.edit_news_modal.edit_news_modal.Ctrl');
goog.require('jxmgrsec.add_news_modal.Ctrl');



/**
 * News module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.page.news.module = angular.module('main.page.news', [
  'ui.router',
  'ui.grid', 
  'ui.grid.pinning', 
  'ui.grid.resizeColumns', 
  'ui.grid.saveState',
  'ui.grid.edit',
  'ui.grid.pagination',
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
jxmgrsec.main.page.news.module.configuration = function($stateProvider) {

  $stateProvider.state('main.page.news', {
    url: '/news',
    templateUrl: 'states/main/page/news/news.html',
    controller: 'NewsCtrl as news'
  });

};



/**
 * Init news module.
 */
jxmgrsec.main.page.news.module
.config(jxmgrsec.main.page.news.module.configuration)
.controller('NewsCtrl', jxmgrsec.main.page.news.Ctrl)
.controller('EditNewsModalCtrl', jxmgrsec.edit_news_modal.edit_news_modal.Ctrl)
.controller('AddNewsModalCtrl', jxmgrsec.add_news_modal.Ctrl)
;
