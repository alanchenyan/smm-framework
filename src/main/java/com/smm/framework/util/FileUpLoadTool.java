package com.smm.framework.util;

import com.smm.framework.exception.ServiceException;
import com.smm.framework.i18n.I18nResource;
import com.smm.framework.i18n.I18nResourceFactory;
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

    private static long DEFAULT_MAX_SIZE_UNIT_KB = 1024 * 5;

    private static String DEFALUT_UPLOAD_FILE_DIRECTORY = "/uploadfiles/";

    private static I18nResource i18nResource = I18nResourceFactory.getI18nResource();

    public static String uploadFile(MultipartFile file) {
        String directory = getDefalutUploadImagesDirectory();
        return uploadFile(file,directory);
    }

    public static String uploadFile(MultipartFile file,String fileDirectoryPath) {
        return uploadFile(file,fileDirectoryPath,DEFAULT_MAX_SIZE_UNIT_KB);
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
            String tips = i18nResource.getValue("max_upload_file_size_01")  + maxFileSizeUnitkb + i18nResource.getValue("max_upload_file_size_02");
            throw new ServiceException(tips);
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
                throw new ServiceException(i18nResource.getValue("bad_filename"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(i18nResource.getValue("file_upload_failed"));
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
            throw new ServiceException(i18nResource.getValue("file_upload_failed"));
        }
        String directory = currentDirectory +DEFALUT_UPLOAD_FILE_DIRECTORY;

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
