'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.terminal.Ctrl');



/**
 * main.db_operation.terminal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.db_operation.terminal.Ctrl.$inject = [
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
jxmgrsec.main.db_operation.terminal.Ctrl = function(
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

  var QUERY_STATUS = "QUERY_STATUS";
  var SEARCH_STATUS = "SEARCH_STATUS";

  $scope.searchStatus = QUERY_STATUS;

  $scope.page_param = {
    page_size: 30,
    current_page: 0,
    query_date: undefined
  }

  var ctrl = this;
  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 30],
    paginationPageSize: 30,
    useExternalPagination: true,
//    useExternalSorting: true
  };

  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions.columnDefs = [{
      field: 'terminal_name',
      name: '终端名称',
      width: '22%',
      pinnedLeft: true
    }, {
      field: 'device_type_id',
      name: '设备类型',
      width: '10%',
      cellFilter: 'mapDeviceType'
    }, {
      field: 'manufactory_name',
      name: '隶属厂商',
      width: '24%'
    }, {
      field: 'activated_at',
      name: '激活时间',
      width: '20%',
      cellFilter: 'unixTodate'
    }, {
      field: 'status',
      name: '状态',
      width: '8%',
      cellFilter: 'mapTerminalStatus'
    }, {
      name: '操作',
      width: '16%',
      cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.checkTerminal(row.entity)" >查看详情</button></div><div class="col-md-offset-3 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.checkUsers(row.entity)" >用户列表</button></div></div>'
    },

  ];
  $scope.gridOptions.enableFiltering = false;
  $scope.gridOptions.enableCellEdit = false;

  // $scope.gridOptions.data = [{
  //   'terminal_name':'name1',
  //   'device_type_id':1,
  //   'manufactory_id': 1,
  //   'activated_at': '2015-5-10',
  //   'status':1

  // }];
  /**
   * @type {String}
   * @nocollapse
   */
  $scope.gridOptions.onRegisterApi = function(gridApi) {
      $scope.gridApi = gridApi;
      $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {

      });

      gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
        if ($scope.searchStatus == QUERY_STATUS) {
          $scope.page_param.current_page = newPage - 1;
          $scope.page_param.page_size = pageSize;
          $scope.queryFile();
        } else {
          $scope.page_param.current_page = newPage - 1;
          $scope.page_param.page_size = pageSize;
          $scope.searchFile();
        }

      });


    }
    /**
     * @type {String}
     * @nocollapse
     */
  ctrl.label = 'some other label from main.db_operation.terminal controller';

  ctrl.device_types = [];
  ctrl.device_types_selected = [];

  ctrl.device_states = [];
  ctrl.device_state_selected = undefined;

  ctrl.manufacturers = [];
  ctrl.manufacturer_selected = [];







  $scope.checkTerminal = function(entity) {

    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/check_terminal_modal/check_terminal_modal.html',
      controller: 'CheckTerminalModalCtrl',
      size: 'lg',
      resolve: {
        entity: function() {

          return entity;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {

    });
  }

  $scope.checkUsers = function(entity) {

    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/check_users_modal/check_users_modal.html',
      controller: 'CheckUsersModalCtrl',
      size: 'lg',
      resolve: {
        entity: function() {

          return entity;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {

    });
  }



  $scope.queryFile = function() {
    $scope.searchStatus = QUERY_STATUS;

    ctrl.jsonData = {
      manufactory_ids: ctrl.manufacturer_selected,
      device_type_ids: ctrl.device_types_selected,
      status: ctrl.device_state_selected,
      page_size: $scope.page_param.page_size,
      current_page: $scope.page_param.current_page,
      query_date: undefined
    };
    db_operation.postTerminals(ctrl.jsonData).then(function(data) {

      $scope.gridOptions.data = data.terminals;
      $scope.gridOptions.totalItems = data.total_count;
    });
  }

  ctrl.fuzzy_search = '';
  $scope.searchFile = function() {
    console.log(ctrl.fuzzy_search);
    $scope.searchStatus = SEARCH_STATUS;

    ctrl.jsonData1 = {
      full_name: ctrl.fuzzy_search,
      page_size: $scope.page_param.page_size,
      current_page: $scope.page_param.current_page,
      query_date: undefined
    };
    db_operation.fuzzySearchTerminals(ctrl.jsonData1).then(function(data) {

      $scope.gridOptions.data = data.terminals;
      $scope.gridOptions.totalItems = data.total_count;
    });
  }

  activate();

  function activate() {
    var promises = [getDeviceTypes(), getTerminalStatusList(), getManufacturers()];
    return $q.all(promises).then(function() {});
  }

  function getManufacturers() {
    return constants.getManufactories().then(function(data) {
      ctrl.manufacturers = data.manufactories;
      console.log(data.manufactories);
      return ctrl.manufacturers;
    });
  }


  function getDeviceTypes() {
    return constants.getDeviceTypes().then(function(data) {
      ctrl.device_types = data.device_types;
      console.log(data.device_types);
      return ctrl.device_types;
    });
  }

  function getTerminalStatusList() {
    return constants.getTerminalStatus().then(function(data) {
      ctrl.device_states = data.terminal_status_list;
      console.log(data.terminal_status_list);
      return ctrl.device_states;
    });
  }

  $scope.searchFile();
};
