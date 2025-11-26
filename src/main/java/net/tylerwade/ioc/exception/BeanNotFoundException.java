package net.tylerwade.ioc.exception;

import lombok.Getter;

@Getter
public class BeanNotFoundException extends RuntimeException {
	private final Class<?> clazz;
	private final String beanName;

	public BeanNotFoundException(String beanName, Class<?> clazz) {
		super(String.format("Bean with name '%s' and class '%s' not found.", beanName, clazz.getName()));
		this.beanName = beanName;
		this.clazz = clazz;
	}
}