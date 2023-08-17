package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.dto.DataSetListDTO;
import com.personalize.personalizeqa.entity.DataSet;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.mapper.DataSetMapper;
import com.personalize.personalizeqa.server.IDataSetService;
import com.personalize.personalizeqa.server.IFileService;
import com.personalize.personalizeqa.strategy.FileStrategy;
import com.personalize.personalizeqa.strategy.impl.AbstractFileStrategy;
import com.personalize.personalizeqa.utils.DateUtils;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class DataSetServiceImpl extends ServiceImpl<DataSetMapper, DataSet> implements IDataSetService {
    @Autowired
    private IdGenerate<Long> idGenerate;
    @Autowired
    private FileStrategy fileStrategy;
    @Autowired
    private IFileService fileService;

    /**
     * 数据集文件上传
     * @param dataName
     * @param dataSetUrl
     * @param dataSetInfo
     * @param files
     * @return
     */
    @Override
    public R<DataSet> upload(String dataName, String dataSetUrl, String dataSetInfo, MultipartFile[] files) {
        if (StringUtils.isBlank(dataName)){
            log.error("数据集名称为空");
            return R.fail("数据集名称为空");
        }
        LocalDateTime now = LocalDateTime.now();
        String DataSetId = idGenerate.generate().toString();
        DataSet dataSet = DataSet.builder().id(DataSetId).dataName(dataName).dataUrl(dataSetUrl).dataInfo(dataSetInfo).createTime(now).updateTime(now).build();
        dataSet.setCreateMonth(DateUtils.formatAsYearMonthEn(now));
        dataSet.setCreateWeek(DateUtils.formatAsYearWeekEn(now));
        dataSet.setCreateDay(DateUtils.formatAsDateEn(now));
        //后期更新人员和创建人员需要前端传参
        dataSet.setCreateUser("0");
        dataSet.setUpdateUser("0");
        boolean isInsert = saveOrUpdate(dataSet);
        for (MultipartFile file:files){
            fileStrategy.upload(file,dataName);
        }
        if (!isInsert){
            log.error("数据集插入失败");
            return R.fail("数据集插入失败");
        }
        //保存好数据集的信息后，要对文件进行上传
        if (files == null || files.length==0){
            return R.fail("无文件上传");
        }
        for (MultipartFile multipartFile:files){
            //单个文件
            File file = fileStrategy.upload(multipartFile, dataName);
            file.setId(idGenerate.generate().toString());
            file.setDataId(DataSetId);
            boolean isFileUpload = fileService.upload(file);
            if (!isFileUpload){
                return R.fail("文件上传失败");
            }
        }
        return R.success(dataSet);
    }

    /**
     * 查询数据集所有信息
     * @return
     */
    @Override
    public R<DataSetListDTO> findAll() {
        List<DataSet> list = list();
        int count = list.size();
        DataSetListDTO dataSetListDTO = new DataSetListDTO();
        dataSetListDTO.setDatas(list);
        dataSetListDTO.setCount(count);
        return R.success(dataSetListDTO);
    }
    /**
     * 通过id查询单条数据集
     */
    @Override
    public R<DataSet> findById(String id) {
        DataSet dataSetById = getOne(new QueryWrapper<DataSet>().eq("id", id));
        return R.success(dataSetById);
    }
}
