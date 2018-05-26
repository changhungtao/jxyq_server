'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmnfsec.main.category_template.Ctrl');



/**
 * Category template controller.
 *
 * @constructor
 * @export
 */
jxmnfsec.main.category_template.Ctrl.$inject = [
  '$scope',
  '$http',
  'i18nService',
  '$q',
  'constants',
  'db_operation'
];
jxmnfsec.main.category_template.Ctrl = function($scope, $http, i18nService, $q, constants, $modal, db_operation) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.category_template controller';
  var ctrl = this;

  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
      field: 'device_type_id',
      name: '设备类型',
      width: '25%',
      pinnedLeft: true,
      cellFilter: 'mapDeviceType'
    }, {
      field: 'terminal_catagory_name',
      name: '终端型号',
      width: '25%'
    }, {
      field: 'template_type',
      name: '模板类型',
      width: '25%',
      cellFilter: 'mapTemplateType'
    }, {
      name: '操作',
      width: '25%',
      cellTemplate: '<div class="row"><div class="col-md-offset-2 col-md-2 text-center"><button class="btn btn-primary btn-sm" style="width:120px" ng-click="grid.appScope.popup(row.entity)" >编辑</button></div></div>'
    }],
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
      gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
        //               if($scope.consultation.pageSize != pageSize){
        //                $scope.consultation.query_date = $filter('dateTounix')($filter('date')(new Date(),'yyyy-M-dd H:mm:ss'));
        //               }
        $scope.dbOperation.current_page = newPage - 1;
        $scope.dbOperation.page_size = pageSize;

        $scope.getTerminalList();
      });
    }

  };

  $scope.dbOperation = {
    device_type_id: undefined,
    terminal_catagory_name: undefined,
    page_size: 10,
    current_page: 0,
    query_date: undefined
  }

  $scope.device_types = constants.gotDEVICETYPES();

  $scope.getTerminalList = function() {
    db_operation.getTerminalCatagory($scope.dbOperation).then(function(res) {
      console.log(res);
      $scope.gridOptions.data = res.terminals;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.dbOperation.query_date = res.query_date;

    }, function(error) {
      console.log(error);
    });
  }
  $scope.getTerminalList();

  var refreshdbOperation = {
    device_type_id: $scope.dbOperation.device_type_id,
    terminal_catagory_name: $scope.dbOperation.terminal_catagory_name,
    page_size: 10,
    current_page: 0,
    query_date: undefined
  }

  var refresh = function() {
    db_operation.getTerminalCatagory(refreshdbOperation).then(function(res) {
      console.log(res);
      $scope.gridOptions.data = res.terminals;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.dbOperation.query_date = res.query_date;

    }, function(error) {
      console.log(error);
    });
  }

  // var getDeviceTypes = function() {
  //   return constants.getDeviceTypes().then(function(data) {
  //     $scope.device_types = data.device_types;
  //   });
  // }

  // getDeviceTypes();

  $scope.popup = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/show_cs_template_modal/show_cs_template_modal.html',
      controller: 'ShowCSTemplateModalCtrl',
      resolve: {
        entity: function() {

          return entity;
        }
      }
    });
    modalInstance.result.then(function() {
      console.log('Modal closed at: ' + new Date());
    }, function() {
      refresh();
      console.log('Modal dismissed at: ' + new Date());
    });

  };

  /* var validParam = function(param) {
     if (param != undefined && param != null && param != "") {
       return true;
     }
     return false;
   }*/

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

  $scope.validParam = function(param) {
    if (param != undefined && param != null && param != "") {
      return true;
    }
    return false;
  }

};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `category_template.?`
 * child controller.
 *
 * @param {String} text
 */
jxmnfsec.main.category_template.Ctrl.prototype.log = function(text) {
  console.log(text);
};
