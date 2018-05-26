/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxmgrsec.data_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.data_type.Filter = function(constants) {
    this.gotDATATYPES = constants.gotDATATYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.data_type.Filter.factory = function(constants) {
    var filter = new jxmgrsec.data_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.data_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var data_types = this.gotDATATYPES();
        var data_type = data_types.filter(function(item){return input === item.data_type_id});
        return data_type.length > 0 ? data_type[0].data_type_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.file_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.file_type.Filter = function(constants) {
    this.gotFILETYPES = constants.gotFILETYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.file_type.Filter.factory = function(constants) {
    var filter = new jxmgrsec.file_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.file_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var file_types = this.gotFILETYPES();
        var file_type = file_types.filter(function(item){return input === item.file_type_id});
        return file_type.length > 0 ? file_type[0].file_type_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.wristband_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.wristband_column.Filter = function(constants) {
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
jxmgrsec.wristband_column.Filter.factory = function(constants) {
    var filter = new jxmgrsec.wristband_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.wristband_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var wristband_columns = this.gotWRISTBANDCOLUMNS();
        var wristband_column = wristband_columns.filter(function(item){return input === item.column_id});
        return wristband_column.length > 0 ? wristband_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.sphygmomanometer_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.sphygmomanometer_column.Filter = function(constants) {
    this.gotSPHYGMOMANOMETERCOLUMNS = constants.gotSPHYGMOMANOMETERCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.sphygmomanometer_column.Filter.factory = function(constants) {
    var filter = new jxmgrsec.sphygmomanometer_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.sphygmomanometer_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var sphygmomanometer_columns = this.gotSPHYGMOMANOMETERCOLUMNS();
        var sphygmomanometer_column = sphygmomanometer_columns.filter(function(item){return input === item.column_id});
        return sphygmomanometer_column.length > 0 ? sphygmomanometer_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.glucosemeter_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.glucosemeter_column.Filter = function(constants) {
    this.gotGLUCOSEMETERCOLUMNS = constants.gotGLUCOSEMETERCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.glucosemeter_column.Filter.factory = function(constants) {
    var filter = new jxmgrsec.glucosemeter_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.glucosemeter_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var glucosemeter_columns = this.gotGLUCOSEMETERCOLUMNS();
        var glucosemeter_column = glucosemeter_columns.filter(function(item){return input === item.column_id});
        return glucosemeter_column.length > 0 ? glucosemeter_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.thermometer_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.thermometer_column.Filter = function(constants) {
    this.gotTHERMOMETERCOLUMNS = constants.gotTHERMOMETERCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.thermometer_column.Filter.factory = function(constants) {
    var filter = new jxmgrsec.thermometer_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.thermometer_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var thermometer_columns = this.gotTHERMOMETERCOLUMNS();
        var thermometer_column = thermometer_columns.filter(function(item){return input === item.column_id});
        return thermometer_column.length > 0 ? thermometer_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.oximeter_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.oximeter_column.Filter = function(constants) {
    this.gotOXIMETERCOLUMNS = constants.gotOXIMETERCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.oximeter_column.Filter.factory = function(constants) {
    var filter = new jxmgrsec.oximeter_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.oximeter_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var oximeter_columns = this.gotOXIMETERCOLUMNS();
        var oximeter_column = oximeter_columns.filter(function(item){return input === item.column_id});
        return oximeter_column.length > 0 ? oximeter_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.fat_column.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.fat_column.Filter = function(constants) {
    this.gotFATCOLUMNS = constants.gotFATCOLUMNS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.fat_column.Filter.factory = function(constants) {
    var filter = new jxmgrsec.fat_column.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.fat_column.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var fat_columns = this.gotFATCOLUMNS();
        var fat_column = fat_columns.filter(function(item){return input === item.column_id});
        return fat_column.length > 0 ? fat_column[0].column_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.comparison_ops.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.comparison_ops.Filter = function(constants) {
    this.gotCOMPARISONOPS = constants.gotCOMPARISONOPS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.comparison_ops.Filter.factory = function(constants) {
    var filter = new jxmgrsec.comparison_ops.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.comparison_ops.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var comparison_ops = this.gotCOMPARISONOPS();
        var comparison_op = comparison_ops.filter(function(item){return input === item.op_id});
        return comparison_op.length > 0 ? comparison_op[0].op_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.logical_ops.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.logical_ops.Filter = function(constants) {
    this.gotLOGICALOPS = constants.gotLOGICALOPS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.logical_ops.Filter.factory = function(constants) {
    var filter = new jxmgrsec.logical_ops.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.logical_ops.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var logical_ops = this.gotLOGICALOPS();
        var logical_op = logical_ops.filter(function(item){return input === item.op_id});
        return logical_op.length > 0 ? logical_op[0].op_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.periods.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.periods.Filter = function(constants) {
    this.gotPERIODS = constants.gotPERIODS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.periods.Filter.factory = function(constants) {
    var filter = new jxmgrsec.periods.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.periods.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var periods = this.gotPERIODS();
        var period = periods.filter(function(item){return input === item.period_id});
        return period.length > 0 ? period[0].period_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.helath_consultation_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.helath_consultation_status.Filter = function(constants) {
    this.gotHEALTHCONSULTATIONSTATUS = constants.gotHEALTHCONSULTATIONSTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.helath_consultation_status.Filter.factory = function(constants) {
    var filter = new jxmgrsec.helath_consultation_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.helath_consultation_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var helath_consultation_statuses = this.gotHEALTHCONSULTATIONSTATUS();
        var helath_consultation_status = helath_consultation_statuses.filter(function(item){return input === item.consultation_status_id});
        return helath_consultation_status.length > 0 ? helath_consultation_status[0].consultation_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.health_data_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.health_data_status.Filter = function(constants) {
    this.gotHEALTHDATASTATUS = constants.gotHEALTHDATASTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.health_data_status.Filter.factory = function(constants) {
    var filter = new jxmgrsec.health_data_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.health_data_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var health_data_statuses = this.gotHEALTHDATASTATUS();
        var health_data_status = health_data_statuses.filter(function(item){return input === item.data_status_id});
        return health_data_status.length > 0 ? health_data_status[0].data_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.health_file_status.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.health_file_status.Filter = function(constants) {
    this.gotHEALTHFILESTATUS = constants.gotHEALTHFILESTATUS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.health_file_status.Filter.factory = function(constants) {
    var filter = new jxmgrsec.health_file_status.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.health_file_status.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var health_file_statuses = this.gotHEALTHFILESTATUS();
        var health_file_status = health_file_statuses.filter(function(item){return input === item.file_status_id});
        return health_file_status.length > 0 ? health_file_status[0].file_status_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.department.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.department.Filter = function(constants) {
    this.gotDEPARTMENTS = constants.gotDEPARTMENTS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.department.Filter.factory = function(constants) {
    var filter = new jxmgrsec.department.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.department.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var departments = this.gotDEPARTMENTS();
        var department = departments.filter(function(item){return input === item.department_id});
        return department.length > 0 ? department[0].department_name : '';
    }
};
