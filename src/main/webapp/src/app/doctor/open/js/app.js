
'use strict';

goog.require('jxdctopn.panel.module');
goog.require('jxdctopn.sign_up.module');

goog.require('jxdctopn.version.Directive.factory');
goog.require('jxdctsec.fileModel.Directive.factory');

goog.require('jxdctopn.version.Service');
goog.require('jxdctopn.check.Filter.factory');
goog.require('jxdctopn.basic.Service');
goog.require('jxdctopn.constants.Factory');

/**
 * Configuration function.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @param {ui.router.$urlRouterProvider} $urlRouterProvider
 * @ngInject
 */
function config($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {

  $urlRouterProvider.otherwise('/panel/sign_in');
  $httpProvider.defaults.withCredentials = true;

  //initialize get if not there
  if (!$httpProvider.defaults.headers.get) {
    $httpProvider.defaults.headers.get = {};    
  }    
  //disable IE ajax request caching
  $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
  // extra
  $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
  $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';

  //================================================
  // Add an interceptor for AJAX errors
  //================================================
  $httpProvider.interceptors.push(function($q, $location) {
    return {
      response: function(response) {
        // do something on success
        return response;
      },
      responseError: function(response) {
        if (response.status === 401)
          $location.url('/panel/sign_in');
        return $q.reject(response);
      }
    };
  });

}

var onChangeConfig = ['$rootScope', '$state',
 function ($rootScope, $state) {

  $rootScope.$on('$stateChangeStart', function (event, toState) {    
    if (toState.name === "panel") { 
      event.preventDefault();
      $state.go('panel.sign_in', {});
    }
  });
  
}];

var endPointConfig = ['$rootScope',
 function ($rootScope, $state) {

  $rootScope.endPoint = '';

}];

/**
 * Main app.
 */
angular.module('app', [
  'ui.router',
  'ngResource',
  jxdctopn.panel.module.name,
  jxdctopn.sign_up.module.name
])
.config(config)
.run(onChangeConfig)
.run(endPointConfig)
.directive('version', jxdctopn.version.Directive.factory)
.directive('fileModel', jxdctsec.fileModel.Directive.factory)
.service('version', jxdctopn.version.Service)
.filter('check', jxdctopn.check.Filter.factory)
.service('basic', jxdctopn.basic.Service)
.factory('constants', jxdctopn.constants.Factory);
