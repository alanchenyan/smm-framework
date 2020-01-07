package com.smm.framework.util;

import com.smm.framework.exception.ServiceException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Alan Chen
 * @description 文件上传工具类
 * @date 2020-01-07
 */
public class FileUpLoadTool {

    public static String uploadFile(MultipartFile file) {
        String directory = getDefalutUploadImagesDirectory();
        return uploadFile(file,directory);
    }

    public static String uploadFile(MultipartFile file,String fileDirectoryPath) {
        long maxFileSizeUnitkb = 30;
        return uploadFile(file,fileDirectoryPath,maxFileSizeUnitkb);
    }

    /**
     *
     * @param file 上传的文件
     * @param fileDirectoryPath
     * @param maxFileSizeUnitkb 文件最大大小（单位KB）
     * @return
     */
    public static String uploadFile(MultipartFile file,String fileDirectoryPath,long maxFileSizeUnitkb) {

        long maxFileSizeByte = maxFileSizeUnitkb  * 1024;
        if(file.getSize() > maxFileSizeByte){
            throw new ServiceException("最大可上传"+maxFileSizeUnitkb+"KB的文件,请调整后重新上传");
        }

        try {
            String oldFileName = file.getOriginalFilename();
            String[] names = oldFileName.split("\\.");
            if(names.length>1){
                String fileType = names[names.length-1];
                String imageName = getRandomImageName()+"."+fileType;
                file.transferTo(new File(fileDirectoryPath + imageName));
                return imageName;
            }else{
                throw new ServiceException("文件名称不合法");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("图片上传失败");
        }
    }

    public static void deleteFile(String imageName) {
        String directory = getDefalutUploadImagesDirectory();
        deleteFileByPath(directory+imageName);
    }

    /**
     *
     * @param fileDirectoryPath
     * @param imageName
     */
    public static void deleteFile(String fileDirectoryPath,String imageName) {
        deleteFileByPath(fileDirectoryPath+imageName);
    }

    public static void deleteFileByPath(String filePath) {
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                FileUtils.forceDelete(file);
            }
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    private static String getDefalutUploadImagesDirectory(){
        String currentDirectory = null;
        try {
            //得到当前目录
            currentDirectory = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("图片上传失败");
        }
        String directory = currentDirectory + "/uploadfiles/";

        File dirFile = new File(directory);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return directory;
    }

    private static String getRandomImageName() {
        return System.currentTimeMillis() + RandomStringUtils.randomAlphanumeric(6);
    }
}
