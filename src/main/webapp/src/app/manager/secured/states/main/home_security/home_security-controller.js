'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.home_security.Ctrl');



/**
 * DB operation controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.home_security.Ctrl = function() {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from home security controller';

 
};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `main.home_security.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.home_security.Ctrl.prototype.log = function(text) {
  console.log(text);
};
