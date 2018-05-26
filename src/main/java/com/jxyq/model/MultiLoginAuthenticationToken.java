package com.jxyq.model;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class MultiLoginAuthenticationToken extends UsernamePasswordToken {
    private String realmName;

    public MultiLoginAuthenticationToken(String username, String password, String realmName){
        super(username, password);
        this.realmName = realmName;
    }

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }
}
