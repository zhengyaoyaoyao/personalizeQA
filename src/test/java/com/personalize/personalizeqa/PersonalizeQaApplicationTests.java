package com.personalize.personalizeqa;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.personalizeqa.dto.DataSetListDTO;
import com.personalize.personalizeqa.entity.*;
import com.personalize.personalizeqa.mapper.DataSetMapper;
import com.personalize.personalizeqa.mapper.FileMapper;
import com.personalize.personalizeqa.mapper.UserMapper;
import com.personalize.personalizeqa.properties.FileServerProperties;
import com.personalize.personalizeqa.server.IDataSetService;
import com.personalize.personalizeqa.server.IFileService;
import com.personalize.personalizeqa.server.ITaskTypeService;
import com.personalize.personalizeqa.utils.DateUtils;
import com.personalize.personalizeqa.utils.NumberHelper;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import com.personalize.personalizeqa.vo.DataListVO;
import com.personalize.personalizeqa.vo.TaskNewFilesListVO;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.personalize.personalizeqa.utils.DateUtils.DEFAULT_MONTH_FORMAT_SLASH;

@SpringBootTest
class PersonalizeQaApplicationTests {
	@Autowired
	private DataSetMapper dataSetMapper;
	@Autowired
	private IDataSetService dataSetService;
	@Autowired
	private IFileService fileService;
	@Autowired
	private ITaskTypeService taskTypeService;
	@Test
	public void test(){
		String dataName = "测试";
		R<Page<TaskType>> taskTypeServiceAll = taskTypeService.findAll(1,3,dataName);
		System.out.println(taskTypeServiceAll);
	}
}
