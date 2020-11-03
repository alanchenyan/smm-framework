package com.smm.framework.util;

import com.smm.framework.exception.ServiceException;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Alan Chen
 * @description 文件下载
 * @date 2020-02-17
 */
public class FileDownloadTool {

    private FileDownloadTool(){}

    public static String downloadPicture(String picUrl) {
        String pictureName = FileUpLoadTool.getRandomImageName()+".jpg";
        String defaultPath = FileUpLoadTool.getDefalutUploadFilesDirectory()+"/"+pictureName;
        downloadPicture(picUrl,defaultPath);
        return FileUpLoadTool.FILE_REDIRECT_NAME+"/"+pictureName;
    }

    public static void downloadPicture(String picUrl,String path) {
        URL url = null;
        try {
            url = new URL(picUrl);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(buffer);//返回Base64编码过的字节数组字符串
            System.out.println(encode);
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downLoadFile(HttpServletResponse response, String fileUrl) {
        String filePath = fileUrl;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ServiceException("您需要下载的文件不存在");
        }
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String[] fileUrlArry = fileUrl.split("/");
        String fileName = fileUrlArry[fileUrlArry.length - 1];
        try {
            String encodeFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + encodeFileName);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ServiceException("文件名编码错误");
        }

        byte[] buffer = new byte[1024];
        OutputStream os = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer);
                i = bis.read(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("读取文件错误");
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceException("关闭数据流错误");
            }
        }
    }
}
