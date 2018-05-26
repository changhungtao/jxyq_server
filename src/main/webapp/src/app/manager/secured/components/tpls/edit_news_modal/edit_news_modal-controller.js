'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.edit_news_modal.edit_news_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.edit_news_modal.edit_news_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$timeout",
  "entity",
  "page",
  "basic"
];
jxmgrsec.edit_news_modal.edit_news_modal.Ctrl = function($scope, $modalInstance,$timeout, entity, page,basic) {

  /**
   * @type {String}
   * @nocollapse
   */
  var ctrl = this;
  console.log(entity);

    $scope.news_pic = undefined;

  $scope.modifyNew = {
    news_resource_id: entity.news_resource_id,
    pic_url: entity.pic_url,
    news_link: entity.news_link
  }

  $scope.disabledBtn = false;
  $scope.modifyNews = function() {
      $scope.disabledBtn = true;
      if(validParam($scope.news_pic)){
          if(!validPic($scope.news_pic,'png|jpg|jpeg|bmp|gif')){$scope.alerts = [];$scope.addAlert("warning","请上传png、jpg、gif类型的文件！");$scope.disabledBtn = false;return;}
          return basic.fileUpload($scope.news_pic).then(function(data){
              $scope.modifyNew.pic_url = data.success_message[0];
              if(validParam($scope.modifyNew.pic_url)){
                  page.modifyNewResources($scope.modifyNew).then(function(res) {
                      $scope.addAlert('success','更改图片成功，3秒钟后自动关闭！');
                      $timeout(function(){
                          $scope.cancel();
                          $scope.disabledBtn = false;
                      },3000);
                  }, function(error) {
//                      console.log(error);
                      $scope.addAlert('danger','更改图片出错！');
                      $scope.disabledBtn = false;
                  });
              }
          },function(error){
              console.log('上传文件出错！');
              $scope.addAlert('danger','更改图片出错！');
              $scope.disabledBtn = false;
          });
      }else{
          console.log('文件无效！');
          $scope.addAlert('danger','图片文件无效！');
          $scope.disabledBtn = false;
      }
  }


  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

    var validParam = function(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
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
};
