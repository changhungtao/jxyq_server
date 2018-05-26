package com.jxyq.app.user_login;

import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.ucpaas.UcpaasCommon;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.model.user.VerificationCode;
import com.jxyq.service.inf.UserService;
import com.jxyq.app.BaseInterface;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class VerificationCodeInterface extends BaseInterface {
    @Autowired
    private UserService userService;

    private int reg_duration = SysConfig.getInstance().getPropertyInt("reg.duration");

    @RequestMapping(value = "/api/open/verification_code", method = RequestMethod.GET)
    public void GetVerificationCodeInterface(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String phone = req.getParameter("phone");
            String used_for_str = req.getParameter("used_for");
            if (isPhoneNum(phone) || StringUtils.isBlank(used_for_str)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            int used_for = Integer.valueOf(used_for_str);
            VerificationCode vCode_db = userService.selVerificationCode(phone, used_for);
            long now = (new Date()).getTime() / 1000;
            if(vCode_db != null){
                if (vCode_db.getCreated_at() + (long)vCode_db.getDuration() > now) {
                    Map<String, Object> res_map=new HashMap<>();
                    res_map.put("phone", phone);
                    res_map.put("verification_code", vCode_db.getContent());
                    content.setSuccess_message(res_map);
                    setResponseContent(res, content);
                    return;
                }
            }
            String code = CommonUtil.produceCode();
            String sms_params = code;
            UcpaasCommon ucpaasCommon = new UcpaasCommon();
            ucpaasCommon.sendTemplateSMS(UcpaasCommon.reg_template, phone, sms_params);

            VerificationCode vCode = new VerificationCode();
            vCode.setPhone(phone);
            vCode.setUsed_for(used_for);
            vCode.setContent(code);
            vCode.setCreated_at(now);
            vCode.setDuration(reg_duration);
            vCode.setStatus(0);
            if(vCode_db == null) {
                vCode.setVerification_code_id(0);
                userService.inVerificationCode(vCode);
            } else {
                vCode.setVerification_code_id(vCode_db.getVerification_code_id());
                userService.upVerificationCode(vCode);
            }

            Map<String, Object> res_map=new HashMap<>();
            res_map.put("phone", phone);
            res_map.put("verification_code", code);
            content.setSuccess_message(res_map);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(),ErrorCode.SYSTEM_ERROR);
        }
    }

    private boolean isPhoneNum(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        Pattern p = Pattern.compile("^\\d{11}$ (file://d%7b11%7d$/)");
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
