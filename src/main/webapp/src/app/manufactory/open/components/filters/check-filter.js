'use strict';

goog.provide('jxmnfopn.check.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfopn.check.Filter = function() {
  this.checkmark = '\u2714';
  this.cross = '\u2718';
  this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfopn.check.Filter.factory = function() {
  var filter = new jxmnfopn.check.Filter();
  return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfopn.check.Filter.prototype.convert = function(input) {
  return input ? this.checkmark : this.cross;
};
