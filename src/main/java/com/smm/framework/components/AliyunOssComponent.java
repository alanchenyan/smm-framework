package com.smm.framework.components;

import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.smm.framework.util.FileUpLoadTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Alan Chen
 * @description 阿里云OSS文件管理
 * @date 2020-07-02
 */
public class AliyunOssComponent {

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    public AliyunOssComponent() {
    }

    public AliyunOssComponent(String endpoint,String accessKeyId,String accessKeySecret,String bucketName) {
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
    }

    public String uploadImage(MultipartFile file) {

        boolean checkResult = checkParameter();
        if(checkResult){
            String tempFileName = FileUpLoadTool.uploadImageByResize(file);
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

       return null;
    }

    public String uploadFile(File file) {

        OSS ossClient = new OSSClientBuilder().build(getEndpoint(), getAccessKeyId(), getAccessKeySecret());
        PutObjectRequest putObjectRequest = new PutObjectRequest(getBucketName(), file.getName(), file);
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();

        return file.getName();
    }

    private boolean checkParameter(){
        if(StringUtils.isBlank(getEndpoint())){
            throw new RuntimeException("请配置endpoint");
        }

        if(StringUtils.isBlank(getAccessKeyId())){
            throw new RuntimeException("请配置accessKeyId");
        }

        if(StringUtils.isBlank(getAccessKeySecret())){
            throw new RuntimeException("请配置accessKeySecret");
        }

        if(StringUtils.isBlank(getBucketName())){
            throw new RuntimeException("请配置bucketName");
        }

        return true;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }
}
