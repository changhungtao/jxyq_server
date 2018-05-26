'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmnfsec.main.health_data.Ctrl');


jxmnfsec.main.health_data.Ctrl.$inject= [
    "$scope",
    "constants",
    "i18nService",
    "db_operation"
];
/**
 * Health data controller.
 *
 * @constructor
 * @export
 */
jxmnfsec.main.health_data.Ctrl = function($scope,constants,i18nService,db_operation) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from health data controller';
  //console.log(constants.gotDATATYPES());
  $scope.dataTypes = constants.gotDATATYPES();

    var numArry = function(count) {
        var nums = [];
        for (var i = 1; i <= count; i++) {
            nums.push(i);
        }
        return nums;
    };
    $scope.ages = numArry(120);

    $scope.healthDataOptions = {
        data_type_id:1,
        terminal_catagory_name:undefined,
        terminal_name:undefined,
        user_phone:undefined,
        begin_age:1,
        end_age:60,
        page_size:10,
        current_page:0,
        query_date:undefined
    };

    $scope.displayOptions = {
        all:true,
        others:false
//        wristband:false,
//        sphygmomanometer:false,
//        oximeter:false,
//        glucosemeter:false,
//        thermometer:false,
//        fat:false
    }

    i18nService.setCurrentLang('zh-CN');

    var allColumns  = [
        {
            field: "data_type_id",
            name: '数据类型',
            width: '8%',
            cellFilter: 'mapDataType',
            pinnedLeft: true
        }, {
            field: 'user_name',
            name: '用户名称',
            width: '8%',
            pinnedLeft: true
        }, {
            field: 'phone',
            name: '用户手机',
            width: '8%',
            pinnedLeft: true
        }, {
            field: 'nick_name',
            name: '用户昵称',
            width: '8%',
            pinnedLeft: true
        }, {
            field: 'terminal_catagory_name',
            name: '终端型号名称',
            width: '10%'
        }, {
            field: 'terminal_name',
            name: '终端名称',
            width: '15%'
        }, {
            field: 'user_age',
            name: '用户年龄',
            width: '10%'
        },{
            field: 'measured_at',
            name: '测量时间',
            width: '15%',
            pinnedLeft: true,
            cellFilter: 'unixTodate'
        }
    ];

    var wristband = [
         {
            field: 'step_count',
            name: '步数',
            width: '10%'
        }, {
            field: 'walk_count',
            name: '行走步数',
            width: '8%'
        }, {
            field: 'run_count',
            name: '跑步步数',
            width: '8%'
        }, {
            field: 'distance',
            name: '距离',
            width: '5%'
        }, {
            field: 'walk_distance',
            name: '行走距离',
            width: '8%'
        }, {
            field: 'run_distance',
            name: '跑步距离',
            width: '8%'
        }, {
            field: 'calories',
            name: '消耗能量',
            width: '8%'
        }, {
            field: 'walk_calories',
            name: '行走消耗能量',
            width: '10%'
        }, {
            field: 'run_calories',
            name: '跑步消耗能量',
            width: '10%'
        }, {
            field: 'deep_duration',
            name: '深睡时间',
            width: '10%'
        }, {
            field: 'shallow_duration',
            name: '浅睡时间',
            width: '10%'
        }, {
            field: 'heart_rate',
            name: '心率',
            width: '10%'
        }, {
            field: 'status',
            name: '评测状态',
            width: '10%',
            cellFilter:'mapHalthDataStatus'
        }, {
            field: 'proposal',
            name: '医生建议',
            width: '10%'
        }
    ];

    var sphygmomanometer = [
         {
            field: 'systolic_pressure',
            name: '收缩压',
            width: '10%'
        }, {
            field: 'diastolic_pressure',
            name: '舒张压',
            width: '10%'
        }, {
            field: 'heart_rate',
            name: '心率',
            width: '10%'
        }, {
            field: 'status',
            name: '评测状态',
            width: '10%',
            cellFilter:'mapHalthDataStatus'
        }, {
            field: 'proposal',
            name: '医生建议',
            width: '10%'
        }
    ];

    var oximeter = [
         {
            field: 'oximeter_value',
            name: '血氧值',
            width: '10%'
        }, {
            field: 'status',
            name: '评测状态',
            width: '10%',
            cellFilter:'mapHalthDataStatus'
        }, {
            field: 'proposal',
            name: '医生建议',
            width: '10%'
        }
    ];

    var glucosemeter = [
         {
            field: 'period',
            name: '测量时间段',
            width: '10%',
            cellFilter: 'mapPeriods'
        }, {
            field: 'glucosemeter_value',
            name: '血糖值',
            width: '10%'
        }, {
            field: 'status',
            name: '评测状态',
            width: '10%',
            cellFilter:'mapHalthDataStatus'
        }, {
            field: 'proposal',
            name: '医生建议',
            width: '10%'
        }
    ];

    var thermometer = [
         {
            field: 'thermometer_value',
            name: '体温',
            width: '10%'
        }, {
            field: 'status',
            name: '评测状态',
            width: '10%',
            cellFilter:'mapHalthDataStatus'
        }, {
            field: 'proposal',
            name: '医生建议',
            width: '10%'
        }
    ];

    var fat = [
         {
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
        }, {
            field: 'status',
            name: '评测状态',
            width: '10%',
            cellFilter:'mapHalthDataStatus'
        }, {
            field: 'proposal',
            name: '医生建议',
            width: '10%'
        }
    ];

/*---------------------------------------------------------------------------------*/
    var unionArry =function(arry){
        if($scope.validParam(arry)){
            for(var i = 0;i < arry.length;i++){
                allColumns.push(arry[i]);
            }
        }
    };

    var unionColumns = function(type){
        allColumns.splice(8);
        switch (type){
            case 1:unionArry(wristband);return allColumns;break;
            case 2:unionArry(sphygmomanometer);return allColumns;break;
            case 3:unionArry(oximeter);return allColumns;break;
            case 4:unionArry(glucosemeter);return allColumns;break;
            case 5:unionArry(thermometer);return allColumns;break;
            case 6:unionArry(fat);return allColumns;break;
        }
    };

    $scope.queryHealthDatas = function(){
        if($scope.validParam($scope.healthDataOptions) && $scope.displayOptions.all){
            db_operation.getHealthDatas($scope.healthDataOptions).then(function(res){
                $scope.gridOptions.data = res.health_data_list;
                $scope.gridOptions.totalItems = res.total_count;
                $scope.healthDataOptions.query_date = res.query_date;
            },function(error){
                console.log(error);
            });
        }
    };

    $scope.dataTypeChanged = function(){
        //每次改变删除其余元素，不然数组会越来越长
        allColumns.splice(8);
        $scope.displayOptions.all = true;
        $scope.displayOptions.others = false;
        unionColumns($scope.healthDataOptions.data_type_id);
//        console.log($scope.healthDataOptions.data_type_id);
//        console.log(allColumns);
        $scope.gridOptions = {
            paginationPageSizes: [10, 20, 50, 100],
            paginationPageSize: 10,
            useExternalPagination: true,
//            useExternalSorting: true,
            columnDefs: allColumns,
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                });
                gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {

                    $scope.healthDataOptions.current_page = newPage - 1;
                    $scope.healthDataOptions.page_size = pageSize;

                    $scope.queryHealthDatas();
                });
            }
        }
        $scope.gridOptions.data = [];//清空上次的数据
        $scope.queryHealthDatas();
    }

    $scope.dataTypeChanged();//初始化ui-grid
    $scope.queryHealthDatas();//第一次默认调用
/*---------------------------------------------------------------------------------*/

    $scope.displayOthers = function(){
        $scope.healthDataOptions.data_type_id = undefined
        $scope.displayOptions.all = false;
        $scope.displayOptions.others = true;

        //wristband
        $scope.gridOptions_wristband = {
            paginationPageSizes: [10, 20, 50, 100],
            paginationPageSize: 10,
            useExternalPagination: true,
//        useExternalSorting: true,
            columnDefs: angular.copy(unionColumns(1)),
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                });
                gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
                    $scope.healthDataOptions.current_page = newPage - 1;
                    $scope.healthDataOptions.page_size = pageSize;

                    queryOthers(1,$scope.gridOptions_wristband);
                });
            }
        }

        //sphygmomanometer
        $scope.gridOptions_sphygmomanometer = {
            paginationPageSizes: [10, 20, 50, 100],
            paginationPageSize: 10,
            useExternalPagination: true,
//        useExternalSorting: true,
            columnDefs: angular.copy(unionColumns(2)) ,
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                });
                gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
                    $scope.healthDataOptions.current_page = newPage - 1;
                    $scope.healthDataOptions.page_size = pageSize;

                    queryOthers(2,$scope.gridOptions_sphygmomanometer);
                });
            }
        }
        //oximeter
        $scope.gridOptions_oximeter = {
            paginationPageSizes: [10, 20, 50, 100],
            paginationPageSize: 10,
            useExternalPagination: true,
//        useExternalSorting: true,
            columnDefs:  angular.copy(unionColumns(3)),
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                });
                gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
                    $scope.healthDataOptions.current_page = newPage - 1;
                    $scope.healthDataOptions.page_size = pageSize;

                    queryOthers(3,$scope.gridOptions_oximeter);
                });
            }
        }
        //glucosemeter
        $scope.gridOptions_glucosemeter = {
            paginationPageSizes: [10, 20, 50, 100],
            paginationPageSize: 10,
            useExternalPagination: true,
//        useExternalSorting: true,
            columnDefs: angular.copy(unionColumns(4)) ,
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                });
                gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
                    $scope.healthDataOptions.current_page = newPage - 1;
                    $scope.healthDataOptions.page_size = pageSize;

                    queryOthers(4,$scope.gridOptions_glucosemeter);
                });
            }
        }
        //thermometer
        $scope.gridOptions_thermometer = {
            paginationPageSizes: [10, 20, 50, 100],
            paginationPageSize: 10,
            useExternalPagination: true,
//        useExternalSorting: true,
            columnDefs: angular.copy(unionColumns(5)) ,
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                });
                gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
                    $scope.healthDataOptions.current_page = newPage - 1;
                    $scope.healthDataOptions.page_size = pageSize;

                    queryOthers(5,$scope.gridOptions_thermometer);
                });
            }
        }
        //fat
        $scope.gridOptions_fat = {
            paginationPageSizes: [10, 20, 50, 100],
            paginationPageSize: 10,
            useExternalPagination: true,
//        useExternalSorting: true,
            columnDefs:  angular.copy(unionColumns(6)),
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                });
                gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
                    $scope.healthDataOptions.current_page = newPage - 1;
                    $scope.healthDataOptions.page_size = pageSize;

                    queryOthers(6,$scope.gridOptions_fat);
                });
            }
        }

        //wristband
        queryOthers(1,$scope.gridOptions_wristband);

        //sphygmomanometer
        queryOthers(2,$scope.gridOptions_sphygmomanometer);

        //oximeter
        queryOthers(3,$scope.gridOptions_oximeter);

        //glucosemeter
        queryOthers(4,$scope.gridOptions_glucosemeter);

        //thermometer
        queryOthers(5,$scope.gridOptions_thermometer);

        //fat
        queryOthers(6,$scope.gridOptions_fat);
    }

    function queryOthers(data_type_id,gridOptions){
        if($scope.validParam(data_type_id) && $scope.validParam($scope.healthDataOptions)){
            $scope.healthDataOptions.data_type_id = data_type_id;
            var reqData = angular.copy($scope.healthDataOptions);
            db_operation.getHealthDatas(reqData).then(function(res){
                gridOptions.data = res.health_data_list;
//                console.log(gridOptions.data);
                gridOptions.totalItems = res.total_count;
                $scope.healthDataOptions.query_date = res.query_date;
                $scope.healthDataOptions.data_type_id = undefined;
            },function(error){
                console.log(error);
            });
        }
    }

    function copyReqData(data_type_id){
        $scope.healthDataOptions.data_type_id = data_type_id;
        return angular.copy($scope.healthDataOptions);
    }
};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `main.health_data.?`
 * child controller.
 *
 * @param {String} text
 */
jxmnfsec.main.health_data.Ctrl.prototype.log = function(text) {
  console.log(text);
};
