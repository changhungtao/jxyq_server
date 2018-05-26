'use strict';
/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.add_mode_blackname_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.add_mode_blackname_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "twatch",
  "entity",
  "imei"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.add_mode_blackname_modal.Ctrl = function($scope, $filter, $modalInstance, $q, constants, twatch, entity, imei) {

  console.log(entity);

  var getAllData = function() {
    for (var i = entity.length - 1; i >= 0; i--) {
      addblack.blacklist.push(entity[i]);
    }
    addblack.blacklist.push($scope.black);
  }

  $scope.black = {
    name: undefined,
    number: undefined
  }

  var addblack = {
    imei: imei,
    blacklist: []
  }

  $scope.addBlack = function() {
    getAllData();
    console.log(addblack);
    twatch.addBlackList(addblack).then(function(res) {
      console.log(res);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });
    //$modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
