/**
 * Created by zhanga.fnst on 2015/6/24.
 */
'use strict';

goog.provide('jxdctsec.fileModel.Directive.factory');

/**
 * A directive that displays the current version.
 *
 * @constructor
 */
jxdctsec.fileModel.Directive = function($parse) {
//    this.version = version;
    this.parse = $parse;
    this.link = this.link.bind(this);
    /** @type {angular.Scope} */
    this.scope;
    /** @type {angular.JQLite} */
    this.elem;
    /** @type {angular.Attributes} */
    this.attrs;
};

/**
 * fileModel directive factory.
 *
 * @param {angular.Service} fileModel
 * @return {Object}
 * @ngInject
 */
//jxdctsec.fileModel.Directive.factory.$inject = [
//    "$parse"
//];
jxdctsec.fileModel.Directive.factory = function($parse) {
    var dir = new jxdctsec.fileModel.Directive($parse);
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
jxdctsec.fileModel.Directive.prototype.link = function(scope, elem, attrs) {

    this.scope = scope;
    this.elem = elem;
    this.attrs = attrs;
    this.restrict = 'A';

    var model = this.parse(attrs.fileModel);
    var modelSetter = model.assign;
    elem.bind('change', function(){
        scope.$apply(function(){
            modelSetter(scope, elem[0].files[0]);
        });
    });
};

