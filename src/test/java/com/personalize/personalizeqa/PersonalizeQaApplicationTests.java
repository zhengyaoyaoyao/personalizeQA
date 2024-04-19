package com.personalize.personalizeqa;

import cn.hutool.core.text.StrPool;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.VoidResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.server.IInfoSourceFileService;
import com.personalize.personalizeqa.server.ITaskService;
import com.personalize.personalizeqa.server.IUserService;
import com.personalize.personalizeqa.storage.LinuxAutoConfig;
import com.personalize.personalizeqa.vo.HomeTaskInfoVO;
import com.personalize.personalizeqa.vo.UserInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

@SpringBootTest
class PersonalizeQaApplicationTests {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Test
	public void createCollection() throws IOException {
		boolean b = mongoTemplate.collectionExists("user");
		System.out.println(b);
	}

//	@Autowired
//	private IInfoSourceFileService infoSourceFileService;
	@Test
	public void test(){
	}
	@Autowired
	private ITaskService taskService;
	@Test
	public void test1(){
		HomeTaskInfoVO homeTaskInfoVO = taskService.taskCompleteInfo();
		System.out.println(homeTaskInfoVO);
	}
	@Resource
	RedisTemplate redisTemplate;
	@Resource
	StringRedisTemplate stringRedisTemplate;
	@Test
	void testRedis(){
		redisTemplate.opsForValue().set("testKey","testValue");
		String value = (String) redisTemplate.opsForValue().get("testKey");
		System.out.println(value);
	}
	@Test
	void contextLoads() {
//		long count = stringRedisTemplate.opsForValue().increment("zzu");
//		System.out.println(count);
//		System.out.println(StringUtils.upperCase("zZu"));//升大写
//		System.out.println(StringUtils.lowerCase("ZZU"));
		long count = 10000L%10000;
		DecimalFormat df = new DecimalFormat("0000");
		String formatted = df.format(count);
		System.out.println(formatted);
	}
	@Autowired
	private IUserService userService;
	@Test
	void testMysql(){
		R<Page<UserInfoVO>> all = userService.findAll(1, 3, "");
		System.out.println(all);
	}
	private OSS buildClient(){
		return new OSSClientBuilder().build("http://oss-cn-beijing.aliyuncs.com/",
				"LTAI5tF1TYko4pncbKXEyAgu",
				"JZ7JxbVRqKqJf9n5aoAy1Fj0BNGDle");

	}
	@Test
	public void uploadFile() {
		OSS client = buildClient();
		//获取OSS空间名称
		String bucketName = "personlized-loan";
		if (!client.doesBucketExist(bucketName)){
			System.out.println("通过验证，但是bucket不存在");
		}else {
			System.out.println("已连接，bucket存在");
		}
	}
	@Test
	public void deletefile(){
		OSS client = buildClient();
		String bucketName = "personlized-loan";
		String relativePath = "2023/shige";
		String fileName = "586e829c-c518-43f9-97e2-0dd4009dc148.json";
		String input = relativePath + StrPool.SLASH + fileName;

		//删除文件
		VoidResult voidResult = client.deleteObject(bucketName,input);
		//关闭阿里云OSS客户端
		client.shutdown();
	}
	@Test
	public void main(){
		for (int i =0;i<5;i++){
			System.out.println(i);
		}
	}
	@Test
	public void downFile(){
		OSS client = buildClient();
		String bucketName = "personlized-loan";
		String relativePath = "2023/shige";
		String fileName = "005025f5-3b72-4a09-a88f-c7198b69045d.json";
		String input = relativePath + StrPool.SLASH + fileName;
		try {
			// ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
			OSSObject ossObject = client.getObject(bucketName, input);

			// 读取文件内容。
			System.out.println("Object content:");
			BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
			StringBuilder fileContent = new StringBuilder();
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				fileContent.append(line).append("\n");
			}
			// 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
			reader.close();
			// ossObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
			ossObject.close();
			System.out.println(fileContent);
		} catch (OSSException oe) {
			System.out.println("Caught an OSSException, which means your request made it to OSS, "
					+ "but was rejected with an error response for some reason.");
			System.out.println("Error Message:" + oe.getErrorMessage());
			System.out.println("Error Code:" + oe.getErrorCode());
			System.out.println("Request ID:" + oe.getRequestId());
			System.out.println("Host ID:" + oe.getHostId());
		} catch (Throwable ce) {
			System.out.println("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message:" + ce.getMessage());
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

}
