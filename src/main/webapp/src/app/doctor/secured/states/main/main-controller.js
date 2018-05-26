'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.Ctrl');



/**
 * Main controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.Ctrl.$inject = [
  "$modal",
   "$window",
  "ModalService",
  "basic"
];
jxdctsec.main.Ctrl = function($scope,$timeout,$modal, $window, ModalService,basic) {

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.label = 'some label from main controller';

  // ctrl.yesNoResult = null;
  // ctrl.complexResult = null;
  // ctrl.customResult = null;

  ctrl.items = ['item1', 'item2', 'item3'];

  ctrl.animationsEnabled = true;

  ctrl.user_center_submenus = [{
    name: "修改资料",
    link: "main.user_center.basics"
  }, {
    name: "修改头像",
    link: "main.user_center.avatar"
  },{
    name: "修改密码",
    link: "main.user_center.password"
  }];

  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.popupCustom = function() {

    ModalService.showModal({
      templateUrl: "components/tpls/custom_modal/custom_modal.html",
      controller: "CustomModalCtrl as customModal"
    }).then(function(modal) {
      modal.close.then(function(result) {
        ctrl.customResult = "All good!";
      });
    });
  };

  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.popupConfirm = function() {

    console.log("hello, world!");

    var modalInstance = $modal.open({
      animation: ctrl.animationsEnabled,
      templateUrl: 'components/tpls/confirm_modal/confirm_modal.html',
      controller: 'ConfirmModalCtrl as confirmModal',
      resolve: {
        items: function () {
          return ctrl.items;
        }
      }
    });

    modalInstance.result.then(function (selectedItem) {
      ctrl.selected = selectedItem;
      console.log('Modal closed at: ' + new Date());
    }, function () {
      console.log('Modal dismissed at: ' + new Date());
    });

  };

    $scope.avatarurl = "../../img/shangbiaokuang.png";
    $scope.doctorName = "医生姓名";
    $scope.getDoctorBasics = function(){
        console.log("医生基本信息！");
        basic.getDoctorBasics().then(function(data){
            if($scope.validParam(data.success_message.avatar_url)){
                $scope.avatarurl = data.success_message.avatar_url;
                console.log($scope.avatarurl);
            }
            if($scope.validParam(data.success_message.full_name)){
                $scope.doctorName = data.success_message.full_name;
            }
            console.log(data);
        },function(error){
            $scope.addAlert("danger","数据加载异常，请稍后再试！");
        });
    }
    //加载医生基本数据
    $scope.getDoctorBasics();

    //登出
    $scope.signOut = function(){
        basic.signOut().then(function(data){
            //console.log(data);
            $window.location.href = '/doctor/open';
        },function(error){
            console.log(error);
            $window.location.href = '/doctor/open';

        });
    }

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

    $scope.validParam = function(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }

};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `main.?`
 * child controller.
 *
 * @param {String} text
 */
jxdctsec.main.Ctrl.prototype.log = function(text) {
  console.log(text);

};
