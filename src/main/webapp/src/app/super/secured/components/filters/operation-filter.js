/**
 * Created by zhanga.fnst on 2015/7/16.
 */
'use strict';

goog.provide('jxsprsec.operation.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.operation.Filter = function(constants) {
    this.gotOPERATIONS = constants.gotOPERATIONS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.operation.Filter.factory = function(constants) {
    var filter = new jxsprsec.operation.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.operation.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var operations = this.gotOPERATIONS();
        var operation = operations.filter(function(item){return input === item.op_title_id});
        return operation.length > 0 ? operation[0].op_title_name : '';
    }
};
