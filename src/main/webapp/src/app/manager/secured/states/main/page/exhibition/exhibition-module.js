'use strict';

/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.page.exhibition.module');


/**
 * Require controller.
 */
goog.require('jxmgrsec.main.page.exhibition.Ctrl');
goog.require('jxmgrsec.edit_exhi_address_modal.edit_exhi_address_modal.Ctrl');
goog.require('jxmgrsec.add_page_modal.Ctrl');


/**
 * Exhibition module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.page.exhibition.module = angular.module('main.page.exhibition', [
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
 * `templateUrl` path must be relative to `insdex.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.page.exhibition.module.configuration = function($stateProvider) {

  $stateProvider.state('main.page.exhibition', {
    url: '/exhibition',
    templateUrl: 'states/main/page/exhibition/exhibition.html',
    controller: 'ExhibitionCtrl as exhibition'
  });

};



/**
 * Init exhibition module.
 */
jxmgrsec.main.page.exhibition.module
.config(jxmgrsec.main.page.exhibition.module.configuration)
.controller('ExhibitionCtrl', jxmgrsec.main.page.exhibition.Ctrl)
.controller('EditExhiAddressModalCtrl', jxmgrsec.edit_exhi_address_modal.edit_exhi_address_modal.Ctrl)
.controller('AddPageModalCtrl', jxmgrsec.add_page_modal.Ctrl)
;
