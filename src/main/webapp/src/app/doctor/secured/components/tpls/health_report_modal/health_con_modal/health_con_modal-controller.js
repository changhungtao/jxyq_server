/**
 * Created by zhanga.fnst on 2015/6/17.
 */
'use strict';

/**
 * Create namespace.
 */
goog.provide('jxdctsec.health_report.health_con_modal.Ctrl');



/**
 * Confirm modal controller.
 *
 * @constructor
 * @export
 */
jxdctsec.health_report.health_con_modal.Ctrl.$inject = [
    "$scope",
    "$modalInstance",
    "$sce",
    "$timeout",
    "entity",
    'health_con'
];
jxdctsec.health_report.health_con_modal.Ctrl = function($scope, $modalInstance ,$sce ,$timeout,entity, health_con, ngAudio) {

    var ctrl = this;
    /**
     * @type {String}
     * @nocollapse
     */
    this.label = 'some label from custom modal controller';

    $scope.entity = entity;
//    console.log("modal:");
//    console.log($scope.entity);

    //$scope.dt = entity.measured_at_date;

    //play audio
    //var url = 'http://193.160.18.41/api/open/download?file_id=95';
//    $scope.show = function(url){
//        this.config = {
//            sources: [
//                {src: $sce.trustAsResourceUrl(url), type: "audio/mpeg"}
//            ],
//            theme: {
//                url: "../../bower_components/videogular-themes-default/videogular.css"
//            }
//        };
//    }

    // $scope.speakBtn = "../../img/speak.png";
    $scope.currentMessage = undefined;
    $scope.currentAudio = undefined;
    $scope.playAudio = function(message){
        if($scope.currentMessage === undefined || $scope.currentMessage != message){
            // 停止原有语音
            if($scope.currentAudio != undefined){
                $scope.currentAudio.stop();
                console.log("停止");
            }
            // 恢复默认图标
            if($scope.currentMessage != undefined){
                $scope.currentMessage.speakBtn = "../../img/speak.png";
            }
            // 暂存当前回复
            $scope.currentMessage = message;
            $scope.currentAudio = ngAudio.load($scope.currentMessage.audio_url);
            $scope.currentAudio.play();
            $scope.currentMessage.speakBtn = "../../img/speak.gif";
            console.log("播放");
            $scope.$watch('currentAudio.paused', function(newValue, oldValue) {
                if (newValue) {
                    $scope.currentMessage.speakBtn = "../../img/speak.png";
                }
            });
        }else{
            if($scope.currentAudio.paused){
                if($scope.currentAudio.remaining > 0){
                    $scope.currentAudio.play();
                    $scope.currentMessage.speakBtn = "../../img/speak.gif";
                    console.log("继续");
                }else{
                    // $scope.currentAudio.restart();
                    $scope.currentAudio.play();
                    $scope.currentMessage.speakBtn = "../../img/speak.gif";
                    console.log("重播");
                }
            }else{
                $scope.currentAudio.pause();
                $scope.currentMessage.speakBtn = "../../img/speak.png";
                console.log("暂停");
            }
        }
    }

    $scope.queryQAandReply = function(){
        health_con.getQAandReply($scope.entity.consultation_id).then(function(res){
//            console.log(res);
            if(res != undefined && res != null){
                $scope.messages = res.qas;
                // $scope.messages.
                for (var i = $scope.messages.length - 1; i >= 0; i--) {
                    if($scope.messages[i].audio_url != undefined){
                        $scope.messages[i].speakBtn = "../../img/speak.png";
                    }
                };
            }
        },function(error){
            console.log("error!");
        })
    }

    $scope.queryQAandReply();

    var onTimeout = function() {
        $scope.queryQAandReply();
        timer = $timeout(onTimeout, 3000);
    };
    var timer = $timeout(onTimeout, 3000);

    $scope.$on("$destroy", function() {
        if (timer) {
            $timeout.cancel(timer);
        }
    });

    $scope.clear = function() {
        $scope.dt = null;
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
    $scope.replyer = {
        content:'',
        qa_type:1       //医生类型
    }
    $scope.sendReply = function(){
        var reply = angular.copy($scope.replyer);
        if(reply.content != undefined && reply.content != ''){
            console.log(reply);
            $scope.messages.push(reply);
            console.log($scope.messages);
        }
        //保存到数据库
        health_con.sendReply($scope.entity.consultation_id,$scope.replyer).then(function(res){
            console.log(res);
            if(res != undefined && res != null && res == $scope.entity.consultation_id){
                console.log("保存成功");
            }else{

            }
            $scope.replyer.content = '';
        },function(error){
            console.log("error!");
        })
    }

};

