'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.Ctrl');



/**
 * DB operation controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.db_operation.Ctrl = function() {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from db operation controller';

 
};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `main.db_operation.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.db_operation.Ctrl.prototype.log = function(text) {
  console.log(text);
};
