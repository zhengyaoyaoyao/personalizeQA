package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.domain.FileDO;
import com.personalize.personalizeqa.dto.FileDeleteDO;
import com.personalize.personalizeqa.dto.FileListDTO;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.enumeration.DataType;
import com.personalize.personalizeqa.exception.BizException;
import com.personalize.personalizeqa.mapper.FileMapper;
import com.personalize.personalizeqa.server.IDataSetService;
import com.personalize.personalizeqa.server.IFileService;
import com.personalize.personalizeqa.strategy.FileStrategy;
import com.personalize.personalizeqa.utils.FileBiz;
import com.personalize.personalizeqa.utils.UserHolder;
import com.personalize.personalizeqa.vo.TaskNewFilesListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {
    /**
     * 上传文件
     * @param file
     * @return
     */
    @Autowired
    private FileStrategy fileStrategy;
    @Autowired
    private FileBiz fileBiz;
    @Autowired
    private IDataSetService dataSetService;
    @Autowired
    private FileMapper fileMapper;
    @Override
    public boolean upload(File file) {
        //上传时保存创建人信息和更新人信息
        LocalDateTime now = LocalDateTime.now();
        //时间信息
        file.setCreateTime(now);
        file.setUpdateTime(now);
        //人员信息
        UserDTO user = UserHolder.getUser();
        file.setCreateUser(user.getUsername());
        file.setUpdateUser(user.getUsername());
        return saveOrUpdate(file);
    }

    @Override
    /**
     * 删除属于dataid数据集的所有文件
     */
    public Integer deleteFilesByDataId(String dataId) {
        //先通过data_id把这些都查出来,如果没有，那么就是已经是空文件夹了，然后还要根据
        List<File> fileDeleteList = list(new QueryWrapper<File>().eq("data_id", dataId));
        if ((fileDeleteList==null)||(fileDeleteList.size()==0)){
            //1表示文件夹本来就是空的
            return 1;
        }
        //删除文件需要得到FileDeleteDO对象，所以要进行对象的转换
        List<FileDeleteDO> fileDeleteDOList = fileDeleteList.stream().map((fi) ->
                FileDeleteDO.builder().relativePath(fi.getRelativePath()).fileName(fi.getFileName()).id(fi.getId()).folder(fi.getFolder()).build()).collect(Collectors.toList());
        //获得文件夹名字,遍历文件
        String rel_folder = "";
        if (!fileDeleteDOList.isEmpty()){
            rel_folder = fileDeleteDOList.get(0).getRelativePath();
            for (FileDeleteDO fileDeleteDO:fileDeleteDOList){
                if (!StringUtils.equals(rel_folder,fileDeleteDO.getRelativePath())){
                    //相当于一个文件在多个地方。
                    log.error("要删除的文件处于多个文件夹，删除失败");
                    //0表示删除失败
                    return 0;
                }
            }
        }
        //先删除真正的文件,
        boolean isDeleteFiles = fileStrategy.delete(fileDeleteDOList);
        if (!isDeleteFiles){
            return 0;
        }
        //批量删除这些文件
        boolean isDelete = remove(new QueryWrapper<File>().eq("data_id", dataId));
        //删除文件所属的文件夹
        fileStrategy.deleteFolder(rel_folder);
        //2表示删除文件+删除了文件夹
        return isDelete?2:0;
    }
    @Override
    public boolean deleteFilesById(String id) {
        //构造文件
        File deleteFile = getById(id);
        String dataSetId = deleteFile.getDataId();
        FileDeleteDO fileDeleteDO = FileDeleteDO.builder()
                .relativePath(deleteFile.getRelativePath())
                .fileName(deleteFile.getFileName())
                .id(deleteFile.getId())
                .folder(deleteFile.getFolder()).build();
        List<FileDeleteDO> fileDeleteDOList = new ArrayList<>();
        fileDeleteDOList.add(fileDeleteDO);
        //先删除真正的文件
        boolean isDeleteFiles = fileStrategy.delete(fileDeleteDOList);
        if (!isDeleteFiles){
            log.error("通过id删除文件：删除文件失败");
            return isDeleteFiles;
        }
        //删除这个文件的数据库信息
        boolean isDelete = removeById(id);
        if (!isDelete){
            log.error("通过id删除文件：删除文件信息失败");
            return isDelete;
        }
        //更新数据集的文件数量
        dataSetService.updateFilesSize(dataSetId,(long)-1);
        //删除数据库信息
        return isDelete&&isDeleteFiles;
    }

    @Override
    public R<FileListDTO> findAll() {
        List<File> list = list();
        FileListDTO fileListDTO = new FileListDTO();
        fileListDTO.setDatas(list);
        return R.success(fileListDTO);
    }

    /**
     * 根据id查询到文件
     * @param id
     * @return
     */
    @Override
    public R<File> findById(String id) {
        File file = getById(id);
        return R.success(file);
    }

    @Override
    public R<File> updateInfo(String id, String fileName) {
        LocalDateTime now = LocalDateTime.now();
        File byId = getById(id);
        byId.setSubmittedFileName(fileName);
        byId.setUpdateTime(now);
        boolean isUpdate = updateById(byId);
        if (isUpdate){
            return R.success(byId);
        }else {
            return R.fail("更新失败");
        }
    }

    @Override
    public void download(HttpServletRequest request, HttpServletResponse response, String id)throws Exception {
        File byId = getById(id);
        down(byId,request,response);
    }

    @Override
    public void downloadDataSet(HttpServletRequest request, HttpServletResponse response, String dataName, String id) throws Exception {
        List<File> lists = list(new QueryWrapper<File>().eq("data_id", id));
        downDataset(lists,dataName,request,response);
    }

    public void down(File file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if (file == null){
            throw BizException.wrap("您下载的文件不存在");
        }
        if (file.getDataType()== DataType.DIR){
            throw BizException.wrap("您下载的不是可下载文件");
        }
        //不需要文件的所有属性，所以使用DO对象瞎子啊
        FileDO fileDOList = FileDO.builder()
                .url(file.getUrl())
                .submittedFileName(file.getSubmittedFileName())
                .size(file.getSize())
                .dataType(file.getDataType())
                .build();
        fileBiz.down(fileDOList,request,response);
    }
    public void downDataset(List<File> lists,String dataName,HttpServletRequest request, HttpServletResponse response) throws Exception{
        if (lists.isEmpty()){
            throw BizException.wrap("您下载的文件不存在！");
        }
        //对象转换
        List<FileDO> fileDOList = lists.stream().map((file)-> FileDO.builder().url(file.getUrl())
                .submittedFileName(file.getSubmittedFileName())
                .size(file.getSize())
                .dataType(file.getDataType())
                .build()).collect(Collectors.toList());
        fileBiz.downDataSet(fileDOList,dataName,request,response);
    }

    @Override
    public TaskNewFilesListVO getFilesNameListByDataName(String dataName) {
        List<Map<String,String>> lists = fileMapper.selectFileNameByFolder(dataName);
        TaskNewFilesListVO taskNewFilesListVO = new TaskNewFilesListVO();
        taskNewFilesListVO.setFileNames(lists);
        return taskNewFilesListVO;
    }
}
