'use strict';
/**
 * Create namespace for `app.js`.
 */
goog.provide('jxmgrsec.main.twatch.module');
/**
 * Require child states.
 */
goog.require('jxmgrsec.main.twatch.number.module');
goog.require('jxmgrsec.main.twatch.mode.module');
goog.require('jxmgrsec.main.twatch.alarm.module');
goog.require('jxmgrsec.main.twatch.route_setting.module');
goog.require('jxmgrsec.main.twatch.pen.module');
goog.require('jxmgrsec.main.twatch.loc.module');
goog.require('jxmgrsec.main.twatch.route.module');
goog.require('jxmgrsec.main.twatch.command.module');
/**
 * Require controller.
 */
goog.require('jxmgrsec.main.twatch.Ctrl');

/**
 * Twatch module.
 *
 *
 * @return {angular.Module}
 */
jxmgrsec.main.twatch.module = angular.module('main.twatch', [
    'ui.router',
    jxmgrsec.main.twatch.number.module.name,
    jxmgrsec.main.twatch.mode.module.name,
    jxmgrsec.main.twatch.alarm.module.name,
    jxmgrsec.main.twatch.route_setting.module.name,
    jxmgrsec.main.twatch.pen.module.name,
    jxmgrsec.main.twatch.loc.module.name,
    jxmgrsec.main.twatch.route.module.name,
    jxmgrsec.main.twatch.command.module.name
]);

/**
 * Configuration function.
 *
 * `templateUrl` path must be relative to `index.html`.
 *
 * @param {ui.router.$stateProvider} $stateProvider
 * @ngInject
 */
jxmgrsec.main.twatch.module.configuration = function($stateProvider) {
    $stateProvider.state('main.twatch', {
        url: '/twatch',
        templateUrl: 'states/main/twatch/twatch.html',
        controller: 'TwatchCtrl as twatch'
    });
};
/**
 * Init twatch module.
 */
jxmgrsec.main.twatch.module.config(jxmgrsec.main.twatch.module.configuration)
.controller('TwatchCtrl', jxmgrsec.main.twatch.Ctrl);
