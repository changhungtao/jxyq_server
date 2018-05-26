/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxmnfsec.product_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfsec.product_type.Filter = function(constants) {
    this.gotPRODUCTTYPES = constants.gotPRODUCTTYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfsec.product_type.Filter.factory = function(constants) {
    var filter = new jxmnfsec.product_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfsec.product_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var product_types = this.gotPRODUCTTYPES();
        var product_type = product_types.filter(function(item){return input === item.product_type_id});
        return product_type.length > 0 ? product_type[0].product_type_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmnfsec.device_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfsec.device_type.Filter = function(constants) {
    this.gotDEVICETYPES = constants.gotDEVICETYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfsec.device_type.Filter.factory = function(constants) {
    var filter = new jxmnfsec.device_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfsec.device_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var device_types = this.gotDEVICETYPES();
        var device_type = device_types.filter(function(item){return input === item.device_type_id});
        return device_type.length > 0 ? device_type[0].device_type_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmnfsec.template_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfsec.template_type.Filter = function(constants) {
    this.gotTEMPLATETYPES = constants.gotTEMPLATETYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfsec.template_type.Filter.factory = function(constants) {
    var filter = new jxmnfsec.template_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfsec.template_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var template_types = this.gotTEMPLATETYPES();
        var template_type = template_types.filter(function(item){return input === item.template_type_id});
        return template_type.length > 0 ? template_type[0].template_type_name : '';
    }
};
