'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.permission.doctor_permission.Ctrl');



/**
 * Doctor permission controller.
 *
 * @constructor
 * @export
 */

jxmgrsec.main.permission.doctor_permission.Ctrl.$inject = [
  '$scope', 
  '$http', 
  '$q',
  '$filter',
  '$state',
  'i18nService',
  'constants',
  'doctor'
];
jxmgrsec.main.permission.doctor_permission.Ctrl = function($scope,$http,$q,$filter,$state,i18nService,constants,doctor) {

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.label = 'some label from db operation controller';
    $scope.query_date = {
        registered_from:new Date('2014/01/01'),
        registered_to:new Date()
    }
    $scope.query_date.registered_to.setHours(23);
    $scope.query_date.registered_to.setMinutes(59);
    $scope.query_date.registered_to.setSeconds(59);

    $scope.doctor = {
        full_name:'',
        registered_from :$scope.query_date.registered_from,
        registered_to :$scope.query_date.registered_to,
        page_size:10,
        current_page:0,
        query_date:undefined
    }

    $scope.queryDoctors = function(){
        $scope.doctor.registered_from = $filter('dateTounix')($filter('date')($scope.query_date.registered_from,'yyyy-M-dd H:mm:ss'));
        $scope.doctor.registered_to = $filter('dateTounix')($filter('date')($scope.query_date.registered_to,'yyyy-M-dd H:mm:ss'));

        doctor.getDoctors($scope.doctor).then(function(data){
            $scope.gridOptions.data = data.doctors;
            $scope.gridOptions.totalItems = data.total_count;
            $scope.doctor.query_date = data.query_date;
        },function(error){
            console.log(error);
        });
    }

    i18nService.setCurrentLang('zh-CN');

    $scope.queryDoctors();

    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs:[{
            field: "full_name",
            name: '医生姓名',
            width: '25%',
            pinnedLeft: true
        }, {
            field: 'registered_at',
            name: '注册时间',
            width: '25%',
            cellFilter: 'unixTodate'
        }, {
            field: 'status',
            name: '状态',
            width: '25%',
            cellFilter:'mapDoctorStatus'
        }, {
            name: '操作',
            width: '25%',
            cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.editPermission(row.entity)" >编辑权限</button></div></div>'
        }],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                //
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.doctor.current_page = newPage - 1;
                $scope.doctor.page_size = pageSize;

                $scope.queryDoctors();
            });
        }
    }

    $scope.editPermission = function(entity) {
        console.log("编辑权限");
        console.log(entity);
        var params = {"doctor_id":entity.doctor_id,"full_name":entity.full_name};
        $state.go('main.permission.doctor_permission_list',params);
//        switch (entity.file_type) {
//            case 2://血压
//                $state.go('main.db_operation.sphygmomanometer_files',params);
//                break;
//            case 4://血糖
//                $state.go('main.db_operation.glucosemeter_files',params);
//                break;
//            case 6://脂肪
//                $state.go('main.db_operation.fat_files',params);
//                break;
//            case 5://体温
//                $state.go('main.db_operation.thermometer_files', params);
//                break;
//            case 1://手环
//                $state.go('main.db_operation.wristband_files', params);
//                break;
//            case 3://血氧
//                $state.go('main.db_operation.oximeter_files', params);
//                break;
//        };

    };

    $scope.dt = new Date();
    $scope.minDate = new Date('1900/01/01');

//    Disable weekend selection
//    date-disabled="disabled(date, mode)"
//    $scope.disabled = function(date, mode) {
//        return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
//    };

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
 * Example function that we'd like to access in our `doctor_permission.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.permission.doctor_permission.Ctrl.prototype.log = function(text) {
  console.log(text);
};
