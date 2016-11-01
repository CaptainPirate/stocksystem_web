package zzh.project.stocksystem;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import zzh.project.stocksystem.config.MvcConfig;
import zzh.project.stocksystem.config.RootConfig;

/**
 * 初始化Spring核心DispatcherServlet
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { RootConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { MvcConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}