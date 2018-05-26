'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.show_cs_template_modal.show_cs_template_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.show_cs_template_modal.show_cs_template_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "entity"
];
jxmgrsec.show_cs_template_modal.show_cs_template_modal.Ctrl = function($scope, $modalInstance, entity) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';

};
