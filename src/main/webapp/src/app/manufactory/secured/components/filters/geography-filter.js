/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxmnfsec.district.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfsec.district.Filter = function(constants) {
    this.gotDISTRICTS = constants.gotDISTRICTS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfsec.district.Filter.factory = function(constants) {
    var filter = new jxmnfsec.district.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfsec.district.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var districts = this.gotDISTRICTS();
        var district = districts.filter(function(item){return input === item.district_id});
        return district.length > 0 ? district[0].district_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmnfsec.province.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfsec.province.Filter = function(constants) {
    this.gotPROVINCES = constants.gotPROVINCES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfsec.province.Filter.factory = function(constants) {
    var filter = new jxmnfsec.province.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfsec.province.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var provinces = this.gotPROVINCES();
        var province = provinces.filter(function(item){return input === item.province_id});
        return province.length > 0 ? province[0].province_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmnfsec.city.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfsec.city.Filter = function(constants) {
    this.gotCITIES= constants.gotCITIES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfsec.city.Filter.factory = function(constants) {
    var filter = new jxmnfsec.city.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfsec.city.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var cities = this.gotCITIES();
        var city = cities.filter(function(item){return input === item.city_id});
        return city.length > 0 ? city[0].city_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmnfsec.zone.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmnfsec.zone.Filter = function(constants) {
    this.gotZONES = constants.gotZONES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmnfsec.zone.Filter.factory = function(constants) {
    var filter = new jxmnfsec.zone.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmnfsec.zone.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var zones = this.gotZONES();
        var zone = zones.filter(function(item){return input === item.zone_id});
        return zone.length > 0 ? zone[0].zone_name : '';
    }
};
