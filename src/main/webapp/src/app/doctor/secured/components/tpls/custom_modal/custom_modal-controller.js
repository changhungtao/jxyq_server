'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.custom_modal.Ctrl');



/**
 * Custom modal controller.
 *
 * @constructor
 * @export
 */
jxdctsec.custom_modal.Ctrl.$inject = [
  "close"
];
jxdctsec.custom_modal.Ctrl = function(close) {

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';
  ctrl.display = true;

  ctrl.close = function() {
    ctrl.display = false;
    close();
  };

};
