package com.personalize.personalizeqa.utils;

import com.personalize.personalizeqa.enumeration.DataType;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 识别文件类型的工具
 */
@Slf4j
public class FileDataTypeUtil {
    final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM");
    private final static String IMAGE = "image";
    private final static String VIDEO = "video";
    private final static String DIR = "application/x-director";
    private final static String AUDIO = "audio";
    private final static String TEXT = "text";

    /**
     * 根据mine类型，返回文件类型
     */
    public static DataType getDataType(String mime){
        if (mime ==null|| "".equals(mime)){
            return DataType.OTHER;
        }
        if (mime.contains(IMAGE)) {
            return DataType.IMAGE;
        } else if (mime.contains(TEXT)
                || mime.startsWith("application/vnd.ms-excel")
                || mime.startsWith("application/msword")
                || mime.startsWith("application/pdf")
                || mime.startsWith("application/vnd.ms-project")
                || mime.startsWith("application/vnd.ms-works")
                || mime.startsWith("application/x-javascript")
                || mime.startsWith("application/vnd.openxmlformats-officedocument")
                || mime.startsWith("application/vnd.ms-word.document.macroEnabled")
                || mime.startsWith("application/vnd.ms-word.template.macroEnabled")
                || mime.startsWith("application/vnd.ms-powerpoint")
        ) {
            return DataType.DOC;
        } else if (mime.contains(VIDEO)) {
            return DataType.VIDEO;
        } else if (mime.contains(DIR)) {
            return DataType.DIR;
        } else if (mime.contains(AUDIO)) {
            return DataType.AUDIO;
        } else {
            return DataType.OTHER;
        }
    }

    public static String getUploadPathPrefix(String uploadPathPrefix){
        //日期文件夹
        String secDir = LocalDate.now().format(DTF);
        //web服务器的绝对路径 D:\\uploadFiles\\,根据日期进行划分文件夹存放
        String absoluthPath = Paths.get(uploadPathPrefix,secDir).toString();
        return absoluthPath;
    }
    public static String getRelativePath(String pathPrefix,String path){
        String remove = StringUtils.remove(path,pathPrefix+ File.separator);
        log.info("remove ={},index={}",remove,remove.lastIndexOf(File.separator));
        String relativePath = StringUtils.substring(remove,0,remove.lastIndexOf(File.separator));
        return relativePath;
    }

}
