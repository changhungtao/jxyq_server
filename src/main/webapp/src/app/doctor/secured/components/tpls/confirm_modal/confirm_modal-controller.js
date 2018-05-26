'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.confirm_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxdctsec.confirm_modal.Ctrl.$inject = [
  "$modalInstance", 
  "items"
];
jxdctsec.confirm_modal.Ctrl = function($modalInstance, items) {

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';

  ctrl.items = items;
  ctrl.selected = {
    item: ctrl.items[0]
  };

  ctrl.ok = function () {
    $modalInstance.close(ctrl.selected.item);
  };

  ctrl.cancel = function () {
    $modalInstance.dismiss('cancel');
  };

};
