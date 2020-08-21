package com.smm.framework.util;

import cn.hutool.core.img.Img;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.smm.framework.exception.ServiceException;
import com.smm.framework.i18n.I18nResource;
import com.smm.framework.i18n.I18nResourceFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Alan Chen
 * @description 文件上传工具类
 * @date 2020-01-07
 */
public class FileUpLoadTool {

    private static final long DEFAULT_MAX_SIZE_UNIT_KB = 1024 * 10;

    private static final String DEFALUT_UPLOAD_FILE_DIRECTORY = "uploadfiles";

    public static final String FILE_REDIRECT_NAME = "files";

    private static final String UTF_8 = "UTF-8";

    private static final double DEFAULT_QUALITY = 0.7;

    private static final I18nResource I18NRESOURCE = I18nResourceFactory.getI18nResource();

    private FileUpLoadTool(){}

    public static String uploadFile(MultipartFile file) {
        String directory = getDefalutUploadFilesDirectory();
        return uploadFile(file,directory,false);
    }

    public static String uploadFile(MultipartFile file,boolean useOriginalFilename) {
        String directory = getDefalutUploadFilesDirectory();
        return uploadFile(file,directory,useOriginalFilename);
    }

    public static String uploadFile(MultipartFile file,long maxFileSizeUnitkb) {
        String directory = getDefalutUploadFilesDirectory();
        return uploadFile(file,directory,FILE_REDIRECT_NAME,false,maxFileSizeUnitkb);
    }

    public static String uploadFile(MultipartFile file,boolean useOriginalFilename,long maxFileSizeUnitkb) {
        String directory = getDefalutUploadFilesDirectory();
        return uploadFile(file,directory,FILE_REDIRECT_NAME,useOriginalFilename,maxFileSizeUnitkb);
    }

    public static String uploadFile(MultipartFile file,String fileDirectoryPath,boolean useOriginalFilename) {
        return uploadFile(file,fileDirectoryPath,FILE_REDIRECT_NAME,useOriginalFilename,DEFAULT_MAX_SIZE_UNIT_KB);
    }

    public static String uploadFile(MultipartFile file,String fileDirectoryPath,String fileRedirectName,boolean useOriginalFilename) {
        return uploadFile(file,fileDirectoryPath,fileRedirectName,useOriginalFilename,DEFAULT_MAX_SIZE_UNIT_KB);
    }

    /**
     * 上传图片并压缩
     * @param file
     * @return
     */
    public static String uploadImageByResize(MultipartFile file) {
        return uploadImageByResize(file,0);
    }

    /**
     * 上传图片并压缩
     * @param file
     * @return
     */
    public static String uploadImageByResize(int scanSize,MultipartFile file) {
        double size = getFileSize(file.getSize(),"M");

        if(size<=0.5){
            return uploadImageByResize(file,DEFAULT_QUALITY,scanSize);
        }else if(size>0.5 && size<=1){
            return uploadImageByResize(file,0.5,scanSize);
        }else if(size>1 && size<=3){
            return FileUpLoadTool.uploadImageByResize(file,0.3,scanSize);
        }else if(size>3 && size<=6){
            return FileUpLoadTool.uploadImageByResize(file,0.2,scanSize);
        }else if(size>6){
            return FileUpLoadTool.uploadImageByResize(file,0.1,scanSize);
        }else{
            return uploadImageByResize(file,DEFAULT_QUALITY,scanSize);
        }
    }

    /**
     * 上传图片并压缩
     * @param file
     * @param quality 压缩比例：0~1
     * @return
     */
    public static String uploadImageByResize(MultipartFile file,double quality) {
        return uploadImageByResize(file,quality,0);
    }

    /**
     * 上传图片并裁剪、压缩
     * @param file
     * @param quality：压缩比例（像素不变）0~1
     * @param scanSize：等比例裁剪成指定尺寸
     * @return
     */
    public static String uploadImageByResize(MultipartFile file,double quality,int scanSize) {
        String originalFileName = FileUpLoadTool.uploadFile(file);
        originalFileName = originalFileName.replaceAll("files/","");
        String newFileName = getRandomImageName()+".jpg";

        File originalFile = FileUtil.file(FileUpLoadTool.getDefalutUploadFilesDirectory()+"/"+originalFileName);

        if(scanSize !=0){
            String scanFileName = imageScale(originalFile,scanSize);
            originalFile = FileUtil.file(FileUpLoadTool.getDefalutUploadFilesDirectory()+"/"+scanFileName);
        }

        try{
            Img.from(originalFile)
                    .setQuality(quality)
                    .write(FileUtil.file(FileUpLoadTool.getDefalutUploadFilesDirectory()+"/"+newFileName));

            //程序结束时，删除原图
            deleteFile(originalFile);

        }catch (Exception e){
            // 如果压缩失败（格式不支持）直接返回原图
            return "files/"+originalFileName;
        }

        return "files/"+newFileName;
    }

    /**
     * 将图片等比例裁剪成指定尺寸
     * @param file
     * @param size
     * @return
     */
    public static String imageScale(File file,int size){
        String newFileName = getRandomImageName()+".jpg";

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = bi.getWidth();
        int height = bi.getHeight();
        int min = width>height?height:width;

        float scale = (float)NumberTool.div(size,min);

        ImgUtil.scale(
                file,
                FileUtil.file(FileUtil.file(FileUpLoadTool.getDefalutUploadFilesDirectory()+"/"+newFileName)),
                scale//缩放比例
        );

        //程序结束时，删除原图
        deleteFile(file);

        return newFileName;
    }

    /**
     *
     * @param file 上传的文件
     * @param fileDirectoryPath
     * @param maxFileSizeUnitkb 文件最大大小（单位KB）
     * @return
     */
    public static String uploadFile(MultipartFile file,String fileDirectoryPath,String fileRedirectName,boolean useOriginalFilename,long maxFileSizeUnitkb) {

        long maxFileSizeByte = maxFileSizeUnitkb  * 1024;
        if(file.getSize() > maxFileSizeByte){
            String tips = I18NRESOURCE.getValue("max_upload_file_size_01")  + maxFileSizeUnitkb + I18NRESOURCE.getValue("max_upload_file_size_02");
            throw new ServiceException(tips);
        }

        try {
            String oldFileName = file.getOriginalFilename();
            if(useOriginalFilename){
                String fileAbsolutePath = fileDirectoryPath + oldFileName;
                String filePath = new String(fileAbsolutePath.getBytes(UTF_8) , UTF_8);
                file.transferTo(new File(filePath));
                return fileRedirectName+"/"+oldFileName;
            }else{
                String[] names = oldFileName.split("\\.");
                if(names.length>1){
                    String fileType = names[names.length-1];
                    String newFileName = getRandomImageName()+"."+fileType;
                    String fileAbsolutePath = fileDirectoryPath + newFileName;
                    String filePath = new String(fileAbsolutePath.getBytes(UTF_8) , UTF_8);
                    file.transferTo(new File(filePath));
                    return fileRedirectName+"/"+newFileName;
                }else{
                    throw new ServiceException(I18NRESOURCE.getValue("bad_filename"));
                }
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


    /**
     * 删除
     * @param files
     */
    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static String getRandomImageName() {
        return System.currentTimeMillis() + RandomStringUtils.randomAlphanumeric(6);
    }


    public static String getDefalutUploadFilesDirectory(){
        return getApplicationUploadFilesDirectory(DEFALUT_UPLOAD_FILE_DIRECTORY);
    }

    public static double getFileSize(Long len, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        return fileSize;
    }
}
