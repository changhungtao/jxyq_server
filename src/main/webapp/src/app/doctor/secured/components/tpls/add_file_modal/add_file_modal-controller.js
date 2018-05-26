'use strict';
/**
 * Create namespace.
 */
goog.provide('jxdctsec.add_file_modal.add_file_modal.Ctrl');
/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxdctsec.add_file_modal.add_file_modal.Ctrl.$inject = [
  "$scope",
  "$modalInstance",
  "$filter",
  "$q",
  "entity",
  "constants",
  "health_file"
];
jxdctsec.add_file_modal.add_file_modal.Ctrl = function(
  $scope,
  $modalInstance,
  $filter,
  $q,
  entity,
  constants,
  health_file
) {

  //    console.log("entity");
  //    console.log(entity);
  //手环
  $scope.wristbandFiles = {
    measured_at: $filter('date')(new Date(), 'yyyy-M-dd H:mm:ss'),
    step_count: undefined,
    walk_count: undefined,
    run_count: undefined,
    distance: undefined,
    walk_distance: undefined,
    run_distance: undefined,
    calories: undefined,
    walk_calories: undefined,
    run_calories: undefined,
    deep_duration: undefined,
    shallow_duration: undefined,
    heart_rate: undefined,
    symptom: undefined,
    proposal: undefined
  }

  $scope.saveWristband = function() {
    //$scope.wristbandFiles.measured_at = $filter('dateTounix')($filter('date')($scope.wristbandFiles.measured_at,'yyyy-M-dd H:mm:ss'));
    $scope.wristbandFiles.measured_at = $filter('dateTounix')($filter('date')(new Date(), 'yyyy-M-dd H:mm:ss'));
    health_file.addWristbandData(entity.patient_id, $scope.wristbandFiles).then(function(data) {
      console.log(data);
      $scope.cancel();
    }, function(error) {
      console.log(error);
      $scope.cancel();
    });
    //console.log('saveWristband......');
  }


  //血压
  $scope.sphygFiles = {
    measured_at: $filter('date')(new Date(), 'yyyy-M-dd H:mm:ss'),
    systolic_pressure: undefined,
    diastolic_pressure: undefined,
    heart_rate: undefined,
    symptom: undefined,
    proposal: undefined
  }
  $scope.saveSphygmomanometer = function() {
    $scope.sphygFiles.measured_at = $filter('dateTounix')($filter('date')($scope.sphygFiles.measured_at, 'yyyy-M-dd H:mm:ss'));
    health_file.addSphygmomanometerData(entity.patient_id, $scope.sphygFiles).then(function(data) {
      console.log(data);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });
    //console.log('saveSphygmomanometer......');
  }

  //血氧
  $scope.oximeterFiles = {
    measured_at: $filter('date')(new Date(), 'yyyy-M-dd H:mm:ss'),
    oximeter_value: undefined,
    symptom: undefined,
    proposal: undefined
  }
  $scope.saveOximeter = function() {
    $scope.oximeterFiles.measured_at = $filter('dateTounix')($filter('date')($scope.oximeterFiles.measured_at, 'yyyy-M-dd H:mm:ss'));
    console.log($scope.oximeterFiles);
    health_file.addOximeterData(entity.patient_id, $scope.oximeterFiles).then(function(data) {
      console.log(data);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });
    //console.log('saveOximeter......');
  }

  //血糖
  $scope.glucosemeterFiles = {
    measured_at: $filter('date')(new Date(), 'yyyy-M-dd H:mm:ss'),
    period: 2,
    glucosemeter_value: undefined,
    symptom: undefined,
    proposal: undefined
  }

  //初始化时间段下拉框
  // $scope.periods = [];
  // var getPeriod = function(){
  //     constants.getPeriods().then(function(data){
  //         console.log('$scope.periods');
  //         $scope.periods = data.periods;
  //         console.log(data.ops);
  //     },function(error){
  //         console.log(error);
  //     })
  // }
  // getPeriod();
  console.log('$scope.periods');
  $scope.periods = constants.gotPERIODS();

  $scope.saveGlucosemeter = function() {
    $scope.glucosemeterFiles.measured_at = $filter('dateTounix')($filter('date')($scope.glucosemeterFiles.measured_at, 'yyyy-M-dd H:mm:ss'));
    health_file.addGlucosemeterData(entity.patient_id, $scope.glucosemeterFiles).then(function(data) {
      console.log(data);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });
    //console.log('saveGlucosemeter......');
  }

  //体温
  $scope.thermometerFiles = {
    measured_at: $filter('date')(new Date(), 'yyyy-M-dd H:mm:ss'),
    thermometer_value: undefined,
    symptom: undefined,
    proposal: undefined
  }
  $scope.saveThermometer = function() {
    $scope.thermometerFiles.measured_at = $filter('dateTounix')($filter('date')($scope.thermometerFiles.measured_at, 'yyyy-M-dd H:mm:ss'));
    health_file.addThermometerData(entity.patient_id, $scope.thermometerFiles).then(function(data) {
      console.log(data);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });
    //console.log('saveThermometer......');
  }

  //脂肪
  $scope.fatFiles = {
    measured_at: $filter('date')(new Date(), 'yyyy-M-dd H:mm:ss'),
    bmi_value: undefined,
    weight_value: undefined,
    fat_value: undefined,
    calorie_value: undefined,
    moisture_value: undefined,
    muscle_value: undefined,
    visceral_fat_value: undefined,
    bone_value: undefined,
    symptom: undefined,
    proposal: undefined
  }

  $scope.saveFat = function() {
    $scope.fatFiles.measured_at = $filter('dateTounix')($filter('date')($scope.fatFiles.measured_at, 'yyyy-M-dd H:mm:ss'));
    health_file.addFatsData(entity.patient_id, $scope.fatFiles).then(function(data) {
      console.log(data);
      $scope.cancel();
    }, function(error) {
      console.log(error);
    });
    //console.log('saveFat......');
  }

  //其他
  $scope.otherFiles = {
      measured_at: $filter('date')(new Date(), 'yyyy-M-dd H:mm:ss'),
      symptom: undefined,
      proposal: undefined
  }

  $scope.saveOthers = function(){
      $scope.otherFiles.measured_at = $filter('dateTounix')($filter('date')($scope.otherFiles.measured_at, 'yyyy-M-dd H:mm:ss'));
      health_file.addOthersData(entity.patient_id, $scope.otherFiles).then(function(data) {
          console.log(data);
          $scope.cancel();
      }, function(error) {
          console.log(error);
      });
  }

  $scope.today = function() {
    $scope.dt = new Date();
  };
  $scope.dt = $scope.today();
  $scope.clear = function() {
    $scope.dt = null;
  };
  // Disable weekend selection
  $scope.disabled = function(date, mode) {
    return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
  };
  $scope.toggleMin = function() {
    $scope.minDate = $scope.minDate ? null : new Date();
  };
  $scope.toggleMin();

  $scope.fat_open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.fat_opened = true;
  };

  $scope.glucosemeter_open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.glucosemeter_opened = true;
  };

  $scope.oximeter_open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.oximeter_opened = true;
  };

  $scope.wristband_open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.wristband_opened = true;
  };

  $scope.sphygmomanometer_open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.sphygmomanometer_opened = true;
  };

  $scope.thermometer_open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.thermometer_opened = true;
  };

  $scope.other_open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.other_opened = true;
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

  $scope.file_types = constants.gotFILETYPES();
  $scope.file_type = {
    selected: $scope.file_types[0]
  };

  activate();

  function activate() {
    // var promises = [getFileTypes()];
    // return $q.all(promises).then(function() {});
  }

  // function getFileTypes() {
  //     return constants.getFileTypes().then(function(data) {
  //         console.log(data);
  //         $scope.file_types = data.file_types;
  //         $scope.file_type = {
  //             selected: data.file_types[0]
  //         };
  //         return $scope.file_types;
  //     });
  // }
  $scope.hideAllPages = function() {
    $scope.fat = true;
    $scope.glucosemeter = true;
    $scope.oximeter = true;
    $scope.sphygmomanometer = true;
    $scope.thermometer = true;
    $scope.wristband = true;
    $scope.other = true;
  }
  $scope.hideAllPages();
  $scope.wristband = false;
  $scope.formName = "wristbandForm";
  $scope.changePage = function() {
    var type = $scope.file_type.selected.file_type_name;

    switch (type) {
      case "脂肪":
        $scope.hideAllPages();
        $scope.fat = false;
        $scope.formName = "fatForm";
        break;
      case "血糖":
        $scope.hideAllPages();
        $scope.glucosemeter = false;
        $scope.formName = "glucosemeterForm";
        break;
      case "血氧":
        $scope.hideAllPages();
        $scope.oximeter = false;
        $scope.formName = "oximeterForm";
        break;
      case "血压":
        $scope.hideAllPages();
        $scope.sphygmomanometer = false;
        $scope.formName = "sphygmomanometerForm";
        break;
      case "体温":
        $scope.hideAllPages();
        $scope.thermometer = false;
        $scope.formName = "thermometerForm";
        break;
      case "手环":
        $scope.hideAllPages();
        $scope.wristband = false;
        $scope.formName = "wristbandForm";
        break;
      case "其他":
        $scope.hideAllPages();
        $scope.other = false;
        $scope.formName = "otherForm";
        break;
      default:
        $scope.hideAllPages();
        break;
    }
  }

    $scope.clearSomeFields = function(){
//        console.log($scope.formName);
        if($scope.formName == 'fatForm'){
            $scope.fatFiles = {
                measured_at: $scope.fatFiles.measured_at,
                bmi_value: undefined,
                weight_value: undefined,
                fat_value: undefined,
                calorie_value: undefined,
                moisture_value: undefined,
                muscle_value: undefined,
                visceral_fat_value: undefined,
                bone_value: undefined,
                symptom: undefined,
                proposal: undefined
            }
        }else if($scope.formName == 'glucosemeterForm'){
            //血糖
            $scope.glucosemeterFiles = {
                measured_at: $scope.glucosemeterFiles.measured_at,
                period: 2,
                glucosemeter_value: undefined,
                symptom: undefined,
                proposal: undefined
            }
        }else if($scope.formName == 'oximeterForm'){
            $scope.oximeterFiles = {
                measured_at: $scope.oximeterFiles.measured_at,
                oximeter_value: undefined,
                symptom: undefined,
                proposal: undefined
            }
        }else if($scope.formName == 'sphygmomanometerForm'){
            $scope.sphygFiles = {
                measured_at: $scope.sphygFiles.measured_at,
                systolic_pressure: undefined,
                diastolic_pressure: undefined,
                heart_rate: undefined,
                symptom: undefined,
                proposal: undefined
            }
        }else if($scope.formName == 'thermometerForm'){
            $scope.thermometerFiles = {
                measured_at: $scope.thermometerFiles.measured_at,
                thermometer_value: undefined,
                symptom: undefined,
                proposal: undefined
            }
        }else if($scope.formName == 'wristbandForm'){
            $scope.wristbandFiles = {
                measured_at: $scope.wristbandFiles.measured_at,
                step_count: undefined,
                walk_count: undefined,
                run_count: undefined,
                distance: undefined,
                walk_distance: undefined,
                run_distance: undefined,
                calories: undefined,
                walk_calories: undefined,
                run_calories: undefined,
                deep_duration: undefined,
                shallow_duration: undefined,
                heart_rate: undefined,
                symptom: undefined,
                proposal: undefined
            }
        }else if($scope.formName == 'otherForm'){
            $scope.otherFiles = {
                measured_at: $scope.otherFiles.measured_at,
                symptom: undefined,
                proposal: undefined
            }
        }else{

        }
    }

  $scope.ok = function() {
    $modalInstance.close();
  };

  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

};
