package com.liaoyin.lyproject.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 普通类调用Spring bean对象： 说明： 1、此类需要放到App.java同包或者子包下才能被扫描，否则失效。
 */
@Component
public class SpringUtil implements ApplicationContextAware {

	private static HttpServletRequest request;

	private static HttpServletResponse response;

	public static HttpServletResponse getResponse() {
		return response;
	}

	@Autowired
	public void setResponse(HttpServletResponse response) {
		SpringUtil.response = response;
	}

	public static HttpServletRequest getRequest() {
		return request;
	}

	@Autowired
	public void setRequest(HttpServletRequest request) {
		SpringUtil.request = request;
	}

	private static ApplicationContext applicationContext = null;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtil.applicationContext == null) {
			SpringUtil.applicationContext = applicationContext;
		}
	}

	// 获取applicationContext
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// 通过name获取 Bean.
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	// 通过class获取Bean.
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	// 通过name,以及Clazz返回指定的Bean
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

	//
	public static void registerBean(String beanName, Class clazz) {
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext
				.getAutowireCapableBeanFactory();

		BeanDefinitionBuilder Builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);

		defaultListableBeanFactory.registerBeanDefinition(beanName, Builder.getBeanDefinition());
	}
}
