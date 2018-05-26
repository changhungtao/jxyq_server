'use strict';

goog.provide('jxmgrsec.version.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxmgrsec.version.Service = function() {
  this.version = '0.0.1';
};

/**
 * Return the current version.
 *
 * @export
 * @return {string}
 */
jxmgrsec.version.Service.prototype.get = function() {
  return this.version;
};
