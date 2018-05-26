'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.show_touch_button_modal.show_touch_button_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.show_touch_button_modal.show_touch_button_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "entity",
  "$filter",
  "home_security"
];
jxmgrsec.show_touch_button_modal.show_touch_button_modal.Ctrl = function($scope, $filter, $modalInstance, entity, home_security) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from custom modal controller';
  var ctrl = this;
//  console.log(entity);

  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
      field: 'touch_button_event_id',
      name: '触摸按钮事件编号',
      width: '20%',
      pinnedLeft: true
    }, {
      field: 'uid',
      name: 'UID',
      width: '20%'
    }, {
      field: 'happened_at',
      name: '发生时间',
      width: '30%',
      cellFilter: 'unixTodate'
    }, {
      field: 'sensor_id',
      name: '传感器编码',
      width: '20%'
    }, {
      field: 'sensor_type',
      name: '传感器类型',
      width: '20%'
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
        $scope.touchEvent.current_page = newPage - 1;
        $scope.touchEvent.page_size = pageSize;

        $scope.getButtonEvent();
      });
    }

  };

  $scope.query_date = {
    activated_from: new Date('2014-01-01'),
    activated_to: new Date()
  }
  $scope.query_date.activated_to.setHours(23);
  $scope.query_date.activated_to.setMinutes(59);
  $scope.query_date.activated_to.setSeconds(59);
  
  var query_date = -1;


    $scope.touchEvent = {
    activated_from: $filter('dateTounix')($filter('date')($scope.query_date.activated_from, 'yyyy-M-dd H:mm:ss')),
    activated_to: $filter('dateTounix')($filter('date')($scope.query_date.activated_to, 'yyyy-M-dd H:mm:ss')),
    page_size: 10,
    current_page: 0,
    query_date: query_date
  }

  $scope.getButtonEvent = function() {

    $scope.touchEvent.activated_from = $filter('dateTounix')($filter('date')($scope.query_date.activated_from, 'yyyy-M-dd H:mm:ss'));
    $scope.touchEvent.activated_to = $filter('dateTounix')($filter('date')($scope.query_date.activated_to, 'yyyy-M-dd H:mm:ss'));

    return home_security.getButtonEvent(entity.touch_button_id, $scope.touchEvent).then(function(res) {
//      console.log(res);
      $scope.gridOptions.data = res.events;
      $scope.gridOptions.totalItems = res.total_count;
      $scope.touchEvent.query_date = res.query_date;

    }, function(error) {
      console.log(error);
    });
  }
  $scope.getButtonEvent();

  $scope.dt = new Date();

  $scope.open_form_1 = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.form1_opened = true;
  };

  $scope.open_form_2 = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.form2_opened = true;
  };




  $scope.dateOptions = {
//    formatYear: 'yy',
    startingDay: 1
  };

  $scope.formats = ['yyyy-MM-dd', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
  $scope.format = $scope.formats[0];

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

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
