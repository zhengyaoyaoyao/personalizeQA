package com.personalize.personalizeqa.storage;


import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.personalize.personalizeqa.dto.FileDeleteDO;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.InfoSourceAttach;
import com.personalize.personalizeqa.entity.InfoSourceFile;
import com.personalize.personalizeqa.properties.FileServerProperties;
import com.personalize.personalizeqa.strategy.impl.AbstractFileStrategy;
import com.personalize.personalizeqa.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.personalize.personalizeqa.utils.StrPool.SLASH;

@Slf4j
@Configuration
@EnableConfigurationProperties(FileServerProperties.class)
@ConditionalOnProperty(name = "personalize.file.type",havingValue = "ALI")
public class AliOssAutoConfigure {
    @Service
    public class AliServiceImpl extends AbstractFileStrategy{

        /*
        构建阿里云OSS客户端
        @Return
         */
        private OSS buildClient(){
            properties = fileProperties.getAli();
            return new OSSClientBuilder().build(properties.getEndpoint(),
                    properties.getAccessKeyId(),
                    properties.getAccessKeySecret());

        }
        protected String getUriPrefix(){
            if (StringUtils.isNotEmpty(properties.getEndpoint())) {
                return properties.getEndpoint();
            } else {
                String prefix = properties.getEndpoint().contains("https://") ? "https://" : "http://";
                return prefix + properties.getBucketName() + "." + properties.getEndpoint().replaceFirst(prefix, "");
            }
        }

        /**
         * 上传标注文件
         * @param file
         * @param multipartFile
         * @throws Exception
         */

        @Override
        public void uploadFile(File file, MultipartFile multipartFile) throws Exception {
            OSS client = buildClient();
            //获取OSS空间名称
            String bucketName = properties.getBucketName();
            if (!client.doesBucketExist(bucketName)){
                //创建存储空间
                client.createBucket(bucketName);
            }
            //生成文件名
            String fileName  = UUID.randomUUID().toString()+ StrPool.DOT+file.getExt();
            //日期文件夹，例如:2020/04
            LocalDateTime now = LocalDateTime.now();
            //生成日期+数据集名字的相对路径
            String relativePath = now.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_FORMAT))+SLASH+file.getFolder().toString();
            String relativeFileName = relativePath+StrPool.SLASH+fileName;
            relativeFileName = StrUtil.replace(relativeFileName, "\\\\",
                    StrPool.SLASH);
            relativeFileName = StrUtil.replace(relativeFileName, "\\",
                    StrPool.SLASH);

            //对象元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentDisposition("attachment;fileName="+file.getSubmittedFileName());
            metadata.setContentType(file.getContextType());

            //上传请求对象
            PutObjectRequest request = new PutObjectRequest(bucketName,relativeFileName,multipartFile.getInputStream(),metadata);
            //上传文件到阿里云OSS空间
            PutObjectResult result = client.putObject(request);
            //文件上传完成后需要设置上传文件相关信息，用于保存到数据库中
            log.info("result={}", JSONObject.toJSONString(result));
            String url = getUriPrefix()+StrPool.SLASH+relativeFileName;
            url = StrUtil.replace(url, "\\\\", StrPool.SLASH);
            url = StrUtil.replace(url, "\\", StrPool.SLASH);
            //写入文件表
            file.setUrl(url);
            file.setFileName(fileName);
            file.setRelativePath(relativePath);
            file.setGroup(result.getETag());
            file.setPath(result.getRequestId());
            //关闭阿里云OSS客户端
            client.shutdown();
        }

        @Override
        public void uploadInfoSourceFileImpl(InfoSourceFile infoSourceFile, MultipartFile multipartFile) throws Exception {
            OSS client = buildClient();
            //获取OSS空间名称
            String bucketName = properties.getBucketName();
            if (!client.doesBucketExist(bucketName)){
                //创建存储空间
                client.createBucket(bucketName);
            }
            //生成文件名
            String fileName  = UUID.randomUUID().toString()+ StrPool.DOT+infoSourceFile.getExt();
            //日期文件夹，例如:2020/04
            LocalDateTime now = LocalDateTime.now();
            //生成日期+数据集名字的相对路径
            String relativePath = now.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_FORMAT))+SLASH+infoSourceFile.getTaskCode().toString();
            String relativeFileName = relativePath+StrPool.SLASH+fileName;
            relativeFileName = StrUtil.replace(relativeFileName, "\\\\",
                    StrPool.SLASH);
            relativeFileName = StrUtil.replace(relativeFileName, "\\",
                    StrPool.SLASH);

            //对象元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentDisposition("attachment;fileName="+infoSourceFile.getSubmittedFileName());
            metadata.setContentType(infoSourceFile.getContextType());

            //上传请求对象
            PutObjectRequest request = new PutObjectRequest(bucketName,relativeFileName,multipartFile.getInputStream(),metadata);
            //上传文件到阿里云OSS空间
            PutObjectResult result = client.putObject(request);
            //文件上传完成后需要设置上传文件相关信息，用于保存到数据库中
            log.info("result={}", JSONObject.toJSONString(result));
            String url = getUriPrefix()+StrPool.SLASH+relativeFileName;
            url = StrUtil.replace(url, "\\\\", StrPool.SLASH);
            url = StrUtil.replace(url, "\\", StrPool.SLASH);
            //写入文件表
            infoSourceFile.setUrl(url);
            infoSourceFile.setFileName(fileName);
            infoSourceFile.setRelativePath(relativePath);
            infoSourceFile.setGroup(result.getETag());
            infoSourceFile.setPath(result.getRequestId());
            //关闭阿里云OSS客户端
            client.shutdown();
        }

        @Override
        public void delete(FileDeleteDO fileDTO) {
            OSS client = buildClient();
            //获取OSS空间名称
            String bucketName = properties.getBucketName();
            //删除文件
            VoidResult voidResult = client.deleteObject(bucketName, fileDTO.getRelativePath() + StrPool.SLASH + fileDTO.getFileName());
            //关闭阿里云OSS客户端
            client.shutdown();
        }

        @Override
        public void deleteFolder(String rel_folder) {
            OSS client = buildClient();
            String bucketName = properties.getBucketName();
            String fileFolderPath = Paths.get(properties.getEndpoint(),properties.getBucketName(),rel_folder).toString();
            client.deleteObject(bucketName,fileFolderPath);
            client.shutdown();
        }

        @Override
        public String getFileContent(String relativePath, String fileName) {
            OSS client = buildClient();
            String bucketName = properties.getBucketName();
            String input = relativePath + StrPool.SLASH + fileName;
            try {
                // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
                OSSObject ossObject = client.getObject(bucketName, input);

                // 读取文件内容。
                System.out.println("Object content:");
                BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
                StringBuilder fileContent = new StringBuilder();
                while (true) {
                    String line = reader.readLine();
                    if (line == null) break;
                    fileContent.append(line).append("\n");
                }
                // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
                reader.close();
                // ossObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
                ossObject.close();
                return fileContent.toString();
            } catch (OSSException oe) {
                System.out.println("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.");
                System.out.println("Error Message:" + oe.getErrorMessage());
                System.out.println("Error Code:" + oe.getErrorCode());
                System.out.println("Request ID:" + oe.getRequestId());
                System.out.println("Host ID:" + oe.getHostId());
            } catch (Throwable ce) {
                System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with OSS, "
                        + "such as not being able to access the network.");
                System.out.println("Error Message:" + ce.getMessage());
            } finally {
                if (client != null) {
                    client.shutdown();
                }
            }
            return null;
        }

        @Override
        public void uploadInfoSourceAttachImpl(InfoSourceAttach infoSourceAttach, MultipartFile multipartFile) throws Exception {
            OSS client = buildClient();
            //获取OSS空间名称
            String bucketName = properties.getBucketName();
            if (!client.doesBucketExist(bucketName)){
                //创建存储空间
                client.createBucket(bucketName);
            }
            //生成文件名
            String fileName  = UUID.randomUUID().toString()+ StrPool.DOT+infoSourceAttach.getExt();
            //日期文件夹，例如:2020/04
            LocalDateTime now = LocalDateTime.now();
            //生成日期+数据集名字的相对路径
            String relativePath = now.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_FORMAT))+SLASH+infoSourceAttach.getTaskCode()+SLASH+"attach".toString();
            String relativeFileName = relativePath+StrPool.SLASH+fileName;
            relativeFileName = StrUtil.replace(relativeFileName, "\\\\",
                    StrPool.SLASH);
            relativeFileName = StrUtil.replace(relativeFileName, "\\",
                    StrPool.SLASH);

            //对象元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentDisposition("attachment;fileName="+infoSourceAttach.getSubmittedFileName());
            metadata.setContentType(infoSourceAttach.getContextType());

            //上传请求对象
            PutObjectRequest request = new PutObjectRequest(bucketName,relativeFileName,multipartFile.getInputStream(),metadata);
            //上传文件到阿里云OSS空间
            PutObjectResult result = client.putObject(request);
            //文件上传完成后需要设置上传文件相关信息，用于保存到数据库中
            log.info("result={}", JSONObject.toJSONString(result));
            String url = getUriPrefix()+StrPool.SLASH+relativeFileName;
            url = StrUtil.replace(url, "\\\\", StrPool.SLASH);
            url = StrUtil.replace(url, "\\", StrPool.SLASH);
            //写入文件表
            infoSourceAttach.setUrl(url);
            infoSourceAttach.setFileName(fileName);
            infoSourceAttach.setRelativePath(relativePath);
            infoSourceAttach.setGroup(result.getETag());
            infoSourceAttach.setPath(result.getRequestId());
            //关闭阿里云OSS客户端
            client.shutdown();
        }
    }


}
