'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmnfsec.add_terminal_catagory_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmnfsec.add_terminal_catagory_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$timeout",
  "$filter",
  "constants",
  "basic",
  "db_operation"
];
jxmnfsec.add_terminal_catagory_modal.Ctrl = function($scope,$modalInstance,$filter,$timeout,constants,basic,db_operation) {

    $scope.device_types = constants.gotDEVICETYPES();
    $scope.product_types = constants.gotPRODUCTTYPES();
    $scope.terminal_states = constants.gotTERMINALSTATUS();

    $scope.terminalPic = undefined;
    $scope.terminal = {
        terminal_catagory_name:undefined,
        code:undefined,
        product_type_id:undefined,
        device_type_id:undefined,
        price:undefined,
        picture:undefined,
        profile:undefined,
        status:undefined
    };

    $scope.disabledAddButton = false;
    $scope.addTerminal = function(){
        $scope.disabledAddButton = true;
        if(!validParam($scope.terminal.terminal_catagory_name)){$scope.addAlert('warning','终端型号必须填写');$scope.disabledAddButton = false;return;}
        if(!validParam($scope.terminal.code)){$scope.addAlert('warning','型号编码必须填写');$scope.disabledAddButton = false;return;}
        if(!validParam($scope.terminal.product_type_id)){$scope.addAlert('warning','产品类型必须填写');$scope.disabledAddButton = false;return;}
        if(!validParam($scope.terminal.device_type_id)){$scope.addAlert('warning','设备类型必须填写');$scope.disabledAddButton = false;return;}
        if(!validParam($scope.terminal.price)){$scope.addAlert('warning','价格必须填写');$scope.disabledAddButton = false;return;}
        if(!validParam($scope.terminalPic)){$scope.addAlert('warning','图片必须填写');$scope.disabledAddButton = false;return;}
        if(!validParam($scope.terminal.profile)){$scope.addAlert('warning','说明信息必须填写');$scope.disabledAddButton = false;return;}
        if(!validParam($scope.terminal.status)){$scope.addAlert('warning','状态必须填写');$scope.disabledAddButton = false;return;}

        if(!validPic($scope.terminalPic,'png|jpg|jpeg|bmp|gif')){$scope.alerts = [];$scope.addAlert("warning","请上传png、jpg、gif类型的文件！");$scope.disabledAddButton = false;return;}

        if(isNaN($scope.terminal.price)){$scope.addAlert('warning','价格无效');$scope.disabledAddButton = false;return;}
        return basic.fileUpload($scope.terminalPic).then(function(res){
            if(validParam(res.success_message[0])){
                $scope.terminal.picture = res.success_message[0];
                //upload file success then add terminal
                return db_operation.addTerCat($scope.terminal).then(function(res){
//                    console.log(res);
                    $scope.addAlert('success','添加终端成功,3秒后自动关闭！');
                    $timeout(function(){
                        $scope.disabledAddButton = false;//提交成功等三秒button可用，避免重复提交
                        $scope.cancel();
                    },3000);
                },function(error){
                    $scope.addAlert('danger','添加终端失败，请稍后再试！');
                    $scope.disabledAddButton = false;
                });
            }else{
                $scope.addAlert('warning','上传文件失败，请稍后再试！');
                $scope.disabledAddButton = false;
            }
        },function(error){
            $scope.addAlert('warning','上传文件失败，请稍后再试！');
            $scope.disabledAddButton = false;
        });
    }

    /**
     * file:上传的文件对象
     * str：允许上传的文件类型字符串，例如：'png|jpg|jpeg|bmp|gif'
     * */
    function validPic(file,str){
        if(file != undefined && file.type != undefined && str != undefined && str != '' && str.length != 0){
            var suffixs = str.split('|');
            var fileSuf = file.type.split('/');
            var res = suffixs.filter(function(item){
                return item == fileSuf[1];
            });
            if(res.length != 0){
                return true;
            }
            return false;
        }
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

    $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

    function validParam(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }
};
