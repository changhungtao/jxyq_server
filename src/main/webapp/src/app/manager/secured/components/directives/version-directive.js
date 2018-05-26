'use strict';

goog.provide('jxmgrsec.version.Directive.factory');

/**
 * A directive that displays the current version.
 *
 * @constructor
 */
jxmgrsec.version.Directive = function(version) {
  this.version = version;
  this.link = this.link.bind(this);
  /** @type {angular.Scope} */
  this.scope;
  /** @type {angular.JQLite} */
  this.elem;
  /** @type {angular.Attributes} */
  this.attrs;
};

/**
 * Version directive factory.
 *
 * @param {angular.Service} version
 * @return {Object}
 * @ngInject
 */
jxmgrsec.version.Directive.factory = function(version) {
  var dir = new jxmgrsec.version.Directive(version);
  return {
    link: dir.link
  };
};

/**
 * Linking function.
 *
 * @param {angular.Scope} scope
 * @param {angular.JQLite} elem
 * @param {angular.Attributes} attrs
 */
jxmgrsec.version.Directive.prototype.link = function(scope, elem, attrs) {

  this.scope = scope;
  this.elem = elem;
  this.attrs = attrs;

  this.elem.text(this.version.get());

};
