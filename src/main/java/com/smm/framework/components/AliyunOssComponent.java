package com.smm.framework.components;

import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.smm.framework.util.FileUpLoadTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Alan Chen
 * @description 阿里云OSS文件管理
 * @date 2020-07-02
 */
@Component
public class AliyunOssComponent {

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    /**
     * 上传缩略图
     * @param scanSize 指定图片尺寸大小
     * @param file
     * @return
     */
    public String uploadImageResizeSmall(int scanSize,MultipartFile file){
        boolean checkResult = checkParameter();
        if(checkResult){
            String tempFileName = FileUpLoadTool.uploadImageByResize(scanSize,file);
            String fileName = dealFile(tempFileName);
            return fileName;
        }
        return null;
    }

    /**
     * 上传图片到OSS(新文件名为随机数、图片会被压缩)
     * @param file
     * @return
     */
    public String uploadImageByResize(MultipartFile file) {

        boolean checkResult = checkParameter();
        if(checkResult){
            String tempFileName = FileUpLoadTool.uploadImageByResize(file);
            String fileName = dealFile(tempFileName);
            return fileName;
        }

       return null;
    }

    /**
     * 上传任意格式的文件(新文件名为随机数)
     * @param file
     * @return
     */
    public String uploadFile(MultipartFile file) {
        boolean checkResult = checkParameter();
        if(checkResult){
            String tempFileName = FileUpLoadTool.uploadFile(file);
            String fileName = dealFile(tempFileName);
            return fileName;
        }
        return null;
    }

    /**
     * 上传任意文件(保留原来的文件名)
     * @param file
     * @return
     */
    public String uploadFileUseOriginalFilename(MultipartFile file) {
       return uploadFileUseOriginalFilename(file,40*1024);
    }

    /**
     * 上传任意文件(保留原来的文件名)
     * @param file
     * @return
     */
    public String uploadFileUseOriginalFilename(MultipartFile file,long maxFileSizeUnitkb) {
        boolean checkResult = checkParameter();
        if(checkResult){
            String tempFileName = FileUpLoadTool.uploadFile(file,true,maxFileSizeUnitkb);
            String fileName = dealFile(tempFileName);
            return fileName;
        }
        return null;
    }

    public String uploadFile(File file) {

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, file.getName(), file);
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();

        return file.getName();
    }

    private boolean checkParameter(){
        if(StringUtils.isBlank(endpoint)){
            throw new RuntimeException("请配置endpoint");
        }

        if(StringUtils.isBlank(accessKeyId)){
            throw new RuntimeException("请配置accessKeyId");
        }

        if(StringUtils.isBlank(accessKeySecret)){
            throw new RuntimeException("请配置accessKeySecret");
        }

        if(StringUtils.isBlank(bucketName)){
            throw new RuntimeException("请配置bucketName");
        }

        return true;
    }

    private String dealFile(String tempFileName){
        String[] tempFileNameArry = tempFileName.split("/");
        if(tempFileNameArry.length == 2){
            tempFileName = tempFileNameArry[1];
        }

        String tempFileDirectory = FileUpLoadTool.getDefalutUploadFilesDirectory()+"/"+tempFileName;
        File tempFile = FileUtil.file(tempFileDirectory);
        String fileName =  uploadFile(tempFile);

        //程序结束时，删除原图
        FileUpLoadTool.deleteFile(tempFile);

        return fileName;
    }
}
