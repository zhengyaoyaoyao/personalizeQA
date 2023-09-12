package com.personalize.personalizeqa;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalize.personalizeqa.es.ElasticClientConfig;
import com.personalize.personalizeqa.server.IInfoSourceFileService;
import com.personalize.personalizeqa.server.ITaskService;
import com.personalize.personalizeqa.vo.HomeTaskInfoVO;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class PersonalizeQaApplicationTests {
	@Autowired
	private ElasticClientConfig elasticClientConfig;
	@Test
	public void ceshi() throws Exception{
		RestHighLevelClient client = elasticClientConfig.getClient();
		String index = "{\n" +
				"  \"title\": {\n" +
				"    \"type\": \"text\"\n" +
				"  },\n" +
				"  \"author\": {\n" +
				"    \"type\": \"text\"\n" +
				"  },\n" +
				"  \"publish_time\": {\n" +
				"    \"type\": \"date\"\n" +
				"  },\n" +
				"  \"url\": {\n" +
				"    \"type\": \"text\"\n" +
				"  },\n" +
				"  \"text\": {\n" +
				"    \"type\": \"text\"\n" +
				"  }\n" +
				"}";
		JSONObject jsonObject = new JSONObject();
		String string = jsonObject.getString("json");
		String indexTemplateJson = "{" +
				"\"mappings\": {" +
				"    \"properties\": "+"{" +
				"        \"title\": {\"type\": \"text\"}," +
				"        \"author\": {\"type\": \"text\"}," +
				"        \"publish_time\": {\"type\": \"date\"}," +
				"        \"url\": {\"type\": \"text\"}," +
				"        \"text\": {\"type\": \"text\"}" +
				"    	}" +
				"	}" +
				"}";
		client.close();
	}
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private IInfoSourceFileService infoSourceFileService;
	@Test
	public void createCollection() throws IOException {
//		mongoTemplate.createCollection("ceshi");
		String fileContent = infoSourceFileService.getFileContent("1149387625539305601");
		System.out.println(fileContent);
//		String jsonFilePath ="C:\\Users\\zheng\\IdeaProjects\\personalizeQA\\personalizeQA\\src\\main\\resources\\doc\\爱莲说.json";
		try {
			// 创建ObjectMapper对象
			ObjectMapper objectMapper = new ObjectMapper();

			// 读取JSON文件并解析为List<Map<String, Object>>
			List<Map<String, Object>> jsonArray = objectMapper.readValue(fileContent, List.class);

			// 遍历List中的Map对象
			for (Map<String, Object> jsonObject : jsonArray) {
				// 处理每个Map对象
				mongoTemplate.save(jsonObject,"ceshi");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	@Autowired
//	private IInfoSourceFileService infoSourceFileService;
	@Test
	public void test(){
		String url ="http://192.168.10.131:10000/dataset-files/2023/zhihu/efdea7e8-4291-4b24-9651-2a7435b709a1.json";
		System.out.println(infoSourceFileService.getFileContent(url));
	}
	@Autowired
	private ITaskService taskService;
	@Test
	public void test1(){
		HomeTaskInfoVO homeTaskInfoVO = taskService.taskCompleteInfo();
		System.out.println(homeTaskInfoVO);
	}
}
