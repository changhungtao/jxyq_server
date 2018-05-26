'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.command.Ctrl');



/**
 * main.twatch.command controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.twatch.command.Ctrl.$inject = [
  "$rootScope",
  "$scope",
  "$http",
  "$modal",
  "$timeout",
  "i18nService",
  "ModalService",
  "$filter",
  "twatch"
];

jxmgrsec.main.twatch.command.Ctrl = function($rootScope, $scope, $http, $modal, $filter, i18nService, ModalService, twatch, $timeout) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.twatch.command controller';
  var ctrl = this;

  console.log($rootScope.imei);

  i18nService.setCurrentLang('zh-CN');
  $scope.gridOptions = {
    paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: true,
//    useExternalSorting: true,
    columnDefs: [{
      field: 'number',
      name: '本机号码',
      width: '25%',
      pinnedLeft: true
    }, {
      field: 'happended_at',
      name: '发送时间',
      width: '25%'
    }, {
      field: 'content',
      name: '发送内容',
      width: '25%'
    }, {
      field: 'command',
      name: '功能说明',
      width: '25%',

    }],
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
      $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {});
      gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
        $scope.commandparam.current_page = newPage - 1;
        $scope.commandparam.page_size = pageSize;

        $scope.queryCommand();
      });
    }
  };

  $scope.query_date = {
    begin_date: new Date('2014-01-01'),
    end_date: new Date()
  }
  $scope.query_date.end_date.setHours(23);
  $scope.query_date.end_date.setMinutes(59);
  $scope.query_date.end_date.setSeconds(59);

  var query_date = -1;
  $scope.commandparam = {
    imei: $rootScope.imei,
    begin_date: $scope.query_date.begin_date,
    end_date: $scope.query_date.end_date,
    page_size: 10,
    current_page: 0,
    query_date: query_date
  }

  $scope.queryCommand = function() {
      $scope.commandparam.begin_date = $filter('dateTounix')($filter('date')($scope.query_date.begin_date, 'yyyy-M-dd H:mm:ss'));
      $scope.commandparam.end_date = $filter('dateTounix')($filter('date')($scope.query_date.end_date, 'yyyy-M-dd H:mm:ss'));

      if ($scope.validParam($rootScope.imei)) {
        $scope.commandparam.imei = $rootScope.imei;
      }

      if ($scope.validParam($scope.commandparam.imei)) {
        twatch.getCommands($scope.commandparam).then(function(res) {
          console.log(res);
          if (res.commands  == "" || res.commands == undefined || res.commands == null) {
            $scope.addAlert('danger', '所选日期范围内暂无指令消息');
          } else {
            $scope.gridOptions.data = res.commands;
            $scope.gridOptions.totalItems = res.total_count;
            $scope.commandparam.query_date = res.query_date;
          }
          //       $rootScope.imei = $scope.commandparam.imei;
          $rootScope.imeiChanged($scope.commandparam.imei);
        }, function(error) {
          console.log(error);
        });
      } else {
        $scope.addAlert('danger', '请先输入IMEI号再查询！');
      }

    }
    //  $scope.queryCommand();

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
//    formatYear: 'yy',
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

  //Alert
  $scope.alerts = [];
  $scope.addAlert = function(type, msg) {
    var alert = {
      'type': type,
      'msg': msg
    };
    $scope.alerts.push(alert);
    $timeout(function() {
      $scope.closeAlert($scope.alerts.indexOf(alert));
    }, 3000);
  };

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };

  $scope.clearAlerts = function() {
    $scope.alerts = [];
  };
};
