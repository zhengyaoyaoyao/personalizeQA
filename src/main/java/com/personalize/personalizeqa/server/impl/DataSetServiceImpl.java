package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.personalize.personalizeqa.vo.DataListVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataSetServiceImpl extends ServiceImpl<DataSetMapper, DataSet> implements IDataSetService {
    @Autowired
    private IdGenerate<Long> idGenerate;
    @Autowired
    private FileStrategy fileStrategy;
    @Autowired
    private IFileService fileService;
    @Autowired
    private DataSetMapper dataSetMapper;

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
        String folder = Paths.get(String.valueOf(now.getYear()),dataName).toString();
        DataSet dataSet = DataSet.builder().id(DataSetId).dataName(dataName).dataUrl(dataSetUrl).dataInfo(dataSetInfo).filesSize((long)files.length).folder(folder).relFolder(dataName).createTime(now).updateTime(now).build();
        dataSet.setCreateMonth(DateUtils.formatAsYearMonthEn(now));
        dataSet.setCreateWeek(DateUtils.formatAsYearWeekEn(now));
        dataSet.setCreateDay(DateUtils.formatAsDateEn(now));
        //后期更新人员和创建人员需要前端传参
        dataSet.setCreateUser("0");
        dataSet.setUpdateUser("0");
        //插入数据集的信息
        boolean isInsert = saveOrUpdate(dataSet);
        /*for (MultipartFile file:files){
            fileStrategy.upload(file,dataName);
        }*/
        if (!isInsert){
            log.error("数据集插入失败");
            return R.fail("数据集插入失败");
        }
        //保存好数据集的信息后，要对文件进行上传
        if (files == null || files.length==0){
            return R.fail("无文件上传");
        }//
        for (MultipartFile multipartFile:files){
            //单个文件
            File file = fileStrategy.upload(multipartFile, dataName);
            String fileId = idGenerate.generate().toString();
            file.setId(fileId);
            file.setDataId(DataSetId);
            boolean isFileUpload = fileService.upload(file);
            if (!isFileUpload){
                return R.fail("文件上传失败");
            }
        }
        return R.success(dataSet);
    }

    /**
     * 查询数据集所有信息，且有模糊搜索
     * @return
     */
    @Override
    public R<Page<DataSet>> findAll(Integer curPage,Integer maxPage,String keyword) {
        QueryWrapper<DataSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("data_name",keyword);
        Page<DataSet> dataSetPage = new Page<>(curPage,maxPage);
        Page<DataSet> result = baseMapper.selectPage(dataSetPage, queryWrapper);
        return R.success(result);
    }

    /**
     * 通过id查询单条数据集
     */
    @Override
    public R<DataSet> findById(String id) {
        DataSet dataSetById = getOne(new QueryWrapper<DataSet>().eq("id", id));
        return R.success(dataSetById);
    }

    /**
     * 删除这个数据集，相应的要把这个数据集包含的文件给删除，因此涉及到文件的批量删除操作
     * @param id
     * @return
     */
    @Override
    public R<Boolean> deleteById(String id) {
        //删除相关文件
        Integer isDelete = fileService.deleteFilesByDataId(id);
        //isDelete:0表示删除失败，1表示这个数据集是空的，2表示这个数据集是有文件并且正常删除了
        if (isDelete==0){
            return R.fail("删除数据集时，文件删除失败");
        }else if (isDelete==1){
            DataSet byId = getById(id);
            String folder = byId.getFolder();//有了文件夹名称，还需要拼接上年份才是路径
            fileStrategy.deleteFolder(folder);
            //这时候缺少了删除文件夹，系统文件夹未删除，需要拼接实现文件夹删除
        }
        //删除数据集文件
        boolean isDeleteDataSet = removeById(id);
        return R.success(isDeleteDataSet);
    }
    /**
     * 更新数据集信息
     */
    @Override
    public R<DataSet> updateById(String id, String dataName, String dataUrl, String dataInfo) {
        LocalDateTime now = LocalDateTime.now();
        DataSet byId = getById(id);
        byId.setDataName(dataName);
        byId.setDataUrl(dataUrl);
        byId.setDataInfo(dataInfo);
        byId.setUpdateTime(now);
        boolean isUpdate = updateById(byId);
        if (isUpdate){
            return R.success(byId);
        }else {
            return R.fail("更新失败");
        }
    }

    /**
     * 对某个数据集添加文件，
     * @param id
     * @param files
     * @return
     */
    @Override
    public R<Boolean> uploadFilesById(String id, MultipartFile[] files) {
        if (id==null){
            log.error("添加文件：不存在此id");
        }
        //查询到数据集
        DataSet byId = getById(id);
        //更新文件数量
        Long preSize = (long)byId.getFilesSize();
        Long size = (long)files.length+preSize;
        byId.setFilesSize(size);
        //更新数据集
        boolean b = updateById(byId);
        if (!b){
            R.fail("为数据集添加filesSize失败");
        }
        //上传文件到文件夹
        for (MultipartFile multipartFile:files){
            //真正的上传文件
            File file = fileStrategy.upload(multipartFile, byId.getRelFolder());
            String fileId = idGenerate.generate().toString();
            file.setId(fileId);
            file.setDataId(id);
            //添加数据库信息
            boolean isFileUpload = fileService.upload(file);
            if (!isFileUpload){
                return R.fail("根据数据集上传文件失败");
            }
        }
        return R.success(b);
    }

    @Override
    public boolean updateFilesSize(String id,Long size) {
        DataSet byId = getById(id);
        Long preSize = byId.getFilesSize();
        byId.setFilesSize(preSize+size);
        return updateById(byId);
    }

    /**
     * 判断是否存在这个
     * @param dataName
     * @return
     */
    @Override
    public boolean isNotExist(String dataName) {
        //判断是否存在，
        LambdaQueryWrapper<DataSet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSet::getDataName, dataName);

        // 使用 count 方法来获取匹配记录的数量
        int count = count(queryWrapper);

        // 如果记录数量大于 0，则说明存在
        return (count == 0);
    }

    @Override
    public void download(HttpServletRequest request, HttpServletResponse response,String dataName, String id)  throws Exception{
        fileService.downloadDataSet(request,response,dataName,id);
    }

    @Override
    public DataListVO datasetList() {
        List<Map<String,String>> datasetNames = dataSetMapper.selectDatasetNames();
        DataListVO  dataListVO = new DataListVO();
        dataListVO.setDataSetList(datasetNames);
        return dataListVO;
    }
}
