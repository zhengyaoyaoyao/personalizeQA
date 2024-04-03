package com.personalize.personalizeqa;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@MapperScan("com.personalize.personalizeqa.mapper")
@SpringBootApplication
@Transactional
public class PersonalizeQaApplication {

	public static void main(String[] args) throws UnknownHostException {
		ConfigurableApplicationContext application = SpringApplication.run(PersonalizeQaApplication.class, args);
		Environment env = application.getEnvironment();
		log.info("\n----------------------------------------------------------\n\t" +
						"应用 '{}' 运行成功! 访问连接:\n\t" +
						"Swagger文档: \t\thttp://{}:{}/swagger-ui.html\n\t" +
						"数据库监控: \t\thttp://{}:{}/druid\n" +
						"----------------------------------------------------------",
				env.getProperty("spring.application.name"),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"),
				"127.0.0.1",
				env.getProperty("server.port"));
	}
}
