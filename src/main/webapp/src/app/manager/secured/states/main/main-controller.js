'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.Ctrl');



/**
 * Main controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.Ctrl.$inject = [
    '$scope',
    '$window',
    '$timeout',
    'basic'
];
jxmgrsec.main.Ctrl = function($scope,$window,$timeout,basic) {

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.label = 'some label from main controller';

  // ctrl.yesNoResult = null;
  // ctrl.complexResult = null;
  // ctrl.customResult = null;

  ctrl.animationsEnabled = true;

  ctrl.db_operation_submenus = [{
    name: "产品类型",
    link: "main.db_operation.product_type"
  }, {
    name: "设备类型",
    link: "main.db_operation.device_type"
  },{
    name: "终端型号",
    link: "main.db_operation.terminal_category"
  },{
    name: "终端",
    link: "main.db_operation.terminal"
  },{
    name: "厂家",
    link: "main.db_operation.manufactory"
  },{
    name: "医生",
    link: "main.db_operation.doctor"
  },{
    name: "用户",
    link: "main.db_operation.user"
  }];

  ctrl.home_security_submenus = [{
    name: "摄像头",
    link: "main.home_security.camera"
  }, {
    name: "紧急按钮",
    link: "main.home_security.button"
  }];

  ctrl.permission_submenus = [{
    name: "厂商权限",
    link: "main.permission.manufactory_permission"
  }, {
    name: "医生权限",
    link: "main.permission.doctor_permission"
  }];

  ctrl.page_submenus = [{
    name: "产品页面",
    link: "main.page.category_system_template"
  }, {
    name: "客户端宣传页面",
    link: "main.page.exhibition"
  }, {
    name: "客户端新闻页面",
    link: "main.page.news"
  }];

  ctrl.twatch_submenus = [{
    name: "号码设置",
    link: "main.twatch.number"
  }, {
    name: "模式设置",
    link: "main.twatch.mode"
  }, {
    name: "提醒设置",
    link: "main.twatch.alarm"
  }, {
    name: "轨迹设置",
    link: "main.twatch.route_setting"
  }, {
    name: "围栏设置",
    link: "main.twatch.pen"
  }, {
    name: "位置查询",
    link: "main.twatch.loc"
  }, {
    name: "运动轨迹",
    link: "main.twatch.route"
  }, {
    name: "信息查询",
    link: "main.twatch.command"
  }];

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


    $scope.avatarurl = "../../img/shangbiaokuang.png";
    $scope.managerName = "管理员姓名";
    $scope.getManagerBasics = function(){
        console.log("管理员基本信息！");
        basic.getManagerBasics().then(function(data){
            if($scope.validParam(data.success_message.avatar_url)){
                $scope.avatarurl = data.success_message.avatar_url;
                console.log($scope.avatarurl);
            }
            if($scope.validParam(data.success_message.full_name)){
                $scope.managerName = data.success_message.full_name;
            }
            console.log(data);
        },function(error){
            $scope.addAlert("danger","数据加载异常，请稍后再试！");
        });
    }
    //加载管理员信息基本数据
    $scope.getManagerBasics();

    //登出
    $scope.signOut = function(){
        basic.signOut().then(function(data){
            //console.log(data);
            $window.location.href = '/manager/open';
        },function(error){
            console.log(error);
            $window.location.href = '/manager/open';

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
jxmgrsec.main.Ctrl.prototype.log = function(text) {
  console.log(text);

};
