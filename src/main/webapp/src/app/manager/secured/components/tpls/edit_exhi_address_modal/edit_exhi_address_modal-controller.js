'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.edit_exhi_address_modal.edit_exhi_address_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.edit_exhi_address_modal.edit_exhi_address_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$timeout",
  "entity",
  "page",
  "basic"
];
jxmgrsec.edit_exhi_address_modal.edit_exhi_address_modal.Ctrl = function($scope, $modalInstance,$timeout , entity, page,basic) {

  /**
   * @type {String}
   * @nocollapse
   */
  var ctrl = this;
  console.log(entity);

  $scope.modifyExh = {
    exhibition_resource_id: entity.exhibition_resource_id,
    pic_url: entity.pic_url
  }

  $scope.publicize_pic = undefined;

  $scope.disabledBtn = false;
  $scope.modifyExhibition = function() {
      $scope.disabledBtn = true;
      if(validParam($scope.publicize_pic)){
          if(!validPic($scope.publicize_pic,'png|jpg|jpeg|bmp|gif')){$scope.alerts = [];$scope.addAlert("warning","请上传png、jpg、gif类型的文件！");$scope.disabledBtn = false;return;}
          return basic.fileUpload($scope.publicize_pic).then(function(data){
              $scope.modifyExh.pic_url = data.success_message[0];
              if(validParam($scope.modifyExh.pic_url)){
                  page.modifyExhibitionResources($scope.modifyExh).then(function(res) {
                      $scope.addAlert('success','更改图片成功，3秒后自动关闭！');
                      $timeout(function(){
                          $scope.disabledBtn = false;
                          $scope.cancel();
                      },3000);
                  }, function(error) {
                      console.log(error);
                      $scope.disabledBtn = false;
                      $scope.addAlert('danger','更改图片出错！');
                  });
              }
          },function(error){
//              console.log('上传文件出错！');
              $scope.disabledBtn = false;
              $scope.addAlert('danger','更改图片出错！');
          });
      }else{
//          console.log('文件无效！');
          $scope.disabledBtn = false;
          $scope.addAlert('danger','图片文件无效！');
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
};
