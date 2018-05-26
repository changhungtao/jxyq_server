/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxmgrsec.twatch_qing_name.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_qing_name.Filter = function(constants) {
    this.gotQINGNAMES = constants.gotQINGNAMES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_qing_name.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_qing_name.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_qing_name.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var names = this.gotQINGNAMES();
        var name = names.filter(function(item){return input === item.name_id});
        return name.length > 0 ? name[0].name_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.twatch_sos_name.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_sos_name.Filter = function(constants) {
    this.gotSOSNAMES = constants.gotSOSNAMES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_sos_name.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_sos_name.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_sos_name.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var names = this.gotSOSNAMES();
        var name = names.filter(function(item){return input === item.name_id});
        return name.length > 0 ? name[0].name_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.twatch_work_mode.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_work_mode.Filter = function(constants) {
    this.gotWORKMODES = constants.gotWORKMODES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_work_mode.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_work_mode.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_work_mode.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var modes = this.gotWORKMODES();
        var mode = modes.filter(function(item){return input === item.mode_id});
        return mode.length > 0 ? mode[0].mode_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.twatch_work_type.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_work_type.Filter = function(constants) {
    this.gotWORKTYPES = constants.gotWORKTYPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_work_type.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_work_type.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_work_type.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var types = this.gotWORKTYPES();
        var type = types.filter(function(item){return input === item.type_id});
        return type.length > 0 ? type[0].type_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.twatch_alarm_event.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_alarm_event.Filter = function(constants) {
    this.gotALARMEVENTS = constants.gotALARMEVENTS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_alarm_event.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_alarm_event.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_alarm_event.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var events = this.gotALARMEVENTS();
        var event_i = events.filter(function(item){return input === item.event_id});
        return event_i.length > 0 ? event_i[0].event_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.twatch_period_state.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_period_state.Filter = function(constants) {
    this.gotPERIODSTATES = constants.gotPERIODSTATES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_period_state.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_period_state.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_period_state.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var states = this.gotPERIODSTATES();
        var state = states.filter(function(item){return input === item.state_id});
        return state.length > 0 ? state[0].state_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.twatch_route_state.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_route_state.Filter = function(constants) {
    this.gotROUTESTATES = constants.gotROUTESTATES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_route_state.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_route_state.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_route_state.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var states = this.gotROUTESTATES();
        var state = states.filter(function(item){return input === item.state_id});
        return state.length > 0 ? state[0].state_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.twatch_gps_mode.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_gps_mode.Filter = function(constants) {
    this.gotGPSMODES = constants.gotGPSMODES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_gps_mode.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_gps_mode.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_gps_mode.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var modes = this.gotGPSMODES();
        var mode = modes.filter(function(item){return input === item.mode_id});
        return mode.length > 0 ? mode[0].mode_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.twatch_pen_shape.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_pen_shape.Filter = function(constants) {
    this.gotPENSHAPES = constants.gotPENSHAPES;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_pen_shape.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_pen_shape.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_pen_shape.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var shapes = this.gotPENSHAPES();
        var shape = shapes.filter(function(item){return input === item.shape_id});
        return shape.length > 0 ? shape[0].shape_name : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.twatch_week_day.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_week_day.Filter = function(constants) {
    this.gotWEEKDAYS = constants.gotWEEKDAYS;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_week_day.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_week_day.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_week_day.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var days = this.gotWEEKDAYS();
        var day = days.filter(function(item){return input === item.day_id});
        return day.length > 0 ? day[0].day_cn_s : '';
    }
};

/***********************************************************************************/
goog.provide('jxmgrsec.twatch_week_days.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxmgrsec.twatch_week_days.Filter = function(constants) {
    this.weekDayHelperInt2Array = constants.weekDayHelperInt2Array;
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxmgrsec.twatch_week_days.Filter.factory = function(constants) {
    var filter = new jxmgrsec.twatch_week_days.Filter(constants);
    return filter.convert;
};

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxmgrsec.twatch_week_days.Filter.prototype.convert = function(input) {
    if(isNaN(input)){
        return '';
    }else {
        var days = this.weekDayHelperInt2Array(input);
        return days.toString();
    }
};
