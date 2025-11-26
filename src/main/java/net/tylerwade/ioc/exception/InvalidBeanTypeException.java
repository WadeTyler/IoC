package net.tylerwade.ioc.exception;

import lombok.Getter;

@Getter
public class InvalidBeanTypeException extends IllegalArgumentException {

	private final Class<?> beanType;
	private final Class<?> parentClass;

	public InvalidBeanTypeException(Class<?> beanType, Class<?> parentClass) {
		super(String.format("Invalid bean type '%s' for bean '%s'", beanType.getName(), parentClass.getName()));
		this.beanType = beanType;
		this.parentClass = parentClass;
	}

	public InvalidBeanTypeException(Class<?> beanType) {
		super(String.format("Invalid bean type '%s'", beanType.getName()));
		this.beanType = beanType;
		this.parentClass = null;
	}
}