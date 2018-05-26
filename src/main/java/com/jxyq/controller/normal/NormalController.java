package com.jxyq.controller.normal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.lang.StringUtils;
import com.jxyq.commons.ucpaas.SysConfig;
import com.jxyq.commons.ucpaas.UcpaasCommon;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.admin.Administrator;
import com.jxyq.model.doctor.Doctor;
import com.jxyq.model.health.Manufactory;
import com.jxyq.model.others.ConstantDescription;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

/**
 * Created by wujj-fnst on 2015/5/27.
 */
@Controller
public class NormalController extends BaseInterface {
    @Autowired
    private AdminService adminService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ManufactoryService manufactoryService;

    @Autowired
    private ConstantService constantService;

    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    @Autowired
    private FileService fileService;

    private int reg_duration = SysConfig.getInstance().getPropertyInt("reg.duration");
    private static final int BUFFER_SIZE = 1024*20;

    @RequestMapping(value = "/common/sign_in_captcha", method = RequestMethod.GET)
    public void createCheckCode(HttpServletRequest req, HttpServletResponse response) throws IOException {
        BufferedImage bi = new BufferedImage(68, 22, BufferedImage.TYPE_INT_RGB);
        //BufferedImage bi = new BufferedImage(80,34,BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        Color c = new Color(200, 150, 255);
        g.setColor(c);
        g.fillRect(0, 0, 68, 22);

        char[] ch = "0123456789".toCharArray();
        Random r = new Random();
        int len = ch.length, index;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            index = r.nextInt(len);
            g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
            g.drawString(ch[index] + "", (i * 15) + 3, 18);
            sb.append(ch[index]);
        }
        req.getSession().setAttribute("captcha", sb.toString());
        ImageIO.write(bi, "JPG", response.getOutputStream());
    }

    /*3.2.1.2 GET /common/verification_code*/
    @RequestMapping(value = "/common/verification_code", method = RequestMethod.GET)
    public void GetVerificationCode(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String phone = req.getParameter("phone");
            String used_for = req.getParameter("used_for");
            String user_name = req.getParameter("user_name");
            String user_role = req.getParameter("user_role");
            if (StringUtils.isBlank(phone) && (StringUtils.isBlank(user_name)
                    || StringUtils.isBlank(user_role))) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }

            if (StringUtils.isBlank(phone)) {
                switch (user_role) {
                    case "manager":
                        Administrator admin = adminService.selAdminByLoginName(user_name);
                        if (admin == null) {
                            setParamWarnRes(res, "用户名不存在", ErrorCode.INVALID_USER);
                            return;
                        }
                        phone = admin.getPhone();
                        break;
                    case "manufactory":
                        Manufactory factory = manufactoryService.selManufactoryByLoginName(user_name);
                        if (factory == null) {
                            setParamWarnRes(res, "用户名不存在", ErrorCode.INVALID_USER);
                            return;
                        }
                        phone = factory.getPhone();
                        break;
                    case "doctor":
                        Doctor doctor = doctorService.selDoctorByLoginName(user_name);
                        if (doctor == null) {
                            setParamWarnRes(res, "用户名不存在", ErrorCode.INVALID_USER);
                            return;
                        }
                        phone = doctor.getPhone();
                        break;
                    case "super":
                        Administrator super_admin = adminService.selAdminByLoginName(user_name);
                        if (super_admin == null) {
                            setParamWarnRes(res, "用户名不存在", ErrorCode.INVALID_USER);
                            return;
                        }
                        phone = super_admin.getPhone();
                        break;
                }
            }

            String code = CommonUtil.produceCode();
            String sms_params = code;
            UcpaasCommon ucpaasCommon = new UcpaasCommon();
            ucpaasCommon.sendTemplateSMS(UcpaasCommon.reg_template, phone, sms_params);

            req.getSession().setAttribute("ver_phone", phone);
            req.getSession().setAttribute("ver_code", code);

            JsonObject json = new JsonObject();
            json.add("verification_code", gson.toJsonTree(code));
            json.add("phone", gson.toJsonTree(phone));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.getMessage(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.1.3 POST /common/uploads*/
    @RequestMapping(value = "/common/uploads", method = RequestMethod.POST)
    public void PostComUploads(@RequestParam("files") MultipartFile[] files,
                               HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String resPath = req.getScheme() + "://" + req.getServerName() + req.getContextPath()
                    + "/api/open/download?file_id=";

            ArrayList filesList = saveFile(files, req.getSession().getServletContext().getRealPath("/") + "../upload/other/");
            ArrayList urlList = new ArrayList();
            if (filesList != null) {
                for (int i = 0; i < filesList.size(); i++) {
                    urlList.add(resPath + filesList.get(i));
                }
            }
            content.setSuccess_message(urlList);
            setResponseContent(res, content);
        } catch (Exception e) {
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    public ArrayList saveFile(MultipartFile[] files, String filePath) {
        ArrayList fileId = new ArrayList();
        if (files == null || files.length == 0) {
            return null;
        }
        try {
            CommonUtil.ifNotExistsCreate(filePath);
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".", file.getOriginalFilename().length()));
                String fileHashName = passwordEncoder.encodePassword(file.getOriginalFilename(), CommonUtil.getRandomString(6));
                file.transferTo(new File(filePath + fileHashName + suffix));
                String subStr = filePath.substring(filePath.lastIndexOf("upload") + 7) + fileHashName + suffix;
                Map<String, Object> map = new HashMap<>();
                map.put("file_path", subStr);
                fileService.insertFile(map);
                fileId.add(map.get("id"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return fileId;
    }

    //    3.2.2.1	GET /common/constants/districts
    @RequestMapping(value = "/common/constants/districts", method = RequestMethod.GET)
    public void districts(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<Map<String, Object>> lists = adminService.selDistricts();
            Map<String, Object> map = new HashMap();
            map.put("districts", lists);
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.2	GET /common/constants/provinces
    @RequestMapping(value = "/common/constants/provinces", method = RequestMethod.GET)
    public void provinces(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String district_id = req.getParameter("district_id");
            Map<String, Object> map = new HashMap(1);
            if (district_id != null) {
                map.put("district_id", district_id);
            }
            List<Map<String, Object>> lists = adminService.selProvinces(map);
            map.clear();
            map.put("provinces", lists);
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.2	GET /common/constants/cities
    @RequestMapping(value = "/common/constants/cities", method = RequestMethod.GET)
    public void cities(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String province_id = req.getParameter("province_id");
            Map<String, Object> map = new HashMap(1);
            if (province_id != null) {
                map.put("province_id", province_id);
            }
            List<Map<String, Object>> lists = adminService.selCities(map);
            map.clear();
            map.put("cities", lists);
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.2.2	GET /common/constants/zones
    @RequestMapping(value = "/common/constants/zones", method = RequestMethod.GET)
    public void zones(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String city_id = req.getParameter("city_id");
            Map<String, Object> map = new HashMap(1);
            if (city_id != null) {
                map.put("city_id", city_id);
            }
            List<Map<String, Object>> lists = adminService.selZones(map);
            map.clear();
            map.put("zones", lists);
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    产品常量
//    3.2.3.1	GET /common/constants/product_types
    @RequestMapping(value = "/common/constants/product_types", method = RequestMethod.GET)
    public void product_types(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<Map<String, Object>> lists = adminService.selProduct_types();
            Map<String, Object> map = new HashMap();
            map.put("product_types", lists);
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/device_types", method = RequestMethod.GET)
    public void device_types(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            String product_type_id = req.getParameter("product_type_id");
            Map<String, Object> map = new HashMap(1);
            if (product_type_id != null) {
                map.put("product_type_id", product_type_id);
            }
            List<Map<String, Object>> lists = adminService.selDevice_types(map);
            map.clear();
            map.put("device_types", lists);
            content.setSuccess_message(map);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    健康常量
//    3.2.4.1	GET /common/constants/data_types
    @RequestMapping(value = "/common/constants/data_types", method = RequestMethod.GET)
    public void data_types(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("data_type");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("data_type_id", constant.getConstant());
                map.put("data_type_name", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("data_types", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.4.2	GET /common/constants/file_types
    @RequestMapping(value = "/common/constants/file_types", method = RequestMethod.GET)
    public void file_types(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("file_type");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("file_type_id", constant.getConstant());
                map.put("file_type_name", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("file_types", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.2.4.3	GET /common/constants/wristband_columns
    @RequestMapping(value = "/common/constants/wristband_columns", method = RequestMethod.GET)
    public void wristband_columns(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("wristband_column");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("column_id", constant.getConstant());
                map.put("column_name", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("columns", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //3.2.4.4	GET /common/constants/sphygmomanometer_columns
    @RequestMapping(value = "/common/constants/sphygmomanometer_columns", method = RequestMethod.GET)
    public void sphygmomanometer_columns(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("sphygmomanometer_column");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("column_id", constant.getConstant());
                map.put("column_name", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("columns", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.2.4.5	GET /common/constants/glucosemeter_columns
    @RequestMapping(value = "/common/constants/glucosemeter_columns", method = RequestMethod.GET)
    public void glucosemeter_columns(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("glucosemeter_column");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("column_id", constant.getConstant());
                map.put("column_name", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("columns", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.2.4.6	GET /common/constants/thermometer_columns
    @RequestMapping(value = "/common/constants/thermometer_columns", method = RequestMethod.GET)
    public void thermometer_columns(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("thermometer_column");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("column_id", constant.getConstant());
                map.put("column_name", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("columns", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.2.4.7	GET /common/constants/oximeter_columns
    @RequestMapping(value = "/common/constants/oximeter_columns", method = RequestMethod.GET)
    public void oximeter_columns(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("oximeter_column");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("column_id", constant.getConstant());
                map.put("column_name", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("columns", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.2.4.8	GET /common/constants/fat_columns
    @RequestMapping(value = "/common/constants/fat_columns", method = RequestMethod.GET)
    public void fat_columns(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("fat_column");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("column_id", constant.getConstant());
                map.put("column_name", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("columns", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.2.4.9	GET /common/constants/comparison_ops
    @RequestMapping(value = "/common/constants/comparison_ops", method = RequestMethod.GET)
    public void comparison_ops(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("comparison_op");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("op_id", constant.getConstant());
                map.put("op_name", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("ops", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    3.2.4.10	GET /common/constants/logical_ops
    @RequestMapping(value = "/common/constants/logical_ops", method = RequestMethod.GET)
    public void ogical_ops(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("logical_op");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("op_id", constant.getConstant());
                map.put("op_name", constant.getExtra());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("ops", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.4.11 GET /common/constants/periods*/
    @RequestMapping(value = "/common/constants/periods", method = RequestMethod.GET)
    public void GetPeriods(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("period");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("period_id", constant.getConstant());
                map.put("period_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("periods", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    状态常量
//    3.2.5.1	GET /common/constants/user_status
    @RequestMapping(value = "/common/constants/user_status", method = RequestMethod.GET)
    public void user_status(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("user_status");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("user_status_id", constant.getConstant());
                map.put("user_status_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("user_status_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/manufactory_status", method = RequestMethod.GET)
    public void manufactory_status(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("manufactory_status");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("manufactory_status_id", constant.getConstant());
                map.put("manufactory_status_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("manufactory_status_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/terminal_status", method = RequestMethod.GET)
    public void terminal_status(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("terminal_status");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("terminal_status_id", constant.getConstant());
                map.put("terminal_status_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("terminal_status_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/doctor_status", method = RequestMethod.GET)
    public void doctor_status(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("doctor_status");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("doctor_status_id", constant.getConstant());
                map.put("doctor_status_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("doctor_status_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/health_consultation_status", method = RequestMethod.GET)
    public void health_consultation_status(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("health_consultation_status");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("consultation_status_id", constant.getConstant());
                map.put("consultation_status_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("consultation_status_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/health_data_status", method = RequestMethod.GET)
    public void health_data_status(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("health_data_status");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("data_status_id", constant.getConstant());
                map.put("data_status_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("data_status_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/health_file_status", method = RequestMethod.GET)
    public void health_file_status(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("health_file_status");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("file_status_id", constant.getConstant());
                map.put("file_status_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("file_status_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    //    状态常量3.2.6.1	GET /common/constants/company_departments
    @RequestMapping(value = "/common/constants/company_departments", method = RequestMethod.GET)
    public void company_departments(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("company_department");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("department_id", constant.getConstant());
                map.put("department_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("departments", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/company_members", method = RequestMethod.GET)
    public void company_members(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("company_members");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("member_id", constant.getConstant());
                map.put("member_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("members", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/company_natures", method = RequestMethod.GET)
    public void company_natures(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("company_nature");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("nature_id", constant.getConstant());
                map.put("nature_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("natures", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/company_industries", method = RequestMethod.GET)
    public void company_industries(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("company_industry");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("industry_id", constant.getConstant());
                map.put("industry_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("industries", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.7.1 GET /common/constants/departments*/
    @RequestMapping(value = "/common/constants/departments", method = RequestMethod.GET)
    public void GetDepartments(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<Map<String, Object>> department = constantService.qryDepartment();

            JsonArray array = new JsonArray();
            for (Map<String, Object> map : department) {
                JsonObject item = new JsonObject();
                item.add("department_id", gson.toJsonTree(map.get("department_id")));
                item.add("department_name", gson.toJsonTree(map.get("name")));
                array.add(item);
            }

            JsonObject json = new JsonObject();
            json.add("departments", array);
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/common/constants/genders", method = RequestMethod.GET)
    public void genders(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("gender");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("gender_id", constant.getConstant());
                map.put("gender_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("genders", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.7.3 GET /common/constants/template_types*/
    @RequestMapping(value = "/common/constants/template_types", method = RequestMethod.GET)
    public void GetConTemplateType(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("template_type");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("template_type_id", constant.getConstant());
                map.put("template_type_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("type_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.8.4 GET /common/constants/user_roles*/
    @RequestMapping(value = "/common/constants/user_roles", method = RequestMethod.GET)
    public void GetConUserRoles(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("user_role");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("user_role_id", constant.getConstant());
                map.put("user_role_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("role_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.8.5 GET /common/constants/op_titles*/
    @RequestMapping(value = "/common/constants/op_titles", method = RequestMethod.GET)
    public void GetConOpTitles(HttpServletRequest req, HttpServletResponse res) {
        ResponseContent content = new ResponseContent();
        try {
            List<ConstantDescription> constants = constantService.selDescriptionByCategory("op_title");
            ArrayList list = new ArrayList();
            for (ConstantDescription constant : constants) {
                Map<String, Object> map = new HashMap();
                map.put("op_title_id", constant.getConstant());
                map.put("op_title_name", constant.getDescription());
                list.add(map);
            }
            JsonObject json = new JsonObject();
            json.add("title_list", gson.toJsonTree(list));
            content.setSuccess_message(json);
            setResponseContent(res, content);
        } catch (JsonSyntaxException e) {
            setRequestErrorRes(res, "JSON格式错误", ErrorCode.REQUEST_PARAM_FAORMAT_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setSystemErrorRes(res, e.toString(), ErrorCode.SYSTEM_ERROR);
        }
    }

    /*3.2.1.4 GET /common/show_pic*/
    @RequestMapping(value = "/common/show_pic", method = RequestMethod.GET)
    public void GetShowPicture(HttpServletRequest req, HttpServletResponse res) {
        try {
            String url = req.getParameter("url");
            int beginIndex = url.indexOf("file_id=") + 8;
            int file_id = Integer.valueOf(url.substring(beginIndex));

            Map<String, Object> map = fileService.selectFileById(Integer.valueOf(file_id));
            if (map == null) {
                setParamWarnRes(res, "下载的文件不存在", ErrorCode.FILE_NOT_EXIST);
                return;
            }

            ServletContext context = req.getSession().getServletContext();
            String appPath = context.getRealPath("/") + "../upload/";
            String fullPath = appPath + map.get("file_path");
            File downloadFile = new File(fullPath);
            if (!downloadFile.exists()) {
                setParamWarnRes(res, "下载的文件不存在", ErrorCode.FILE_NOT_EXIST);
                return;
            }

            FileInputStream inputStream = new FileInputStream(downloadFile);
            OutputStream outStream = res.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();
        } catch (IOException e) {
            setRequestErrorRes(res, "文件下载失败", ErrorCode.FAILDOWNLOAD);
            return;
        } catch (Exception e) {
            setSystemErrorRes(res, "系统错误", ErrorCode.SYSTEM_ERROR);
            return;
        }
    }


}
