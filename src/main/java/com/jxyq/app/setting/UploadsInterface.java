package com.jxyq.app.setting;

import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.commons.util.CommonUtil;
import com.jxyq.model.others.ResponseContent;
import com.jxyq.service.inf.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadsInterface extends BaseInterface {
    @Autowired
    private FileService insertFileInfo;
    @Autowired
    private MessageDigestPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/api/uploads", method = RequestMethod.POST)
    public void UploadFilesInterface(@RequestParam("files") MultipartFile[] files,
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

    //上传多文件
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
                insertFileInfo.insertFile(map);
                fileId.add(map.get("id"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return fileId;
    }
}
