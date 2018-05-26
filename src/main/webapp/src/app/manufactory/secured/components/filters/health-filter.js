/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxmnfsec.data_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfsec.data_type.Filter = function(constants) {
    this.gotDATATYPES = constants.gotDATATYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfsec.data_type.Filter.factory = function(constants) {
    var filter = new jxmnfsec.data_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfsec.data_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var data_types = this.gotDATATYPES();
        var data_type = data_types.filter(function(item){return input === item.data_type_id});
        return data_type.length > 0 ? data_type[0].data_type_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmnfsec.periods.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfsec.periods.Filter = function(constants) {
    this.gotPERIODS = constants.gotPERIODS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfsec.periods.Filter.factory = function(constants) {
    var filter = new jxmnfsec.periods.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfsec.periods.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var periods = this.gotPERIODS();
        var period = periods.filter(function(item){return input === item.period_id});
        return period.length > 0 ? period[0].period_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmnfsec.health_data_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfsec.health_data_status.Filter = function(constants) {
    this.gotHEALTHDATASTATUS = constants.gotHEALTHDATASTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfsec.health_data_status.Filter.factory = function(constants) {
    var filter = new jxmnfsec.health_data_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfsec.health_data_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var health_data_statuses = this.gotHEALTHDATASTATUS();
        var health_data_status = health_data_statuses.filter(function(item){return input === item.data_status_id});
        return health_data_status.length > 0 ? health_data_status[0].data_status_name : '';
    }
};

