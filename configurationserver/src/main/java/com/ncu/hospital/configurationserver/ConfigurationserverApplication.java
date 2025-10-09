package com.ncu.hospital.configurationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigurationserverApplication {
	public static void main(String[] args) {
		SpringApplication app=new SpringApplication(ConfigurationserverApplication.class);
		String mode=System.getenv("config_mode");
		if("native".equalsIgnoreCase(mode)){
			app.setAdditionalProfiles("native");
		}
		else{
			app.setAdditionalProfiles("git");
		}
		app.run(args);
	}

}
