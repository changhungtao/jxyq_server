'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.health_data.Ctrl');



/**
 * Terminal category controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.health_data.Ctrl.$inject = [
  "$scope",
  "$http",
  "$modal",
  "$state",
  '$filter',
  '$q',
  "i18nService",
  "ModalService",
  "constants",
  "health_data"
];
jxmgrsec.main.health_data.Ctrl = function(
    $scope, 
    $http, 
    $modal, 
    $state,
    $filter,
    $q,
    i18nService, 
    ModalService, 
    constants,
    health_data
  ){

  /**
   * @type {String}
   * @nocollapse
   */

  var ctrl = this;

  $scope.query_date = {
      measured_from: new Date('2014-01-01'),
      measured_to: new Date()
  }
  $scope.query_date.measured_to.setHours(23);
  $scope.query_date.measured_to.setMinutes(59);
  $scope.query_date.measured_to.setSeconds(59);

  $scope.measurement = {
      data_type_ids:[1],
      status:1,
      measured_from:$scope.query_date.created_from,
      measured_to:$scope.query_date.created_to,
      phone:undefined,
      page_size:10,
      current_page:0,
      query_date:undefined
  }

//    $scope.device_types = constants.gotDEVICETYPES();
    $scope.data_types = constants.gotDATATYPES();

    $scope.evaluated_status = constants.gotHEALTHDATASTATUS();

    $scope.getMeasurements = function(){
        if($scope.validParam($scope.measurement)){
            $scope.measurement.measured_from = $filter('dateTounix')($filter('date')($scope.query_date.measured_from, 'yyyy-M-dd H:mm:ss'));
            $scope.measurement.measured_to = $filter('dateTounix')($filter('date')($scope.query_date.measured_to, 'yyyy-M-dd H:mm:ss'));

            if($scope.measurement.phone == ""){
                $scope.measurement.phone = undefined;
            }

            //后需要传得是数组，但只取第一个元素
            if($scope.measurement.data_type_ids[0] == undefined){
                var arry = [$scope.measurement.data_type_ids];
                $scope.measurement.data_type_ids = arry;
            }

         return  health_data.getMeasurements($scope.measurement).then(function(res){
             $scope.gridOptions.data = res.measurements;
             $scope.gridOptions.totalItems = res.total_count;
             $scope.measurement.query_date = res.query_date;
         });
        }
    }

    $scope.getMeasurements();


    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs:[
            {
                field:'data_type',
                name: '数据类型',
                width: '10%',
                cellFilter:'mapDataType',
                pinnedLeft: true
            },{
                field:'user_full_name',
                name: '用户名称',
                width: '10%',
                pinnedLeft: true
            },{
                field:'user_nick_name',
                name: '用户昵称',
                width: '10%',
                pinnedLeft: true
            },{
                field:'user_phone',
                name: '用户手机',
                width: '10%',
                pinnedLeft: true
            },{
                field:'terminal_name',
                name: '终端名称',
                width: '15%'
//                pinnedLeft: true
            },{
                field:'manufactory_name',
                name: '厂商名称',
                width: '15%'
            },{
                field:'measured_at',
                name: '数据收集时间',
                width: '15%',
                cellFilter:'unixTodate'
            },{
                field:'status',
                name: '评测状态',
                width: '10%',
                cellFilter:'mapHalthDataStatus'
            },{
                name: '操作',
                width: '15%',
                cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.modifyHealData(row.entity)" >编辑健康数据</button></div></div>',
                pinnedRight: true
            }],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                //
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.measurement.current_page = newPage - 1;
                $scope.measurement.page_size = pageSize;

                $scope.getMeasurements();
            });
        }
    }

  i18nService.setCurrentLang('zh-CN');

  var templateUrls= [
      'components/tpls/health_data_modals/wristband_modal/wristband_modal.html',
      'components/tpls/health_data_modals/sphygmomanometer_modal/sphygmomanometer_modal.html',
      'components/tpls/health_data_modals/oximeter_modal/oximeter_modal.html',
      'components/tpls/health_data_modals/glucosemeter_modal/glucosemeter_modal.html',
      'components/tpls/health_data_modals/thermometer_modal/thermometer_modal.html',
      'components/tpls/health_data_modals/fat_modal/fat_modal.html'
  ];

   var terminal_types = [
       'wristband',
       'sphygmomanometer',
       'oximeter',
       'glucosemeter',
       'thermometer',
       'fat'
   ];

  var controller_list = [
      'WristbandModalCtrl',
      'SphygmomanometerModalCtrl',
      'OximeterModalCtrl',
      'GlucosemeterModalCtrl',
      'ThermometerModalCtrl',
      'FatModalCtrl'
  ];

  $scope.modifyHealData=function(entity){
    var type = entity.data_type;
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: templateUrls[type -1],
      controller: controller_list[type -1],
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

    $scope.dt = new Date();
    $scope.minDate = new Date('1900/01/01');

    $scope.toggleMin = function() {
        $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.open1 = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened1 = true;
    };

    $scope.dateOptions = {
//        formatYear: 'yy',
        startingDay: 1
    };

    $scope.formats = ['yyyy-MM-dd', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[2];

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



};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `main.health_data.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.health_data.Ctrl.prototype.log = function(text) {
  console.log(text);
};
