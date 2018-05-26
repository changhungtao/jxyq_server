/**
 * Created by zhanga.fnst on 2015/7/16.
 */
'use strict';

goog.provide('jxsprsec.user_role.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.user_role.Filter = function(constants) {
    this.gotUSERROLES = constants.gotUSERROLES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.user_role.Filter.factory = function(constants) {
    var filter = new jxsprsec.user_role.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.user_role.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var user_roles = this.gotUSERROLES();
        var user_role = user_roles.filter(function(item){return input === item.user_role_id});
        return user_role.length > 0 ? user_role[0].user_role_name : '';
    }
};

