'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmnfsec.main.Ctrl');

jxmnfsec.main.Ctrl.$inject = [
    "$window",
    "basic"
];

/**
 * Main controller.
 *
 * @constructor
 * @export
 */
// jxmnfsec.main.Ctrl.$inject = ['$scope'];
jxmnfsec.main.Ctrl = function($scope,$window,basic,$timeout) {

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.label = 'some label from main controller';
  // $scope.items = [
  //   'The first choice!',
  //   'And another choice for you.',
  //   'but wait! A third!'
  // ];

  // $scope.status = {
  //   isopen: false
  // };

  // $scope.toggled = function(open) {
  //   $log.log('Dropdown is now: ', open);
  // };

  // $scope.toggleDropdown = function($event) {
  //   $event.preventDefault();
  //   $event.stopPropagation();
  //   $scope.status.isopen = !$scope.status.isopen;
  // }

  ctrl.user_center_submenus = [{
    name: "基本资料",
    link: "main.user_center.basics"
  }, {
    name: "修改头像",
    link: "main.user_center.avatar"
  }, {
    name: "修改密码",
    link: "main.user_center.password"
  }];

    $scope.avatarurl = "../../img/shangbiaokuang.png";
    $scope.manufactoryName = "厂商名称";
    $scope.getManufactoryBasics = function(){
        console.log("厂商基本信息！");
        basic.getManufactoryBasics().then(function(data){
            if($scope.validParam(data.success_message.logo_url)){
                $scope.avatarurl = data.success_message.logo_url;
                console.log($scope.avatarurl);
            }
            if($scope.validParam(data.success_message.full_name)){
                $scope.manufactoryName = data.success_message.full_name;
            }
            console.log(data);
        },function(error){
            $scope.addAlert("danger","数据加载异常，请稍后再试！");
        });
    }
    //加载厂商信息基本数据
    $scope.getManufactoryBasics();



    //登出
    $scope.signOut = function(){
        basic.signOut().then(function(data){
            //console.log(data);
            $window.location.href = '/manufactory/open';
        },function(error){
            console.log(error);
            $window.location.href = '/manufactory/open';

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
jxmnfsec.main.Ctrl.prototype.log = function(text) {
  console.log(text);

};
