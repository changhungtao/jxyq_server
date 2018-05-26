package com.jxyq.app.filter;

import com.jxyq.app.user_login.LoginInterface;
import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.util.ResponseUtil;
import com.jxyq.commons.util.TokenHandler;
import com.jxyq.config.SpringContextHolder;
import com.jxyq.model.others.ErrorMessage;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.UserService;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
public class TokenFilter implements Filter {
    private TokenHandler tokenHandler;
    private List notCheckURLList = new ArrayList();
    private UserService userService;
    private String token_secret = SysConfig.getInstance().getProperty("token.secret");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        tokenHandler = new TokenHandler(token_secret);

        String notCheckURLListStr = filterConfig.getInitParameter("notCheckURLList");
        if (notCheckURLListStr != null) {
            StringTokenizer st = new StringTokenizer(notCheckURLListStr, ";");
            notCheckURLList.clear();
            while (st.hasMoreTokens()) {
                notCheckURLList.add(st.nextToken());
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        if (checkReqURLInNotFilterList(req)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String token = req.getHeader(LoginInterface.AUTH_HEADER_NAME);
        userService = (UserService) SpringContextHolder.getBean("UserService");
        if (token != null) {
            User token_user = tokenHandler.parseUserFromToken(token);
            if (token_user != null) {
                User user = userService.selUserByPhone(token_user.getPhone());
                if (user != null && token_user.getPassword().equals(user.getPassword()) &&
                        user.getStatus() == BeanProperty.UserStatus.NORMAL) {
                    servletRequest.setAttribute("user", user);
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
                if (user.getStatus() == BeanProperty.UserStatus.CLOSE) {
                    ResponseUtil.setStatusCode(res, HttpServletResponse.SC_OK);
                    ResponseUtil.setContentType(res, "text/html; charset=utf-8");
                    ErrorMessage emsg = new ErrorMessage(ErrorCode.USER_CLOSE);
                    emsg.setFaultstring("用户被禁用.");
                    ResponseUtil.setContext(res, emsg);
                    return;
                }
            }
        }

        ResponseUtil.setStatusCode(res, HttpServletResponse.SC_UNAUTHORIZED);
        ResponseUtil.setContentType(res, "text/html; charset=utf-8");
        ErrorMessage emsg = new ErrorMessage(101);
        emsg.setFaultstring("Unauthentication.");
        ResponseUtil.setContext(res, emsg);
        return;
    }

    @Override
    public void destroy() {
    }

    private boolean checkReqURLInNotFilterList(HttpServletRequest request) {
        String url = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());

        for (int i = 0; i < notCheckURLList.size(); i++) {
            String notCheckURL = (String) notCheckURLList.get(i);
            if (notCheckURL.equals(url)) return true;
            if (notCheckURL.endsWith("*")) {
                String notCheck = notCheckURL.substring(0, notCheckURL.lastIndexOf("/"));
                if (url.startsWith(notCheck)) return true;
            }
        }
        return false;
    }
}
