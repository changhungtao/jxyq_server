'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.check_users_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.check_users_modal.Ctrl.$inject = [
  "$scope",
  "$filter",
  "$modalInstance",
  '$q',
  "constants",
  "i18nService",
  "db_operation",
  "entity"
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.check_users_modal.Ctrl = function($scope, $filter,$modalInstance,$q,constants,i18nService,db_operation,entity) {


  $scope.product_types=[];
  $scope.product_types_selected={selected:undefined};



  activate();

  function activate() {
    var promises = [getProductTypes()];
    return $q.all(promises).then(function() {
    });
  }

  function getProductTypes() {
    return constants.getProductTypes().then(function (data) {
      $scope.product_types = data.product_types;

      return $scope.product_types;
    });
  }

   $scope.gridOptions = {
    paginationPageSizes: [10, 20, 30],
    paginationPageSize: 10,
  };

  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions.columnDefs = [{
    field:'user_phone',
    name: '用户手机',
    width: '25%',
    pinnedLeft: true
  }, {
    field:'user_name',
    name: '用户名称',
    width: '25%'
  },
  {
    field:'used_count',
    name: '使用次数',
    width: '25%'
  },
  {
    field:'last_used_at',
    name: '最近使用时间',
    width: '25%',
    cellFilter: 'unixTodate'
  }];
  $scope.gridOptions.enableFiltering = false;
   $scope.gridOptions.enableCellEdit = false;

  $scope.gridOptions.data = [{
    'user_phone':'12345678901',
    'user_name':'xxx',
    'used_count': '12',
    'last_used_at': '2015-5-10',
  },{
    'user_phone':'12345678901',
    'user_name':'xxx',
    'used_count': '12',
    'last_used_at': '2015-5-10',
  }];
  /**
   * @type {String}
   * @nocollapse
   */
    $scope.gridOptions.onRegisterApi=function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
//                if (sortColumns.length == 0) {
//                    paginationOptions.sort = null;
//                } else {
//                    paginationOptions.sort = sortColumns[0].sort.direction;
//                }
//                //getPage();
            });

          }
  db_operation.getTerminalDetails(entity.terminal_id).then(function(data){
    console.log(data);
    $scope.gridOptions.data = data.used_by;
    

  });
 
db_operation
  $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
