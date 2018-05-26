'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.push_message.Ctrl');



/**
 * Push message controller.
 *
 * @constructor
 * @export
 */

jxmgrsec.main.push_message.Ctrl.$inject = [
  '$scope',
  '$http',
  '$q',
  'i18nService',
  'constants',
  'push_message'
];
jxmgrsec.main.push_message.Ctrl = function($scope, $http, $q, $timeout, i18nService, constants, $modal, push_message) {
  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.push_message controller';
  var ctrl = this;

  $scope.device_types = constants.gotDEVICETYPES();

  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [ {
      field: 'terminal_catagory_name',
      name: '终端型号名称',
      width: '25%'
    }, {
      field: 'device_type_id',
      name: '设备类型',
      width: '20%',
      cellFilter: 'mapDeviceType'
    },{
        field: 'manufactory_name',
        name: '厂商名称',
        width: '30%',
        pinnedLeft: true
    }, {
      name: '操作',
      width: '25%',
      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.popup(row.entity)" >推送</button></div></div>'
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
        $scope.catagoryList.current_page = newPage - 1;
        $scope.catagoryList.page_size = pageSize;

        $scope.queryCatagoryList();
      });
    }
  };

  var query_date = -1;
  $scope.catagoryList = {
    manufactory_name: undefined,
    device_type_id: undefined,
    terminal_catagory_name: undefined,
    page_size: 10,
    current_page: 0,
    query_date: query_date
  }

  $scope.queryCatagoryList = function() {
    push_message.getCatagoryList($scope.catagoryList).then(function(res) {
//      console.log(res);
      $scope.gridOptions.data = res.catagories;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.catagoryList.query_date = res.query_date;
    }, function(error) {
      console.log(error);
    });
  }
  $scope.queryCatagoryList();

  $scope.popup = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/show_push_message_modal/show_push_message_modal.html',
      controller: 'ShowPushMessageModalCtrl',
      resolve: {
        entity: function() {

          return entity;
        }
      }
    });
    modalInstance.result.then(function() {
//      console.log('Modal closed at: ' + new Date());
    }, function() {
//      console.log('Modal dismissed at: ' + new Date());
    });

  };

  var validParam = function(param) {
    if (param != undefined && param != null && param != "") {
      return true;
    }
    return false;
  }

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


};




/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `push_message.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.push_message.Ctrl.prototype.log = function(text) {
  console.log(text);
};
