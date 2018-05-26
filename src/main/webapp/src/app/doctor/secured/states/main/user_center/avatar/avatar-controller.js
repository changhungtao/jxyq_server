'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.user_center.avatar.Ctrl');
jxdctsec.main.user_center.avatar.Ctrl.$inject = [
   "$scope",
   "basic"
]

/**
 * main.user_center.avatar controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.user_center.avatar.Ctrl = function($scope,basic) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.user_center.avatar controller';

    $scope.avatarFile = undefined;
    //应该从session中获取doctor的信息，加载doctor的头像，暂时写死
    $scope.avatarurl = "../../../img/touxiang.png";
    //$scope.avatarurl = "http://193.160.18.41/server/api/open/download?file_id=109";

    $scope.getDoctorBasics = function(){
        basic.getDoctorBasics().then(function(data){
            if(validParam(data.success_message.avatar_url)){
                $scope.avatarurl = data.success_message.avatar_url;
            }
        },function(error){
            $scope.addAlert("danger","数据加载异常，请稍后再试！");
        });
    }
    //加载医生基本数据
    $scope.getDoctorBasics();

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

    $scope.saveAvatar = function(){
        if(validParam($scope.avatarFile)){
            if(validPic($scope.avatarFile,'png|jpg|jpeg|bmp|gif')){
                basic.fileUpload($scope.avatarFile).then(function(res){
                    $scope.avatarurl = res.success_message[0];
                    //update advar url
                    basic.updateDocAvatar({avatar_url:$scope.avatarurl}).then(function(res){
                        $scope.addAlert("success","上传头像成功！");
                        $scope.getDoctorBasics();
                    },function(error){
                        $scope.addAlert("danger","数据加载异常，请稍后再试！");
                    })
                },function(error){
                    console.log(error);
                    $scope.addAlert("danger","数据加载异常，请稍后再试！");
                });
            }else{
                $scope.addAlert("warning","请上传png、jpg类型的文件！");
            }
        }else{
            $scope.addAlert("warning","请选择文件！");
        }

    }

    var validParam = function(param){
        if(param != undefined && param != null && param != ""){
            return true;
        }
        return false;
    }
};
