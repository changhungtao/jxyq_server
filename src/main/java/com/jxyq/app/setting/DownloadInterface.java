package com.jxyq.app.setting;

import com.jxyq.app.BaseInterface;
import com.jxyq.commons.constants.ErrorCode;
import com.jxyq.service.inf.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class DownloadInterface extends BaseInterface {
    @Autowired
    private FileService fileService;

    private static final int BUFFER_SIZE = 1024*20;

    @RequestMapping(value = "/api/open/download", method = RequestMethod.GET)
    public void DownloadFileInterface(HttpServletRequest req, HttpServletResponse res) {
        try {
            String fileId = req.getParameter("file_id");
            if (StringUtils.isBlank(fileId)) {
                setRequestErrorRes(res, "缺少必要请求参数", ErrorCode.REQUEST_PARAM_MISS);
                return;
            }
            Map<String, Object> map = fileService.selectFileById(Integer.valueOf(fileId));
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
            String mimeType = context.getMimeType(fullPath);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            res.setContentType(mimeType);
            res.setContentLength((int) downloadFile.length());

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    downloadFile.getName());
//            res.setHeader(headerKey, headerValue);

            OutputStream outStream = res.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();
        }catch (IOException e){
//            setRequestErrorRes(res, "文件下载失败", ErrorCode.FAILDOWNLOAD);
            return;
        }catch (Exception e){
            setSystemErrorRes(res, "系统错误", ErrorCode.SYSTEM_ERROR);
            return;
        }
    }
}
