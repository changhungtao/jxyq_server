'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_data.fat_datas.Ctrl');



/**
 * main.health_data.fat_datas controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.health_data.fat_datas.Ctrl.$inject = [
  "$scope",
  "$http",
  "$modal",
  "$filter",
  "i18nService",
  "ModalService",
  "health_data"
  
];

jxdctsec.main.health_data.fat_datas.Ctrl = function($scope,$http,$modal,$stateParams,$filter,i18nService,ModalService,health_data) {
  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.health_data.fat_datas controller';
    $scope.permission_name = $stateParams.permission_name;
//    console.log("test:");
//    console.log($scope.permission_name);
//    console.log($stateParams.permission_id);

    $scope.page_param = {
        page_size:10,
        current_page:0,
        query_date:undefined
    }

    $scope.queryFatDatas = function(){
        health_data.getPermissionData($stateParams.permission_id, $scope.page_param).then(function(res){
            console.log('脂肪数据');
            console.log(res);
            $scope.gridOptions.data = res.data_list;
            $scope.page_param.query_date = res.query_date;
            $scope.gridOptions.totalItems = res.total_count;
        });
    }
    $scope.queryFatDatas();

    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs:[
            {
                field: "phone",
                name: '手机号码',
                width: '10%',
                pinnedLeft: true,
                grouping: { groupPriority: 0}
            },  {
                field: "nick_name",
                name: '昵称',
                width: '10%',
                pinnedLeft: true
            }, {
                field: 'measured_at',
                name: '数据收集时间',
                width: '15%',
                cellFilter: 'unixTodate'
            }, {
                field: 'bmi_value',
                name: 'BMI',
                width: '5%'
            }, {
                field: 'weight_value',
                name: '体重',
                width: '5%'
            }, {
                field: 'bone_value',
                name: '骨骼',
                width: '5%'
            },  {
                field: 'calorie_value',
                name: '卡路里',
                width: '8%'
            }, {
                field: 'fat_value',
                name: '脂肪含量',
                width: '8%'
            },{
                field: 'moisture_value',
                name: '水分含量',
                width: '8%'
            }, {
                field: 'muscle_value',
                name: '肌肉含量',
                width: '8%'
            }, {
                field: 'visceral_fat_value',
                name: '内脏脂肪含量',
                width: '8%'
            },{
                name: '评测',
                enableFiltering: false,
                width: '15%',
                cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.evaluation(row.entity)" >评测</button></div></div>',
                pinnedRight: true
            }
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
                $scope.page_param.current_page = newPage - 1;
                $scope.page_param.page_size = pageSize;

                $scope.queryFatDatas();
            });
        }
    }
  i18nService.setCurrentLang('zh-CN');

  $scope.evaluation = function(entity) {
    var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'components/tpls/health_data_operation_modals/evaluation_fat_modal.html',
      controller: 'FatEvaluationCtrl',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });

    modalInstance.result.then(function() {

      //console.log('Modal closed at: ' + new Date());
    }, function() {
      //console.log('Modal dismissed at: ' + new Date());
        //是date类型才转换
        if(isNaN(entity.measured_at)){
            entity.measured_at = $filter('dateTounix')(entity.measured_at);
        }
    });

  };

};
