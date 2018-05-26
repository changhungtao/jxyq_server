'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.permission.Ctrl');



/**
 * Permission controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.permission.Ctrl = function() {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from db operation controller';

 
};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `main.permission.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.permission.Ctrl.prototype.log = function(text) {
  console.log(text);
};
