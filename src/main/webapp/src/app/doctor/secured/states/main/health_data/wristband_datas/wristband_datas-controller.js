'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_data.wristband_datas.Ctrl');



/**
 * main.health_data.wristband_datas controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.health_data.wristband_datas.Ctrl.$inject = [
  "$scope",
  "$http",
  "$modal",
  "$stateParams",
  "i18nService",
  "ModalService",
  "health_data",
  "$filter"
];
jxdctsec.main.health_data.wristband_datas.Ctrl = function($scope,$http,$modal,$stateParams,i18nService,ModalService,health_data,$filter) {

  /**
   * @type {String}
   * @nocollapse
   */
  $scope.permission_name = $stateParams.permission_name;
  this.label = 'some other label from main.db_operation.wristband_files controller';
  var ctrl=this;
  i18nService.setCurrentLang('zh-CN');

    $scope.page_param = {
        page_size:10,
        current_page:0,
        query_date:undefined
    }

    $scope.queryWristbandDatas = function(){
        health_data.getPermissionData($stateParams.permission_id, $scope.page_param).then(function(res){
            console.log('手环数据');
            console.log(res);
            $scope.gridOptions.data = res.data_list;
            $scope.page_param.query_date = res.query_date;
            $scope.gridOptions.totalItems = res.total_count;
        });
    }
    $scope.queryWristbandDatas();
    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs:[
            {
                field: "phone",
                name: '手机号码',
                width: '8%',
                pinnedLeft: true,
                grouping: { groupPriority: 0}
            },  {
                field: "nick_name",
                name: '昵称',
                width: '8%',
                pinnedLeft: true
            },{
                field: 'measured_at',
                name: '数据收集时间',
                width: '13%',
                cellFilter: 'unixTodate'
            }, {
                field: 'step_count',
                name: '步数',
                width: '7%'
            }, {
                field: 'walk_count',
                name: '行走步数',
                width: '10%'
            }, {
                field: 'run_count',
                name: '跑步步数',
                width: '10%'
            }, {
                field: 'distance',
                name: '距离',
                width: '7%'
            },  {
                field: 'walk_distance',
                name: '行走距离',
                width: '10%'
            }, {
                field: 'run_distance',
                name: '跑步距离',
                width: '10%'
            }, {
                field: 'calories',
                name: '消耗能量',
                width: '10%'
            },{
                field: 'walk_calories',
                name: '行走消耗能量',
                width: '13%'
            },{
                field: 'run_calories',
                name: '跑步消耗能量',
                width: '13%'
            },{
                field: 'deep_duration',
                name: '深睡时间',
                width: '10%'
//                pinnedRight: true
            },
            {
                field: 'shallow_duration',
                name: '浅睡时间',
                width: '10%'
            },
            {
                field: 'heart_rate',
                name: '心率',
                width: '7%'
//                pinnedRight: true
            },
            {
                name: '评测',
                enableFiltering: false,
                width: '10%',
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
//               if($scope.consultation.pageSize != pageSize){
//                $scope.consultation.query_date = $filter('dateTounix')($filter('date')(new Date(),'yyyy-M-dd H:mm:ss'));
//               }
//                console.log(newPage);
//                console.log(pageSize);
                $scope.page_param.current_page = newPage - 1;
                $scope.page_param.page_size = pageSize;

                $scope.queryWristbandDatas();
            });
        }
    }

//  $http.get('https://rawgit.com/angular-ui/ui-grid.info/gh-pages/data/500_complex.json')
//    .success(function(data) {
//
//    });


  $scope.evaluation = function(entity) {
    var modalInstance = $modal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'components/tpls/health_data_operation_modals/evaluation_wristband_modal.html',
      controller: 'WristbandEvaluationCtrl',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });

    modalInstance.result.then(function() {
      //console.log('Modal closed at12121212: ' + new Date());
    }, function() {
      console.log('Modal dismissed at: ' + new Date());
        //是date类型才转换
        if(isNaN(entity.measured_at)){
            entity.measured_at = $filter('dateTounix')(entity.measured_at);
        }
    });

  };
};

