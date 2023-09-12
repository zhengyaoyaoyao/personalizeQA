package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.InfoSource;
import com.personalize.personalizeqa.entity.InfoSourceFile;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.Task;
import com.personalize.personalizeqa.mapper.TaskMapper;
import com.personalize.personalizeqa.server.IInfoSourceFileService;
import com.personalize.personalizeqa.server.ITaskService;
import com.personalize.personalizeqa.strategy.FileStrategy;
import com.personalize.personalizeqa.utils.UserHolder;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import com.personalize.personalizeqa.vo.HomeTaskInfoVO;
import com.personalize.personalizeqa.vo.TaskShowListVO;
import com.personalize.personalizeqa.vo.TasksListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {
    @Autowired
    private IdGenerate<Long> idGenerate;
    @Autowired
    private FileStrategy fileStrategy;
    @Autowired
    private IInfoSourceFileService infoSourceFileService;
    @Autowired
    private TaskMapper taskMapper;
    @Override
    public Boolean insert(String taskName,String taskCollectionName, String charge, List<String> proMembers, String taskTime, String infoSource, String infoSourceRule,Boolean status) {
        String id = idGenerate.generate().toString();
        LocalDateTime now = LocalDateTime.now();
        UserDTO curUser = UserHolder.getUser();
        String curUserName = curUser.getUsername();
        Task task = Task.builder()
                .id(id).taskName(taskName).taskCollectionName(taskCollectionName).charge(charge).taskTime(taskTime).taskSourceName(infoSource).taskRule(infoSourceRule)
                .createTime(now).createUser(curUserName).updateTime(now).updateUser(curUserName).status(status)
                .build();
        //保存任务-成员
        for (String username:proMembers){
            String taskUserId = idGenerate.generate().toString();
            taskMapper.saveTaskUser(taskUserId,id,username);
        }
        //保存任务信息
        boolean save = save(task);
        return save;
    }
    //


    @Override
    public Boolean updateById(String id, String taskName,String taskCollectionName, String charge, List<String> proMembers, String taskTime, String infoSource, Boolean status) {
        Task task = getById(id);
        //更新人员信息，那需要先把原来的人删了，然后在加上。把这个任务的人都删了，然后重新insert
        Boolean isDeleteMembers = taskMapper.deleteMembersByTaskId(id);
        if (!isDeleteMembers){
            return false;
        }
        //重新添加这些人
        for (String user : proMembers){
            String taskUserId = idGenerate.generate().toString();
            taskMapper.saveTaskUser(taskUserId,id,user);
        }
        task.setTaskName(taskName);
        task.setTaskCollectionName(taskCollectionName);
        task.setCharge(charge);
        task.setTaskTime(taskTime);
        task.setTaskSourceName(infoSource);
        task.setUpdateTime(LocalDateTime.now());
        task.setUpdateUser(UserHolder.getUser().getUsername());
        task.setStatus(status);
        boolean b = updateById(task);
        return b;
    }

    //
    public LocalDateTime transDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(date, formatter);
        return parse;
    }

    @Override
    public R<Page<TaskShowListVO>> findAll(Integer curPage, Integer maxPage, String keyword) {
        //先查询所有的任务
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("task_name",keyword);
        Page<Task> taskPage = new Page<>(curPage,maxPage);
        Page<Task> resultTask = baseMapper.selectPage(taskPage,queryWrapper);
        List<TaskShowListVO> taskShowListVOS = new ArrayList<>();
        List<Task> records = resultTask.getRecords();
        for (Task task:records){
            //通过任务id查询信源名称，
            String infoSourceName = taskMapper.getInfoSourceNameById(task.getTaskSourceName());
            //通过任务id查询组员
            List<String> members = taskMapper.getMembersByTaskId(task.getId());
            TaskShowListVO taskShowListVO = new TaskShowListVO();
            taskShowListVO.setId(task.getId());
            taskShowListVO.setCreateTime(task.getCreateTime());
            taskShowListVO.setUpdateTime(task.getUpdateTime());
            taskShowListVO.setCreateUser(task.getCreateUser());
            taskShowListVO.setUpdateUser(task.getUpdateUser());
            taskShowListVO.setTaskName(task.getTaskName());
            taskShowListVO.setTaskCollectionName(task.getTaskCollectionName());
            taskShowListVO.setCharge(task.getCharge());
            taskShowListVO.setMembers(members);
            taskShowListVO.setTaskTime(task.getTaskTime());
            taskShowListVO.setInfoSourceName(infoSourceName);
            taskShowListVO.setInfoSourceRule(task.getTaskRule());
            taskShowListVO.setStatus(task.isStatus());
            //最终放到TaskShowListVO中
            taskShowListVOS.add(taskShowListVO);
        }
        Page<TaskShowListVO> tasksListVOPage=new Page<>(curPage,maxPage);
        tasksListVOPage.setRecords(taskShowListVOS);
        tasksListVOPage.setTotal(resultTask.getTotal());
        tasksListVOPage.setCurrent(resultTask.getCurrent());
        tasksListVOPage.setSize(resultTask.getSize());
        return R.success(tasksListVOPage);
    }

    /**
     *
     * @param id 任务id
     * @return
     */
    @Override
    public Boolean deleteById(String id) {
        //需要删除任务-用户表
        Boolean deleteUser = taskMapper.deleteMembersByTaskId(id);
        //需要删除这个任务涉及的所有文件,根据文件id，把这个都查出来，然后删.
        List<String> infoSourceFilesByTaskId = taskMapper.getInfoSourceFilesByTaskId(id);
        for (String fileId :infoSourceFilesByTaskId){
            Boolean aBoolean = infoSourceFileService.deleteFile(fileId);
            if (!aBoolean){
                log.error("删除采集任务下的所有文件时失败");
                return aBoolean;
            }
        }
        //删除信源采集任务
        boolean deleteTask = removeById(id);
        return deleteUser&&deleteTask;
    }

    @Override
    public R<Boolean> uploadFilesById(String id, String taskCode, MultipartFile[] files) {
        if (id==null){
            log.error("添加文件，不存在此id");
        }
        //上传文件到文件夹
        for (MultipartFile multipartFile:files){
            //真正的上传文件
            InfoSourceFile infoSourceFile = fileStrategy.uploadInfoSourceFile(multipartFile, taskCode);
            String fileId = idGenerate.generate().toString();
            infoSourceFile.setId(fileId);
            infoSourceFile.setTaskId(id);
            infoSourceFile.setStatus(false);
            boolean upload = infoSourceFileService.upload(infoSourceFile);
            if (!upload){
                return R.fail("根据任务上传文件");
            }
        }
        return R.success(true,"上传文件成功");
    }
    @Override
    public boolean isNotExist(String taskName) {
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getTaskName,taskName);
        int count = count(queryWrapper);
        return count==0;
    }

    @Override
    public HomeTaskInfoVO taskCompleteInfo() {
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::isStatus,0);
        int countUnSuccess = count(queryWrapper);
        LambdaQueryWrapper<Task> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Task::isStatus,1);
        int countSuccess = count(queryWrapper1);
        HomeTaskInfoVO homeTaskInfoVO = new HomeTaskInfoVO();
        homeTaskInfoVO.setSuccess(countSuccess);
        homeTaskInfoVO.setUnsuccess(countUnSuccess);
        return homeTaskInfoVO;
    }
}
