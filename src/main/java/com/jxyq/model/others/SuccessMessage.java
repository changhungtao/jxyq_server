package com.jxyq.model.others;

import java.util.Date;

/**
 * 服务器通信的公共返回值
 */
public class SuccessMessage<T> {

    private T success_message;

    public T getSuccess_message() {
        return success_message;
    }

    public void setSuccess_message(T success_message) {
        this.success_message = success_message;
    }
}
