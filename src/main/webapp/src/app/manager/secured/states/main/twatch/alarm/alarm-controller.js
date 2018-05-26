'use strict';

/**
 * Create namespace.
 */
goog.provide('jxmgrsec.main.twatch.alarm.Ctrl');



/**
 * main.twatch.alarm controller.
 *
 * @constructor
 * @export
 */
jxmgrsec.main.twatch.alarm.Ctrl.$inject = [
  "$rootScope",
  "$scope",
  "$http",
  "$modal",
  "$timeout",
  "$state",
  '$filter',
  '$q',
  "i18nService",
  'constants',
  'twatch'
];
jxmgrsec.main.twatch.alarm.Ctrl = function(
  $rootScope,
  $scope,
  $timeout,
  $http,
  $modal,
  $state,
  $filter,
  $q,
  i18nService,
  constants,
  twatch
) {

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some other label from main.twatch.alarm controller';
  var ctrl = this;

  i18nService.setCurrentLang('zh-CN');

  $scope.gridOptions = {
    paginationPageSizes: [25, 50, 75],
    paginationPageSize: 25,
    /*paginationPageSizes: [10, 20, 50, 100],
    paginationPageSize: 10,
    useExternalPagination: false,
    useExternalSorting: false,*/
    columnDefs: [{
      field: 'eventname',
      name: '提醒事件',
      width: '15%',
      cellFilter: 'mapTwatchAlarmEvent',
      pinnedLeft: true
    }, {
      field: 'start',
      name: '开始时间',
      width: '15%',
      pinnedLeft: true
    }, {
      field: 'msg',
      name: '内容',
      width: '15%',
      pinnedLeft: true
    }, {
      field: 'times',
      name: '间隔',
      width: '10%',
      pinnedLeft: true
    }, {
      field: 'interval',
      name: '次数',
      width: '10%',
      pinnedLeft: true
    }, {
      field: 'weeks',
      name: '星期几',
      width: '10%',
      cellFilter: 'mapWeekDays',
      pinnedLeft: true
    }, {
      field: 'playvoice_en',
      name: '激活状态',
      width: '10%',
      cellFilter: 'mapTwatchPeriodState',
      pinnedLeft: true
    }, {
      name: '操作',
      width: '15%',
      cellTemplate: '<div class="row ui-grid-cell-contents"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-xs" ng-click="grid.appScope.modifyAlarm(row.entity)">修改</button><button class="btn btn-primary btn-xs" ng-click="grid.appScope.removeAlarm(row.entity)">删除</button></div></div>',
      pinnedRight: true
    }],
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
    }
  }

  /* $scope.gridOptions.data = [
     {
       'event_type': 1,
       'start': 1,
       'interval': 1,
       'times': 1,
       'weeks': 1,
       'playvoice_en': 1
     }
   ];*/

  $scope.query = {
    imei: $rootScope.imei
  }

  $scope.jsonImei = {
    imei: undefined
  }

  $scope.queryImei = function() {
    $scope.jsonImei.imei = $scope.query.imei;
    $scope.queryPlayVoice();
  };

  $scope.queryPlayVoice = function() {
      twatch.getPlayVoices($scope.jsonImei).then(function(res) {
        console.log(res);
        if (res == "" || res == undefined || res == null) {
          $scope.addAlert('danger', '暂无提醒设置，请点击添加提醒');
        } else {
          $scope.gridOptions.data = res;
        }
        //      $rootScope.imei = $scope.jsonImei.imei;
        $rootScope.imeiChanged($scope.jsonImei.imei);
      }, function(error) {
        console.log(error);
      });
    }
    //$scope.queryPlayVoice();

  $scope.modifyAlarm = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/voice_modal/modify_voice_pieroid_modal.html',
      controller: 'ModifyVoicePieroidModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        },
        imei: function() {
          return $scope.jsonImei.imei;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.queryPlayVoice();
    });
  }

  $scope.removeAlarm = function(entity) {

    var deleteperiod = {
      imei: $scope.jsonImei.imei,
      eventname: entity.eventname,
      eventid: entity.eventid
    }
    twatch.deletePlayVoices(deleteperiod).then(function(res) {
      console.log(res);
      $scope.queryPlayVoice();
    }, function(error) {
      console.log(error);
    });

  }

  ctrl.addAlert = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/voice_modal/add_voice_pieroid_modal.html',
      controller: 'AddVoicePieroidModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });
    modalInstance.result.then(function() {

    }, function() {
      $scope.queryPlayVoice();
    });
  }

};
