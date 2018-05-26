'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.db_operation.fat_files.Ctrl');



/**
 * main.db_operation.fat_files controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.db_operation.fat_files.Ctrl.$inject = [
  "$scope",
  "$http",
  "$modal",
  "i18nService",
  "ModalService",
  "db_operation"

];

jxdctsec.main.db_operation.fat_files.Ctrl = function($scope, $http, $modal, $stateParams, i18nService, ModalService, db_operation) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.db_operation.fat_files controller';
  $scope.patient_id = $stateParams.patient_id;
  $scope.file_type = $stateParams.file_type;

  var fileParas = {
    file_type: $scope.file_type,
    page_size: 10,
    current_page: 0,
    query_date: undefined
  }

  var getFatFiles = function() {
    db_operation.getPatFiles($scope.patient_id, fileParas).then(function(data) {
      console.log(data);
      $scope.patient_name = data.patient_name;
      $scope.patient_phone = data.patient_phone;

      $scope.gridOptions.data = data.files;
      fileParas.query_date = data.query_date;
      $scope.gridOptions.totalItems = data.total_count;
    }, function(error) {
      console.log(error);
    });
  }
  getFatFiles();

  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
        field: 'measured_at',
        name: '数据收集时间',
        width: '15%',
        cellFilter: 'unixTodate'
      }, {
        field: 'bmi_value',
        name: 'BMI',
        width: '15%'
      }, {
        field: 'weight_value',
        name: '体重',
        width: '10%'
      }, {
        field: 'fat_value',
        name: '脂肪含量',
        width: '10%'
      }, {
        field: 'calorie_value',
        name: '卡路里',
        width: '10%'
      }, {
        field: 'moisture_value',
        name: '水分含量',
        width: '10%'
      }, {
        field: 'muscle_value',
        name: '肌肉含量',
        width: '10%'
      }, {
        field: 'visceral_fat_value',
        name: '内脏脂肪含量',
        width: '10%'
      }, {
        field: 'bone_value',
        name: '骨骼',
        width: '10%'
      }
      //            ,{
      //                name: '评测',
      //                enableFiltering: false,
      //                width: '10%',
      //                cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.evaluation(row.entity)" >评测</button></div></div>',
      //                pinnedRight: true
      //            }
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
        fileParas.current_page = newPage - 1;
        fileParas.page_size = pageSize;

        getFatFiles();
      });
    }
  }
  i18nService.setCurrentLang('zh-CN');
};
