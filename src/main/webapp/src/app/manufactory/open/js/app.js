
'use strict';

goog.require('jxmnfopn.panel.module');
goog.require('jxmnfopn.sign_up.module');

goog.require('jxmnfopn.version.Directive.factory');
goog.require('jxmnfopn.fileModel.Directive.factory');

goog.require('jxmnfopn.version.Service');
goog.require('jxmnfopn.check.Filter.factory');
goog.require('jxmnfopn.basic.Service');
goog.require('jxmnfopn.constants.Factory');

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
  $httpProvider.defaults.withCredentials = true;

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
  jxmnfopn.panel.module.name,
  jxmnfopn.sign_up.module.name
])
.config(config)
.run(onChangeConfig)
.run(endPointConfig)
.directive('version', jxmnfopn.version.Directive.factory)
.directive('fileModel', jxmnfopn.fileModel.Directive.factory)
.service('version', jxmnfopn.version.Service)
.filter('check', jxmnfopn.check.Filter.factory)
.service('basic', jxmnfopn.basic.Service)
.factory('constants', jxmnfopn.constants.Factory);
