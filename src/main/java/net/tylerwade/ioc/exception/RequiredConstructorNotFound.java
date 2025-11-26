package net.tylerwade.ioc.exception;

public class RequiredConstructorNotFound extends IllegalArgumentException {

	private final Class<?> clazz;

	public RequiredConstructorNotFound(Class<?> clazz) {
		this.clazz = clazz;
		super(String.format("No suitable DI constructor found for class '%s'.", clazz.getName()));
	}
}