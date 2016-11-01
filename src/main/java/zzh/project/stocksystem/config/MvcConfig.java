package zzh.project.stocksystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.google.gson.Gson;

import zzh.project.stocksystem.interceptor.AuthInterceptor;

@EnableAsync
@Configuration
@ComponentScan({ "zzh.project.stocksystem.web" })
public class MvcConfig extends WebMvcConfigurationSupport {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		// 返回视图解析器
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		return super.requestMappingHandlerMapping();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 插入一个从拦截器用于权限校验
		registry.addInterceptor(authInterceptor())
			.addPathPatterns("/app/**")
			.excludePathPatterns("/app/register", "/app/login");
	}

	@Bean
	public AuthInterceptor authInterceptor() {
		return new AuthInterceptor();
	}

	@Bean
	public Gson gson() {
		return new Gson();
	}
}
