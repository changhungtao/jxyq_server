'use strict';
/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.delete_mode_blackname_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.delete_mode_blackname_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "entity",
  "twatch",
  "griddata",
  "imei"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.delete_mode_blackname_modal.Ctrl = function($scope, $filter,$modalInstance,$q,constants,entity, twatch, griddata, imei) {

  console.log(entity);
  console.log(griddata);
  console.log(imei);

  var getAllData = function() {
    for (var i = griddata.length - 1; i >= 0; i--) {
      if (entity.$$hashKey == griddata[i].$$hashKey) {
        //addblack.blacklist.pop($scope.black);
      } else {
        addblack.blacklist.push(griddata[i]);
      }
    }
  }

  $scope.black = {
    name: entity.name,
    number: entity.number
  }

  var addblack = {
    imei: imei,
    blacklist: []
  }

  $scope.deleteBlack = function() {
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
