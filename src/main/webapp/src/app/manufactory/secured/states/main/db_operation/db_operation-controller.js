'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmnfsec.main.db_operation.Ctrl');



/**
 * DB operation controller.
 *
 * @constructor
 * @export
 */

jxmnfsec.main.db_operation.Ctrl.$inject = [
  '$scope', 
  '$http', 
  '$q',
  'i18nService',
  'constants',
  'db_operation'
];//'db_operation'
jxmnfsec.main.db_operation.Ctrl = function(
  $scope, 
  $http, 
  $q,
  i18nService,
  constants,
  db_operation
) { //,db_operation

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.label = 'some label from db operation controller';

  var NAME_STATUS = 'NAME_STATUS';
  var TYPE_STATUS = "TYPE_STATUS";

  $scope.searchStatus = NAME_STATUS;


  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs:[{
          field:'device_type_id',
          name: '设备类型',
          width: '25%',
          pinnedLeft: true,
          cellFilter: 'mapDeviceType'
        }, {
          field:'terminal_catagory_name',
          name: '终端型号',
          width: '25%'
        }, {
          field:'terminal_name',
          name: '终端名称',
          width: '25%'

        }, {
          field:'activated_at',
          name: '激活时间',
          width: '25%',
          cellFilter: 'unixTodate'
      }
//            , {
//          field:'status',
//          name: '状态',
//          width: '15%',
//          cellFilter: 'mapTerminalStatus'
//      },
//      {
//        name: '操作',
//        width: '15%',
//        cellTemplate: '<div class="col-md-offset-3 col-md-5 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.changeStatus(row.entity)" >{{(row.entity.status == 1 ? "禁止": "开启" )}}</button></div>'
//      }
        ],

      onRegisterApi: function(gridApi) {
        $scope.gridApi = gridApi;
        $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
//                if (sortColumns.length == 0) {
//                    paginationOptions.sort = null;
//                } else {
//                    paginationOptions.sort = sortColumns[0].sort.direction;
//                }
//                //getPage();
        });
        gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
          if ($scope.searchStatus == NAME_STATUS) {
            $scope.terminalSearchNameData.current_page = newPage - 1;
            $scope.terminalSearchNameData.page_size = pageSize;
            $scope.getTerminalsByName();
          } else{
            $scope.terminalSearchTypeData.current_page = newPage - 1;
            $scope.terminalSearchTypeData.page_size = pageSize;
            $scope.getTerminalsByType();
          }
        });
        }
    };


  $scope.terminalSearchNameData = {
    name: '',
    page_size:10,
    current_page:0,
    query_date:undefined
  };

  $scope.terminalSearchTypeData = {
    device_type_ids:undefined,
    status: undefined,
    page_size:10,
    current_page:0,
    query_date:undefined
  };

  $scope.terminal = {
    terminal_id: '',
    terminal_name: '',
    terminal_catagory_id: '',
    terminal_catagory_name: '',
    device_type_id: '',
    activated_at: '',
    status: ''
  };


  $scope.device_types = constants.gotDEVICETYPES();
  $scope.terminal_states = constants.gotTERMINALSTATUS();

  $scope.changeStatus = function(entity){
    entity.status = entity.status == 1 ? 2:1;
    var reqData = {
      status:''
    }
    reqData.status = entity.status;
    console.log(entity);
    db_operation.putTerminal(entity.terminal_id,reqData).then(function (data) {

    });
  };

  // $scope.getDeviceTypes = function() {
  //   return constants.getDeviceTypes().then(function (data) {
  //     $scope.device_types = data.device_types;
  //     return $scope.device_types;
  //   });
  // }

  // $scope.getTerminalStatus = function(){
  //   return constants.getTerminalStatus().then(function(data){
  //     console.log(data.terminal_status_list);
  //     $scope.terminal_states = data.terminal_status_list;
  //   });
  // }

  $scope.getTerminalsByName = function(){
    console.log($scope.terminalSearchNameData);
      db_operation.getSeachedTerminals($scope.terminalSearchNameData).then(function (res) {
        console.log(res);
        $scope.searchStatus = NAME_STATUS;
        $scope.gridOptions.data = res.terminals;
        $scope.terminalSearchNameData.query_date = res.query_date;
        $scope.gridOptions.totalItems = res.total_count;
    });
  }

  $scope.regetTerminalsByName = function(){
    $scope.terminalSearchNameData.current_page = 0;
    $scope.terminalSearchNameData.page_size = 10;
    $scope.gridOptions.paginationCurrentPage = 1; 
    $scope.getTerminalsByName();
  }

  $scope.getTerminalsByType = function(){
      console.log($scope.terminalSearchTypeData);
      db_operation.getTerminalsByType($scope.terminalSearchTypeData).then(function (res) {
        console.log(res);
        $scope.searchStatus = TYPE_STATUS;
        $scope.gridOptions.data = res.terminals;
        $scope.terminalSearchTypeData.query_date = res.query_date;
        $scope.gridOptions.totalItems = res.total_count;
    });
  }

  $scope.regetTerminalsByType = function(){
    $scope.terminalSearchTypeData.current_page = 0;
    $scope.terminalSearchTypeData.page_size = 10;
    $scope.gridOptions.paginationCurrentPage = 1; 
    $scope.getTerminalsByType();
  }

  $scope.activate = function(){
    var promises = [$scope.getTerminalsByName()];
    return $q.all(promises).then(function() {
    });
  }

  //初始化界面
  $scope.activate();



};


 

/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `db_operation.?`
 * child controller.
 *
 * @param {String} text
 */
jxmnfsec.main.db_operation.Ctrl.prototype.log = function(text) {
  console.log(text);
};
