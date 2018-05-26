package com.jxyq.controller.shiro;

import com.google.gson.JsonObject;
import com.jxyq.app.BaseInterface;
import com.jxyq.model.Role;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.health.Manufactory;
import com.jxyq.service.inf.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ShiroController extends BaseInterface {
    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/login_err", method = RequestMethod.GET)
    public void ShiroLoginErr(HttpServletRequest req, HttpServletResponse res) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @RequestMapping(value = "/unauthorized_err", method = RequestMethod.GET)
    public void ShiroUnauthorizedErr(HttpServletRequest req, HttpServletResponse res) {
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Subject subject = SecurityUtils.getSubject();
        String role = "";
        if (subject == null || subject.getPrincipal() == null) {
            role = "null";
        } else {
            String auth_role = subject.getPrincipal().getClass().getName();
            if (auth_role.equals(Administrator.class.getName())) {
                role = "manager";
                Administrator auth = (Administrator) subject.getPrincipal();
                auth = adminService.selAdminWithRoleByLoginName(auth.getLogin_name());
                List<Role> role_list = auth.getRoleList();
                for (Role user_role : role_list) {
                    if (user_role.getName().indexOf("super") != -1) {
                        role = "super";
                        break;
                    }
                }
            } else if (auth_role.equals(Manufactory.class.getName())) {
                role = "manufactory";
            } else if (auth_role.equals(Doctor.class.getName())) {
                role = "doctor";
            } else {
                role = "other";
            }
        }

        JsonObject json = new JsonObject();
        json.add("role", gson.toJsonTree(role));
        setResponseContent(res, json);
    }

    @RequestMapping(value = "/doctor/open", method = RequestMethod.GET)
    public String GetDoctorOpen0() {
        return "redirect:/doctor/open/";
    }

    @RequestMapping(value = "/doctor/open/", method = RequestMethod.GET)
    public String GetDoctorOpen() {
        return "/doctor/open/index.html";
    }

    @RequestMapping(value = "/doctor/secured", method = RequestMethod.GET)
    public String GetDoctorSecured0() {
        return "redirect:/doctor/secured/";
    }

    @RequestMapping(value = "/doctor/secured/", method = RequestMethod.GET)
    public String GetDoctorSecured() {
        return "/doctor/secured/index.html";
    }

    @RequestMapping(value = "/manager/open", method = RequestMethod.GET)
    public String GetManagerOpen0() {
        return "redirect:/manager/open/";
    }

    @RequestMapping(value = "/manager/open/", method = RequestMethod.GET)
    public String GetManagerOpen() {
        return "/manager/open/index.html";
    }

    @RequestMapping(value = "/manager/secured", method = RequestMethod.GET)
    public String GetManagerSecured0() {
        return "redirect:/manager/secured/";
    }

    @RequestMapping(value = "/manager/secured/", method = RequestMethod.GET)
    public String GetManagerSecured() {
        return "/manager/secured/index.html";
    }

    @RequestMapping(value = "/manufactory/open", method = RequestMethod.GET)
    public String GetManufactoryOpen0() {
        return "redirect:/manufactory/open/";
    }

    @RequestMapping(value = "/manufactory/open/", method = RequestMethod.GET)
    public String GetManufactoryOpen() {
        return "/manufactory/open/index.html";
    }

    @RequestMapping(value = "/manufactory/secured", method = RequestMethod.GET)
    public String GetManufactorySecured0() {
        return "redirect:/manufactory/secured/";
    }

    @RequestMapping(value = "/manufactory/secured/", method = RequestMethod.GET)
    public String GetManufactorySecured() {
        return "/manufactory/secured/index.html";
    }

    @RequestMapping(value = "/super/open", method = RequestMethod.GET)
    public String GetSuperOpen0() {
        return "redirect:/super/open/";
    }

    @RequestMapping(value = "/super/open/", method = RequestMethod.GET)
    public String GetSuperOpen() {
        return "/super/open/index.html";
    }

    @RequestMapping(value = "/super/secured", method = RequestMethod.GET)
    public String GetSuperSecured0() {
        return "redirect:/super/secured/";
    }

    @RequestMapping(value = "/super/secured/", method = RequestMethod.GET)
    public String GetSuperSecured() {
        return "/super/secured/index.html";
    }
}
