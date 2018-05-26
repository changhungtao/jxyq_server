'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.permission.doctor_permission_list.Ctrl');



/**
 * Doctor permission controller.
 *
 * @constructor
 * @export
 */

jxmgrsec.main.permission.doctor_permission_list.Ctrl.$inject = [
  '$scope', 
  '$http', 
  '$q',
  '$modal',
  '$stateParams',
  '$window',
  'i18nService',
  'constants',
  'doctor'
];
jxmgrsec.main.permission.doctor_permission_list.Ctrl = function($scope, $http, $q,$stateParams,$modal, $window,i18nService,constants,doctor) {

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.label = 'some label from db operation controller';

  $scope.doctor_id = $stateParams.doctor_id;
  //$scope.doctor_id = 3;
  $scope.full_name = $stateParams.full_name;

    console.log($scope.doctor_id);
    console.log($scope.full_name);

    $scope.pageParams = {
        page_size:10,
        current_page:0,
        query_date:undefined
    }

    $scope.getDoctorPermissions = function(){
        if($scope.validParam($scope.doctor_id) && $scope.validParam($scope.pageParams)){
            doctor.getDoctorPermissions($scope.doctor_id,$scope.pageParams).then(function(data){
                if($scope.validParam(data)){
                    $scope.gridOptions.data = data.permission_list;
                    $scope.gridOptions.totalItems = data.total_count;
                    $scope.pageParams.query_date = data.query_date;
                }
            },function(error){
                alert(error);
            });
        }else{
            alert('医生ID不能为空！');
        }

    }

    i18nService.setCurrentLang('zh-CN');

    $scope.getDoctorPermissions();

    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs:[{
            field: "permission_name",
            name: '权限名称',
            width: '15%',
            pinnedLeft: true
        }, {
            field: 'district_id',
            name: '区域编号',
            width: '15%',
            cellFilter: 'mapDistrict'
        }, {
            field: 'province_id',
            name: '省份',
            width: '15%',
            cellFilter:'mapProvince'
        }, {
            field: 'city_id',
            name: '城市',
            width: '15%',
            cellFilter:'mapCity'
        }, {
            field: 'zone_id',
            name: '区县',
            width: '15%',
            cellFilter:'mapZone'
        }, {
            field: 'data_type',
            name: '数据类型',
            width: '15%',
            cellFilter:'mapDataType'
            }, {
            name: '筛选条件',
            width: '10%',
            cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.editPermission(row.entity)" >编辑</button></div></div>'
        }],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                //
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.pageParams.current_page = newPage - 1;
                $scope.pageParams.page_size = pageSize;

                $scope.getDoctorPermissions();
            });
        }
    }

    $scope.editPermission = function(entity){
        var modalInstance = $modal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'components/tpls/doctor_permission_modal/edit_doctor_permission_modal/edit_doctor_permission_modal.html',
            controller: 'EditDoctorPermissionModalCtrl as editDocPermissionModalCtrl',
            size:'lg',
            resolve: {
                entity: function() {
                    return entity;
                },
                doctor_id:function(){
                    return $scope.doctor_id;
                }
            }
        });
        modalInstance.result.then(function() {
            console.log('Modal closed at: ' + new Date());
        }, function() {
            //console.log('Modal dismissed at: ' + new Date());
            //$window.location.reload();

        });
    }

    //pop add doctor permission modal
    $scope.openAddPermissionModal = function(){
        var modalInstance = $modal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'components/tpls/doctor_permission_modal/add_doctor_permission_modal/add_doctor_permission_modal.html',
            controller: 'AddDoctorPermissionModalCtrl as addDocPermissionModalCtrl',
            size:'lg',
            resolve: {
                doctor_id:function(){
                    return $scope.doctor_id;
                }
            }
        });
        modalInstance.result.then(function() {
            console.log('Modal closed at: ' + new Date());
        }, function() {
            //console.log('Modal dismissed at: ' + new Date());
            //$window.location.reload();
            $scope.getDoctorPermissions();

        });
    }

};




 

/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `doctor_permission_list.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.permission.doctor_permission_list.Ctrl.prototype.log = function(text) {
  console.log(text);
};
