/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxdctsec.product_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxdctsec.product_type.Filter = function(constants) {
    this.gotPRODUCTTYPES = constants.gotPRODUCTTYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxdctsec.product_type.Filter.factory = function(constants) {
    var filter = new jxdctsec.product_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxdctsec.product_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var product_types = this.gotPRODUCTTYPES();
        var product_type = product_types.filter(function(item){return input === item.product_type_id});
        return product_type.length > 0 ? product_type[0].product_type_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxdctsec.device_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxdctsec.device_type.Filter = function(constants) {
    this.gotDEVICETYPES = constants.gotDEVICETYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxdctsec.device_type.Filter.factory = function(constants) {
    var filter = new jxdctsec.device_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxdctsec.device_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var device_types = this.gotDEVICETYPES();
        var device_type = device_types.filter(function(item){return input === item.device_type_id});
        return device_type.length > 0 ? device_type[0].device_type_name : '';
    }
};
