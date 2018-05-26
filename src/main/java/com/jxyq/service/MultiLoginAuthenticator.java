package com.jxyq.service;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

public class MultiLoginAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken token) throws AuthenticationException {
        assertRealmsConfigured();
        Realm loginRealm = lookupRealm(token);
        return doSingleRealmAuthentication(loginRealm, token);
    }

    protected Realm lookupRealm(AuthenticationToken token) throws AuthenticationException {
        Collection<Realm> realms = getRealms();
        for (Realm realm : realms) {
            if (realm.supports(token)) {
                return realm;
            }
        }
        throw new AuthenticationException("No realm configured for Client " + token.toString());
    }
}
