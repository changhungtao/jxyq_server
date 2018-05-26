/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxsprsec.data_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.data_type.Filter = function(constants) {
    this.gotDATATYPES = constants.gotDATATYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.data_type.Filter.factory = function(constants) {
    var filter = new jxsprsec.data_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.data_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var data_types = this.gotDATATYPES();
        var data_type = data_types.filter(function(item){return input === item.data_type_id});
        return data_type.length > 0 ? data_type[0].data_type_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.file_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.file_type.Filter = function(constants) {
    this.gotFILETYPES = constants.gotFILETYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.file_type.Filter.factory = function(constants) {
    var filter = new jxsprsec.file_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.file_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var file_types = this.gotFILETYPES();
        var file_type = file_types.filter(function(item){return input === item.file_type_id});
        return file_type.length > 0 ? file_type[0].file_type_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.wristband_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.wristband_column.Filter = function(constants) {
    this.checkmark = '\u2714';
    this.cross = '\u2718';
    this.gotWRISTBANDCOLUMNS = constants.gotWRISTBANDCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.wristband_column.Filter.factory = function(constants) {
    var filter = new jxsprsec.wristband_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.wristband_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var wristband_columns = this.gotWRISTBANDCOLUMNS();
        var wristband_column = wristband_columns.filter(function(item){return input === item.column_id});
        return wristband_column.length > 0 ? wristband_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.sphygmomanometer_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.sphygmomanometer_column.Filter = function(constants) {
    this.gotSPHYGMOMANOMETERCOLUMNS = constants.gotSPHYGMOMANOMETERCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.sphygmomanometer_column.Filter.factory = function(constants) {
    var filter = new jxsprsec.sphygmomanometer_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.sphygmomanometer_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var sphygmomanometer_columns = this.gotSPHYGMOMANOMETERCOLUMNS();
        var sphygmomanometer_column = sphygmomanometer_columns.filter(function(item){return input === item.column_id});
        return sphygmomanometer_column.length > 0 ? sphygmomanometer_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.glucosemeter_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.glucosemeter_column.Filter = function(constants) {
    this.gotGLUCOSEMETERCOLUMNS = constants.gotGLUCOSEMETERCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.glucosemeter_column.Filter.factory = function(constants) {
    var filter = new jxsprsec.glucosemeter_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.glucosemeter_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var glucosemeter_columns = this.gotGLUCOSEMETERCOLUMNS();
        var glucosemeter_column = glucosemeter_columns.filter(function(item){return input === item.column_id});
        return glucosemeter_column.length > 0 ? glucosemeter_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.thermometer_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.thermometer_column.Filter = function(constants) {
    this.gotTHERMOMETERCOLUMNS = constants.gotTHERMOMETERCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.thermometer_column.Filter.factory = function(constants) {
    var filter = new jxsprsec.thermometer_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.thermometer_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var thermometer_columns = this.gotTHERMOMETERCOLUMNS();
        var thermometer_column = thermometer_columns.filter(function(item){return input === item.column_id});
        return thermometer_column.length > 0 ? thermometer_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.oximeter_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.oximeter_column.Filter = function(constants) {
    this.gotOXIMETERCOLUMNS = constants.gotOXIMETERCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.oximeter_column.Filter.factory = function(constants) {
    var filter = new jxsprsec.oximeter_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.oximeter_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var oximeter_columns = this.gotOXIMETERCOLUMNS();
        var oximeter_column = oximeter_columns.filter(function(item){return input === item.column_id});
        return oximeter_column.length > 0 ? oximeter_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.fat_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.fat_column.Filter = function(constants) {
    this.gotFATCOLUMNS = constants.gotFATCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.fat_column.Filter.factory = function(constants) {
    var filter = new jxsprsec.fat_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.fat_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var fat_columns = this.gotFATCOLUMNS();
        var fat_column = fat_columns.filter(function(item){return input === item.column_id});
        return fat_column.length > 0 ? fat_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.comparison_ops.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.comparison_ops.Filter = function(constants) {
    this.gotCOMPARISONOPS = constants.gotCOMPARISONOPS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.comparison_ops.Filter.factory = function(constants) {
    var filter = new jxsprsec.comparison_ops.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.comparison_ops.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var comparison_ops = this.gotCOMPARISONOPS();
        var comparison_op = comparison_ops.filter(function(item){return input === item.op_id});
        return comparison_op.length > 0 ? comparison_op[0].op_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.logical_ops.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.logical_ops.Filter = function(constants) {
    this.gotLOGICALOPS = constants.gotLOGICALOPS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.logical_ops.Filter.factory = function(constants) {
    var filter = new jxsprsec.logical_ops.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.logical_ops.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var logical_ops = this.gotLOGICALOPS();
        var logical_op = logical_ops.filter(function(item){return input === item.op_id});
        return logical_op.length > 0 ? logical_op[0].op_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.periods.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.periods.Filter = function(constants) {
    this.gotPERIODS = constants.gotPERIODS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.periods.Filter.factory = function(constants) {
    var filter = new jxsprsec.periods.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.periods.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var periods = this.gotPERIODS();
        var period = periods.filter(function(item){return input === item.period_id});
        return period.length > 0 ? period[0].period_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.helath_consultation_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.helath_consultation_status.Filter = function(constants) {
    this.gotHEALTHCONSULTATIONSTATUS = constants.gotHEALTHCONSULTATIONSTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.helath_consultation_status.Filter.factory = function(constants) {
    var filter = new jxsprsec.helath_consultation_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.helath_consultation_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var helath_consultation_statuses = this.gotHEALTHCONSULTATIONSTATUS();
        var helath_consultation_status = helath_consultation_statuses.filter(function(item){return input === item.consultation_status_id});
        return helath_consultation_status.length > 0 ? helath_consultation_status[0].consultation_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.health_data_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.health_data_status.Filter = function(constants) {
    this.gotHEALTHDATASTATUS = constants.gotHEALTHDATASTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.health_data_status.Filter.factory = function(constants) {
    var filter = new jxsprsec.health_data_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.health_data_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var health_data_statuses = this.gotHEALTHDATASTATUS();
        var health_data_status = health_data_statuses.filter(function(item){return input === item.data_status_id});
        return health_data_status.length > 0 ? health_data_status[0].data_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.health_file_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.health_file_status.Filter = function(constants) {
    this.gotHEALTHFILESTATUS = constants.gotHEALTHFILESTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.health_file_status.Filter.factory = function(constants) {
    var filter = new jxsprsec.health_file_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.health_file_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var health_file_statuses = this.gotHEALTHFILESTATUS();
        var health_file_status = health_file_statuses.filter(function(item){return input === item.file_status_id});
        return health_file_status.length > 0 ? health_file_status[0].file_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxsprsec.department.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.department.Filter = function(constants) {
    this.gotDEPARTMENTS = constants.gotDEPARTMENTS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.department.Filter.factory = function(constants) {
    var filter = new jxsprsec.department.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.department.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var departments = this.gotDEPARTMENTS();
        var department = departments.filter(function(item){return input === item.department_id});
        return department.length > 0 ? department[0].department_name : '';
    }
};
