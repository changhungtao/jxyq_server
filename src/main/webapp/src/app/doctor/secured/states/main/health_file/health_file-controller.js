'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.main.health_file.Ctrl');



/**
 * Terminal category controller.
 *
 * @constructor
 * @export
 */
jxdctsec.main.health_file.Ctrl.$inject = [
  '$scope', 
  '$http', 
  '$q',
  '$window',
  'i18nService',
  'constants',
  'health_file'
];
jxdctsec.main.health_file.Ctrl = function($scope, $http, i18nService,$q,$window,constants,$modal,ModalService,health_file){

  /**
   * @type {String}
   * @nocollapse
   */
  this.label = 'some label from category template controller';
    var ctrl = this;
    var numArry = function(count){
        var nums = [];
        for(var i = 1;i <= count;i++){
            nums.push(i);
        }
        return nums;
    }
    $scope.ages = numArry(120);

    activate();

    function activate() {
        // var promises = [getGenders()];
        // return $q.all(promises).then(function() {
        // });
        $scope.genders = constants.gotGENDERS();
        $scope.gender = {selected: undefined};
    }

    // function getGenders(){
    //     return constants.getGenders().then(function(data){
    //         $scope.genders =  data.genders;
    //         $scope.gender = {selected: undefined};
    //         return $scope.genders;
    //     });
    // }

    $scope.health_file={
        name:undefined,
        begin_age:10,
        end_age:60,
        gender:undefined,
        page_size:10,
        current_page:0,
        phone:undefined,
        query_date:undefined
    }

    $scope.getPatient = function(){
        health_file.getHealthFile($scope.health_file).then(function(data){
            console.log(data);
            //具体看对象结构是否为data.files;
            $scope.gridOptions.data = data.patients;
            $scope.gridOptions.totalItems = data.total_count;
            $scope.health_file.query_date = data.query_date;
        },function(error){
            console.log(error);
        });
    }

    $scope.getPatient();

    $scope.gridOptions = {
        paginationPageSizes: [10, 20, 50, 100],
        paginationPageSize: 10,
        useExternalPagination: true,
//        useExternalSorting: true,
        columnDefs:[
            {
                field:'patient_id',
                name: '患者编号',
                width: '10%',
                pinnedLeft: true
            }, {
                field:'name',
                name: '患者姓名',
                width: '10%'
            },
            {
                field:'birthday',
                name: '出生年月',
                width: '10%'
            },{
                field:'gender',
                name: '性别',
                width: '10%',
                cellFilter:'mapGender'
            },{
                field:'phone',
                name: '手机',
                width: '10%'
            },{
                field:'address',
                name: '患者住址',
                width: '10%'
            },{
                field:'profile',
                name: '用药史',
                width: '10%'
            },{
                name: '操作',
                width: '30%',
                cellTemplate: '<div class="row"><div class="col-md-offset-1 col-md-2 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.popup(row.entity)" >编辑</button></div><div class="col-md-offset-2 col-md-2 text-center"><button class="btn btn-primary btn-sm" ng-click="grid.appScope.addFile(row.entity)">录入数据</button></div></div>'
            }],
            onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
            });
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.health_file.current_page = newPage - 1;
                $scope.health_file.page_size = pageSize;

                $scope.getPatient();
            });
        }
    }

    i18nService.setCurrentLang('zh-CN');

    $scope.popup = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/edit_patient_modal/edit_patient_modal.html',
      controller: 'EditPatientModalCtrl',
      resolve: {
        entity: function() {
          return entity;
        }
      }
    });
    modalInstance.result.then(function() {
      console.log('Modal closed at: ' + new Date());
    }, function() {
      console.log('Modal dismissed at: ' + new Date());
    });

  };

  $scope.addFile = function(entity) {
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/add_file_modal/add_file_modal.html',
      controller: 'AddFileModalCtrl',
       size:'lg',
      resolve: {
        entity: function() {
          return entity;
        },
        'FILETYPESPromise': function(constants){
          return constants.FILETYPESPromise;
        },
        'PERIODSPromise': function(constants){
          return constants.PERIODSPromise;
        }
      }
    });
    modalInstance.result.then(function() {
      console.log('Modal closed at: ' + new Date());
    }, function() {
      console.log('Modal dismissed at: ' + new Date());
    });

  };

  ctrl.addPatient=function(){
    var modalInstance = $modal.open({
      animation: true,
      templateUrl: 'components/tpls/add_patient_modal/add_patient_modal.html',
      controller: 'AddPatientModalCtrl'
     
    });
    modalInstance.result.then(function() {
     
    }, function() {
        $scope.health_file.phone = undefined;
        $scope.health_file.gender = undefined;
        $scope.health_file.name = undefined;

        $scope.getPatient();

//        $window.location.reload();
    });
  }
  
};



/**
 * Write `text` to stdout.
 *
 * Example function that we'd like to access in our `main.health_file.?`
 * child controller.
 *
 * @param {String} text
 */
jxdctsec.main.health_file.Ctrl.prototype.log = function(text) {
  console.log(text);
};
