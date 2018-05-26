'use strict';

/**
 * Create namespace.
 */
goog.provide('jxspropn.panel.Ctrl');



/**
 * Panel controller.
 *
 * @constructor
 * @export
 */
jxspropn.panel.Ctrl = function() {

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
jxspropn.panel.Ctrl.prototype.log = function(text) {
  console.log(text);
};
