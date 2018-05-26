'use strict';

/**
 * Create namespace.
 */
goog.provide('jxsprsec.main.user_center.Ctrl');



/**
 * User center controller.
 *
 * @constructor
 * @export
 */
jxsprsec.main.user_center.Ctrl = function() {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from user center controller';
  this.a = function (){
    console.log("hi");
  }
};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `user_center.?`
 * child controller.
 *
 * @param {String} text
 */
jxsprsec.main.user_center.Ctrl.prototype.log = function(text) {
  console.log("hello");
};
