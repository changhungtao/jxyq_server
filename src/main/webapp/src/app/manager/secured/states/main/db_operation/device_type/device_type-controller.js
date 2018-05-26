'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.db_operation.device_type.Ctrl');



/**
 * main.db_operation.device_type controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.db_operation.device_type.Ctrl.$inject = [
  "$scope",
  "$http",
  "$modal",
  "$state",
  '$filter',
  '$q',
  "$window",
  "i18nService",
  "ModalService",
  'constants',
  'db_operation'
];
jxmgrsec.main.db_operation.device_type.Ctrl = function(
  $scope,
  $http,
  $modal,
  $state,
  $filter,
  $q,
  $window,
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
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    enableFiltering:false,
    enableCellEdit:false
  };

  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions.columnDefs = [{
    field: 'device_type_id',
    name: '设备类型名称',
    width: '25%',
    cellFilter: 'mapDeviceType',
    pinnedLeft: true
  }, {
    field: 'product_type_id',
    name: '隶属产品类别',
    width: '25%',
    cellFilter: 'mapProductType'
  }, {
    field: 'registered_at',
    name: '注册时间',
    width: '25%',
    cellFilter: 'unixTodate'
  }, {
    name: '操作',
    width: '25%',
    cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.checkManufacturies(row.entity)" >查看生产厂商</button></div><div class="col-md-offset-2 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-disabled="true" ng-click="grid.appScope.modifyDeviceType(row.entity)">编辑设备类型</button></div></div>'
  }, ];
  $scope.gridOptions.enableFiltering = false;
  $scope.gridOptions.enableCellEdit = false;


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

  ctrl.device_types = constants.gotDEVICETYPES();
  ctrl.device_types_selected = [];

  ctrl.registered_from = new Date('2014-01-01');
  ctrl.registered_to = new Date();
  ctrl.registered_to.setHours(23);
  ctrl.registered_to.setMinutes(59);
  ctrl.registered_to.setSeconds(59);

  ctrl.label = 'some other label from main.db_operation.product_type controller';

  $scope.queryFile = function() {
    $scope.searchStatus = QUERY_STATUS;
    ctrl.jsonData = {
      device_type_ids: ctrl.device_types_selected,
      registered_from: $filter('dateTounix')($filter('date')(ctrl.registered_from, 'yyyy-M-dd H:mm:ss')),
      registered_to: $filter('dateTounix')($filter('date')(ctrl.registered_to, 'yyyy-M-dd H:mm:ss')),
      page_size: $scope.page_param.page_size,
      current_page: $scope.page_param.current_page
    }
      if (ctrl.jsonData.registered_from > ctrl.jsonData.registered_to) {
          $scope.addAlert("danger", "开始时间不能早于结束时间");
          return;
      };
    db_operation.postDBDeviceTypes(ctrl.jsonData).then(function(data) {
      $scope.gridOptions.data = data.device_types;
      $scope.gridOptions.totalItems = data.total_count;
    });
  }


  ctrl.devicekeyWord = "";
  $scope.searchFile = function() {

    $scope.searchStatus = SEARCH_STATUS;

    ctrl.jsonData1 = {
      name: ctrl.devicekeyWord,
      registered_from: $filter('dateTounix')($filter('date')(ctrl.registered_from, 'yyyy-M-dd H:mm:ss')),
      registered_to: $filter('dateTounix')($filter('date')(ctrl.registered_to, 'yyyy-M-dd H:mm:ss')),
      page_size: $scope.page_param.page_size,
      current_page: $scope.page_param.current_page,
      query_date: undefined
    };
    db_operation.fuzzySearchDeviceTypes(ctrl.jsonData1).then(function(data) {

      $scope.gridOptions.data = data.device_types;
      $scope.gridOptions.totalItems = data.total_count;
    });
  }




  $scope.checkManufacturies = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/check_manufacturers_modal/check_manufacturers_modal.html',
      controller: 'CheckManufacturersModalCtrl',
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

  $scope.modifyDeviceType = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/modify_device_type_modal/modify_device_type_modal.html',
      controller: 'ModifyDeviceTypeModalCtrl',
      resolve: {
        entity: function() {

          return entity;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.queryFile();
    });
  }

  ctrl.addDeviceType = function() {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/add_device_type_modal/add_device_type_modal.html',
      controller: 'AddDeviceTypeModalCtrl'

    });
    modalInstance.result.then(function() {
    }, function() {
        $window.location.reload();
//      $scope.page_param.query_date = undefined;
//      $scope.queryFile();
    });
  }

  $scope.dt = new Date();

  // Disable weekend selection
  $scope.disabled = function(date, mode) {
    return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
  };

  $scope.toggleMin = function() {
    $scope.minDate = $scope.minDate ? null : new Date();
  };
  $scope.toggleMin();

  $scope.open_form_1 = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.form1_opened = true;
  };

  $scope.open_form_2 = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.form2_opened = true;
  };




  $scope.dateOptions = {
//    formatYear: 'yy',
    startingDay: 1
  };

  $scope.formats = ['yyyy-MM-dd', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
  $scope.format = $scope.formats[0];

  var tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  var afterTomorrow = new Date();
  afterTomorrow.setDate(tomorrow.getDate() + 2);
  $scope.events = [{
    date: tomorrow,
    status: 'full'
  }, {
    date: afterTomorrow,
    status: 'partially'
  }];

  $scope.getDayClass = function(date, mode) {
    if (mode === 'day') {
      var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

      for (var i = 0; i < $scope.events.length; i++) {
        var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

        if (dayToCheck === currentDay) {
          return $scope.events[i].status;
        }
      }
    }

    return '';
  };



  $scope.searchFile();

};
