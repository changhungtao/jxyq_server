/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxsprsec.user_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.user_status.Filter = function(constants) {
    this.gotUSERSTATUS = constants.gotUSERSTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.user_status.Filter.factory = function(constants) {
    var filter = new jxsprsec.user_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.user_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var user_statuses = this.gotUSERSTATUS();
        var user_status = user_statuses.filter(function(item){return input === item.user_status_id});
        return user_status.length > 0 ? user_status[0].user_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.manufactory_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.manufactory_status.Filter = function(constants) {
    this.gotMANUFACTORYSTATUS = constants.gotMANUFACTORYSTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.manufactory_status.Filter.factory = function(constants) {
    var filter = new jxsprsec.manufactory_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.manufactory_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var manufactory_statuses = this.gotMANUFACTORYSTATUS();
        var manufactory_status = manufactory_statuses.filter(function(item){return input === item.manufactory_status_id});
        return manufactory_status.length > 0 ? manufactory_status[0].manufactory_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.terminal_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.terminal_status.Filter = function(constants) {
    this.gotTERMINALSTATUS = constants.gotTERMINALSTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.terminal_status.Filter.factory = function(constants) {
    var filter = new jxsprsec.terminal_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.terminal_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var terminal_statuses = this.gotTERMINALSTATUS();
        var terminal_status = terminal_statuses.filter(function(item){return input === item.terminal_status_id});
        return terminal_status.length > 0 ? terminal_status[0].terminal_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.doctor_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.doctor_status.Filter = function(constants) {
    this.gotDOCTORSTATUS = constants.gotDOCTORSTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.doctor_status.Filter.factory = function(constants) {
    var filter = new jxsprsec.doctor_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.doctor_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var doctor_statuses = this.gotDOCTORSTATUS();
        var doctor_status = doctor_statuses.filter(function(item){return input === item.doctor_status_id});
        return doctor_status.length > 0 ? doctor_status[0].doctor_status_name : '';
    }
};

