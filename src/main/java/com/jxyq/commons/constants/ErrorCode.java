package com.jxyq.commons.constants;

public class ErrorCode {
    /**
     * 操作成功
     */
    public final static int SUCCESS = 0;

    /**
     * 请求参数格式错误，解析失败
     */
    public final static int REQUEST_PARAM_FAORMAT_ERROR = 1001001;

    /**
     * 缺少必要请求参数
     */
    public final static int REQUEST_PARAM_MISS = 1001002;

    /**
     * 密码错误
     */
    public final static int PASSWORD_ERROR = 1001003;

    /**
     * 用户不存在
     */
    public final static int INVALID_USER = 1001004;

    /**
     * 修改密码时 老密码错误
     */
    public final static int OLD_PASSWORD_ERROR = 1001005;

    /**
     * 无效的token
     */
    public final static int INVALID_TOKEN_ERROR = 1001006;

    /**
     * 用户没有登录
     */
    public final static int EXIST_USER = 1001007;

    /**
     * 用户已注册
     */
    public final static int REGISTER_USER = 1001008;
    /**
     * 用户积分已用完
     */
    public final static int INVALID_POINTS = 1001009;

    /**
     * 验证码为空
     */
    public final static int NONE_CODE = 1001020;
    /**
     * 验证码过期
     */
    public final static int INVALID_CODE = 1001021;
    /**
     * 验证码错误
     */
    public final static int ERROR_CODE = 1001022;
    /**
     * 手机格式错误
     */
    public final static int INVALID_PHONE = 1001010;

    /**
     * 密码格式错误
     */
    public final static int INVALID_PWD = 1001011;

    /**
     * 旧密码错误
     */
    public final static int INVALID_OLDPWD = 1001012;

    /**
     * 该手机号未获得验证码
     */
    public final static int INVALID_PHONE_CODE = 1001013;

    /**
     * 手机号已存在
     */
    public final static int PHONEEXISTS = 1001014;
    /**
     * 厂商不存在
     */
    public final static int INVALID_FACTORY = 1001015;

    /**
     * 相关设备类型不存在或状态异常
     */
    public final static int INVALID_DEVICE = 1001016;
    /**
     * 相关产品类型不存在或状态异常
     */
    public final static int INVALID_PRODUCT = 1001017;
    /**
     * 相关终端型号不存在或状态异常
     */
    public final static int INVALID_TERMINALCATAGORY = 1001018;
    /**
     * 关联账户失败
     */
    public final static int FAIASSOCIATE = 1001019;

    /**
     * 文件上传失败
     */
    public final static int FAILUPLOAD = 1002001;

    /**
     * 文件上传失败
     */
    public final static int FAILDOWNLOAD = 1002002;

    /**
     * 系统错误
     */
    public final static int SYSTEM_ERROR = 1001111;

    /**
     * 用户权限不足
     */
    public final static int NO_PERMISSION = 1002010;

    /**
     * 重复操作
     */
    public final static int DUPLICATE_DONE = 1002020;

    /**
     * 手机未注册
     */
    public final static int NO_REGISTERED = 1002021;

    /**
     * 设备未与用户关联
     */
    public final static int NO_RELATED = 1002030;

    /**
     * 参数不合法
     */
    public final static int PARAM_ERROR = 1002031;

    /**
     * 文件不存在
     */
    public final static int FILE_NOT_EXIST = 1002032;

    /**
     * 监护对象不存在
     */
    public final static int PUPIL_NOT_EXIST = 1002033;

    /**
     * 围栏设置不存在
     */
    public final static int PEN_NOT_EXIST = 1002034;

    /**
     * 数据不匹配
     */
    public final static int DATA_NOT_MATCHING = 1002035;

    /**
     * 用户被禁用
     */
    public final static int USER_CLOSE = 1002036;

    /*
    * 用户未填写真实姓名
    */
    public final static int UNREAL_FULL_NAME = 1002037;

    /*
    * 用户填写真实姓名不超过24小时
    */
    public final static int REAL_NAME_UNCHECK = 1002038;
}
