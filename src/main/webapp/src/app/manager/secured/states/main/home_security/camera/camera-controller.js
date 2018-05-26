'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.home_security.camera.Ctrl');



/**
 * Camera controller.
 *
 * @constructor
 * @export
 */

jxmgrsec.main.home_security.camera.Ctrl.$inject = [
  '$scope',
  '$http',
  '$q',
  'i18nService',
  'constants',
  'home_security'
];
jxmgrsec.main.home_security.camera.Ctrl = function($scope, $http, $q, i18nService, constants, home_security) {
  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.home_security.camera controller';
  var ctrl = this;

  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
      field: 'dev_nickname',
      name: '用户名称',
      width: '20%',
      pinnedLeft: true
    },
    {
      field: 'user_name',
      name: '用户昵称',
      width: '20%'
    },
    {
      field: 'user_phone',
      name: '用户手机',
      width: '25%'
    }, {
      field: 'dev_uid',
      name: 'UID',
      width: '20%'
    }, {
      field: 'dev_pwd',
      name: '密码',
      width: '20%'
    }
//        , {
//      name: '操作',
//      width: '20%',
//      cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="" >解除绑定</button></div></div>'
//    }
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
      gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
        //               if($scope.consultation.pageSize != pageSize){
        //                $scope.consultation.query_date = $filter('dateTounix')($filter('date')(new Date(),'yyyy-M-dd H:mm:ss'));
        //               }
        $scope.cameraPhone.current_page = newPage - 1;
        $scope.cameraPhone.page_size = pageSize;

        $scope.getCameras();
      });
    }
  };

  // $scope.gridOptions.enableFiltering = false;
  // $scope.gridOptions.enableCellEdit = false;

  // $scope.gridOptions.data = [{
  //   'dev_name': '用户1',
  //   'dev_uid': '0001',
  //   'dev_pwd': '123456'

  // }, {
  //   'dev_name': '用户2',
  //   'dev_uid': '0002',
  //   'dev_pwd': '123456'

  // }, {
  //   'dev_name': '用户3',
  //   'dev_uid': '0003',
  //   'dev_pwd': '123456'

  // }];
  var query_date = -1;
  $scope.cameraPhone = {
    user_phone: undefined,
    page_size: 10,
    current_page: 0,
    query_date: query_date
  }

  
  $scope.getCameras = function() {
    console.log("00000000000");
    home_security.getCameras($scope.cameraPhone).then(function(res) {
      console.log(res);
      $scope.gridOptions.data = res.cameras;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.cameraPhone.query_date = res.query_date;

    }, function(error) {
      console.log(error);
    });
  }
  $scope.getCameras();


};




/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `camera.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.home_security.camera.Ctrl.prototype.log = function(text) {
  console.log(text);
};
