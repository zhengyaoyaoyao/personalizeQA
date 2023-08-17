package com.personalize.personalizeqa.storage;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.personalize.personalizeqa.dto.FileDTO;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.properties.FileServerProperties;
import com.personalize.personalizeqa.strategy.impl.AbstractFileStrategy;
import com.personalize.personalizeqa.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
            // web服务器存放的绝对路径，例如：D:\\uploadFiles\\oss-file-service\\2020\\04
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
        public void delete(FileDTO fileDTO) {

        }
    }
}
