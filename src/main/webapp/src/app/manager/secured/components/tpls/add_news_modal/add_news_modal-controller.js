'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.add_news_modal.Ctrl');



jxmgrsec.add_news_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "$timeout",
  "constants",
  "page",
  "basic"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.add_news_modal.Ctrl = function($scope, $filter,$modalInstance,$timeout,$q,constants, page,basic) {

  $scope.newsPic = undefined;
  $scope.addNew = {
    pic_url: undefined,
    news_link: undefined
  }

    $scope.disabledAddButton = false;
    $scope.addNews = function(){
        $scope.disabledAddButton = true;
        if(!validParam($scope.newsPic)){$scope.alerts = [];$scope.addAlert('warning','请选择文件');$scope.disabledAddButton = false;return;}
        if(!validParam($scope.addNew.news_link)){$scope.alerts = [];$scope.addAlert('warning','新闻链接必须填写');$scope.disabledAddButton = false;return;}
        if(!validPic($scope.newsPic,'png|jpg|jpeg|bmp|gif')){$scope.alerts = [];$scope.addAlert("warning","请上传png、jpg、gif类型的文件！");$scope.disabledAddButton = false;return;}


        return basic.fileUpload($scope.newsPic).then(function(res){
            if(validParam(res.success_message[0])){
                $scope.addNew.pic_url = res.success_message[0];
                //upload file success then add terminal
                return  page.addNewResources($scope.addNew).then(function(res){
//                    console.log(res);
                    $scope.addAlert('success','添加添加客户新闻成功，3秒后自动关闭！');
                    $timeout(function(){
                        $scope.disabledAddButton = false;//提交成功等三秒button可用，避免重复提交
                        $scope.cancel();
                    },3000);
                },function(error){
                    $scope.addAlert('danger','添加添加客户新闻失败，请稍后再试！');
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

//  $scope.addNews = function() {
//    page.addNewResources($scope.addNew).then(function(res) {
//      console.log(res);
//      $scope.cancel();
//
//    }, function(error) {
//      console.log(error);
//    });
//  }

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

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

    function validParam(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }


};