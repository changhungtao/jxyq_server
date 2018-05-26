'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.permission.manufactory_permission.Ctrl');



/**
 * Manufactory permission controller.
 *
 * @constructor
 * @export
 */

jxmgrsec.main.permission.manufactory_permission.Ctrl.$inject = [
  '$scope', 
  '$http', 
  '$q',
  '$filter',
  '$modal',
  'i18nService',
  'constants',
  'manufactory'
];
jxmgrsec.main.permission.manufactory_permission.Ctrl = function($scope, $http, $q,$filter,$modal,i18nService,constants,manufactory) {

  var ctrl = this;
  /**
   * @type {String}
   * @nocollapse
   */
  ctrl.label = 'some label from manufactory permission controller';

    $scope.query_date = {
        registered_from:new Date('2014/01/01'),
        registered_to:new Date()
    }
    $scope.query_date.registered_to.setHours(23);
    $scope.query_date.registered_to.setMinutes(59);
    $scope.query_date.registered_to.setSeconds(59);

    $scope.manufactory = {
        manufactory_name:undefined,
        device_type_ids:undefined,
        registered_from:$scope.query_date.registered_from,
        registered_to:$scope.query_date.registered_to,
        page_size:10,
        current_page:0,
        query_date:undefined
    }

    $scope.device_types = constants.gotDEVICETYPES();
//    console.log('filter');
//    console.log($filter('mapDeviceType')(1));

    $scope.queryManufactory = function(){
        $scope.manufactory.registered_from = $filter('dateTounix')($filter('date')($scope.query_date.registered_from,'yyyy-M-dd H:mm:ss'));
        $scope.manufactory.registered_to = $filter('dateTounix')($filter('date')($scope.query_date.registered_to,'yyyy-M-dd H:mm:ss'));
        manufactory.getManufactories($scope.manufactory).then(function(data){
            //console.log(data);
            if($scope.validParam(data.manufactories.length)){
                for(var j = 0; j < data.manufactories.length;j++){
                    if($scope.validParam(data.manufactories[j].device_type_ids.length && data.manufactories[j].device_type_ids.length != 0)){
                        var device_types = data.manufactories[j].device_type_ids;
                        var device_types_str = '';
                        for(var i = 0;i < device_types.length;i++){
                            device_types_str += $filter('mapDeviceType')(device_types[i]) + ', ';
                        }
                        data.manufactories[j].device_type_str = device_types_str;
                    }else if(data.manufactories[j].device_type_ids.length == 0){
                        data.manufactories[j].device_type_str = '无';
                    }
                }
            }
//            console.log('data.manufactories');
//            console.log(data.manufactories);
            $scope.gridOptions.data = data.manufactories;
            $scope.gridOptions.totalItems = data.total_count;
            $scope.manufactory.query_date = data.query_date;
            console.log(data);
        },function(error){
            console.log(error);
        });
    }

    $scope.queryManufactory();

    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs:[{
            field: "manufactory_name",
            name: '厂商名称',
            width: '20%',
            pinnedLeft: true
        }, {
            field: 'registered_at',
            name: '注册时间',
            width: '25%',
            cellFilter: 'unixTodate'
        }, {
            field: 'device_type_str',
            name:'设备类型',
            width: '20%',
            cellFilter: ''
        }, {
            field: 'status',
            name: '状态',
            width: '20%',
            cellFilter:'mapManufactoryStatus'
        }, {
            name: '操作',
            width: '20%',
            cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.editManufactory(row.entity)" >编辑</button></div></div>'
        }],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                //
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.manufactory.current_page = newPage - 1;
                $scope.manufactory.page_size = pageSize;

                $scope.queryManufactory();
            });
        }
    }

    $scope.editManufactory = function(entity){
        var modalInstance = $modal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'components/tpls/manufactory_permission_modal/edit_manufactory_permission_modal/edit_manufactory_permission_modal.html',
            controller: 'EditManufactoryPermissionModalCtrl as editManPermissionModalCtrl',
            resolve: {
                entity: function() {
                    return entity;
                }
            }
        });
        modalInstance.result.then(function() {
            console.log('Modal closed at: ' + new Date());
        }, function() {
            $scope.queryManufactory();
            //console.log('Modal dismissed at: ' + new Date());
            //$window.location.reload();
        });
    }


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
 * Example function that we'd like to access in our `manufactory_permission.?`
 * child controller.
 *
 * @param {String} text
 */
jxmgrsec.main.permission.manufactory_permission.Ctrl.prototype.log = function(text) {
  console.log(text);
};
