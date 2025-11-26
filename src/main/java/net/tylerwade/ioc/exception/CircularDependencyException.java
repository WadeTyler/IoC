package net.tylerwade.ioc.exception;

import lombok.Getter;

@Getter
public class CircularDependencyException extends IllegalStateException {

	private final Class<?> dependentClass;
	private final Class<?> parentClass;

	public CircularDependencyException(Class<?> dependentClass, Class<?> parentClass) {
		this.dependentClass = dependentClass;
		this.parentClass = parentClass;
		super(String.format("Circular dependency detected: '%s' depends on '%s'.", dependentClass.getName(), parentClass.getName()));
	}
}