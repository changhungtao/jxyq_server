'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.terminal_category.Ctrl');



/**
 * main.db_operation.terminal_category controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.db_operation.terminal_category.Ctrl.$inject = [
  "$scope",
  "$http",
  "$modal",
  "$state",
  '$filter',
  '$q',
  "i18nService",
  "ModalService",
  'constants',
  'db_operation'
];
jxmgrsec.main.db_operation.terminal_category.Ctrl = function(
  $scope,
  $http,
  $modal,
  $state,
  $filter,
  $q,
  i18nService,
  ModalService,
  constants,
  db_operation
) {

    i18nService.setCurrentLang('zh-CN');

    $scope.device_types = constants.gotDEVICETYPES();
//    console.log($scope.device_types);
    $scope.terminal_catelogry = {
        manufactory_name:undefined,
        device_type_id:undefined,
        terminal_catagory_name:undefined,
        page_size:10,
        current_page:0,
        query_date:undefined
    };

    $scope.getTerminalLists = function(){
        if($scope.terminal_catelogry.terminal_catagory_name == ''){$scope.terminal_catelogry.terminal_catagory_name = undefined;}
        if($scope.terminal_catelogry.manufactory_name == ''){$scope.terminal_catelogry.manufactory_name = undefined;}
        return db_operation.postTerminalCatagorise($scope.terminal_catelogry).then(function(data){
            console.log(data);
            $scope.gridOptions.data = data.catagories;
            $scope.gridOptions.totalItems = data.total_count;
            $scope.terminal_catelogry.query_date = data.query_date;
        },function(error){
            console.log(error);
        });
    }

    $scope.getTerminalLists();

    $scope.defaultPic = "../../img/default.png";

    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs: [{
            field: "terminal_catagory_name",
            name: '终端型号名称',
            width: '15%',
            pinnedLeft: true
        },{
            field: 'manufactory_name',
            name: '厂商名称',
            width: '10%'
        }, {
            field: 'code',
            name: '终端型号编码',
            width: '10%'
        }, {
            field: 'device_type_id',
            name: '设备类型',
            width: '8%',
            cellFilter: 'mapDeviceType'
        }, {
            field: 'price',
            name: '价格',
            width: '8%'
        }, {
//            field: 'picture',
            name: '终端型号图片',
            width: '10%',
            cellTemplate:'<div><a  href={{row.entity.picture}} target="_blank" ng-if="row.entity.picture"><span style="color: royalblue">查看</span></a></div>'
        }, {
            field: 'profile',
            name: '终端型号描述',
            width: '10%'
        }, {
            field: 'created_at',
            name: '添加时间',
            width: '10%',
            cellFilter: 'unixTodate'
        }, {
            field: 'status',
            name: '终端型号状态',
            width: '10%',
            cellFilter:'mapTerminalStatus'
        }, {
            name: '操作',
            width: '20%',
//            cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-4"><button class="btn col-md-12 btn-primary btn-xs" ng-click="grid.appScope.editTerminalCatagory(row.entity)" >编辑</button></div><div class="col-md-offset-1 col-md-4"><button  class="btn col-md-12 btn-primary btn-xs" ng-click="grid.appScope.updateStatus(row.entity)">{{row.entity.status == 1 ? "禁用" : "启用"}}</button></div></div>'
            cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-4"><button  class="btn col-md-12 btn-primary btn-xs" ng-click="grid.appScope.updateStatus(row.entity)">{{row.entity.status == 1 ? "禁用" : "启用"}}</button></div></div>'

        }],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
            });
            gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
                $scope.terminal_catelogry.current_page = newPage - 1;
                $scope.terminal_catelogry.page_size = pageSize;

                $scope.getTerminalLists();
            });
        }
    }

//    $scope.addTerminalCatagory = function(){
//        var modalInstance = $modal.open({
//            animation: true,
//            templateUrl: 'components/tpls/terminal_catagory_modal/add_terminal_catagory_modal/add_terminal_catagory_modal.html',
//            controller: 'AddTerminalCategoryCtrl'
//        });
//        modalInstance.result.then(function() {
//        }, function() {
//            $scope.terminal.query_date = undefined;
//            $scope.getTerminalLists();
//        });
//    }

//    $scope.editTerminalCatagory = function(entity){
//        var modalInstance = $modal.open({
//            animation: true,
//            templateUrl: 'components/tpls/terminal_catagory_modal/edit_terminal_catagory_modal/edit_terminal_catagory_modal.html',
//            controller: 'EditTerminalCategoryCtrl',
//            resolve: {
//                entity: function() {
//                    return entity;
//                }
//            }
//        });
//        modalInstance.result.then(function() {
//        }, function() {
//            $scope.terminal.query_date = undefined;
//            $scope.getTerminalLists();
//        });
//    }

    $scope.updateStatus = function(entity){
        if($scope.validParam(entity.status)){
            if(entity.status == 1){
                return db_operation.updateTerCatSataus(entity.manufactory_id,entity.terminal_catagory_id,{status:2}).then(function(){
                    $scope.getTerminalLists();
                });
            }else if(entity.status == 2){
                return db_operation.updateTerCatSataus(entity.manufactory_id,entity.terminal_catagory_id,{status:1}).then(function(){
                    $scope.getTerminalLists();
                });
            }
        }
    }
};
