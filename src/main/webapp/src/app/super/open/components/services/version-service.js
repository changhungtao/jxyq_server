'use strict';

goog.provide('jxspropn.version.Service');

/**
 * Version service. In the angular-seed this is a 'value service' but I wanted
 * to show how to use services as JS classes. In the AngularJS world
 * 'service services' are classes.
 *
 * @constructor
 */
jxspropn.version.Service = function() {
  this.version = '0.0.1';
};

/**
 * Return the current version.
 *
 * @export
 * @return {string}
 */
jxspropn.version.Service.prototype.get = function() {
  return this.version;
};
