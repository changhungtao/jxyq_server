package com.jxyq.commons.util;

public class OnlineCount {
    private static OnlineCount instance = null;
    public long doctor_count = 0;
    public long admin_count = 0;
    public long factory_count = 0;

    private OnlineCount(){
        doctor_count = 0;
        admin_count = 0;
        factory_count = 0;
    }

    public static synchronized OnlineCount getInstance() {
        if (instance == null) {
            instance = new OnlineCount();
        }
        return instance;
    }
}
