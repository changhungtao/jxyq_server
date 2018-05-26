package com.jxyq.commons.constants;

public final class BeanProperty {
    public static final String PAGING = "paging";

    /*1:正常/2:禁用*/
    public static final class UserStatus {
        public static final int NORMAL = 1;
        public static final int CLOSE = 2;
    }

    /*1:在线/0:离线*/
    public static final class OnlineStatus {
        public static final int OFFLINE = 0;
        public static final int ONLINE = 1;
    }

    /*0:虚假/1:真实*/
    public static final class RealStatus{
        public static final int UNREAL = 0;
        public static final int REAL = 1;
    }

    /*operation_type：0:登陆客户端/1:完善个人资料/2:每日健康体测/3:登陆论坛/4:管理员操作*/
    public static final class OperationType {
        public static final int LOGIN_CLIENT = 0;
        public static final int IMPROVE_INFORMATION = 1;
        public static final int HEALTH_MONITOR = 2;
        public static final int LOGIN_BLOG = 3;
        public static final int ADMIN_OPERATION = 4;
    }

    /*qa_type:0:提问/1:回复*/
    public static final class QAType {
        public static final int QUESTION = 0;
        public static final int ANSWER = 1;
    }

    /*health_consultations中 status答复状态：/1: 未回答/2: 回答中/3: 已评价/4: 已屏蔽*/
    public static final class HealthStatus {
        public static final int NoAnswer = 1;
        public static final int Answering = 2;
        public static final int Comment = 3;
        public static final int Shield = 4;
    }

    /*question_and_reply && question_and_reply_resource 中 status（1未审/2通过/3屏蔽）*/
    public static final class QAStatus {
        public static final int UnCheck = 1;
        public static final int Pass = 2;
        public static final int Shield = 3;
    }

    /*健康数据评测状态*/
    public static final class ProposalStatus {
        public static final int UN_PROPOSAL = 1;
        public static final int HAS_PROPOSAL = 2;
    }

    /*1:正常/2:禁用*/
    public static final class DeviceTypeStatus {
        public static final int NORMAL = 1;
        public static final int DELETE = 2;
    }

    /*0:男/1:女*/
    public static final class Gender {
        public static final int DEFAULT = 0;
        public static final int MALE = 0;
        public static final int FEMALE = 1;
    }

    /*1:次监护人/2:主监护人*/
    public static final class PupilLevel {
        public static final int SECOND_LEVEL = 1;
        public static final int PRIMARY_LEVEL = 2;
    }

    /*厂商模板使用类型*/
    public static final class Using_default {
        public static final int using_default = 1;
        public static final int using_manufactory = 2;
    }

    /*1:PC;2:Android;3:iOS*/
    public static final class ComputeType {
        public static final int PC = 1;
        public static final int ANDROID = 2;
        public static final int IOS = 3;
    }

    /*模板类型: 1:系统;2:厂商*/
    public static final class Template_Type {
        public static final int SYSTEM = 1;
        public static final int FACTORY = 2;
    }

    /*修改金额:1:增加/2:减少*/
    public static final class MarkType {
        public static final int ADD = 1;
        public static final int SUBTRACT = 2;
    }
}

