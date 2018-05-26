/**
 * Created by zhanga.fnst on 2015/6/16.
 */
'use strict';

goog.provide('jxsprsec.unixTodate.Filter.factory');

/**
 * Convert boolean values to unicode checkmark or cross.
 *
 * @constructor
 */
jxsprsec.unixTodate.Filter = function() {
    this.checkmark = '\u2714';
    this.cross = '\u2718';
    this.convert = this.convert.bind(this);
};

/**
 * Version directive factory.
 *
 * @return {function}
 */
jxsprsec.unixTodate.Filter.factory = function() {
    var filter = new jxsprsec.unixTodate.Filter();
    return filter.convert;
};

//日期格式化
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1,  //月份
        "d+": this.getDate(),       //日
        "h+": this.getHours(),      //小时
        "m+": this.getMinutes(),    //分
        "s+": this.getSeconds(),    //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * Convert truthy and falsy values to unicode symbols.
 *
 * @param {string} input
 */
jxsprsec.unixTodate.Filter.prototype.convert = function(input) {
    if(!input){
        return '';
    }else {
        var now = (new Date(parseInt(input) * 1000)).Format("yyyy-MM-dd hh:mm:ss");
        return now.toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
    }
};

