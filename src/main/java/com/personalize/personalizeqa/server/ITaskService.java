package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.Task;
import com.personalize.personalizeqa.vo.HomeTaskInfoVO;
import com.personalize.personalizeqa.vo.TaskShowListVO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface ITaskService extends IService<Task> {

    Boolean insert(String taskName,String taskCollectionName, String charge, List<String> proMembers, String taskTime, String infoSource, String infoSourceRule, Boolean status);

    R<Page<TaskShowListVO>> findAll(Integer page, Integer perPage, String keyword);

    Boolean updateById(String id, String taskName,String taskCollectionName, String charge, List<String> proMembers, String taskTime, String infoSource, Boolean status);

    Boolean deleteById(String id);

    R<Boolean> uploadFilesById(String id, String taskCode, MultipartFile[] files);

    boolean isNotExist(String taskName);

    HomeTaskInfoVO taskCompleteInfo();
}
