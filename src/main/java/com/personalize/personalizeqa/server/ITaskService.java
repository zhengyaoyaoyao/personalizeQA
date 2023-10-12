package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.Task;
import com.personalize.personalizeqa.vo.HomeTaskInfoVO;
import com.personalize.personalizeqa.vo.TaskShowListVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ITaskService extends IService<Task> {

    Boolean insert(String taskName,String taskCollectionName, String charge, List<String> proMembers, String taskTime, String infoSource, String infoSourceRule,String taskNote, Boolean status);

    R<Page<TaskShowListVO>> findAll(Integer page, Integer perPage, String keyword);

    Boolean updateById(String id, String taskName,String taskCollectionName, String charge, List<String> proMembers, String taskTime, String taskNote, Boolean status);

    Boolean deleteById(String id);

    R<Boolean> uploadFilesById(String id, String taskCode, MultipartFile[] files);

    boolean isNotExist(String taskName);

    HomeTaskInfoVO taskCompleteInfo();

    ResponseEntity<byte[]> uploadAttachsById(String id, String taskCode, MultipartFile[] files);
}
