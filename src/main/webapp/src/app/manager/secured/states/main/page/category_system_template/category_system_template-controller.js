'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.page.category_system_template.Ctrl');



/**
 * Category system template controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.page.category_system_template.Ctrl.$inject = [
  '$scope',
  '$http',
  '$q',
  '$timeout',
  'i18nService',
  'constants',
  'page',
  'basic'
];
jxmgrsec.main.page.category_system_template.Ctrl = function($scope, $http, i18nService, $q, $timeout,constants, $modal, ModalService, page, basic) {

  this.label = 'some label from category template controller';
  var ctrl = this;


  $scope.web = {
    template: ''
  }

  $scope.getSystemTemplate = function() {
    page.getWebTemplates().then(function(res) {
      console.log(res);
      $scope.web.template = res.template;

    }, function(error) {
      console.log(error);
    });
  }
  $scope.getSystemTemplate();

  $scope.templatefile = undefined; //系统模板
  var uploadtemplatefile = function() {
    //系统模板
    if (validParam($scope.templatefile)) {
      return basic.fileUpload($scope.templatefile).then(function(res) {
        $scope.web.template = res.success_message[0];
//        console.log("上传系统模板成功！");
        return res;
      }, function(error) {
//        console.log("上传系统模板失败！");
        console.log(error);
        return error;
      });
    } else {
      return $q.when(0);
    }
  }

  $scope.disabledBtn = false;
  $scope.modifySystemTemplate = function() {
      if(validParam($scope.templatefile)){
          if(validPic($scope.templatefile,'png|jpg|jpeg|bmp|gif')){
              $scope.disabledBtn = true;
              var promise = [uploadtemplatefile()];
              return $q.all(promise).then(function() {
                  page.modifyWebTemplates($scope.web).then(function(res) {
//        console.log(res);
                      $scope.addAlert("success", "修改成功！");
                      $timeout(function(){
                          $scope.disabledBtn = false;
                      },3000);
                  }, function(error) {
                      console.log(error);
                      $scope.addAlert("danger", "保存失败,请稍后再试！");
                      $scope.disabledBtn = false;
                  })
              });
          }else{
              $scope.addAlert("warning","请上传png、jpg、gif类型的文件！");
          }
      }else{
          $scope.addAlert("warning","请选择文件！");
      }
  }


  var validParam = function(param) {
    if (param != undefined && param != null && param != "") {
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

/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `category_system_template.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.page.category_system_template.Ctrl.prototype.log = function(text) {
  console.log(text);
};
