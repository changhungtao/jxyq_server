'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.home_security.button.Ctrl');



/**
 * Button controller.
 *
 * @constructor
 * @export
 */

jxmgrsec.main.home_security.button.Ctrl.$inject = [
  '$scope',
  '$http',
  '$q',
  'i18nService',
  'constants',
  'home_security'
];
jxmgrsec.main.home_security.button.Ctrl = function($scope, $http, $q, i18nService, constants, $modal, home_security) {
  /**
   * @type {String}
   * @nocollapse
   */

  this.label = 'some other label from main.home_security.button controller';
  var ctrl = this;

  i18nService.setCurrentLang('zh-CN');
  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
      field: 'touch_button_id',
      name: '触摸按钮编号',
      width: '16%',
      pinnedLeft: true
    }, {
      field: 'name',
      name: '触摸按钮名称',
      width: '16%'
    }, {
      field: 'uid',
      name: '触摸按钮uid',
      width: '16%'
    },{
      field: 'user_phone',
      name: '用户手机',
      width: '16%'
    },
    {
      field: 'user_name',
      name: '用户昵称',
      width: '16%'
    }, {
      name: '操作',
      width: '20%',
      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.popup(row.entity)" >查看事件</button></div></div>'
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
        $scope.buttonPhone.current_page = newPage - 1;
        $scope.buttonPhone.page_size = pageSize;

        $scope.getTouchButtons();
      });
    }
  };

  //$scope.gridOptions.enableFiltering = false;
  //$scope.gridOptions.enableCellEdit = false;

  // $scope.gridOptions.data = [{
  //   'touch_button_id': '0001',
  //   'name': '按钮1',
  //   'uid': '123456'

  // }, {
  //   'touch_button_id': '0002',
  //   'name': '按钮2',
  //   'uid': '123456'

  // }, {
  //   'touch_button_id': '0003',
  //   'name': '按钮3',
  //   'uid': '123456'

  // }];

  var query_date = -1;
  $scope.buttonPhone = {
    user_phone: undefined,
    page_size: 10,
    current_page: 0,
    query_date: query_date
  }

  $scope.getTouchButtons = function() {
    home_security.getTouchButtons($scope.buttonPhone).then(function(res) {
      console.log(res);
      $scope.gridOptions.data = res.touch_buttons;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.buttonPhone.query_date = res.query_date;

    }, function(error) {
      console.log(error);
    });
  }
  $scope.getTouchButtons();

  $scope.popup = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/show_touch_button_modal/show_touch_button_modal.html',
      controller: 'ShowTouchButtonModalCtrl',
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




};


/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `button.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.home_security.button.Ctrl.prototype.log = function(text) {
  console.log(text);
};
