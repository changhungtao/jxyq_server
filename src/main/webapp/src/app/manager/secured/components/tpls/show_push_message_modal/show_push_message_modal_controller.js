'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.show_push_message_modal.show_push_message_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.show_push_message_modal.show_push_message_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$timeout",
  "entity",
  "$filter",
  "push_message"
];
jxmgrsec.show_push_message_modal.show_push_message_modal.Ctrl = function($scope, $filter, $modalInstance, $timeout, entity, push_message) {

  /**
   * @type {String}
   * @nocollapse
   */
  var ctrl = this;
  console.log(entity);

  $scope.pushMSG = {
    manufactory_id:entity.manufactory_id,
    terminal_catagory_id:entity.terminal_catagory_id,
    content: undefined
  }

  $scope.disableBtn = false;
  $scope.pushMessages = function() {
    $scope.disableBtn = true;
    push_message.pushMessage($scope.pushMSG).then(function(res) {
//      console.log(res);
      $scope.addAlert("success", "推送成功,三秒后自动关闭！");
        $timeout(function(){
            $scope.cancel();
        },3000);
      //$scope.cancel();
    }, function(error) {
      $scope.addAlert("danger", "推送失败,请稍后再试！");
        $scope.disableBtn = false;
//      console.log(error);
    });
}
  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

  //Alert
  $scope.alerts = [];
  $scope.addAlert = function(type, msg) {
    var alert = {
      'type': type,
      'msg': msg
    };
    $scope.alerts.push(alert);
    $timeout(function() {
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
