'use strict';

/**
 * Create namespace.
 */
goog.provide('jxsprsec.main.user_center.avatar.Ctrl');
jxsprsec.main.user_center.avatar.Ctrl.$inject = [
   "$scope",
   "basic"
]


/**
 * main.user_center.avatar controller.
 *
 * @constructor
 * @export
 */
jxsprsec.main.user_center.avatar.Ctrl = function($scope,basic) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.user_center.avatar controller';
   
   $scope.avatarFile = undefined;
    //应该从session中获取manager的信息，加载manager的头像，暂时写死
    $scope.avatarurl = "../../../img/touxiang.png";
    //$scope.avatarurl = "http://193.160.18.41/server/api/open/download?file_id=109";

    $scope.getManagerBasics = function(){
        basic.getManagerBasics().then(function(data){
            if(validParam(data.success_message.avatar_url)){
                $scope.avatarurl = data.success_message.avatar_url;
            }
        },function(error){
            $scope.addAlert("danger","数据加载异常，请稍后再试！");
        });
    }
    //加载管理员基本数据
    $scope.getManagerBasics();

    $scope.saveAvatar = function(){
        if(validParam($scope.avatarFile)){
            if(validPic($scope.avatarFile,'png|jpg|jpeg|bmp|gif')){
                basic.fileUpload($scope.avatarFile).then(function(res){
                    console.log(res);
                    $scope.avatarurl = res.success_message[0];
                    //update advar url
                    basic.updateManAvatar({avatar_url:$scope.avatarurl}).then(function(res){
                        $scope.addAlert("success","上传头像成功");
                        $scope.getManagerBasics();
                    },function(error){
                        $scope.addAlert("danger","上传头像异常，请稍后再试！");
                    })
                },function(error){
                    console.log(error);
                    $scope.addAlert("danger","上传头像异常，请稍后再试！");
                });
            }else{
                $scope.addAlert("warning","请上传png、jpg、gif类型的文件！");
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

};
