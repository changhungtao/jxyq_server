package com.jxyq.app.user_login;

import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.UserService;
import com.jxyq.app.BaseInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class GetNewsInterface extends BaseInterface {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/open/news_carousel", method = RequestMethod.GET)
    public void GetNews(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<Map<String, Object>> news = userService.getNews();
            content.setSuccess_message(news);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(),ErrorCode.SYSTEM_ERROR);
        }
    }
}
