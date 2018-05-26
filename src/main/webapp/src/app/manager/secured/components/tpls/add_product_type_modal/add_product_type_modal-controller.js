'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.add_product_type_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.add_product_type_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "$timeout",
  "constants",
  "db_operation"
];
jxmgrsec.add_product_type_modal.Ctrl = function($scope, $filter,$modalInstance,$q,constants,db_operation,$timeout) {

  $scope.disableBtn = false;
  $scope.submit = function() {
    $scope.disableBtn = true;
    db_operation.postAddProductTypes({name:$scope.productName}).then(function(succ){
        $scope.addAlert("success", "添加成功,3秒钟后自动关闭！");
        $timeout(function(){
        $scope.cancel();
        $scope.disableBtn = false;
        },3000);
    },function(error){
//      console.log(error);
        $scope.disableBtn = false;
        $scope.addAlert("danger", "添加失败，请稍后再试！");
    });
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

  //Alert
    $scope.alerts = [];
    $scope.addAlert = function (type, msg) {
        var alert = {'type': type, 'msg': msg};
        $scope.alerts.push(alert);
        $timeout(function(){
            $scope.closeAlert($scope.alerts.indexOf(alert));         
        }, 3000);
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.clearAlerts = function() {
        $scope.alerts = [];
    };

};
