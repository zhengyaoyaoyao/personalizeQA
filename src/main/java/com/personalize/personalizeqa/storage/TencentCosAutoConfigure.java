package com.personalize.personalizeqa.storage;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSException;
import com.personalize.personalizeqa.dto.FileDeleteDO;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.InfoSourceAttach;
import com.personalize.personalizeqa.entity.InfoSourceFile;
import com.personalize.personalizeqa.properties.FileServerProperties;
import com.personalize.personalizeqa.strategy.impl.AbstractFileStrategy;
import com.personalize.personalizeqa.utils.DateUtils;
import com.personalize.personalizeqa.utils.StrPool;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.personalize.personalizeqa.utils.StrPool.SLASH;

@Slf4j
@Configuration
@EnableConfigurationProperties(FileServerProperties.class)
@ConditionalOnProperty(name="personalize.file.type",havingValue = "TENCENT")
public class TencentCosAutoConfigure {
    @Service
    public class TencentServiceImpl extends AbstractFileStrategy{
        private COSClient buildClient(){
            properties = fileProperties.getTencent();
            COSCredentials credentials = new BasicCOSCredentials(properties.getAccessKeyId(),properties.getAccessKeySecret());
            ClientConfig clientConfig = new ClientConfig(new Region(properties.getRegion()));
            return new COSClient(credentials,clientConfig);
        }
        protected String getUriPrefix(){
            if (StringUtils.isNotEmpty(properties.getEndpoint())) {
                return properties.getEndpoint();
            } else {
                String prefix = properties.getEndpoint().contains("https://") ? "https://" : "http://";
                return prefix + properties.getBucketName() + "." + properties.getEndpoint().replaceFirst(prefix, "");
            }
        }
        @Override
        public String getFileContent(String relativePath, String fileName) {
            COSClient client = buildClient();
            String bucketName = properties.getBucketName();
            String input = relativePath + cn.hutool.core.text.StrPool.SLASH + fileName;
            try{
                //cosObject包含文件所在的存储空间名称，文件名称、文件元信息以及一个输入流
                COSObject cosObject = client.getObject(bucketName,input);
                //读取文件内容
                System.out.println("Object content:");
                BufferedReader reader = new BufferedReader(new InputStreamReader(cosObject.getObjectContent()));
                StringBuilder fileContent = new StringBuilder();
                while (true){
                    String line = reader.readLine();
                    if (line == null) break;
                    fileContent.append(line).append("\n");
                }
                // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
                reader.close();
                // cosObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
                cosObject.close();
                return fileContent.toString();
            }catch (OSSException oe) {
                System.out.println("Caught an COSException, which means your request made it to COS, "
                        + "but was rejected with an error response for some reason.");
                System.out.println("Error Message:" + oe.getErrorMessage());
                System.out.println("Error Code:" + oe.getErrorCode());
                System.out.println("Request ID:" + oe.getRequestId());
                System.out.println("Host ID:" + oe.getHostId());
            } catch (Throwable ce) {
                System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with COS, "
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
        public void uploadFile(File file, MultipartFile multipartFile) throws Exception {
            COSClient clien = buildClient();
            try {
                //获取COS空间名称
                String bucketName = properties.getBucketName();
                if (!clien.doesBucketExist(bucketName)){
                    //创建存储空间
                    clien.createBucket(bucketName);
                }
                //生成文件名
                String fileName = UUID.randomUUID().toString()+ StrPool.DOT+file.getExt();
                //日期文件夹，例如:2020/04
                LocalDateTime now = LocalDateTime.now();
                //生成日期+数据集名字的相对路径
                String relativePath = now.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_FORMAT))+SLASH+file.getFolder().toString();
                String relativeFileName = relativePath+ cn.hutool.core.text.StrPool.SLASH+fileName;
                relativeFileName = StrUtil.replace(relativeFileName, "\\\\",
                        cn.hutool.core.text.StrPool.SLASH);
                relativeFileName = StrUtil.replace(relativeFileName, "\\",
                        cn.hutool.core.text.StrPool.SLASH);
                //对象元数据
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentDisposition("attachment;fileName="+URLEncoder.encode(file.getSubmittedFileName(),"UTF-8"));
                metadata.setContentType(file.getContextType());
                metadata.setContentLength(multipartFile.getInputStream().available());
                //上传请求对象
                PutObjectRequest request = new PutObjectRequest(bucketName,relativeFileName,multipartFile.getInputStream(),metadata);
                //上传文件到腾讯COS空间
                PutObjectResult result = clien.putObject(request);
//                log.info("上传的requestId:",result.getRequestId());
                //文件上传完成后需要设置上传文件相关信息，用于保存到数据库中
                log.info("result={}", JSONObject.toJSONString(result));
                String url = getUriPrefix()+ cn.hutool.core.text.StrPool.SLASH+relativeFileName;
                url = StrUtil.replace(url, "\\\\", cn.hutool.core.text.StrPool.SLASH);
                url = StrUtil.replace(url, "\\", cn.hutool.core.text.StrPool.SLASH);
                //写入文件表
                file.setUrl(url);
                file.setFileName(fileName);
                file.setRelativePath(relativePath);
                file.setGroup(result.getETag());
                file.setPath(result.getRequestId());
            }finally {
                //关闭客户端
                clien.shutdown();
            }
        }

        @Override
        public void uploadInfoSourceFileImpl(InfoSourceFile infoSourceFile, MultipartFile multipartFile) throws Exception {
            COSClient client = buildClient();
            try {
                //获取COS空间名称
                String bucketName = properties.getBucketName();
                if (!client.doesBucketExist(bucketName)){
                    //创建存储空间
                    client.createBucket(bucketName);
                }
                //生成文件名
                //生成文件名
                String fileName  = UUID.randomUUID().toString()+ cn.hutool.core.text.StrPool.DOT+infoSourceFile.getExt();
                //日期文件夹，例如:2020/04
                LocalDateTime now = LocalDateTime.now();
                //生成日期+数据集名字的相对路径
                String relativePath = now.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_FORMAT))+SLASH+infoSourceFile.getTaskCode().toString();
                String relativeFileName = relativePath+ cn.hutool.core.text.StrPool.SLASH+fileName;
                relativeFileName = StrUtil.replace(relativeFileName, "\\\\",
                        cn.hutool.core.text.StrPool.SLASH);
                relativeFileName = StrUtil.replace(relativeFileName, "\\",
                        cn.hutool.core.text.StrPool.SLASH);
                //对象元数据
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentDisposition("attachment;fileName="+URLEncoder.encode(infoSourceFile.getSubmittedFileName(),"UTF-8"));
                metadata.setContentType(infoSourceFile.getContextType());
                metadata.setContentLength(multipartFile.getInputStream().available());
                //上传请求对象
                PutObjectRequest request  = new PutObjectRequest(bucketName,relativeFileName,multipartFile.getInputStream(),metadata);
//                request.setStorageClass(StorageClass.Standard_IA);
                //上传文件到COS空间
                PutObjectResult result = client.putObject(request);
                //文件上传完成后需要设置上传文件相关信息,用于保存到数据库中。
                System.out.println("id:"+result.getRequestId());
                String url = getUriPrefix()+ cn.hutool.core.text.StrPool.SLASH+relativeFileName;
                url = StrUtil.replace(url, "\\\\", cn.hutool.core.text.StrPool.SLASH);
                url = StrUtil.replace(url, "\\", cn.hutool.core.text.StrPool.SLASH);
                //写入文件表
                infoSourceFile.setUrl(url);
                infoSourceFile.setFileName(fileName);
                infoSourceFile.setRelativePath(relativePath);
                infoSourceFile.setGroup(result.getETag());
                infoSourceFile.setPath(result.getRequestId());
            }finally {
                //关闭腾讯云COS客户端
                client.shutdown();
            }
        }
        @Override
        public void uploadInfoSourceAttachImpl(InfoSourceAttach infoSourceAttach, MultipartFile multipartFile) throws Exception{
            COSClient client = buildClient();
            try {

                //获取COS空间
                String bucketName = properties.getBucketName();
                if (!client.doesBucketExist(bucketName)){
                    //创建存储空间
                    client.createBucket(bucketName);
                }
                //生成文件名
                String fileName  = UUID.randomUUID().toString()+ cn.hutool.core.text.StrPool.DOT+infoSourceAttach.getExt();
                //日期文件夹，例如:2020/04
                LocalDateTime now = LocalDateTime.now();
                //生成日期+数据集名字的相对路径
                String relativePath = now.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_YEAR_FORMAT))+SLASH+infoSourceAttach.getTaskCode()+SLASH+"attach".toString();
                String relativeFileName = relativePath+ cn.hutool.core.text.StrPool.SLASH+fileName;
                relativeFileName = StrUtil.replace(relativeFileName, "\\\\",
                        cn.hutool.core.text.StrPool.SLASH);
                relativeFileName = StrUtil.replace(relativeFileName, "\\",
                        cn.hutool.core.text.StrPool.SLASH);
                //对象元数据
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentDisposition("attachment;fileName="+URLEncoder.encode(infoSourceAttach.getSubmittedFileName(),"UTF-8"));
                metadata.setContentType(infoSourceAttach.getContextType());
                metadata.setContentLength(multipartFile.getInputStream().available());
                //上传请求对象
                PutObjectRequest request = new PutObjectRequest(bucketName,relativeFileName,multipartFile.getInputStream(),metadata);
                request.setStorageClass(StorageClass.Standard_IA);
                //上传文件到COS空间
                PutObjectResult result = client.putObject(request);
                //文件上传完成后需要设置上传文件信息，用于保存到数据库中.
//                log.info("result={}", JSONObject.toJSONString(result));
                String url = getUriPrefix()+ cn.hutool.core.text.StrPool.SLASH+relativeFileName;
                url = StrUtil.replace(url, "\\\\", cn.hutool.core.text.StrPool.SLASH);
                url = StrUtil.replace(url, "\\", cn.hutool.core.text.StrPool.SLASH);
                //写入文件表
                infoSourceAttach.setUrl(url);
                infoSourceAttach.setFileName(fileName);
                infoSourceAttach.setRelativePath(relativePath);
                infoSourceAttach.setGroup(result.getETag());
                infoSourceAttach.setPath(result.getRequestId());
            }finally {
                //关闭COS客户端
                client.shutdown();
            }
        }

        @Override
        public void delete(FileDeleteDO fileDTO) {
            COSClient client = buildClient();
            //获取OSS空间名称
            String bucketName = properties.getBucketName();
            //删除文件
            client.deleteObject(bucketName,fileDTO.getRelativePath()+ SLASH+fileDTO.getFileName());
            //关闭阿里云COS客户端
            client.shutdown();
        }

        @Override
        public void deleteFolder(String rel_folder) {
            COSClient client = buildClient();
            String bucketName = properties.getBucketName();
            String fileFolderPath = Paths.get(properties.getEndpoint(),properties.getBucketName(),rel_folder).toString();
            client.deleteObject(bucketName,fileFolderPath);
            client.shutdown();
        }
    }
}
