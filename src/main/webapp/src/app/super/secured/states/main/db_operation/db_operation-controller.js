'use strict';

/**
 * Create namespace.
 */
goog.provide('jxsprsec.main.db_operation.Ctrl');



/**
 * DB operation controller.
 *
 * @constructor
 * @export
 */

jxsprsec.main.db_operation.Ctrl.$inject = ['$scope', '$http', 'i18nService','$q','constants','basic'];
jxsprsec.main.db_operation.Ctrl = function($scope, $http, i18nService,$q,constants,$modal,ModalService,basic) {

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.label = 'some label from db operation controller';

  /**
   * gridOptions似乎必须放在$scope上？简单地改成ctrl运行有错误
   */
  $scope.manager = {
      login_name:undefined,
      page_size:10,
      current_page:0,
      query_date:undefined
  }



  $scope.gridOptions = {
      paginationPageSizes: [10, 20, 50, 100],
      paginationPageSize: 10,
      useExternalPagination: true,
//      useExternalSorting: true,
      columnDefs:[
          {
              field:'login_name',
              name: '账户名',
              width: '10%',
              pinnedLeft: true
          }, {
              field:'full_name',
              name: '真实姓名',
              width: '10%'
          },  {
              field:'gender',
              name: '性别',
              width: '10%',
              cellFilter:'mapGender'
              // pinnedRight: true
          },
          {
              field:'birthday',
              name: '出生日期',
              width: '10%'
              // pinnedRight: true
          },
          {
              field:'phone',
              name: '手机',
              width: '10%'
              // pinnedRight: true
          },
          {
              field:'email',
              name: '邮箱',
              width: '20%'
              // pinnedRight: true
          },{
              name: '操作',
              width: '35%',
//              cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.popup(row.entity)" >修改资料</button></div><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.setRight(row.entity)">设置权限</button></div><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.changeManStatus(row.entity)">{{row.entity.status == 1 ? "禁用" : "启用"}}</button></div><div class="col-md-2"><button class="btn btn-warning btn-xs" ng-click="grid.appScope.deleteManager(row.entity)" >删除</button></div></div>',
              cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.popup(row.entity)" >修改资料</button></div><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.setRight(row.entity)">设置权限</button></div><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.changeManStatus(row.entity)">{{row.entity.status == 1 ? "禁用" : "启用"}}</button></div></div>',

              pinnedRight: true
          }],
      onRegisterApi: function(gridApi) {
          $scope.gridApi = gridApi;
          $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
              //
          });
          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
              $scope.manager.current_page = newPage - 1;
              $scope.manager.page_size = pageSize;

              $scope.getNormalManagers();
          });
      }
  }

    $scope.getNormalManagers = function(){
        basic.getNormalManagers($scope.manager).then(function(data){
            if($scope.validParam(data)){
                $scope.gridOptions.data = data.success_message.normal_managers;
                $scope.gridOptions.totalItems = data.success_message.total_count;
                $scope.manager.query_date = data.success_message.query_date;
            }else{
                console.log(data);
            }
        },function(error){
//            alert(error);
            console.log(error);
        });
    }

    $scope.getNormalManagers();

  i18nService.setCurrentLang('zh-CN');

 ctrl.device_types = [];
  ctrl.device_types_selected = [];
 
  ctrl.device_states=[{'id':'1','state':'正常'},{'id':'2','state':'禁用'}];
  ctrl.device_state_selected={selected:undefined};

     //activate();

  function activate() {
    var promises = [getDeviceTypes()];
    return $q.all(promises).then(function() {
    });
  }

  function getDeviceTypes() {
    return constants.getDeviceTypes().then(function (data) {
      ctrl.device_types = data.device_types;
      ctrl.device_types_selected = [];
      return ctrl.device_types;
    });
  }

  $scope.addManagerModal = function() {
         var modalInstance = $modal.open({
             animation: true,
             templateUrl: 'components/tpls/add_manager_modal/add_manager_modal.html',
             controller: 'AddManagerModalCtrl',
             size:'lg'
         });

      modalInstance.result.then(function() {
          console.log('Modal closed at: ' + new Date());
      }, function() {
          //console.log('Modal dismissed at: ' + new Date());
          $scope.getNormalManagers();
      });
     }

  $scope.popup = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/modify_manager_modal/modify_manager_modal.html',
      controller: 'ModifyManagerModalCtrl',
      size:'lg',
      resolve: {
        entity: function() {

          return entity;
        }
      }
    });
    modalInstance.result.then(function() {
      console.log('Modal closed at: ' + new Date());
    }, function() {
      //console.log('Modal dismissed at: ' + new Date());
        $scope.getNormalManagers();
    });

  };

 $scope.setRight = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/setRight_manager_modal/setRight_manager_modal.html',
      controller: 'SetRightManagerModalCtrl as SetRightManagerModalCtrl',
      size:'lg',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });
    modalInstance.result.then(function() {
      console.log('Modal closed at: ' + new Date());
    }, function() {
        $scope.getNormalManagers();
    });
  };

    $scope.changeManStatus = function(entity){
        if(entity.status == 1){//启用状态
            basic.updateStatus(entity.administrator_id,{status:2}).then(function(data){
                if($scope.validParam(data.success_message)){
                    $scope.addAlert('success','禁用成功!');
                    $scope.getNormalManagers();
                }else{
                    $scope.addAlert('danger','禁用失败!');
                }
            },function(error){
                $scope.addAlert('danger','禁用失败!');
            });
        }else if(entity.status == 2){
            basic.updateStatus(entity.administrator_id,{status:1}).then(function(data){
                if($scope.validParam(data.success_message)){
                    $scope.addAlert('success','启用成功!');
                    $scope.getNormalManagers();
                }else{
                    $scope.addAlert('danger','启用失败!');
                }
            },function(error){
                $scope.addAlert('danger','启用失败!');
            });
        }
    }

    $scope.deleteManager = function(entity){
        if($scope.validParam(entity.administrator_id)){
            basic.deleteManager(entity.administrator_id).then(function(data){
                if($scope.validParam(data.success_message)){
                    $scope.addAlert('success','删除管理员成功!');
                    $scope.getNormalManagers();
                }else{
                    $scope.addAlert('danger','删除管理员失败!');
                }
            },function(error){
                $scope.addAlert('danger','删除管理员出错，请稍后再试!');
            });
        }
    }


 };


 
/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `db_operation.?`
 * child controller.
 *
 * @param {String} text
 */
jxsprsec.main.db_operation.Ctrl.prototype.log = function(text) {
  console.log(text);
};
