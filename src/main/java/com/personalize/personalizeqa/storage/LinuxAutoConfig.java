package com.personalize.personalizeqa.storage;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.personalize.personalizeqa.dto.FileDeleteDO;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.InfoSourceAttach;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.personalize.personalizeqa.utils.StrPool.SLASH;

@Slf4j
@Configuration
@EnableConfigurationProperties(FileServerProperties.class)
@ConditionalOnProperty(name = "personalize.file.type",havingValue = "LINUX")
public class LinuxAutoConfig {
    @Service
    public class LinuxServiceImpl extends AbstractFileStrategy{
        private void buildClient(){
            properties = fileProperties.getLinux();
        }
        @Override
        public String getFileContent(String relativePath, String fileName) {
            buildClient();
            String absolutePath = Paths.get(properties.getEndpoint(),properties.getBucketName(),relativePath,fileName).toString();
            java.io.File file = new java.io.File(absolutePath);
            if(!file.exists()){
                log.error("文件不存在");
                return null;
            }
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                String fileContent = new String(fileBytes, "UTF-8");  // 根据实际编码调整
                return fileContent;
            } catch (IOException e) {
                log.error("读取文件内容时出错：" + e.getMessage());
                return null;
            }
        }
        @Override
        public void uploadFile(File file, MultipartFile multipartFile) throws Exception {
            buildClient();
            //生成文件名
            String fileName = UUID.randomUUID().toString()+ StrPool.DOT+file.getExt();
            LocalDateTime now = LocalDateTime.now();
            //生成日期+数据集名字的相对路径
            String relativePath = Paths.get(now.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_FORMAT))+SLASH+file.getFolder()).toString();
            //生成绝对路径
            String absolutePath = Paths.get(properties.getEndpoint(),properties.getBucketName(),relativePath).toString();
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
        public void uploadInfoSourceFileImpl(InfoSourceFile infoSourceFile, MultipartFile multipartFile) throws Exception {
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
        public void uploadInfoSourceAttachImpl(InfoSourceAttach infoSourceAttach, MultipartFile multipartFile) throws Exception {
            buildClient();
            String fileName = UUID.randomUUID().toString()+StrPool.DOT+infoSourceAttach.getExt();
            LocalDateTime now = LocalDateTime.now();
            String relativePath = Paths.get(now.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_FORMAT))+SLASH+infoSourceAttach.getTaskCode()+SLASH+"attach").toString();
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
            infoSourceAttach.setUrl(url);
            infoSourceAttach.setFileName(fileName);
            infoSourceAttach.setRelativePath(relativePath);
        }

        @Override
        public void delete(FileDeleteDO fileDTO) {
            buildClient();
            String filePath = Paths.get(properties.getEndpoint(),properties.getBucketName(),fileDTO.getRelativePath(),fileDTO.getFileName()).toString();
            java.io.File file = new java.io.File(filePath);
            FileUtils.deleteQuietly(file);
        }

        @Override
        public void deleteFolder(String rel_folder) {
            buildClient();
            String filePath = Paths.get(properties.getEndpoint(),properties.getBucketName(),rel_folder).toString();
            java.io.File fileFolder = new java.io.File(filePath);
            if ((!fileFolder.exists())||(!fileFolder.isDirectory())){
                log.error("不是文件夹或者不存在文件夹");
            }
            java.io.File[] files = fileFolder.listFiles();
            if (files.length!=0){
                log.error("文件夹不为空");
            }
            fileFolder.delete();
        }
    }
}
