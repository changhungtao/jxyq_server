'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmnfsec.show_cs_template_modal.show_cs_template_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmnfsec.show_cs_template_modal.show_cs_template_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "entity",
  "constants",
  "db_operation",
  "basic"
];
jxmnfsec.show_cs_template_modal.show_cs_template_modal.Ctrl = function($scope, $modalInstance, $q, $timeout, basic, constants, entity, db_operation) {

  var ctrl = this;


  $scope.editTemplate = entity;

  console.log('$scope.editTemplate');
  console.log($scope.editTemplate);

  $scope.newmodel = {
    device_type_id: entity.device_type_id,
    terminal_catagory_id: entity.terminal_catagory_id,
    using_default: $scope.editTemplate.template_type == 1,
    template: entity.template_url
  }

  var getDeviceTypes = function() {
    return constants.getDeviceTypes().then(function(data) {
      $scope.device_types = data.device_types;
    });
  }

  getDeviceTypes();

  var getTemplateTypes = function() {
    return constants.getTemplateTypes().then(function(data) {
      $scope.type_list = data.type_list;
    });
  }
  getTemplateTypes();

  $scope.templatefile = undefined; //原模板
  var uploadupModelfile = function() {
    //原模板
    if (validParam($scope.templatefile)) {
      return basic.fileUpload($scope.templatefile).then(function(res) {
        $scope.newmodel.template = res.success_message[0];
        console.log("上传原模板成功！");
        return res;
      }, function(error) {
        console.log("上传原模板失败！");
        console.log(error);
        return error;
      });
    } else {
      return $q.when(0);
    }
  }

  $scope.putProductModel = function() {

    var promise = [uploadupModelfile()];
    return $q.all(promise).then(function() {
      upload();
    });
  }

  $scope.disenableSaveButton = false;
  var upload = function() {
//    $scope.clearAlerts();
    $scope.disenableSaveButton = true;
    $scope.newmodel.using_default = $scope.editTemplate.template_type == 1;
    return db_operation.putProductModel($scope.newmodel).then(function(res) {
      $scope.addAlert("success","提交成功");
        $timeout(function(){
            $scope.disenableSaveButton = false;
        },3000);

    }, function(error) {
      console.log(error);
      $scope.addAlert("danger","提交失败，请稍后再试！");
    });
  }

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

  $scope.clear = function() {
    $scope.upModelFile = undefined;
  };

 //Alert
  $scope.alerts = [];
  $scope.addAlert = function(type, msg) {
    var alert = {
      'type': type,
      'msg': msg
    };
    $scope.alerts.push(alert);
    $timeout(function() {
      $scope.closeAlert($scope.alerts.indexOf(alert));
    }, 3000);
  };

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };

  $scope.clearAlerts = function() {
    $scope.alerts = [];
  };

  var validParam = function(param) {
    if (param != undefined && param != null && param != "") {
      return true;
    }
    return false;
  }

};
