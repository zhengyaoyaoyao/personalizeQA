package com.personalize.personalizeqa.storage;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.personalize.personalizeqa.dto.FileDeleteDO;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.InfoSourceFile;
import com.personalize.personalizeqa.properties.FileServerProperties;
import com.personalize.personalizeqa.strategy.impl.AbstractFileStrategy;
import com.personalize.personalizeqa.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.core.env.Environment;

import static com.personalize.personalizeqa.utils.StrPool.SLASH;

/**
 * 本地上传策略
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(FileServerProperties.class)
@ConditionalOnProperty(name = "personalize.file.type",havingValue = "LOCAL")
public class LocalAutoConfigure {
    @Service
    public class LocalServiceImpl extends AbstractFileStrategy{

        /**
         * 获得配置文件的属性
         */
        private void buildClient(){
            properties= fileProperties.getLocal();
            //http://192.168.10.131:10000
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                String uri = "http://"+localhost.getHostAddress().toString()+":10000";
                properties.setUriPrefix(uri);
            }catch (UnknownHostException e){
                log.error("无法正确获取地址，当前地址为：{}",properties.getUriPrefix());
            }
        }

        /**
         * 具体的上传方法
         * @param file
         * @param multipartFile
         * @throws Exception
         */
        @Override
        public void uploadFile(File file, MultipartFile multipartFile) throws Exception {
            buildClient();
            //生成文件名
            String fileName = UUID.randomUUID().toString() + StrPool.DOT + file.getExt();
            LocalDateTime now = LocalDateTime.now();
            //生成日期+数据集名字的相对路径
            String relativePath = Paths.get(now.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_FORMAT))+SLASH+file.getFolder()).toString();
            // web服务器存放的绝对路径，例如：D:\\uploadFiles\\dataset-files\\2023
            String absolutePath = Paths.get(properties.getEndpoint(), properties.getBucketName(), relativePath).toString();
            //目标输出文件
            java.io.File outFile = new java.io.File(Paths.get(absolutePath,fileName).toString());
            //向目标文件写入数据
            org.apache.commons.io.FileUtils.writeByteArrayToFile(outFile,multipartFile.getBytes());

            String url = new StringBuilder(getUriPrefix())
                    .append(properties.getBucketName())
                    .append(StrPool.SLASH)
                    .append(relativePath)
                    .append(StrPool.SLASH)
                    .append(fileName)
                    .toString();
            //替换掉windows环境的\路径
            url = StrUtil.replace(url, "\\\\", StrPool.SLASH);
            url = StrUtil.replace(url, "\\", StrPool.SLASH);
            file.setUrl(url);
            file.setFileName(fileName);
            file.setRelativePath(relativePath);
        }

        @Override
        public void uoloadInfoSourceFileImpl(InfoSourceFile infoSourceFile, MultipartFile multipartFile) throws Exception {
            buildClient();
            String fileName = UUID.randomUUID().toString()+StrPool.DOT+infoSourceFile.getExt();
            LocalDateTime now = LocalDateTime.now();
            String relativePath = Paths.get(now.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_FORMAT))+SLASH+infoSourceFile.getTaskCode()).toString();
            String absolutePath = Paths.get(properties.getEndpoint(),properties.getBucketName(),relativePath).toString();
            java.io.File outFile = new java.io.File(Paths.get(absolutePath,fileName).toString());
            org.apache.commons.io.FileUtils.writeByteArrayToFile(outFile,multipartFile.getBytes());
            String url = new StringBuilder(getUriPrefix())
                    .append(properties.getBucketName())
                    .append(StrPool.SLASH)
                    .append(relativePath)
                    .append(StrPool.SLASH)
                    .append(fileName)
                    .toString();
            url = StrUtil.replace(url, "\\\\", StrPool.SLASH);
            url = StrUtil.replace(url, "\\", StrPool.SLASH);
            infoSourceFile.setUrl(url);
            infoSourceFile.setFileName(fileName);
            infoSourceFile.setRelativePath(relativePath);
        }

        @Override
        public void delete(FileDeleteDO fileDeleteDO) {
            buildClient();
            //拼接删除文件的绝对磁盘路径
            String filePath = Paths.get(properties.getEndpoint(), properties.getBucketName(), fileDeleteDO.getRelativePath(), fileDeleteDO.getFileName()).toString();
            java.io.File file = new java.io.File(filePath);
            FileUtils.deleteQuietly(file);
        }

        @Override
        public void deleteFolder(String rel_folder) {
            buildClient();
            String fileFolderPath = Paths.get(properties.getEndpoint(),properties.getBucketName(),rel_folder).toString();
            java.io.File fileFolder = new java.io.File(fileFolderPath);
            if ((!fileFolder.exists())||(!fileFolder.isDirectory())){
                log.error("不是文件夹或者不存在文件夹");
            }
            java.io.File[] files = fileFolder.listFiles();
            if (files.length!=0){
                log.error("不是空的文件夹，请先确认是否删除里面文件");
            }
            fileFolder.delete();
        }

        @Override
        public String getFileContent(String relativePath, String fileName) {
            return null;
        }
    }
}
