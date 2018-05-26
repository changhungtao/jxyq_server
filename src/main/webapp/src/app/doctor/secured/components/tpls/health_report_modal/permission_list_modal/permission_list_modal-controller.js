/**
 * Created by zhanga.fnst on 2015/6/23.
 */
'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.health_report.permission_list_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxdctsec.health_report.permission_list_modal.Ctrl.$inject = [
    "$scope",
    "$modalInstance",
    "$sce",
    "entity"
];
jxdctsec.health_report.permission_list_modal.Ctrl = function($scope, $modalInstance ,entity) {

    var ctrl = this;
    /**
     * @type {String}
     * @nocollapse
     */
    this.label = 'some label from custom modal controller';

    $scope.filters = entity.filters;
    $scope.type = entity.data_type;
//    console.log('$scope.filters');
//    console.log($scope.filters);

//    var callFilter = function(data_type){
//        switch (data_type){
//            case 1:$scope.filterName = 'mapWristbandColumn';break;
//            case 2:$scope.filterName = 'mapSphygmomanometerColumn';break;
//            case 3:$scope.filterName = 'mapOximeterColumn';break;
//            case 4:$scope.filterName = 'mapGlucosemeterColumn';break;
//            case 5:$scope.filterName = 'mapThermometerColumn';break;
//            case 6:$scope.filterName = 'mapFatColumn';break;
//        }
//    }
//
//    callFilter($scope.filters.data_type);

    $scope.clear = function() {
        $scope.dt = null;
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    }
};
