'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctopn.panel.Ctrl');



/**
 * Panel controller.
 *
 * @constructor
 * @export
 */
jxdctopn.panel.Ctrl = function() {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from panel controller';

};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `panel.?`
 * child controller.
 *
 * @param {String} text
 */
jxdctopn.panel.Ctrl.prototype.log = function(text) {
  console.log(text);
};
