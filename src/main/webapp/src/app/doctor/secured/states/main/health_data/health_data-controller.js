'use strict';
/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_data.Ctrl');
/**
 * Health data controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.health_data.Ctrl = function() {
    /**
     * @type {String}
     * @nocollapse
     */
    this.label = 'some label from health data controller';
};

jxdctsec.main.health_data.Ctrl.prototype.log = function(text) {
    console.log(text);
};