package com.smm.framework.util;

import com.smm.framework.exception.ServiceException;
import com.smm.framework.i18n.I18nResource;
import com.smm.framework.i18n.I18nResourceFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Alan Chen
 * @description
 * @date 2020/9/8
 */
public class ApplicationDirectoryTool {

    private static final I18nResource I18NRESOURCE = I18nResourceFactory.getI18nResource();

    private ApplicationDirectoryTool(){}

    public static String getApplicationUploadFilesDirectory(String uploadFileDirectory){
        String currentDirectory = null;
        try {
            //得到当前目录
            currentDirectory = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(I18NRESOURCE.getValue("file_upload_failed"));
        }
        String directory = currentDirectory +"/"+uploadFileDirectory+"/";

        File dirFile = new File(directory);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return directory;
    }
}
