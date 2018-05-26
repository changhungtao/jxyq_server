'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.pen_point_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.pen_point_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "entity"
];
jxmgrsec.pen_point_modal.Ctrl = function($scope, $filter,$modalInstance,$q,constants,entity) {
    $scope.entity = entity;
    console.log($scope.entity);

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
};
