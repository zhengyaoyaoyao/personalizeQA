package com.personalize.personalizeqa.utils;


import cn.hutool.core.util.StrUtil;
import com.personalize.personalizeqa.domain.FileDO;
import com.personalize.personalizeqa.enumeration.DataType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FileBiz {
    //防止原文件名重名情况，因此需要有标识
    private static String buildNameFileName(String fileName, Integer order) {
        return StrUtil.strBuilder(fileName).insert(fileName.lastIndexOf("."), "(" + order + ")").toString();
    }

    /**
     * 文件下载的具体方法
     */

    public void down(FileDO fileDO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //计算要下载的文件总大小
        int fileSize = NumberHelper.intValueOf0(fileDO.getSize());
        //确定要下载的文件名称
        String downLoadFileName = fileDO.getSubmittedFileName();
        Map<String, String> fileMap = new HashMap<>();
        fileMap.put(downLoadFileName, fileDO.getUrl());
        ZipUtils.zipFilesByInputStream(fileMap, Long.valueOf(fileSize), downLoadFileName, request, response);
    }

    public void downDataSet(List<FileDO> fileDOList, String dataName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int fileSize = fileDOList.stream().filter(
                (file) -> file != null && !file.getDataType().eq(DataType.DIR) && StringUtils.isNotEmpty(file.getUrl())).mapToInt(
                (file) -> NumberHelper.intValueOf0(file.getSize())).sum();
        //确定要下载的文件名称
        String downLoadFileName = dataName;
        if (fileDOList.size() > 1) {
            downLoadFileName = StringUtils.substring(downLoadFileName, 0, StringUtils.lastIndexOf(downLoadFileName, ".")) + ".zip";
        }
        //fileDOList ===>Map<String,String>
        Map<String, String> fileMap = new LinkedHashMap<>(fileDOList.size());
        //处理下载文件名称相同情况
        Map<String, Integer> duplicateFile = new HashMap<>(fileDOList.size());
        fileDOList.stream().filter((file) -> file != null && !file.getDataType().eq(DataType.DIR) && StringUtils.isNotEmpty(file.getUrl())).forEach(
                (file) -> {
                    //原始文件名称
                    String submittedFileName = file.getSubmittedFileName();
                    if (fileMap.containsKey(submittedFileName)) {
                        //存在重复，看下在不在重复里面，在则基础上加1，否则就是1
                        //在duplicateFile中有了
                        if (duplicateFile.containsKey(submittedFileName)) {
                            duplicateFile.put(submittedFileName, duplicateFile.get(submittedFileName) + 1);//原基础上+1
                        } else {
                            //不存在那就为1，原本为0
                            duplicateFile.put(submittedFileName, 1);
                        }
                        submittedFileName = buildNameFileName(submittedFileName, duplicateFile.get(submittedFileName));
                    }
                    fileMap.put(submittedFileName, file.getUrl());
                });
        ZipUtils.zipFilesByInputStream(fileMap,Long.valueOf(fileSize),downLoadFileName,request,response);

    }
}