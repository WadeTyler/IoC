package net.tylerwade.ioc.exception;

import java.lang.reflect.Constructor;

public class BeanCreationException extends IllegalArgumentException {
	private final Class<?> clazz;
	private final Constructor<?> constructor;

	public BeanCreationException(Class<?> clazz, Constructor<?> constructor, Throwable cause) {
		this.clazz = clazz;
		this.constructor = constructor;
		super(String.format("Failed to create bean of type '%s' using constructor '%s'.", clazz.getName(), constructor), cause);
	}
}