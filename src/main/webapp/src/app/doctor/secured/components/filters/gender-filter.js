/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxdctsec.gender.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxdctsec.gender.Filter = function(constants) {
    this.gotGENDERS = constants.gotGENDERS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxdctsec.gender.Filter.factory = function(constants) {
    var filter = new jxdctsec.gender.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxdctsec.gender.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var genders = this.gotGENDERS();
        var gender = genders.filter(function(item){return input === item.gender_id});
        return gender.length > 0 ? gender[0].gender_name : '';
    }
};
