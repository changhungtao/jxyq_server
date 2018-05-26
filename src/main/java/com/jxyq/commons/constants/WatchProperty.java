package com.jxyq.commons.constants;

public final class WatchProperty {
    /*手表位置上传方式: 1 定位; 2 惯性触发上传; 3 SOS按键触发上传*/
    public static final class RouteType{
        public static final int DW = 1;
        public static final int AUTO = 2;
        public static final int SOS = 3;
    }

    /*0:未删除/1:已删除*/
    public static final class DeletedFlag{
        public static final int UNDELETED = 0;
        public static final int DELETED = 1;
    }

    /*1:园/2:多边形*/
    public static final class PenSetType{
        public static final int CIRCLE = 1;
        public static final int HEXAGON = 2;
    }

    /*0:未读/1:已读*/
    public static final class ReadStatus{
        public static final int UNREAD = 0;
        public static final int READ = 1;
    }

    /*0:未启用/1:启用*/
    public static final class EnableStatus{
        public static final int ALL = -1;
        public static final int UNABLE = 0;
        public static final int ENABLE = 1;
    }

    /*0:单一模式/1:上课隐身模式*/
    public static final class ModeType{
        public static final int SINGLE_MODE = 0;
        public static final int COMPLEX_MODE = 1;
    }

    /*1家长/2GPS模式/3儿童模式/4校园模式/5上课隐身*/
    public static final class Mode{
        public static final int PARENT = 1;
        public static final int GPS = 2;
        public static final int CHILD = 3;
        public static final int SCHOOL = 4;
        public static final int HIDE = 5;
    }
}