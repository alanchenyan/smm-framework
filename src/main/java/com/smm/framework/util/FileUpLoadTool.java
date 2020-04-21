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

    private static final long DEFAULT_MAX_SIZE_UNIT_KB = 1024 * 10;

    private static final String DEFALUT_UPLOAD_FILE_DIRECTORY = "/uploadfiles/";

    public static final String FILE_REDIRECT_NAME = "files";

    private static final I18nResource I18NRESOURCE = I18nResourceFactory.getI18nResource();

    private FileUpLoadTool(){}

    public static String uploadFile(MultipartFile file) {
        String directory = getDefalutUploadFilesDirectory();
        return uploadFile(file,directory);
    }

    public static String uploadFile(MultipartFile file,long maxFileSizeUnitkb) {
        String directory = getDefalutUploadFilesDirectory();
        return uploadFile(file,directory,FILE_REDIRECT_NAME,maxFileSizeUnitkb);
    }

    public static String uploadFile(MultipartFile file,String fileDirectoryPath) {
        return uploadFile(file,fileDirectoryPath,FILE_REDIRECT_NAME,DEFAULT_MAX_SIZE_UNIT_KB);
    }

    public static String uploadFile(MultipartFile file,String fileDirectoryPath,String fileRedirectName) {
        return uploadFile(file,fileDirectoryPath,fileRedirectName,DEFAULT_MAX_SIZE_UNIT_KB);
    }

    /**
     *
     * @param file 上传的文件
     * @param fileDirectoryPath
     * @param maxFileSizeUnitkb 文件最大大小（单位KB）
     * @return
     */
    public static String uploadFile(MultipartFile file,String fileDirectoryPath,String fileRedirectName,long maxFileSizeUnitkb) {

        long maxFileSizeByte = maxFileSizeUnitkb  * 1024;
        if(file.getSize() > maxFileSizeByte){
            String tips = I18NRESOURCE.getValue("max_upload_file_size_01")  + maxFileSizeUnitkb + I18NRESOURCE.getValue("max_upload_file_size_02");
            throw new ServiceException(tips);
        }

        try {
            String oldFileName = file.getOriginalFilename();
            String[] names = oldFileName.split("\\.");
            if(names.length>1){
                String fileType = names[names.length-1];
                String imageName = getRandomImageName()+"."+fileType;
                file.transferTo(new File(fileDirectoryPath + imageName));
                return fileRedirectName+"/"+imageName;
            }else{
                throw new ServiceException(I18NRESOURCE.getValue("bad_filename"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(I18NRESOURCE.getValue("file_upload_failed"));
        }
    }

    public static void deleteFile(String imageName) {
        String directory = getDefalutUploadFilesDirectory();
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

    public static String getDefalutUploadFilesDirectory(){
        String currentDirectory = null;
        try {
            //得到当前目录
            currentDirectory = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(I18NRESOURCE.getValue("file_upload_failed"));
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
