package com.jxyq.other.listener;

import com.jxyq.commons.util.OnlineCount;
import com.jxyq.model.MultiLoginAuthenticationToken;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.health.Manufactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationListenerLogger implements AuthenticationListener {

    @Override
    public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
        String realm_name = ((MultiLoginAuthenticationToken) token).getRealmName();
        OnlineCount cnt = OnlineCount.getInstance();
        switch (realm_name) {
            case "DoctorRealm":
                System.out.println("医生登录");
                cnt.doctor_count = cnt.doctor_count + 1;
                System.out.println(cnt.doctor_count);
                break;
            case "AdministratorRealm":
                System.out.println("管理员登录");
                cnt.admin_count = cnt.admin_count + 1;
                System.out.println(cnt.admin_count);
                break;
            case "ManufactoryRealm":
                System.out.println("厂商登录");
                cnt.factory_count = cnt.factory_count + 1;
                System.out.println(cnt.factory_count);
                break;
            default:
                break;
        }

    }

    @Override
    public void onFailure(AuthenticationToken token, AuthenticationException ae) {
        System.out.println("登录失败");
    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        String object_name = principals.getPrimaryPrincipal().getClass().getName();
        OnlineCount cnt = OnlineCount.getInstance();
        if (object_name == Doctor.class.getName()) {
            System.out.println("医生退出");
            cnt.doctor_count = cnt.doctor_count - 1;
            if (cnt.doctor_count < 0) cnt.doctor_count = 0;
            System.out.println(cnt.doctor_count);
        } else if (object_name == Manufactory.class.getName()) {
            System.out.println("厂商退出");
            cnt.factory_count = cnt.factory_count - 1;
            if (cnt.factory_count < 0) cnt.factory_count = 0;
            System.out.println(cnt.factory_count);
        } else if (object_name == Administrator.class.getName()) {
            System.out.println("管理员退出");
            cnt.admin_count = cnt.admin_count - 1;
            if (cnt.admin_count < 0) cnt.admin_count = 0;
            System.out.println(cnt.admin_count);
        }
    }
}