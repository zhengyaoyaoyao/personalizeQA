package com.personalize.personalizeqa.server.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalize.personalizeqa.dto.FileDeleteDO;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.InfoSourceFile;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.mapper.InfoSourceFileMapper;
import com.personalize.personalizeqa.server.IInfoSourceFileService;
import com.personalize.personalizeqa.server.IInfoSourceService;
import com.personalize.personalizeqa.strategy.FileStrategy;
import com.personalize.personalizeqa.utils.UserHolder;
import com.personalize.personalizeqa.vo.InfoSourceFileVO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class InfoSourceFileImpl  extends ServiceImpl<InfoSourceFileMapper, InfoSourceFile> implements IInfoSourceFileService {
    @Autowired
    private FileStrategy fileStrategy;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public boolean upload(InfoSourceFile infoSourceFile) {
        LocalDateTime now  = LocalDateTime.now();
        infoSourceFile.setCreateTime(now);
        infoSourceFile.setUpdateTime(now);
        UserDTO user = UserHolder.getUser();
        infoSourceFile.setCreateUser(user.getUsername());
        infoSourceFile.setUpdateUser(user.getUsername());
        return saveOrUpdate(infoSourceFile);
    }


    @Override
    public List<InfoSourceFileVO> findAll(String id) {
        List<InfoSourceFile> taskId = list(new QueryWrapper<InfoSourceFile>().eq("task_id", id));
        List<InfoSourceFileVO> result = new LinkedList<>();
        for (InfoSourceFile infoSourceFile:taskId){
            InfoSourceFileVO infoSourceFileVO = new InfoSourceFileVO();
            infoSourceFileVO.setId(infoSourceFile.getId());
            infoSourceFileVO.setTaskCode(infoSourceFile.getTaskCode());
            infoSourceFileVO.setFileName(infoSourceFile.getSubmittedFileName());
            infoSourceFileVO.setUrl(infoSourceFile.getUrl());
            infoSourceFileVO.setStatus(infoSourceFile.getStatus());
            infoSourceFileVO.setCreateTime(infoSourceFile.getCreateTime());
            infoSourceFileVO.setCreateUser(infoSourceFile.getCreateUser());
            result.add(infoSourceFileVO);
        }
        return result;
    }

    @Override
    public String getFileContent(String id) {
        InfoSourceFile byId = getById(id);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(byId.getUrl());

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        // 从响应实体中获取文件内容
                        String content = EntityUtils.toString(entity);
                        return content;
                    }
                } else {
                    // 处理请求失败的情况
                    return "文件获取失败";
                    // 您可以抛出异常或者返回适当的错误消息
                }
            }
        } catch (Exception e) {
            // 处理异常
            log.error("文件获取失败");
        }
        return "文件获取失败";
    }

    /**
     * 根据
     * @param id
     * @return
     */
    @Override
    public Boolean deleteFile(String id) {
        InfoSourceFile deleteFile = getById(id);
        FileDeleteDO fileDeleteDO = FileDeleteDO.builder()
                .relativePath(deleteFile.getRelativePath())
                .fileName(deleteFile.getFileName())
                .id(deleteFile.getId())
                .folder(deleteFile.getTaskCode()).build();
        List<FileDeleteDO> fileDeleteDOList = new ArrayList<>();
        fileDeleteDOList.add(fileDeleteDO);
        //先删除真正文件
        boolean isDeleteFiles = fileStrategy.delete(fileDeleteDOList);
        if (!isDeleteFiles){
            log.error("通过id删除文件，删除文件失败");
            return isDeleteFiles;
        }
        //删除这个文件的数据库信息
        boolean isDelete = removeById(id);
        if (!isDelete){
            log.error("通过id删除文件，删除文件信息失败");
            return isDelete;
        }
        return isDeleteFiles&&isDelete;
    }

    @Override
    public R<Boolean> checkFile(String id) {
        InfoSourceFile byId = getById(id);

        //获得文件，从而要获得这个文件的json内容
        String fileContent = getFileContent(id);
        if (StrUtil.isBlank(fileContent)){
            return R.fail("获取文件内容异常");
        }
        String collection = byId.getTaskCode().trim();
        //确保获得文件内容，
        //1. 要先创建taskCode集合，
        if (mongoTemplate.collectionExists(collection)){
            mongoTemplate.createCollection(byId.getTaskCode());
        }
        try {
            fileContent = fileContent.replaceAll("\uFEFF", "");
            // 创建ObjectMapper对象
            ObjectMapper objectMapper = new ObjectMapper();
            // 读取JSON文件并解析为List<Map<String, Object>>
            List<Map<String, Object>> jsonArray = objectMapper.readValue(fileContent, List.class);
            // 遍历List中的Map对象
            for (Map<String, Object> jsonObject : jsonArray) {
                // 处理每个Map对象
                mongoTemplate.save(jsonObject,collection);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return R.fail("校验失败，无法通过");
        }
        byId.setStatus(true);
        boolean b = updateById(byId);
        return R.success(b,"数据库载入成功");
    }
}
