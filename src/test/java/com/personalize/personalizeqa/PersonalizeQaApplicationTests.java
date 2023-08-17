package com.personalize.personalizeqa;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.personalize.personalizeqa.entity.DataSet;
import com.personalize.personalizeqa.entity.User;
import com.personalize.personalizeqa.mapper.DataSetMapper;
import com.personalize.personalizeqa.mapper.UserMapper;
import com.personalize.personalizeqa.utils.DateUtils;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
	@Test
	public void testSelect() {
		System.out.println(("----- selectAll method test ------"));
		DataSet dataSet = dataSetMapper.selectOne(new QueryWrapper<DataSet>().eq("id","1141431233289388065"));
		System.out.println(dataSet);

	}
	@Test
	public void testTime(){
		LocalDateTime now = LocalDateTime.now();
		String relativePath = Paths.get(LocalDate.now().format(DateTimeFormatter.ofPattern(DEFAULT_MONTH_FORMAT_SLASH))).toString();
		System.out.println(relativePath);
	}
	@Autowired
	private  IdGenerate<Long> idGenerate;
	@Test
	public void testIdGenerate(){
		String id = idGenerate.generate().toString();
		System.out.println(id);
	}

}
