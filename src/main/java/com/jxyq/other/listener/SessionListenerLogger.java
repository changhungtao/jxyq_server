package com.jxyq.other.listener;

import com.jxyq.commons.util.OnlineCount;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.health.Manufactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionListenerLogger implements SessionListener {

    private static final Logger log = LoggerFactory.getLogger(SessionListenerLogger.class);

    @Override
    public void onStart(Session session) {
        log.info("Session started for [{}]", session.getId());
    }

    @Override
    public void onStop(Session session) {
        log.info("Session stoped for [{}]", session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        log.info("Session espirated for [{}]", session.getId());
        SimplePrincipalCollection principal = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (principal == null) return;

        Object obj = principal.getPrimaryPrincipal();
        if (obj == null) return;

        OnlineCount onlineCount = OnlineCount.getInstance();
        if (obj instanceof Doctor) {
            onlineCount.doctor_count = onlineCount.doctor_count - 1;
            if (onlineCount.doctor_count < 0) {
                onlineCount.doctor_count = 0;
            }
        } else if (obj instanceof Manufactory) {
            onlineCount.factory_count = onlineCount.factory_count - 1;
            if (onlineCount.factory_count < 0) {
                onlineCount.factory_count = 0;
            }
        } else if (obj instanceof Administrator) {
            onlineCount.admin_count = onlineCount.admin_count - 1;
            if (onlineCount.admin_count < 0) {
                onlineCount.admin_count = 0;
            }
        }
    }
}
