package zzh.project.stocksystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import cn.jpush.api.JPushClient;

@Configuration
@PropertySource({ "classpath:jpush.properties" })
public class PushConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placehodlerConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Autowired
	Environment env;

	@Bean
	public JPushClient jPushClient() {
		return new JPushClient(env.getRequiredProperty("masterSecret"),
				env.getRequiredProperty("appKey"));
	}
}
