/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxsprsec.company_department.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.company_department.Filter = function(constants) {
    this.gotCOMPANYDEPARTMENTS = constants.gotCOMPANYDEPARTMENTS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.company_department.Filter.factory = function(constants) {
    var filter = new jxsprsec.company_department.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.company_department.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var company_departments = this.gotCOMPANYDEPARTMENTS();
        var company_department = company_departments.filter(function(item){return input === item.department_id});
        return company_department.length > 0 ? company_department[0].department_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.company_member.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.company_member.Filter = function(constants) {
    this.gotCOMPANYMEMBERS = constants.gotCOMPANYMEMBERS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.company_member.Filter.factory = function(constants) {
    var filter = new jxsprsec.company_member.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.company_member.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var company_members = this.gotCOMPANYMEMBERS();
        var company_member = company_members.filter(function(item){return input === item.member_id});
        return company_member.length > 0 ? company_member[0].member_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.company_nature.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.company_nature.Filter = function(constants) {
    this.gotCOMPANYNATURES = constants.gotCOMPANYNATURES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.company_nature.Filter.factory = function(constants) {
    var filter = new jxsprsec.company_nature.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.company_nature.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var company_natures = this.gotCOMPANYNATURES();
        var company_nature = company_natures.filter(function(item){return input === item.nature_id});
        return company_nature.length > 0 ? company_nature[0].nature_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.company_industry.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.company_industry.Filter = function(constants) {
    this.gotCOMPANYINDUSTRIES = constants.gotCOMPANYINDUSTRIES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.company_industry.Filter.factory = function(constants) {
    var filter = new jxsprsec.company_industry.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.company_industry.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var company_industries = this.gotCOMPANYINDUSTRIES();
        var company_industry = company_industries.filter(function(item){return input === item.industry_id});
        return company_industry.length > 0 ? company_industry[0].industry_name : '';
    }
};
