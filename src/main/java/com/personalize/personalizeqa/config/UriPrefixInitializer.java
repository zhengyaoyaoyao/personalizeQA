package com.personalize.personalizeqa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Collections;

@Component
public class UriPrefixInitializer implements ApplicationRunner {

    private final ConfigurableEnvironment env;

    @Autowired
    public UriPrefixInitializer(ConfigurableEnvironment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String serverHost = InetAddress.getLocalHost().getHostAddress();
        String serverPort = env.getProperty("server.port");

        // Modify the uri-prefix using serverHost and serverPort
        String newUriPrefix = "http://" + serverHost + ":" + serverPort;

        // Set the new uri-prefix in the environment properties
        env.getPropertySources().addFirst(
                new MapPropertySource("dynamicProperties", Collections.singletonMap("personalize.file.local.uri-prefix", newUriPrefix))
        );
    }
}
