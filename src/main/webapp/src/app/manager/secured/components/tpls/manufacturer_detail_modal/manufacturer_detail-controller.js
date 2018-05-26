'use strict';

/**
 * Create namespace.
 */
//goog.provide('jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl');
goog.provide('jxmgrsec.manufacturer_detail_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl.$inject = [
jxmgrsec.manufacturer_detail_modal.Ctrl.$inject = [
    "$scope",
    "$filter",
    "$modalInstance",
    '$q',
    "entity",
    "constants",
    "db_operation",
    "basic",
    '$timeout',
    'md5'
];
//jxdctsec.health_data_operation_modals.evaluation_modal.Ctrl = function($scope, $filter,$modalInstance, entity,health_data) {
jxmgrsec.manufacturer_detail_modal.Ctrl = function($scope, $filter, $modalInstance, $q, entity, constants, db_operation, basic, $timeout, md5) {

  
     $scope.manuId = entity;

    $scope.company_departments = constants.gotCOMPANYDEPARTMENTS();
    $scope.device_types = constants.gotDEVICETYPES();
    $scope.company_members_list = constants.gotCOMPANYMEMBERS();
    $scope.company_industry_list = constants.gotCOMPANYINDUSTRIES();
    $scope.company_nature_list = constants.gotCOMPANYNATURES();
    $scope.province_list = constants.gotPROVINCES();
    $scope.city_list = constants.gotCITIES();
    $scope.county_list = constants.gotZONES();

    $scope.business_licencefile = undefined; //工商营业执照
    $scope.internal_certificatefile = undefined; //组织机构代码证
    $scope.local_certificatefile = undefined; //税务登记证-国税
    $scope.code_certificatefile = undefined; //税务登记证-地税
    $scope.logo_file = undefined; //税务登记证-地税

    $scope.manufactory = {
        login_name: undefined,
        full_name: undefined,
        contactor: undefined,
        password: undefined,
        department: undefined,
        telephone: undefined,
        phone: undefined,
        email: undefined,
        code: undefined,
        profile: undefined,
        province_id: undefined,
        city_id: undefined,
        zone_id: undefined,
        address: undefined,
        device_type_ids: undefined,
        members: undefined,
        industry: undefined,
        nature: undefined,
        business_licence: undefined,
        internal_certificate: undefined,
        local_certificate: undefined,
        code_certificate: undefined,
        logo_url: undefined
    };
    $scope.new_password = "";
    $scope.confirm_password = "";


    $scope.getManuById = function() {
        db_operation.getManuDetail($scope.manuId).then(function(res) {
//            console.log(res);
            $scope.manufactory = res;
            // if (validParam($scope.signUpInf.birthday)) {
            //     $scope.birthday_date = new Date($scope.signUpInf.birthday);
            // }
            // if (validParam($scope.signUpInf.avatar_url)) {
            //     $scope.headUrl = $scope.signUpInf.avatar_url;
            // }
            // if (validParam($scope.signUpInf.physician_certificate)) {
            //     $scope.physicianUrl = $scope.signUpInf.physician_certificate;
            // }
            // if (validParam($scope.signUpInf.practicing_certificate)) {
            //     $scope.practicingUrl = $scope.signUpInf.practicing_certificate;
            // }
        });
    }

    $scope.activate = function() {
        var promises = [$scope.getManuById()];
        return $q.all(promises).then(function() {});
    }

    $scope.activate();

  

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

};
