/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxmgrsec.user_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.user_status.Filter = function(constants) {
    this.gotUSERSTATUS = constants.gotUSERSTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.user_status.Filter.factory = function(constants) {
    var filter = new jxmgrsec.user_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.user_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var user_statuses = this.gotUSERSTATUS();
        var user_status = user_statuses.filter(function(item){return input === item.user_status_id});
        return user_status.length > 0 ? user_status[0].user_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.manufactory_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.manufactory_status.Filter = function(constants) {
    this.gotMANUFACTORYSTATUS = constants.gotMANUFACTORYSTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.manufactory_status.Filter.factory = function(constants) {
    var filter = new jxmgrsec.manufactory_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.manufactory_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var manufactory_statuses = this.gotMANUFACTORYSTATUS();
        var manufactory_status = manufactory_statuses.filter(function(item){return input === item.manufactory_status_id});
        return manufactory_status.length > 0 ? manufactory_status[0].manufactory_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.terminal_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.terminal_status.Filter = function(constants) {
    this.gotTERMINALSTATUS = constants.gotTERMINALSTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.terminal_status.Filter.factory = function(constants) {
    var filter = new jxmgrsec.terminal_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.terminal_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var terminal_statuses = this.gotTERMINALSTATUS();
        var terminal_status = terminal_statuses.filter(function(item){return input === item.terminal_status_id});
        return terminal_status.length > 0 ? terminal_status[0].terminal_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.doctor_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.doctor_status.Filter = function(constants) {
    this.gotDOCTORSTATUS = constants.gotDOCTORSTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.doctor_status.Filter.factory = function(constants) {
    var filter = new jxmgrsec.doctor_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.doctor_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var doctor_statuses = this.gotDOCTORSTATUS();
        var doctor_status = doctor_statuses.filter(function(item){return input === item.doctor_status_id});
        return doctor_status.length > 0 ? doctor_status[0].doctor_status_name : '';
    }
};

